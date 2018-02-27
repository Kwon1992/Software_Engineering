/*
 * Vertex Implementation.
 */
public class Person {
    private String name;
    private Person next;
    private boolean isVisited = false;

    public Person(String name) {
        this.name = name;
        this.next = null;
    }

    public String getName() {
        return name;
    }

    public Person getLink() {
        return next;
    }

    public boolean getVisited() {
        return isVisited;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(Person person) {
        this.next = person;
    }

    public void setVisited(boolean visit) {
        isVisited = visit;
    }

    @Override
    public String toString() {
        return name;
    }

}
