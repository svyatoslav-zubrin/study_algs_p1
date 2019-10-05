/* *****************************************************************************
 *  Name: Slava Zubrin
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {

    public static void main(String[] args) {

        int num = Integer.parseInt(args[0]);

        if (num == 0) return;

        RandomizedQueue<String> queue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            queue.enqueue(item);
        }

        for (int i = 0; i < num; i++) {
            StdOut.println(queue.dequeue());
        }
    }
}