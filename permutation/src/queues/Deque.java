/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        private Item item; // actual object
        private Node next; // pointer to the next element
        private Node prev; // pointer to the previous element
    }

    // variables
    private int n; // length of the deque / number of contained objects
    private Node first; // pointer to the first element of the deque
    private Node last; // pointer to the last element of the deque

    // construct an empty deque
    public Deque() {
        n = 0;
        this.last = null;
        this.first = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException();
        }

        if (n == 0) {
            this.addVeryFirstNode(item);
            n++;
        }
        else {
            Node newNode = new Node();
            newNode.item = item;
            newNode.next = this.first;
            this.first.prev = newNode;
            this.first = newNode;

            n++;

            if (n == 2) {
                this.first.next = this.last;
                this.last.prev = this.first;
            }
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException();
        }

        if (n == 0) {
            this.addVeryFirstNode(item);
            n++;
        }
        else {
            Node newNode = new Node();
            newNode.item = item;
            newNode.prev = this.last;
            this.last.next = newNode;
            this.last = newNode;

            n++;

            if (n == 2) {
                this.first.next = this.last;
                this.last.prev = this.first;
            }
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (n == 0) {
            throw new NoSuchElementException();
        }

        Item item = first.item;
        Node oldFirst = this.first;
        this.first = oldFirst.next;
        oldFirst.item = null;
        oldFirst.next = null;

        n--;

        if (n == 0) {
            this.last.item = null;
            this.last.prev = null;
            this.last.next = null;
            this.last = null;
        }
        else if (n == 1) {
            this.last = this.first;
        }

        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (n == 0) {
            throw new NoSuchElementException();
        }
        else if (n == 1) {
            Item item = last.item;

            last.item = null;
            last = null;
            first = null;
            n = 0;

            return item;
        }
        else {
            Item item = last.item;

            Node oldLast = last;
            last = last.prev;
            last.next = null;
            oldLast.item = null;
            oldLast.prev = null;
            n--;

            return item;
        }
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new TheIterator();
    }

    // Testing (required)

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(3);
        deque.addFirst(2);
        deque.addFirst(1);
        deque.addLast(4);
        deque.addLast(5);
        deque.addFirst(0);

        boolean failed = false;

        if (deque.size() != 6) {
            failed = true;
            StdOut.println("Error: incorrect size: " + deque.size() + "instead of 6");
        }
        if (deque.first.item != 0) {
            failed = true;
            StdOut.println("Error: incorrect first element: " + deque.first.item + "instead of 0");
        }
        if (deque.last.item != 5) {
            failed = true;
            StdOut.println("Error: incorrect last element: " + deque.first.item + "instead of 5");
        }

        Integer first = deque.removeFirst();
        Integer last = deque.removeLast();

        if (first != 0 || last != 5) {
            failed = true;
            StdOut.println("Error: incorrect element removed from the deque");
        }
        if (deque.size() != 4) {
            failed = true;
            StdOut.println("Error: incorrect size: " + deque.size() + "instead of 4");
        }
        if (deque.first.item != 1) {
            failed = true;
            StdOut.println("Error: incorrect first element: " + deque.first.item + "instead of 1");
        }
        if (deque.last.item != 4) {
            failed = true;
            StdOut.println("Error: incorrect last element: " + deque.first.item + "instead of 4");
        }

        Iterator<Integer> iterator = deque.iterator();
        int i = 1;
        while (iterator.hasNext()) {
            Integer nextValue = iterator.next();
            if (!deque.isEmpty()) { // debug log
                StdOut.println("from iterator: " + nextValue
                                       + ", first: " + deque.first.item
                                       + " of " + deque.size());
            }
            if (nextValue != i) {
                failed = true;
                StdOut.println("Error: incorrect next element: " + nextValue + "instead of " + i);
            }
            i++;
        }

        if (!deque.isEmpty()) {
            failed = true;
            StdOut.println("Error: deque should be empty by that time");
        }

        if (!failed) {
            StdOut.println("Deque success! :)");
            StdOut.println("-----------------------------------------------");
        }
    }

    // Helpers

    // adds the very first node to the deque
    private void addVeryFirstNode(Item item) {
        Node node = new Node();
        node.item = item;
        node.prev = null;
        node.next = null;
        this.first = node;
        this.last = node;
    }

    // debug description of the deque inner state
    void description() {
        Node current = first;
        if (current == null) {
            System.out.println("[]");
        }
        else {
            System.out.println("[");
            while (current != null) {
                System.out.println(current.item);
                System.out.println(", ");
                current = current.next;
            }
            System.out.println("]");
        }
        System.out.println("");
    }

    // Inner iterator class

    private class TheIterator implements Iterator<Item> {
        private Node current = first; // current node

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (current == null) {
                throw new NoSuchElementException();
            }

            Item item = current.item;

            Node oldCurrent = current;

            current = current.next;
            if (current != null) {
                current.prev = null;
            }
            first = current;

            oldCurrent.prev = null;
            oldCurrent.next = null;
            oldCurrent.item = null;

            n--;

            return item;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }
}
