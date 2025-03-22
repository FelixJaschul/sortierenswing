package Sort;

// Heap sort implementation - via Claude 3.7
public class Heap implements SortAlgorithm {

    private int heapSize;
    private int currentNode;
    private int compareIndex;
    private boolean heapifyInProgress;
    private boolean stepComplete;
    private boolean buildingHeap;

    @Override
    public String getName() {
        return "Heap Sort";
    }

    @Override
    public void resetForNewStep(int[] array, int step) {
        stepComplete = false;

        if (step == 0) {
            // First step: initialize
            heapSize = array.length;
            buildingHeap = true;
            currentNode = (heapSize / 2) - 1; // Start from last non-leaf node
            heapifyInProgress = false;
        } else if (buildingHeap) {
            // Continue building heap
            if (currentNode < 0) {
                // Transition to extract phase
                buildingHeap = false;
                heapSize = array.length;
            }
        }

        // If we're done building the heap and have extracted all elements
        if (!buildingHeap && heapSize <= 1) {
            stepComplete = true;
            return;
        }

        // If we're not currently heapifying, start a new heapify operation
        if (!heapifyInProgress) {
            int heapifyRoot;
            if (buildingHeap) {
                // Start heapify on current node in build phase
                heapifyRoot = currentNode;
            } else {
                // In extract phase, we first swap root with last element
                int temp = array[0];
                array[0] = array[heapSize - 1];
                array[heapSize - 1] = temp;

                // Reduce heap size and heapify the root
                heapSize--;
                heapifyRoot = 0;
            }

            heapifyInProgress = true;
            compareIndex = heapifyRoot;
        }
    }

    @Override
    public boolean microStep(int[] array) {
        if (stepComplete) {
            return true;
        }

        // If we're heapifying
        if (heapifyInProgress) {
            int left = 2 * compareIndex + 1;
            int right = 2 * compareIndex + 2;
            int largest = compareIndex;

            // Find largest among root, left child and right child
            if (left < heapSize && array[left] > array[largest]) {
                largest = left;
            }

            if (right < heapSize && array[right] > array[largest]) {
                largest = right;
            }

            // If largest is not root
            if (largest != compareIndex) {
                // Swap
                int temp = array[compareIndex];
                array[compareIndex] = array[largest];
                array[largest] = temp;

                // Continue heapify on the affected subtree
                compareIndex = largest;
                return false;
            } else {
                // This subtree is heapified
                heapifyInProgress = false;

                if (buildingHeap) {
                    // Move to next node in build phase
                    currentNode--;
                    if (currentNode < 0) {
                        // Build phase complete
                        buildingHeap = false;
                    }
                }

                // This step is complete
                stepComplete = true;
                return true;
            }
        }

        // Should not reach here
        stepComplete = true;
        return true;
    }
}
