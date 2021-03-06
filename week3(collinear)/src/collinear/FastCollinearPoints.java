/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private static final int MIN_SEGMENT_LENGTH = 4;

    private final LineSegment[] segments;
    private final int segmentsNumber;

    public FastCollinearPoints(Point[] points) {
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
        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);

        // StdOut.println("Points: " + Arrays.toString(points));

        ArrayList<Point> segmentsOnTheGoMin = new ArrayList<>();
        ArrayList<Point> segmentsOnTheGoMax = new ArrayList<>();
        ArrayList<Double> segmentsOnTheGoSlope = new ArrayList<>();

        for (int i = 0; i <= sortedPoints.length - MIN_SEGMENT_LENGTH; i++) {
            Point thePoint = sortedPoints[i];

            // StdOut.println(" -------------- ");
            // StdOut.println("thePoint: " + thePoint.toString());

            Point[] theRestPoints = new Point[sortedPoints.length - 1 - i];
            Double[] theRestSlopes = new Double[sortedPoints.length - 1 - i];
            for (int j = 0; j < sortedPoints.length - (i + 1); j++) {
                theRestPoints[j] = sortedPoints[j + (i + 1)];
                theRestSlopes[j] = thePoint.slopeTo(sortedPoints[j + (i + 1)]);
            }

            int[] theRestPermutation = Merge.indexSort(theRestSlopes);

            // StdOut.println("slopes: " + Arrays.toString(theRestSlopes));
            // StdOut.println("points: " + Arrays.toString(theRestPoints));
            // StdOut.println("permut: " + Arrays.toString(theRestPermutation));

            double lastSlope = theRestSlopes[theRestPermutation[0]];
            Point currentSegmentFinalPoint = theRestPoints[theRestPermutation[0]];
            int currentSegmentSize = 2;
            for (int j = 1; j < theRestSlopes.length; j++) {
                double currentSlope = theRestSlopes[theRestPermutation[j]];
                if (lastSlope == currentSlope) {
                    // add point to the segment
                    currentSegmentSize += 1;
                    // StdOut.println("segmentSize: " + currentSegmentSize);
                    currentSegmentFinalPoint = theRestPoints[theRestPermutation[j]];

                    boolean isLastPoint = j == theRestSlopes.length - 1;
                    // StdOut.println("j: " + j + "(" + (theRestSlopes.length - 1) + "), isLast: " + isLastPoint);

                    if (!isLastPoint) { continue; }

                    if (currentSegmentSize >= MIN_SEGMENT_LENGTH) {
                        ArrayList<Integer> indices = indicesOf(lastSlope, segmentsOnTheGoSlope);
                        boolean shouldAddNewSegment = true;
                        for (int index: indices) {
                            Point maxPoint = segmentsOnTheGoMax.get(index);
                            if (maxPoint.equals(currentSegmentFinalPoint)) {
                                shouldAddNewSegment = false;
                                break;
                            }
                        }

                        if (shouldAddNewSegment) {
                            segmentsOnTheGoMin.add(thePoint);
                            segmentsOnTheGoMax.add(currentSegmentFinalPoint);
                            segmentsOnTheGoSlope.add(lastSlope);
                            // StdOut.println("add segment: " + thePoint.toString() + " -> " + currentSegmentFinalPoint.toString() + ", slope: " + currentSlope);
                        }
                    }
                } else {
                    if (currentSegmentSize >= MIN_SEGMENT_LENGTH) {
                        ArrayList<Integer> indices = indicesOf(lastSlope, segmentsOnTheGoSlope);

                        // StdOut.println("indices: " + Arrays.toString(indices));

                        boolean shouldAddNewSegment = true;
                        for (int index: indices) {
                            Point maxPoint = segmentsOnTheGoMax.get(index);
                            if (maxPoint.equals(currentSegmentFinalPoint)) {
                                shouldAddNewSegment = false;
                                break;
                            }
                        }

                        if (shouldAddNewSegment) {
                            segmentsOnTheGoMin.add(thePoint);
                            segmentsOnTheGoMax.add(currentSegmentFinalPoint);
                            segmentsOnTheGoSlope.add(lastSlope);
                            // StdOut.println("add segment: " + thePoint.toString() + " -> " + currentSegmentFinalPoint.toString() + ", slope: " + currentSlope);
                        }
                    }

                    // prepare for new segment search
                    lastSlope = currentSlope;
                    currentSegmentSize = 2;
                    currentSegmentFinalPoint = theRestPoints[theRestPermutation[j]];
                }
            }
        }

        this.segmentsNumber = segmentsOnTheGoMin.size();
        this.segments = new LineSegment[segmentsOnTheGoMin.size()];
        for (int i = 0; i < segmentsOnTheGoMin.size(); i++) {
            LineSegment s = new LineSegment(segmentsOnTheGoMin.get(i), segmentsOnTheGoMax.get(i));
            this.segments[i] = s;
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return this.segmentsNumber;
    }

    // the line segments
    public LineSegment[] segments() {
        return this.segments.clone();
    }

    // Private

    private static <T> ArrayList<Integer> indicesOf(T val, ArrayList<T> arr) {
        ArrayList<Integer> retval = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).equals(val)) {
                retval.add(i);
            }
        }
        return retval;
    }

    // Client code
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

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
