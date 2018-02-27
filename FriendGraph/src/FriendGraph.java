public class FriendGraph {

    private Person[] people = new Person[50];
    private int vertices = 0;

    /*
     * add Person to the people array :: same as insert vertex
     */
    public void addPerson(Person a) {
        if (vertices >= 50) {
            System.out.println("Graph is Full!");
            return;
        }
        people[vertices] = a;
        vertices += 1;
        System.out.println(a + " is added. (" + vertices + "/50)");
    }

    public void addFriendship(String name1, String name2) {
        int i = 0, j = 0;
        boolean isName1 = false, isName2 = false;

        // Is name1 is in this graph?
        for (; i < vertices; i++) {
            if (people[i].getName() == name1) {
                isName1 = true;
                break;
            }
        }

        // If name1 is in, then is name2 is in this graph?
        for (; j < vertices; j++) {
            if (people[j].getName() == name2) {
                isName2 = true;
                break;
            }
        }

        if (isName1 && isName2) {
            if (name1 == name2) {
                System.out.println("Error: Same Name. [" + name1 + "]");
                return;
            }

            Person newNode1 = new Person(name1);
            Person newNode2 = new Person(name2);

            InsertNode(people[i], newNode2);
            InsertNode(people[j], newNode1);
        } else {
            // who's name is wrong?
            if ((!isName1) && (!isName2)) { // both
                System.out.println(name1 + " and " + name2 + " are not in the graph.");
            } else if (!isName1) { // name1
                System.out.println(name1 + " is not in the graph.");
            } else { // name2
                System.out.println(name2 + " is not in the graph.");
            }

        }
    }

    private void InsertNode(Person list, Person node) {
        while (list.getLink() != null) {
            list = list.getLink();
        }

        if (list.getLink() == null) {
            list.setLink(node);
        }
    }

    /*
     * function: getDistance()
     *
     * assumption : person's name is unique.
     *
     * description:
     * there is a connection __ return how far 2 people are compare
     * same person __ return 0
     * there is no connection __ return -1
     *
     */
    public int getDistance(String source, String dest) {
        // if name is identical, return 0.
        if (source == dest) {
            return 0;
        } else {
            Person start = null;
            for (int i = 0; i < vertices; i++) {
                if (people[i].getName() == source) {
                    start = people[i];
                    break;
                }
            }
            return BFS(start, dest);
        }

    }


    private int BFS(Person start, String end) {
        Queue queue = new Queue();
        int distance = 1;

        if (start == null) {
            return -1;
        }

        // Before BFS, reset isVisited
        for (int i = 0; i < vertices; i++) {
            people[i].setVisited(false);
        }

        start.setVisited(true);
        queue.enqueue(start);

        while (!queue.isEmpty()) {

            Person p = queue.dequeue();
            do {
                Person next = p.getLink();

                for (int i = 0; i < vertices; i++) {
                    if (people[i].getName() == next.getName()) {
                        next = people[i];
                        break;
                    }
                }

                if (!(next.getVisited())) {
                    next.setVisited(true);
                    queue.enqueue(next);
                }

                if (next.getName() == end) {
                    return distance;
                }

                p = p.getLink();

            } while (p.getLink() != null);
            distance++;
        }
        return -1;
    }

}
