public class Queue {
    private int head, tail;
    private Person[] personQueue = new Person[50];

    public Queue() {
        head = 0;
        tail = 0;
    }

    public boolean isEmpty() {
        return (head == tail);
    }

    public boolean isFull() {
        return (((tail + 1) % 50) == head);
    }

    public void enqueue(Person person) {
        if (isFull()) {
            System.out.println("Queue is Full.");
        } else {
            tail = (tail + 1) % 50;
            personQueue[tail] = person;
        }
    }

    public Person dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is Empty.");
            return null;
        } else {
            head = (head + 1) % 50;
            return personQueue[head];
        }
    }
}
