package utils;

import java.util.Random;

/**
 * 
 * @author Patryk Gliszczynski
 * @author Mateusz Ledzianowski
 */

public abstract class Utilities {
    
    public static final Random RAND = new Random();
    
    public static void swap(Object[] array, int i, int j) {
        Object tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }
    
    public static void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }
    
    public static void reverse(int[] array, int from, int to) {
        while (from < to) {
            swap(array, from++, to--);
        }
    }
    
    public static void shuffle(int[] array) {
        for (int i = array.length - 1; i > 0; --i) {
            swap(array, RAND.nextInt(i + 1), i);
        }
    }
}