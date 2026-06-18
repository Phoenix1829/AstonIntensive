package ru.phoenix.task1;

public class App {

    public static void main(String[] args) {

        ChainedHashMap<String, Integer> map = new ChainedHashMap<>();

        map.put("Mikhail", 10);
        map.put("Stanislav", 20);
        map.put("Roman", 30);

        System.out.println("Map: " + map);

        System.out.println("Size: " + map.size());

        System.out.println("Stanislav = " + map.get("Stanislav"));

        System.out.println("Contains: "
                + map.containsKey("Roman"));

        map.remove("Mikhail");

        System.out.println("After remove: " + map);

        System.out.println("Size: " + map.size());

        System.out.println("Is empty: "
                + map.isEmpty());
    }
}