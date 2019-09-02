/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class BruteCollinearPoints {

    private List<LineSegment> segments = new ArrayList<>();

    // Public

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    for (int m = k + 1; m < points.length; m++) {
                        Point p1 = points[i];
                        Point p2 = points[j];
                        Point p3 = points[k];
                        Point p4 = points[m];

                        double slope1to2 = p1.slopeTo(p2);
                        double slope1to3 = p1.slopeTo(p3);
                        double slope1to4 = p1.slopeTo(p4);

                        if (slope1to2 == slope1to3 && slope1to3 == slope1to4) {
                            LineSegment seg = constructSegment(new Point[] { p1, p2, p3, p4 });
                            this.segments.add(seg);
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return this.segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] retval = new LineSegment[this.segments.size()];
        return this.segments.toArray(retval);
    }

    // Private

    private LineSegment constructSegment(Point[] points) {
        Point minPoint = points[0];
        Point maxPoint = points[0];

        for (int i = 1; i < points.length; i++) {
            if (minPoint.compareTo(points[i]) > 0) {
                minPoint = points[i];
            } else if (maxPoint.compareTo(points[i]) < 0) {
                maxPoint = points[i];
            }
        }

        if (minPoint.compareTo(maxPoint) == 0) {
            return null;
        }

        return new LineSegment(minPoint, maxPoint);
    }

    // Main

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        BruteCollinearPoints bcp = new BruteCollinearPoints(points);
        System.out.println(bcp.numberOfSegments());
        for (LineSegment segment: bcp.segments()) {
            StdOut.println(segment);
            segment.draw();
        }

        // // print and draw the line segments
        // FastCollinearPoints collinear = new FastCollinearPoints(points);
        // for (LineSegment segment : collinear.segments()) {
        //     StdOut.println(segment);
        //     segment.draw();
        // }

        StdDraw.show();
    }
}
