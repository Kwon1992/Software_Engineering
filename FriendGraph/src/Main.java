public class Main {
    public static void main(String[] args) {
        FriendGraph graph = new FriendGraph();

        Person john = new Person("John");
        Person tom = new Person("Tom");
        Person jane = new Person("Jane");
        Person marry = new Person("Marry");

        graph.addPerson(john);
        graph.addPerson(tom);
        graph.addPerson(jane);
        graph.addPerson(marry);

        graph.addFriendship("John", "Tom");
        graph.addFriendship("Tom", "Jane");
        graph.addFriendship("John", "John");

        System.out.println(graph.getDistance("John", "Tom"));
        System.out.println(graph.getDistance("John", "Jane"));
        System.out.println(graph.getDistance("John", "John"));
        System.out.println(graph.getDistance("John", "Marry"));
    }
}
