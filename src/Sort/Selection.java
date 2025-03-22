package Sort;

// Heap sort implementation - via Claude 3.7
public class Selection implements SortAlgorithm {

    private int currentStep;
    private int minIndex;
    private int compareIndex;
    private boolean stepComplete;
    private boolean findingMin;

    @Override
    public String getName() {
        return "Selection Sort";
    }

    @Override
    public void resetForNewStep(int[] array, int step) {
        currentStep = step;
        stepComplete = false;

        // If we've processed all elements, we're done
        if (step >= array.length - 1) {
            stepComplete = true;
            return;
        }

        // Start finding minimum in the unsorted part of the array
        minIndex = step;
        compareIndex = step + 1;
        findingMin = true;
    }

    @Override
    public boolean microStep(int[] array) {
        if (stepComplete) {
            return true;
        }

        if (findingMin) {
            // If we've compared with all elements in the unsorted part
            if (compareIndex >= array.length) {
                findingMin = false;
                return false;
            }

            // Compare current element with current minimum
            if (array[compareIndex] < array[minIndex]) {
                minIndex = compareIndex;
            }

            // Move to next element
            compareIndex++;
            return false;
        } else {
            // Swap the found minimum with the first element of unsorted part
            if (minIndex != currentStep) {
                int temp = array[currentStep];
                array[currentStep] = array[minIndex];
                array[minIndex] = temp;
            }

            // This step is complete
            stepComplete = true;
            return true;
        }
    }
}
