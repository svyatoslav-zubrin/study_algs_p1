/* *****************************************************************************
 *  Name: BruteCollinearPoints
 *  Date: 3 Nov 2018
 *  Description: Brute-force search of all 4-dotted line segments
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private final Point[] points;
    private final LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        // input checks
        if (points == null) throw new IllegalArgumentException();

        for (Point point: points) {
            if (point == null) throw new IllegalArgumentException();
        }

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].equals(points[j])) {
                    throw new IllegalArgumentException();
                }
            }
        }

        // actual segments construction starts here
        this.points = points.clone();
        Arrays.sort(this.points);

        this.segments = findSegments();
    }

    // Public

    // the number of line segments
    public int numberOfSegments() {
        return this.segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return this.segments.clone();
    }

    // Helpers

    private LineSegment[] findSegments() {
        ArrayList<LineSegment> constructedSegments = new ArrayList<>();

        for (int i = 0; i < this.points.length; i++) {
            for (int j = i + 1; j < this.points.length; j++) {
                for (int k = j + 1; k < this.points.length; k++) {
                    for (int m = k + 1; m < this.points.length; m++) {
                        Point p1 = points[i];
                        Point p2 = points[j];
                        Point p3 = points[k];
                        Point p4 = points[m];

                        double s2 = p1.slopeTo(p2);
                        double s3 = p1.slopeTo(p3);
                        double s4 = p1.slopeTo(p4);

                        if (s2 == s3 && s3 == s4) {
                            // System.out.println(" ========== New segment ========== ");
                            // StdOut.println("Points: " + p1 + "->" + p2 + "->" + p3 + "->" + p4);
                            // StdOut.println("Slopes: " + segmentsCount + " for: " + p1 + "-" + p2 + "-" + p3 + "-" + p4 + " (" + i + ", " + j  + ", " + k + ", " + m + ")");
                            // StdOut.println("  1->2: " + s2);
                            // StdOut.println("  1->3: " + s3);
                            // StdOut.println("  1->4: " + s4);
                            // StdOut.println("  inds: " + " (" + i + ", " + j  + ", " + k + ", " + m + ")");
                            constructedSegments.add(new LineSegment(p1, p4));
                        }
                    }
                }
            }
        }

        return constructedSegments.toArray(new LineSegment[0]);
    }

    // Main
    public static void main(String[] args) {
        In in = new In(args[0]);      // input file
        int n = in.readInt();         // number of data points in the input file

        Point[] points = new Point[n];

        int counter = 0;
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            points[counter] = new Point(i, j);
            counter++;
        }

        BruteCollinearPoints solution = new BruteCollinearPoints(points);
        LineSegment[] segments = solution.segments();
        for (LineSegment segment : segments) {
            StdOut.println(segment);
        }
    }
}
