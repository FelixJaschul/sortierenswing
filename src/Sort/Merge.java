package Sort;

// Merge sort implementation - via Claude 3.7
public class Merge implements SortAlgorithm {

    // State variables for visualization
    private int[] tempArray;
    private int leftIndex;
    private int rightIndex;
    private int mergeIndex;
    private int currentStep;
    private boolean stepComplete;
    private State currentState;

    @Override
    public String getName() {
        return "Merge Sort";
    }

    @Override
    public void resetForNewStep(int[] array, int step) {
        currentStep = step;
        stepComplete = false;

        // For step 0, we need to initialize the temp array
        if (step == 0) {
            tempArray = new int[array.length];
            System.arraycopy(array, 0, tempArray, 0, array.length);
        }

        // Calculate the current merge parameters based on the step
        int width = (int) Math.pow(2, step);

        // If width is greater than or equal to array length, sorting is complete
        if (width >= array.length) {
            stepComplete = true;
            return;
        }

        // Initialize indices for the current merge step
        leftIndex = 0;
        rightIndex = 0;
        mergeIndex = 0;
        currentState = State.COMPARING;

        // Copy the array to tempArray for this step
        System.arraycopy(array, 0, tempArray, 0, array.length);
    }

    @Override
    public boolean microStep(int[] array) {
        // If step is complete, return true
        if (stepComplete) {
            return true;
        }

        // Calculate the current merge parameters based on the step
        int width = (int) Math.pow(2, currentStep);

        // If width is greater than or equal to array length, sorting is complete
        if (width >= array.length) {
            stepComplete = true;
            return true;
        }

        // Calculate the current merge boundaries
        int left = mergeIndex * width * 2;
        int mid = Math.min(left + width, array.length);
        int right = Math.min(left + width * 2, array.length);

        // If we've processed all merges for this step
        if (left >= array.length) {
            stepComplete = true;
            return true;
        }

        // Perform one micro-step of the merge operation
        switch (currentState) {
            case COMPARING:
                // If both subarrays have elements, compare them
                if (leftIndex < mid - left && rightIndex < right - mid) {
                    if (array[left + leftIndex] <= array[mid + rightIndex]) {
                        tempArray[left + leftIndex + rightIndex] = array[left + leftIndex];
                        leftIndex++;
                    } else {
                        tempArray[left + leftIndex + rightIndex] = array[mid + rightIndex];
                        rightIndex++;
                    }
                    return false;
                }
                // If only left subarray has elements
                else if (leftIndex < mid - left) {
                    currentState = State.COPYING_LEFT;
                    return false;
                }
                // If only right subarray has elements
                else if (rightIndex < right - mid) {
                    currentState = State.COPYING_RIGHT;
                    return false;
                }
                // If both subarrays are empty, move to the next merge
                else {
                    // Copy the merged section back to the original array
                    System.arraycopy(tempArray, left, array, left, right - left);

                    // Move to the next merge
                    mergeIndex++;
                    leftIndex = 0;
                    rightIndex = 0;
                    currentState = State.COMPARING;
                    return false;
                }

            case COPYING_LEFT:
                // Copy remaining elements from left subarray
                tempArray[left + leftIndex + rightIndex] = array[left + leftIndex];
                leftIndex++;

                // If we've copied all elements from left subarray, move to the next merge
                if (leftIndex >= mid - left) {
                    // Copy the merged section back to the original array
                    System.arraycopy(tempArray, left, array, left, right - left);

                    // Move to the next merge
                    mergeIndex++;
                    leftIndex = 0;
                    rightIndex = 0;
                    currentState = State.COMPARING;
                }
                return false;

            case COPYING_RIGHT:
                // Copy remaining elements from right subarray
                tempArray[left + leftIndex + rightIndex] = array[mid + rightIndex];
                rightIndex++;

                // If we've copied all elements from right subarray, move to the next merge
                if (rightIndex >= right - mid) {
                    // Copy the merged section back to the original array
                    System.arraycopy(tempArray, left, array, left, right - left);

                    // Move to the next merge
                    mergeIndex++;
                    leftIndex = 0;
                    rightIndex = 0;
                    currentState = State.COMPARING;
                }
                return false;
        }

        return false;
    }

    // States for the merge sort algorithm
    private enum State {COMPARING, COPYING_LEFT, COPYING_RIGHT}
}
