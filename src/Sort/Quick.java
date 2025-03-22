package Sort;

// Quick sort implementation - via Claude 3.7
public class Quick implements SortAlgorithm {

    private int[] subArrayStack;
    private int stackSize;

    private int pivotIndex;
    private int left;
    private int right;
    private int i, j;
    private int pivotValue;
    private State state;
    private boolean stepComplete;
    public Quick() {
        subArrayStack = new int[100]; // Can store up to 50 partitions
        stackSize = 0;
    }

    @Override
    public String getName() {
        return "Quick Sort";
    }

    @Override
    public void resetForNewStep(int[] array, int step) {
        // First step: initialize with the whole array
        if (step == 0) {
            stackSize = 0;
            // Push initial partition (whole array)
            pushSubArray(0, array.length - 1);
        }

        // If stack is empty, we're done
        if (stackSize == 0) {
            stepComplete = true;
            return;
        }

        // Pop next subarray to process
        popSubArray();

        // Skip trivial cases
        if (left >= right) {
            stepComplete = true;
            return;
        }

        // Initialize partition state
        pivotIndex = right;  // Choose rightmost element as pivot
        pivotValue = array[pivotIndex];
        i = left - 1;        // Index of smaller element
        j = left;            // Current element to compare

        state = State.START_PARTITION;
        stepComplete = false;
    }

    private void pushSubArray(int l, int r) {
        if (l < r) {  // Only push if there's work to do
            // Ensure stack has space
            if (stackSize + 2 >= subArrayStack.length) {
                int[] newStack = new int[subArrayStack.length * 2];
                System.arraycopy(subArrayStack, 0, newStack, 0, stackSize);
                subArrayStack = newStack;
            }

            // Push the subarray bounds
            subArrayStack[stackSize++] = l;
            subArrayStack[stackSize++] = r;
        }
    }

    private void popSubArray() {
        if (stackSize >= 2) {
            right = subArrayStack[--stackSize];
            left = subArrayStack[--stackSize];
        }
    }

    @Override
    public boolean microStep(int[] array) {
        if (stepComplete) {
            return true;
        }

        switch (state) {
            case START_PARTITION:
                // Start the partition process
                state = State.SCANNING;
                return false;

            case SCANNING:
                // If we've scanned all elements
                if (j >= right) {
                    state = State.SWAP_PIVOT;
                    return false;
                }

                // Compare current element with pivot
                if (array[j] <= pivotValue) {
                    i++;
                    if (i != j) {
                        state = State.SWAP_ELEMENTS;
                    } else {
                        j++;
                    }
                } else {
                    j++;
                }
                return false;

            case SWAP_ELEMENTS:
                // Swap elements
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;

                j++;
                state = State.SCANNING;
                return false;

            case SWAP_PIVOT:
                // Place pivot in its correct position
                int pivotFinalPos = i + 1;

                // Only swap if needed
                if (pivotFinalPos != pivotIndex) {
                    temp = array[pivotFinalPos];
                    array[pivotFinalPos] = array[pivotIndex];
                    array[pivotIndex] = temp;
                }

                state = State.FINISH_PARTITION;
                return false;

            case FINISH_PARTITION:
                // Pivot is now at position i+1
                int p = i + 1;

                // Push subarrays for future processing (right first, then left)
                pushSubArray(p + 1, right);
                pushSubArray(left, p - 1);

                stepComplete = true;
                return true;
        }

        return false;
    }

    private enum State {
        START_PARTITION, SCANNING, SWAP_ELEMENTS, SWAP_PIVOT, FINISH_PARTITION
    }
}
