/*
 * Fuzzy search implementation using the Wagner-Fischer algorithm,
 * adapted from the pseudocode for Damerau-Levenshtein distance on Wikipedia.
 *
 * Source: https://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance
 * License: Creative Commons Attribution-ShareAlike 4.0 International (CC BY-SA 4.0)
 * (https://creativecommons.org/licenses/by-sa/4.0/)
 *
 * Changes: Implemented the algorithm in Java,
 * added cache to reduce computation cost for repeated search.
 */

package dook.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides fuzzy string matching using the optimal string alignment distance.
 */
public class DamerauLevenshtein {
    private record Pair(String textA, String textB) {}

    private static Map<Pair, Integer> cache = new HashMap<>();

    /**
     * Returns the optimal string alignment distance between two strings.
     *
     * @param  textA The first string.
     * @param  textB The second string.
     * @return       The optimal string alignment distance.
     */
    public static int getOsaDistance(String textA, String textB) {
        Pair cacheKey = new Pair(textA, textB);
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        int lengthA = textA.length();
        int lengthB = textB.length();

        int[][] wagnerFischerMatrix =
            new int[lengthA + 1][lengthB + 1];

        for (int i = 1; i <= lengthA; i++) {
            wagnerFischerMatrix[i][0] = i;
        }
        for (int j = 1; j <= lengthB; j++) {
            wagnerFischerMatrix[0][j] = j;
        }

        for (int i = 1; i <= lengthA; i++) {
            for (int j = 1; j <= lengthB; j++) {
                int cost = textA.charAt(i - 1) == textB.charAt(j - 1) ? 0 : 1;

                wagnerFischerMatrix[i][j] = Math.min(Math.min(
                    wagnerFischerMatrix[i - 1][j] + 1,
                    wagnerFischerMatrix[i][j - 1] + 1),
                    wagnerFischerMatrix[i - 1][j - 1] + cost
                );

                if (i > 1 && j > 1
                        && textA.charAt(i - 1) == textB.charAt(j - 2)
                        && textA.charAt(i - 2) == textB.charAt(j - 1)) {
                    wagnerFischerMatrix[i][j] = Math.min(
                        wagnerFischerMatrix[i][j],
                        wagnerFischerMatrix[i - 2][j - 2] + cost
                    );
                }
            }
        }

        int distance = wagnerFischerMatrix[lengthA][lengthB];
        cache.put(cacheKey, distance);
        return distance;
    }
}
