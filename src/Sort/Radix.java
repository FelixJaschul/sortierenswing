package Sort;

import java.util.Arrays;

// Radix sort implementation - via Claude 3.7
public class Radix implements SortAlgorithm {

    private final int[] count = new int[256];
    // Fields
    private int currentDigit = 0;
    private int currentIndex = 0;
    private int positionIndex = 0;
    private int placementIndex;
    private int copyIndex = 0;
    private int[] sortedArr;
    private boolean stepComplete = false;
    private Phase currentPhase = Phase.COUNTING;

    @Override
    public String getName() {
        return "Radix Sort";
    }

    // Method to reset state for a new step
    public void resetForNewStep(int[] array, int step) {
        currentDigit = step;
        currentIndex = 0;
        Arrays.fill(count, 0);
        sortedArr = new int[array.length];
        currentPhase = Phase.COUNTING;
        positionIndex = 0;
        placementIndex = array.length - 1;
        copyIndex = 0;
        stepComplete = false;
    }

    // Method to perform a single micro-step of the algorithm
    public boolean microStep(int[] array) {
        if (stepComplete) return true;

        int shift = currentDigit;

        // Counting phase - count occurrences of each digit
        if (currentPhase == Phase.COUNTING) {
            if (currentIndex < array.length) {
                count[(array[currentIndex] >>> shift) & 0xFF]++;
                currentIndex++;
                return false;
            } else {
                currentPhase = Phase.POSITION_CALCULATION;
                return false;
            }
        }

        // Position calculation phase - calculate positions
        if (currentPhase == Phase.POSITION_CALCULATION) {
            if (positionIndex < 256) {
                if (positionIndex > 0) count[positionIndex] += count[positionIndex - 1];
                positionIndex++;
                return false;
            } else {
                currentPhase = Phase.PLACEMENT;
                return false;
            }
        }

        // Placement phase - place elements in sorted order
        if (currentPhase == Phase.PLACEMENT) {
            if (placementIndex >= 0) {
                int digit = (array[placementIndex] >>> shift) & 0xFF;
                sortedArr[--count[digit]] = array[placementIndex];
                placementIndex--;
                return false;
            } else {
                currentPhase = Phase.COPY;
                return false;
            }
        }

        // Copy phase - copy sorted array back to original
        if (currentPhase == Phase.COPY) {
            if (copyIndex < array.length) {
                array[copyIndex] = sortedArr[copyIndex];
                copyIndex++;
                return false;
            } else {
                stepComplete = true;
                return true;
            }
        }

        return true;
    }

    private enum Phase {COUNTING, POSITION_CALCULATION, PLACEMENT, COPY}
}