import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    private Node first = null;
    private Node last = null;
    private int size = 0;

    public Deque() { }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        checkOnNull(item);

        Node newNode = new Node();
        newNode.item = item;

        if (isEmpty()) {
            last = newNode;
        } else {
            newNode.next = first;
            first.prev = newNode;
        }

        first = newNode;
        size += 1;
    }

    // add the item to the end
    public void addLast(Item item) {
        checkOnNull(item);

        Node newNode = new Node();
        newNode.item = item;

        if (isEmpty()) {
            first = newNode;
        } else {
            newNode.prev = last;
            last.next = newNode;
        }

        last = newNode;
        size += 1;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        checkOnEmpty();

        Item result = first.item;

        if (size == 1) {
            first = null;
            last = null;
        } else {
            first = first.next;
            first.prev = null;
        }

        size -= 1;

        return result;
    }

    // remove and return the item from the end
    public Item removeLast() {
        checkOnEmpty();

        Item result = last.item;

        if (size == 1) {
            first = null;
            last = null;
        } else {
            last = last.prev;
            last.next = null;
        }

        size -= 1;
        return result;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private void checkOnNull(Item item) {
        if (item == null) throw new NullPointerException();
    }

    private void checkOnEmpty() {
        if (isEmpty()) throw new NoSuchElementException();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addLast(5);

        int first = deque.removeFirst();
        StdOut.println(first);

        deque.addLast(2);
        deque.addLast(-4);

        for (int i : deque) {
            StdOut.println(i);
        }
    }
}