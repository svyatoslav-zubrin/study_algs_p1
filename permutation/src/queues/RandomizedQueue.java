/* *****************************************************************************
 *  Name:
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
    private Item[] items;
    private int head = -1; // index of the first item in the queue (starts from 0)
    private int tail = -1; // index of the last item in the queue (starts from 0)

    // construct an empty randomized queue
    public RandomizedQueue() {
        Object[] tmp = new Object[1];
        items = (Item[]) tmp;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return sizeCalculated() == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return sizeCalculated();
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException();
        }

        if (sizeCalculated() == sizeReserved()) {
            resize(sizeReserved() * 2);
        }
        else if (tail == sizeReserved() - 1) { // no more space at the end of the array
            reorder();
        }

        if (isEmpty()) {
            head += 1;
        }

        tail += 1;
        items[tail] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Item result = null;
        if (sizeCalculated() == 1) {
            result = items[head];
            items[head] = null;
            head = -1;
            tail = -1;
        }
        else {
            result = extractRandomItem();
        }

        if (sizeCalculated() != 0 && sizeCalculated() <= sizeReserved() / 4) {
            if (head > 0) {
                reorder();
            }
            resize(sizeReserved() / 2);
        }

        return result;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        if (head == tail) {
            return items[head];
        }

        int randomIndex = StdRandom.uniform(head, tail + 1);
        return items[randomIndex];
    }

    // Debug methods

    // prints internal state of the queue (for debug purposes)
    private void description() {
        if (head == -1 && tail == -1) {
            System.out.print("[]");
        }
        else {
            System.out.print("[");
            for (int i = head; i <= tail; i++) {
                System.out.print(items[i]);
                System.out.print(", ");
            }
            System.out.print("]");
        }

        System.out.print(" :");
        System.out.print(sizeCalculated());
        System.out.print("/");
        System.out.print(sizeReserved());

        System.out.println();
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new TheIterator();
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
            if (rq.size() != --i) {
                failed = true;
                StdOut.println("Error: rand.queue has incorrect size (3)");
            }
            if (iterator2.hasNext()) {
                Integer nextValue2 = iterator2.next();
                StdOut.println("Next2: " + nextValue2);
                rq.description();
                if (rq.size() != --i) {
                    failed = true;
                    StdOut.println("Error: rand.queue has incorrect size (4)");
                }
            }
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

    // resizes the storage to given size storing data
    private void resize(int capacity) {
        Item[] copy = (Item[]) (new Object[capacity]);
        int n = capacity;
        if (sizeReserved() < capacity) {
            n = sizeReserved();
        }
        for (int i = 0; i < n; i++) {
            copy[i] = items[i];
            items[i] = null;
        }
        items = copy;
    }

    // moves actual data to the beginning of the storage
    private void reorder() {
        for (int i = head; i < tail; i++) {
            items[i - head] = items[i];
            items[i] = null;
        }

        tail = tail - head;
        head = 0;
    }

    // size of the queue based on real data stored
    private int sizeCalculated() {
        if (head > -1 && tail > -1) {
            return tail - head + 1;
        }
        else {
            return 0;
        }
    }

    // max available size of the queue
    private int sizeReserved() {
        return items.length;
    }

    // actually extracts random item from the storage and makes sure that
    // actual data placed in interrupted sequence (moving last item to the
    // extracted place)
    private Item extractRandomItem() {
        int randomIndex = StdRandom.uniform(head, tail + 1);

        Item result = items[randomIndex];
        if (randomIndex != tail) {
            items[randomIndex] = items[tail];
            items[tail] = null;
        }
        tail -= 1;

        return result;
    }

    // Inner iterator class

    // todo: make sure that more than one iterator can exist and operate simultaneously
    // todo: construction must take linear time
    // todo: operations next() and hasNext() must take constant worst-case time
    // todo: may (and will need to) use a linear amount of extra memory per iterator

    private class TheIterator implements Iterator<Item> {
        public boolean hasNext() {
            return !isEmpty();
        }

        public Item next() {
            if (isEmpty()) {
                throw new NoSuchElementException();
            }

            return extractRandomItem();
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }
}
