/* *****************************************************************************
 *  Name: Slava Zubrin
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/*
Performance requirements.
1. Your randomized queue implementation must support each randomized queue operation
    (besides creating an iterator) in constant amortized time. That is, any intermixed
    sequence of m randomized queue operations (starting from an empty queue) must take
    at most cm steps in the worst case, for some constant c.
2. A randomized queue containing n items must use at most 48n + 192 bytes of memory.
3. Your iterator implementation must:
    - support operations next() and hasNext() in constant worst-case time;
    - and construction in linear time;
    - you may (and will need to) use a linear amount of extra memory per iterator.
 */

public class RandomizedQueue<Item> implements Iterable<Item> {

    // Variables
    private Node<Item> first = null;
    private int size = 0;

    private class Node<Item> {
        private Item item;
        private Node<Item> next;

        Node(Item item) {
            this.item = item;
        }
    }

    // construct an empty randomized queue
    public RandomizedQueue() { }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException();
        }

        Node<Item> newNode = new Node<Item>(item);

        if (isEmpty()) {
            first = newNode;
        } else {
            Node<Item> node = first;
            while (node.next != null) {
                node = node.next;
            }
            node.next = newNode;
        }

        size += 1;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Item result = null;

        if (size == 1) {
            result = first.item;
            first = null;
            size = 0;
            return result;

        }

        Node<Item> chosenNode = first;
        Node<Item> beforeChosenNode = null;
        int randomIndex = StdRandom.uniform(this.size);
        if (randomIndex > 0) {
            for (int i = 1; i < randomIndex; i++) {
                beforeChosenNode = chosenNode;
                chosenNode = chosenNode.next;
            }
        }

        result = chosenNode.item;
        Node<Item> afterChosenNode = chosenNode.next;

        if (beforeChosenNode != null && afterChosenNode != null) {
            // in the middle of the list
            beforeChosenNode.next = afterChosenNode;
            size -= 1;
        } else if (beforeChosenNode != null) {
            // end of the list
            beforeChosenNode.next = null;
            size -= 1;
        } else if (afterChosenNode != null) {
            // start of the list
            first = afterChosenNode;
            size -= 1;
        } else {
            // single element in the list, actually never should happen :)
            first = null;
            size = 0;
        }

        return result;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        if (this.size == 1) {
            return first.item;
        }

        Node<Item> chosenNode = first;
        int randomIndex = StdRandom.uniform(this.size);
        if (randomIndex > 0) {
            for (int i = 1; i < randomIndex; i++) {
                chosenNode = chosenNode.next;
            }
        }
        return chosenNode.item;
    }

    // Debug methods

    // prints internal state of the queue (for debug purposes)
    private void description() {
        if (isEmpty()) {
            System.out.print("[]");
        }
        else {
            System.out.print("[");
            Node<Item> current = first;
            do {
                System.out.print(current.item + ", ");
                current = current.next;
            } while (current != null);
            System.out.print("]");
        }

        System.out.print(" : " + size);
        System.out.println();
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator(this);
    }

    // Unit testing
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();

        rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(3);
        rq.enqueue(4);
        rq.enqueue(5);
        rq.description();

        boolean failed = false;

        if (rq.size() != 5) {
            failed = true;
            StdOut.println("Error: rand.queue has incorrect size (1)");
        }

        Integer item = rq.dequeue();
        StdOut.println("Extracted: " + item);
        rq.description();

        if (rq.size() != 4) {
            failed = true;
            StdOut.println("Error: rand.queue has incorrect size (2)");
        }

        Iterator<Integer> iterator1 = rq.iterator();
        Iterator<Integer> iterator2 = rq.iterator();
        int i = rq.size();
        while (iterator1.hasNext()) {
            Integer nextValue1 = iterator1.next();
            StdOut.println("Next1: " + nextValue1);
            rq.description();
            if (rq.size() != i) {
                failed = true;
                StdOut.println("Error: rand.queue has incorrect size (3)");
            }
            if (iterator2.hasNext()) {
                Integer nextValue2 = iterator2.next();
                StdOut.println("Next2: " + nextValue2);
                rq.description();
                if (rq.size() != i) {
                    failed = true;
                    StdOut.println("Error: rand.queue has incorrect size (4)");
                }
            }
        }

        int rq_size = rq.size();
        System.out.println("rq.size = " + rq_size);

        for (int j = 0; j < rq_size; j++) {
            int extracted = rq.dequeue();
            StdOut.println("deque #" + j + " : " + extracted);
        }

        if (!rq.isEmpty()) {
            failed = true;
            StdOut.println("Error: rand.deque should be empty by that time");
        }


        if (!failed) {
            StdOut.println("Rand.queue success!");
        }
    }

    // Helpers

    private Item[] items() {
        Item[] retval = (Item[]) new Object[size];

        if (isEmpty()) {
            return retval;
        }

        Node<Item> current = first;
        retval[0] = first.item;

        for (int i = 1; i < size; i++) {
            current = current.next;
            retval[i] = current.item;
        }

        return retval;
    }


    // Inner iterator class

    // todo: make sure that more than one iterator can exist and operate simultaneously
    // todo: construction must take linear time
    // todo: operations next() and hasNext() must take constant worst-case time
    // todo: may (and will need to) use a linear amount of extra memory per iterator

    private class RandomIterator implements Iterator<Item> {

        private Item[] items;
        private int[] randomIndexes;
        private int nextIndex = 0;

        RandomIterator(RandomizedQueue<Item> queue) {
            items = queue.items();
            int itemsCount = queue.size;
            randomIndexes = new int[itemsCount];

            if (itemsCount == 0) { return; }

            for (int i = 0; i < itemsCount; i++) {
                boolean indexAddedAndUnique = false;
                do {
                    int randomIndex = StdRandom.uniform(itemsCount);
                    boolean isUnique = true;
                    for (int j = 0; j < i; j++) {
                        if (randomIndexes[j] == randomIndex) {
                            isUnique = false;
                        }
                    }
                    if (isUnique) {
                        randomIndexes[i] = randomIndex;
                        indexAddedAndUnique = true;
                    }
                } while (!indexAddedAndUnique);
            }
        }

        @Override
        public boolean hasNext() {
            if (nextIndex >= items.length) {
                return false;
            }

            return true;
        }

        @Override
        public Item next() {
            if (nextIndex < randomIndexes.length) {
                int itemIndex = randomIndexes[nextIndex];
                nextIndex += 1;
                return items[itemIndex];
            } else {
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }
}
