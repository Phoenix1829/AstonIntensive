package ru.phoenix.task1;

/**
 * Пользовательская реализация HashMap на основе массива бакетов.
 *
 * Коллизии разрешаются с помощью метода цепочек (Separate Chaining):
 * каждый бакет представляет собой начало связного списка,
 * содержащего элементы с одинаковым хеш-индексом.
 */
public class ChainedHashMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;

    private final Node<K, V>[] buckets;

    private int size;

    @SuppressWarnings("unchecked")
    public ChainedHashMap() {
        buckets = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    private void validateKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
    }

    private int getIndex(K key) {
        return (key.hashCode() & 0x7fffffff) % buckets.length;
    }

    public void put(K key, V value) {

        validateKey(key);
        int index = getIndex(key);

        Node<K, V> current = buckets[index];

        if (current == null) {
            buckets[index] = new Node<>(key, value);
            size++;
            return;
        }

        while (true) {

            if (current.getKey().equals(key)) {
                current.setValue(value);
                return;
            }

            if (current.getNext() == null) {
                current.setNext(new Node<>(key, value));
                size++;
                return;
            }

            current = current.getNext();
        }
    }

    public V get(K key) {

        validateKey(key);

        int index = getIndex(key);

        Node<K, V> current = buckets[index];

        while (current != null) {

            if (current.getKey().equals(key)) {
                return current.getValue();
            }

            current = current.getNext();
        }

        return null;
    }

    public V remove(K key) {

        validateKey(key);

        int index = getIndex(key);

        Node<K, V> current = buckets[index];
        Node<K, V> previous = null;

        while (current != null) {

            if (current.getKey().equals(key)) {

                if (previous == null) {
                    buckets[index] = current.getNext();
                } else {
                    previous.setNext(current.getNext());
                }

                size--;

                return current.getValue();
            }

            previous = current;
            current = current.getNext();
        }

        return null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(K key) {

        validateKey(key);

        int index = getIndex(key);

        Node<K, V> current = buckets[index];

        while (current != null) {

            if (current.getKey().equals(key)) {
                return true;
            }

            current = current.getNext();
        }

        return false;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("{");

        boolean first = true;

        for (Node<K, V> bucket : buckets) {

            Node<K, V> current = bucket;

            while (current != null) {

                if (!first) {
                    sb.append(", ");
                }

                sb.append(current.getKey())
                        .append("=")
                        .append(current.getValue());

                first = false;
                current = current.getNext();
            }
        }

        sb.append("}");

        return sb.toString();
    }
}