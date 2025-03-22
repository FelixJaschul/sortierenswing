package Sort;

// Interface for all sorting algorithms that can be visualized
public interface SortAlgorithm {

    // Get the algorithm name
    String getName();

    // Reset the algorithm state for a new step
    void resetForNewStep(int[] array, int step);

    // Perform a single micro-step of the algorithm
    boolean microStep(int[] array);
}

