/*
 * Mateusz Ledzianowski inf117226
 * Patryk Gliszczynski inf117288
 */
package utils;

import java.util.Iterator;


public class OptGenerator <Type> implements Iterable<int[]> {    
    private final int[] array;
    private int pointA, pointB;

    public OptGenerator(int[] array) {
       this.array = array;
    }

    @Override
    public Iterator<int[]> iterator() {
        Iterator<int[]> it = new Iterator<int[]>() {
            private int pointA = -1;
            private int pointB = 0;

            @Override
            public boolean hasNext() {
                return this.pointA < array.length - 2 && this.pointB < array.length;
            }

            @Override
            public int[] next() {
                if (this.pointA > -1) {
                    Utilities.swap(array, this.pointA, this.pointB);
                } else {
                    this.pointA++;
                }
                this.pointB++;

                if (this.pointB == array.length) {
                    this.pointA++;
                    this.pointB = this.pointA + 1;
                }
                Utilities.swap(array, this.pointA, this.pointB);
                return array;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }    
}

    
