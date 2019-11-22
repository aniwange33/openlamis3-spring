/**
 *
 * @author user1
 */
package org.fhi360.lamis.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RandomNumberGenerator {   
    
    public int[] random(int size) {
        Random random = new Random();
        int numbers[] = new int[size];
        int n = 0;
        for (int idx = 1; idx <= size; ++idx){
           int randomNumber = random.nextInt(100);
           numbers[n] = randomNumber;
           n++;
        }           
        return numbers;
    }
    
    public int[] random(int size, int start, int end) {
        Random random = new Random();
        int numbers[] = new int[size];
        int n = 0;
        for (int idx = 1; idx <= size; ++idx){
           if (start > end) {
               throw new IllegalArgumentException("Start cannot exceed End.");
           }
           //get the range, casting to long to avoid overflow problems
           long range = (long) end - (long) start + 1;
           // compute a fraction of the range, 0 <= frac < range
           long fraction = (long)(range * random.nextDouble());
           int randomNumber =  (int)(fraction + start);    
           numbers[n] = randomNumber;
           n++;
        }
        return numbers;
    }   

    public int[] randomUnique(int size, int start, int end) {     
        int random[] = new int[size];
        //define ArrayList to hold Integer objects
        ArrayList numbers = new ArrayList();
        for(int i = start; i < end; i++) {
            numbers.add(i+1);           
        }
        Collections.shuffle(numbers);
        for(int j =0; j < size; j++)     {
             random[j] = (Integer) numbers.get(j);
        }
        return random;
    }   

}
