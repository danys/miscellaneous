package me.sunnen.perceptron;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Perceptron learning algorithm
 */
public class App {

  private final int maxX = 1;
  private final int minX = -1;

  private Point p1, p2;
  private Point x[];
  private int y[];
  private int z[];


  private List<Integer> plaIterations;
  private List<Double> fgErrors;

  public App() {
    //
  }


  public static class Point {

    private double x;
    private double y;

    public Point(double x, double y) {
      this.x = x;
      this.y = y;
    }

    public double getX() {
      return x;
    }

    public double getY() {
      return y;
    }
  }

  public void run(int N, int plaRuns) {
    this.plaIterations = new LinkedList<Integer>();
    this.fgErrors = new LinkedList<Double>();
    for (int i = 0; i < plaRuns; i++) {
      System.out.println("Run: " + (i + 1));
      runPla(N);
    }
    computeAverage(plaRuns);
    computefgErrorsAverage(plaRuns);
  }

  private void computeAverage(int plaRuns) {
    double avg = 0.0;
    Iterator<Integer> it = plaIterations.iterator();
    int counter = 0;
    while (it.hasNext()) {
      avg += it.next();
      counter++;
    }
    if (counter != plaRuns) {
      throw new IllegalStateException("Number of runs not as expected!");
    }
    avg = avg / plaRuns;
    System.out.println("Average number of iterations: " + avg);
  }

  private void computefgErrorsAverage(int plaRuns) {
    double avg = 0.0;
    Iterator<Double> it = fgErrors.iterator();
    int counter = 0;
    while (it.hasNext()) {
      avg += it.next();
      counter++;
    }
    if (counter != plaRuns) {
      throw new IllegalStateException("Number of runs not as expected!");
    }
    avg = avg / plaRuns;
    System.out.println("Average error probability: " + avg);
  }

  private void runPla(int N) {
    initializeRun(N);
    int nIterations = 0;
    boolean done = false;
    double w0 = 0, w1 = 0, w2 = 0;
    List<Integer> misclassifiedIndexes;
    while (!done) {
      //Classify N x points according to current w
      done = true;
      misclassifiedIndexes = new ArrayList<Integer>();
      for (int i = 0; i < N; i++) {
        z[i] = computePerceptron(w0, w1, w2, x[i]);
        if (z[i] != y[i]) {
          done = false;
          misclassifiedIndexes.add(i);
        }
      }
      if (!done) {
        //Choose a random point to improve w
        int index = misclassifiedIndexes
            .get(ThreadLocalRandom.current().nextInt(misclassifiedIndexes.size()));
        w1 = w1 + y[index] * x[index].getX();
        w2 = w2 + y[index] * x[index].getY();
        w0 = w0 + y[index];
      }
      nIterations++;
    }
    this.plaIterations.add(nIterations);
    System.out.println("Iterations performed: " + nIterations);
    fgErrors.add(computeFgErrors(w0, w1, w2));
  }

  double computeFgErrors(double w0, double w1, double w2) {
    int trials = 100;
    int errors = 0;
    Point newP;
    for(int i=0;i<trials;i++){
      newP = randomPoint();
      if (computePerceptron(w0,w1,w2,newP)!=computeY(p1,p1,newP)) errors++;
    }
    return (double)(errors/trials);
  }

  private int computePerceptron(double w0, double w1, double w2, Point x) {
    return (int) Math.signum(w1 * x.getX() + w2 * x.getY() + w0);
  }

  private void initializeRun(int N) {
    //Choose two points in 2D uniformly at random for f
    p1 = randomPoint();
    p2 = randomPoint();
    //Choose N
    this.x = new Point[N];
    this.y = new int[N];
    this.z = new int[N];
    for (int i = 0; i < N; i++) {
      x[i] = randomPoint();
      y[i] = computeY(p1, p2, x[i]);
    }
  }

  private int computeY(Point p1, Point p2, Point p) {
    return (p.getY()
        >= ((p2.getY() - p1.getY()) / (p2.getX() - p1.getX())) * (p.getX() - p1.getX()) + p1.getY())
        ? 1 : -1;
  }

  private Point randomPoint() {
    Double p1 = (ThreadLocalRandom.current().nextDouble() - 0.5) * (maxX - minX);
    Double p2 = (ThreadLocalRandom.current().nextDouble() - 0.5) * (maxX - minX);
    return new Point(p1, p2);
  }

  public static void main(String[] args) {

    App app = new App();
    app.run(100, 1000);
  }
}
