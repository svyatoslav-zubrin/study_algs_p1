/* *****************************************************************************
 *  Name: Slava Zubrin
 *  Date: today
 *  Description: smth
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
    private int itemsCount = 0;
    private int sizeReserved = 2;

    // Public

    public RandomizedQueue() { 
        Object[] tmp = new Object[sizeReserved];
        items = (Item[]) tmp;
    }

    public boolean isEmpty() {
        return itemsCount == 0;
    }

    public int size() {
        return itemsCount;
    }

    public void enqueue(Item item) {    
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (itemsCount == sizeReserved) {
            sizeReserved *= 2;
            resize(sizeReserved);
        }

        items[itemsCount] = item;
        itemsCount += 1;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int index = StdRandom.uniform(itemsCount);
        Item result = items[index];

        int tailIndex = itemsCount - 1;
        if (index != tailIndex) {
            swap(index, tailIndex);
        }

        itemsCount -= 1;

        if (itemsCount != 0 && itemsCount <= sizeReserved / 4) {
            sizeReserved /= 2;
            resize(sizeReserved);
        }

        return result;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int index = StdRandom.uniform(itemsCount);
        return items[index];
    }

    public Iterator<Item> iterator() {
        return new RandomIterator(this);
    }

    // Helpers

    private void resize(int newSize) {
        Item[] copy = (Item[]) (new Object[newSize]);
        for (int i = 0; i < itemsCount; i++) {
            copy[i] = items[i];
            items[i] = null;
        }
        items = copy;
    }

    private void swap(int toIndex, int fromIndex) {
        items[toIndex] = items[fromIndex];
        items[fromIndex] = null;
    }

    // Debug routines

    private void description() {
        StdOut.println(" ------------------- ");
        if (isEmpty()) {
            StdOut.println("[]");
        }
        else {
            StdOut.print("[");
            for (int i = 0; i < itemsCount; i++) {
                StdOut.print(items[i]);
                StdOut.print(", ");
            }
            StdOut.print("]");
        }
        StdOut.println("");
    }

    // Inner iterator class

    private class RandomIterator implements Iterator<Item> {
        private final RandomizedQueue<Item> queue;

        RandomIterator(RandomizedQueue<Item> queue) {
            this.queue = queue;
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public Item next() {
            if (queue.isEmpty()) {
                throw new NoSuchElementException();
            }
            return queue.dequeue();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // Unit testing (required)

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
            i -= 1;
            if (rq.size() != i) {
                failed = true;
                StdOut.println("Error: rand.queue has incorrect size (3)");
            }
            if (iterator2.hasNext()) {
                Integer nextValue2 = iterator2.next();
                StdOut.println("Next2: " + nextValue2);
                rq.description();
                i -= 1;
                if (rq.size() != i) {
                    failed = true;
                    StdOut.println("Error: rand.queue has incorrect size (4)");
                }
            }
        }

        int rqSize = rq.size();
        System.out.println("rq.size = " + rqSize);

        for (int j = 0; j < rqSize; j++) {
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
}
