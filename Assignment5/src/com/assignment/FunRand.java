package com.assignment;

import java.util.Random;

public class FunRand {

  /**
   * Generates a random value according to an exponential distribution
   *
   * @param timeMean mean value
   * @return a random value according to an exponential distribution
   */
  public static double Exp(double timeMean) {
    double a = 0;
    while (a == 0) {
      a = Math.random();
    }
    a = -timeMean * Math.log(a);

    return a;
  }

  /**
   * Generates a random value according to a uniform distribution
   *
   * @param timeMin
   * @param timeMax
   * @return a random value according to a uniform distribution
   */
  public static double Unif(double timeMin, double timeMax) {
    double a = 0;
    while (a == 0) {
      a = Math.random();
    }
    a = timeMin + a * (timeMax - timeMin);

    return a;
  }

  /**
   * Generates a random value according to a normal (Gauss) distribution
   *
   * @param timeMean
   * @param timeDeviation
   * @return a random value according to a normal (Gauss) distribution
   */
  public static double Norm(double timeMean, double timeDeviation) {
    double a;
    Random r = new Random();
    a = timeMean + timeDeviation * r.nextGaussian();

    return a;
  }
}
