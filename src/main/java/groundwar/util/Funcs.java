package groundwar.util;

import java.util.Random;

public class Funcs {

    /**
     * Generates a random number in the range [min, max].
     *
     * @param random the random generator to be used
     * @param min    the minimum of the number
     * @param max    the maximum of the number
     */
    public static float randomInRange(Random random, float min, float max) {
        return random.nextFloat() * (max - min) + min;
    }
}
