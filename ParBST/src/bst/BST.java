package bst;


/** **************************************************************************
 *  The sequential Binary Search Tree (for storing int values)
 *
 *****************************************************************************/

import java.util.*;
import java.util.concurrent.locks.*;
import java.util.function.Consumer;

public class BST {
    private Node root;
    private Lock bstLock;

    public BST() {
        root = null;
        bstLock = new ReentrantLock();
    }

    /*****************************************************
     *
     *            INSERT
     *
     ******************************************************/
    public void parInsert(int data) {
        bstLock.lock();
        if (root == null) {
            root = new Node(data);
            bstLock.unlock();
        } else {
            Node cur = root;
            cur.lock();
            bstLock.unlock();

            while (true) {
                if (data < cur.data) {
                    if (cur.left == null) {
                        cur.left = new Node(data);
                        cur.unlock();
                        return;
                    }
                    cur.left.lock();
                    cur.unlock();
                    cur = cur.left;
                } else if (data > cur.data) {
                    if (cur.right == null) {
                        cur.right = new Node(data);
                        cur.unlock();
                        return;
                    }
                    cur.right.lock();
                    cur.unlock();
                    cur = cur.right;
                } else {
                    cur.unlock();
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

    // you don't need to implement hand-over-hand lock for this function.
    public int findMin()
    {
        if (root == null) {
            throw new RuntimeException("cannot findMin.");
        }
        Node n = root;
        while (n.left != null) {
            n = n.left;
        }
        return n.data;
    }


    public boolean parSearch(int data) {
        Node cur = null;
        bstLock.lock();
        if(root == null) {
            bstLock.unlock();
            return false;
        } else { // root is exist
            cur = root;
            cur.lock();
            bstLock.unlock();

            while(true) {
                if(cur.data == data) {
                    cur.unlock();
                    return true;
                } else if(cur.data > data) {
                    if(cur.left == null) { // no data.
                        break;
                    }
                    cur.left.lock();
                    cur.unlock();
                    cur = cur.left;
                } else { // if (cur.data < data)
                    if(cur.right == null) { //no data
                        break;
                    }
                    cur.right.lock();
                    cur.unlock();
                    cur = cur.right;
                }
            }
            // break -> no data.
            cur.unlock();
            return false;
        }
    }

    /*****************************************************
     *
     *            DELETE
     *
     ******************************************************/

    public void parDelete(int data) {
        bstLock.lock();
        if (root == null) {
            bstLock.unlock();
        } else { // root != null
            Node cur = root;
            Node par;
            cur.lock();

            if (cur.data > data) {
                par = cur;
                cur = cur.left;
            } else if (cur.data < data) {
                par = cur;
                cur = cur.right;
            } else { // cur.data == data
                Node rep = findReplacement(cur);
                root = rep;

                if (rep != null) {
                    rep.left = cur.left;
                    rep.right = cur.right;
                }
                cur.unlock();
                bstLock.unlock();
                return;
            }


            if(cur == null) return;

            cur.lock();
            bstLock.unlock();

            while (true) {
                if (cur.data != data) {
                    par.unlock();
                    par = cur;
                    if (cur.data > data) {
                        cur = cur.left;
                    } else {
                        cur = cur.right;
                    }
                } else {
                    Node rep = findReplacement(cur);

                    if (par.data > data) par.left = rep;
                    else par.right = rep;

                    if (rep != null) {
                        rep.left = cur.left;
                        rep.right = cur.right;
                    }

                    cur.unlock();
                    par.unlock();
                    return;
                }

                if (cur == null) break;
                else cur.lock();
            }
            par.unlock();
            return;
        }
    }

    private Node findReplacement(Node sub) {

        Node cur = null;
        Node par = sub;

        if (sub.left != null) {
            cur = sub.left;
            cur.lock();


            while (cur.right != null) {
                if (par != sub) par.unlock();
                par = cur;
                cur = cur.right;
                cur.lock();
            }


            if (cur.left != null) cur.left.lock();

            if (par == sub)
                par.left = cur.left;
            else {

                par.right = cur.left;
                par.unlock();
            }

            if (cur.left != null) cur.left.unlock();

            cur.unlock();

        } else if (sub.right != null) {
            cur = sub.right;
            cur.lock();

            while (cur.left != null) {
                if (par != sub) par.unlock();
                par = cur;
                cur = cur.left;
                cur.lock();
            }

            if (cur.right != null) cur.right.lock();

            if (par == sub)
                par.right = cur.right;
            else {
                par.left = cur.right;
                par.unlock();
            }

            if (cur.right != null) cur.right.unlock();

            cur.unlock();

        } else {
            return null;
        }

        return cur;
    }


    /*************************************************
     *
     *            TRAVERSAL
     *
     **************************************************/

    public void traversal(final Consumer<Node> func) {
        traversal(root, func);
    }

    private void traversal(Node r, final Consumer<Node> func) {
        if (r != null) {
            func.accept(r);
            traversal(r.left, func);
            traversal(r.right, func);
        }
    }

    public void preOrderTraversal()
    {
        preOrderHelper(root);
    }
    private void preOrderHelper(Node r)
    {
        if (r != null)
        {
            System.out.print(r+" ");
            preOrderHelper(r.left);
            preOrderHelper(r.right);
        }
    }

    public void inOrderTraversal()
    {
        inOrderHelper(root);
    }
    private void inOrderHelper(Node r)
    {
        if (r != null)
        {
            inOrderHelper(r.left);
            System.out.print(r+" ");
            inOrderHelper(r.right);
        }
    }

    private class Node {
        private int data;
        private Node left, right;
        private Lock mLock;

        public Node(int data, Node l, Node r) {
            left = l;
            right = r;
            this.data = data;
            mLock = new ReentrantLock();
        }

        public Node(int data) {
            this(data, null, null);
        }

        public String toString() {
            return "" + data;
        }

        public void lock() {
            mLock.lock();
        }

        public void unlock() {
            mLock.unlock();
        }
    }
}
