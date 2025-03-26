import Sort.*;

import javax.swing.*;

// Main class to start the application
class Main {

    public static void main(String[] args) { // size 40 is optimal
        SwingUtilities.invokeLater(() -> new SortVisualizer(40, new Radix()));
    }
}
