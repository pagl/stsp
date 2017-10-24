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
    
    public static void reverse(int[] array, int from, int to) {
        while (from < to) {
            swap(array, from, to);
            from++;
            to--;
        }    
    }
}