package utils;

/**
 * 
 * @author Patryk Gliszczynski
 * @author Mateusz Ledzianowski
 */

public abstract class Utilities {
    
    public static void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }
}
