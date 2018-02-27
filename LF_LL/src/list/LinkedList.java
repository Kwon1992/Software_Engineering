package list;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class LinkedList {
    private Node head; // int MIN
    private Node tail; // int MAX

    public LinkedList() {
        head = new Node(Integer.MIN_VALUE);
        tail = new Node(Integer.MAX_VALUE);
        head.next.set(tail, false);
        //tail.next.set(head,false);
    }

    public boolean insert(int data) {
        while (true) {
            Window window = find(head, data);
            Node pred = window.pred, curr = window.curr;
            if (curr.data == data) {
                return false;
            } else {
                Node node = new Node(data);
                node.next = new AtomicMarkableReference(curr, false);
                if (pred.next.compareAndSet(curr, node, false, false)) {
                    return true;
                }
            }
        }
    }

    public boolean delete(int data) {
        while (true) {
            Window window = find(head, data);
            Node pred = window.pred, curr = window.curr;
            if (curr.data != data) {
                return false;
            } else {
                Node succ = curr.next.getReference();
                if (!curr.next.attemptMark(succ, true)) continue;
                pred.next.compareAndSet(curr, succ, false, false);
                return true;
            }
        }
    }

    public boolean search(int data) {
        boolean[] marked = {false};
        Node curr = head;
        while (curr.data < data) {
            curr = curr.next.getReference();
            Node succ = curr.next.get(marked); // curr is marked? (for check)
        }
        return (curr.data == data && !marked[0]);
    }


    public class Node {
        int data;
        AtomicMarkableReference<Node> next; // AtomicMarkableReference

        public Node(int data) {
            this.data = data;
            this.next = new AtomicMarkableReference<Node>(null, false);
        }
    }


    static class Window {

        public Node pred, curr;

        public Window(Node pred, Node curr) {
            this.pred = pred;
            this.curr = curr;
        }
    }

    public Window find(Node head, int data) {
        Node pred = null, curr = null, succ = null;
        boolean[] marked = {false};
        boolean snip;

        retry:
        while (true) {
            pred = head;
            curr = pred.next.getReference();
            while (true) {
                succ = curr.next.get(marked);
                while (marked[0]) {
                    snip = pred.next.compareAndSet(curr, succ, false, false);
                    if (!snip) {
                        continue retry;
                    }
                    curr = succ;
                    succ = curr.next.get(marked);
                }
                if (curr.data >= data) {
                    return new Window(pred, curr);
                }
                pred = curr;
                curr = succ;
            }
        }
    }
}


