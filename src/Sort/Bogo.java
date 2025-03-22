package Sort;

import java.util.Random;

// Heap sort implementation - via Claude 3.7
public class Bogo implements SortAlgorithm {

    private final Random random;
    private boolean stepComplete;
    private int shuffleCount;

    public Bogo() {
        random = new Random();
        shuffleCount = 0;
    }

    @Override
    public String getName() {
        return "Bogo Sort";
    }

    @Override
    public void resetForNewStep(int[] array, int step) {
        stepComplete = false;

        // Check if array is already sorted
        if (isSorted(array)) {
            stepComplete = true;
            return;
        }

        // If not sorted, we'll shuffle in the microStep
    }

    @Override
    public boolean microStep(int[] array) {
        if (stepComplete) {
            return true;
        }

        // Shuffle the array
        shuffleArray(array);
        shuffleCount++;

        // Check if array is sorted after shuffling
        if (isSorted(array)) {
            stepComplete = true;
        }

        return stepComplete;
    }

    private boolean isSorted(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) {
                return false;
            }
        }
        return true;
    }

    private void shuffleArray(int[] array) {
        // Fisher-Yates shuffle
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            // Swap array[i] with array[index]
            int temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }
    }
}
