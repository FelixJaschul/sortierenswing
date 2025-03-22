package Sort;

// Bubble sort implementation - via Claude 3.7
public class Bubble implements SortAlgorithm {

    private int outerIndex = 0;
    private int innerIndex = 0;
    private boolean stepComplete = false;
    private State currentState = State.COMPARING;

    @Override
    public String getName() {
        return "Bubble Sort";
    }

    @Override
    public void resetForNewStep(int[] array, int step) {
        outerIndex = step;
        innerIndex = 0;
        currentState = State.COMPARING;
        stepComplete = false;
    }

    @Override
    public boolean microStep(int[] array) {
        if (stepComplete) return true;

        // If we've reached the end of this pass
        if (innerIndex >= array.length - 1 - outerIndex) {
            stepComplete = true;
            return true;
        }

        if (currentState == State.COMPARING) {
            if (array[innerIndex] > array[innerIndex + 1]) {
                currentState = State.SWAPPING;
            } else {
                innerIndex++;
            }
            return false;
        }

        if (currentState == State.SWAPPING) {
            // Swap elements
            int temp = array[innerIndex];
            array[innerIndex] = array[innerIndex + 1];
            array[innerIndex + 1] = temp;

            innerIndex++;
            currentState = State.COMPARING;
            return false;
        }

        return false;
    }

    private enum State {COMPARING, SWAPPING}
}
