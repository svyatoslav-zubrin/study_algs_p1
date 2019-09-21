/* *****************************************************************************
 *  Name: Slava Zubrin
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

public class Permutation {

    public static void main(String[] args) {
        Integer num = Integer.valueOf(args[0]);
        if (num == 0) return;

        RandomizedQueue<String> queue = new RandomizedQueue<>();
        try {
            String item = StdIn.readString();
            while (item != null) {
                queue.enqueue(item);
                item = StdIn.readString();
            }
        } catch (NoSuchElementException exception) { }

        for (int i = 0; i < num; i++) {
            StdOut.println(queue.dequeue());
        }
    }
}