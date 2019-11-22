/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.utility;

import java.util.Random;

/** 
 Generate pseudo-random floating point values, with an 
 approximately Gaussian (normal) distribution.

 Many physical measurements have an approximately Gaussian 
 distribution; this provides a way of simulating such values. 
*/

public final class RandomGaussian {
    
    public static void generate() {
        RandomGaussian gaussian = new RandomGaussian();
        double MEAN = 100.0f; 
        double VARIANCE = 5.0f;
        for (int idx = 1; idx <= 10; ++idx){
          log("Generated : " + gaussian.getGaussian(MEAN, VARIANCE));
        }
    }

    private Random fRandom = new Random();

    private double getGaussian(double aMean, double aVariance){
        return aMean + fRandom.nextGaussian() * aVariance;
    }

    private static void log(Object aMsg){
       System.out.println(String.valueOf(aMsg));
    }            
}
