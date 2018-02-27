package bst;

/** **************************************************************************
 *  The sequential Binary Search Tree (for storing int values)
 *
 *****************************************************************************/

import java.util.concurrent.locks.*;

public class RWBST {
    private Node root;
    private Lock bstLock;

    public RWBST() {
        root = null;
        bstLock = new ReentrantLock();
    }

    /*****************************************************
     *
     *            INSERT
     *
     ******************************************************/
    public void parRWLockInsert(int data) {
        bstLock.lock();
        if (root == null) {
            root = new Node(data);
            bstLock.unlock();
        } else {
            Node cur = root , pred = null;
            cur.readLock.lock();
            bstLock.unlock();

            while (true) {
                if (data < cur.data) {
                    if (cur.left == null) {
                        if(pred != null) pred.writeLock.lock();
                        cur.readLock.unlock();
                        cur.writeLock.lock();
                        cur.left = new Node(data);
                        cur.writeLock.unlock();
                        if(pred != null) pred.writeLock.unlock();
                        return;
                    }

                    cur.left.readLock.lock();
                    cur.readLock.unlock();
                    cur = cur.left;

                } else if (data > cur.data) {
                    if (cur.right == null) {
                        if(pred != null) pred.writeLock.lock();
                        cur.readLock.unlock();
                        cur.writeLock.lock();
                        cur.right = new Node(data);
                        cur.writeLock.unlock();
                        if(pred != null) pred.writeLock.unlock();
                        return;
                    }
                    cur.right.readLock.lock();
                    cur.readLock.unlock();
                    cur = cur.right;
                } else {
                    cur.readLock.unlock();
                    return;
                }
            }
        }
    }

    /*****************************************************
     *
     *            SEARCH
     *
     ******************************************************/


    public boolean parRWLockSearch(int data) {
        Node cur;
        bstLock.lock();
        if(root == null) {
            bstLock.unlock();
            return false;
        } else { // root is exist
            cur = root;
            cur.readLock.lock();
            bstLock.unlock();

            while(true) {
                if(cur.data == data) {
                    cur.readLock.unlock();
                    return true;
                } else if(cur.data > data) {
                    if(cur.left == null) { // no data.
                        break;
                    }
                    cur.left.readLock.lock();
                    cur.readLock.unlock();
                    cur = cur.left;
                } else { // if (cur.data < data)
                    if(cur.right == null) { //no data
                        break;
                    }
                    cur.right.readLock.lock();
                    cur.readLock.unlock();
                    cur = cur.right;
                }
            }
            // break -> no data.
            cur.readLock.unlock();
            return false;
        }
    }


    private class Node {
        private int data;
        private Node left, right;
        private ReadWriteLock readWriteLock;
        private Lock readLock;
        private Lock writeLock;

        public Node(int data, Node l, Node r)
        {
            left = l; right = r;
            this.data = data;
            readWriteLock = new ReentrantReadWriteLock();
            readLock = readWriteLock.readLock();
            writeLock = readWriteLock.writeLock();
        }

        public Node(int data)
        {
            this(data, null, null);
        }

        public String toString()
        {
            return ""+data;
        }

    }
}
