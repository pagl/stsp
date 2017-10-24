/*
 * Mateusz Ledzianowski inf117226
 * Patryk Gliszczynski inf117288
 */
package utils;

public class Utilities {
    
    public static void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }
}
