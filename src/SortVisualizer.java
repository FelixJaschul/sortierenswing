import Sort.Bogo;
import Sort.Bubble;
import Sort.Heap;
import Sort.Merge;
import Sort.Quick;
import Sort.Radix;
import Sort.Selection;
import Sort.SortAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SortVisualizer extends JFrame {

    private final int[] array;
    private final JPanel chartPanel;
    private final Timer timer;
    private SortAlgorithm algorithm;
    private int currentStep = 0;
    private boolean isRunning = false;
    private JPopupMenu popupMenu;
    private Color barColor = new Color(163, 191, 213);
    private Color highlightColor = barColor;
    private int DELAY = 20;

    public SortVisualizer(int size, SortAlgorithm algorithm) {
        // Create and shuffle array
        array = createShuffledArray(size);
        this.algorithm = algorithm;

        // Set up the frame
        setTitle(algorithm.getName() + " Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Create chart panel with original white background
        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawChart(g);

                // Draw the green circle if isRunning is true
                if (isRunning) drawCircle(g);
            }
        };
        chartPanel.setBackground(Color.WHITE);

        // Create popup menu
        createPopupMenu();

        // Add mouse listener to chart panel for right-click events
        chartPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopupMenu(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopupMenu(e);
                }
            }
        });

        // Add chart panel to frame
        add(chartPanel, BorderLayout.CENTER);

        // Set up timer for animation
        // milliseconds between micro-steps
        timer = new Timer(DELAY, e -> performMicroStep());

        // Initialize
        algorithm.resetForNewStep(array, currentStep);

        // Show the frame
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createPopupMenu() {
        // Creates Pop Up Menu for Settings, ...
        popupMenu = new JPopupMenu() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // white background
                g2d.setColor(new Color(255, 255, 255));
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Add a subtle border
                g2d.setColor(new Color(220, 220, 220));
                g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

                g2d.dispose();
            }

            // Override to ensure the popup doesn't paint outside our rounded rectangle
            @Override
            public void show(Component invoker, int x, int y) {
                // Set the popup menu to be non-opaque
                setOpaque(false);

                // Make all components in the popup non-opaque
                for (Component component : getComponents()) {
                    if (component instanceof JComponent) {
                        ((JComponent) component).setOpaque(false);
                    }
                }

                super.show(invoker, x, y);
            }
        };

        // Set border to create padding inside the rounded rectangle
        popupMenu.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JMenu settingsMenu = createStyledMenu("Settings"); // Settings submenu
        JMenu colorMenu = createStyledMenu("Colors"); // Color settings submenu

        // Bar color options
        JMenuItem blueBarItem = createStyledMenuItem("Blue Bars");
        blueBarItem.addActionListener(e -> {
            barColor = new Color(163, 191, 213);
            highlightColor = barColor;
            // Force menu recreation to update highlight colors
            createPopupMenu();
            chartPanel.repaint();
        });

        JMenuItem greenBarItem = createStyledMenuItem("Green Bars");
        greenBarItem.addActionListener(e -> {
            barColor = new Color(144, 203, 144);
            highlightColor = barColor;
            // Force menu recreation to update highlight colors
            createPopupMenu();
            chartPanel.repaint();
        });

        JMenuItem redBarItem = createStyledMenuItem("Red Bars");
        redBarItem.addActionListener(e -> {
            barColor = new Color(203, 144, 144);
            highlightColor = barColor;
            // Force menu recreation to update highlight colors
            createPopupMenu();
            chartPanel.repaint();
        });

        JMenuItem delayItem = createStyledMenuItem("Delay"); // Delay setting menu item
        delayItem.addActionListener(e -> {
            // Create a custom input dialog for delay setting
            JDialog delayDialog = new JDialog(this, "Set Animation Delay", true);
            delayDialog.setLayout(new BorderLayout());
            delayDialog.setSize(350, 180);
            delayDialog.setMinimumSize(new Dimension(300, 150));
            delayDialog.setLocationRelativeTo(this);
            delayDialog.setResizable(true);

            // Create main content panel with a more flexible layout
            JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

            // Create a styled panel for the input field
            JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
            inputPanel.setBackground(new Color(245, 245, 245));
            inputPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            // Create a label explaining what the delay is
            JLabel label = new JLabel("Enter delay in milliseconds (1-1000):");
            label.setFont(label.getFont().deriveFont(Font.BOLD));

            // Create a text field with the current delay as default
            JTextField delayField = new JTextField(String.valueOf(DELAY), 10);
            delayField.setFont(new Font("SansSerif", Font.PLAIN, 14));
            delayField.setHorizontalAlignment(JTextField.CENTER);

            // Add a hint about what the delay affects
            JLabel hintLabel = new JLabel("Lower values = faster animation");
            hintLabel.setFont(hintLabel.getFont().deriveFont(Font.ITALIC, 11f));
            hintLabel.setForeground(new Color(100, 100, 100));

            // Create a panel for the buttons with some spacing
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            buttonPanel.setOpaque(false);

            // Create Apply and Cancel buttons with consistent sizing
            JButton applyButton = new JButton("Apply");
            JButton cancelButton = new JButton("Cancel");

            // Make buttons the same size
            Dimension buttonSize = new Dimension(100, 30);
            applyButton.setPreferredSize(buttonSize);
            cancelButton.setPreferredSize(buttonSize);

            // Add action listener to Apply button
            applyButton.addActionListener(event -> {
                try {
                    // Parse the input value
                    int newDelay = Integer.parseInt(delayField.getText().trim());

                    // Validate the input (ensure it's within reasonable bounds)
                    if (newDelay >= 1 && newDelay <= 1000) {
                        // Update the delay
                        DELAY = newDelay;

                        // Update the timer delay
                        timer.setDelay(DELAY);

                        // Close the dialog
                        delayDialog.dispose();
                    } else {
                        // Show error message for invalid range
                        JOptionPane.showMessageDialog(delayDialog,
                                "Please enter a value between 1 and 1000.",
                                "Invalid Input",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    // Show error message for invalid input
                    JOptionPane.showMessageDialog(delayDialog,
                            "Please enter a valid number.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                }
            });

            // Add action listener to Cancel button
            cancelButton.addActionListener(event -> delayDialog.dispose());

            // Add glue to push buttons to the right
            buttonPanel.add(Box.createHorizontalGlue());
            buttonPanel.add(applyButton);
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Space between buttons
            buttonPanel.add(cancelButton);

            // Add components to the input panel
            JPanel labelPanel = new JPanel(new BorderLayout());
            labelPanel.setOpaque(false);
            labelPanel.add(label, BorderLayout.NORTH);
            labelPanel.add(Box.createRigidArea(new Dimension(0, 5)), BorderLayout.CENTER);
            labelPanel.add(hintLabel, BorderLayout.SOUTH);

            inputPanel.add(labelPanel, BorderLayout.NORTH);
            inputPanel.add(delayField, BorderLayout.CENTER);

            // Add panels to the content panel
            contentPanel.add(inputPanel, BorderLayout.CENTER);
            contentPanel.add(buttonPanel, BorderLayout.SOUTH);

            // Add content panel to dialog
            delayDialog.add(contentPanel, BorderLayout.CENTER);

            // Set default button and focus
            delayDialog.getRootPane().setDefaultButton(applyButton);
            delayField.requestFocusInWindow();

            // Select all text in the field for easy editing
            delayField.selectAll();

            // Show the dialog
            delayDialog.setVisible(true);
        });

        // Add color options to color menu
        colorMenu.add(blueBarItem);
        colorMenu.add(greenBarItem);
        colorMenu.add(redBarItem);
        // Add color menu to settings menu
        settingsMenu.add(colorMenu);
        // Add Delay setter to settings menu
        settingsMenu.add(delayItem);

        // Sorting Algorithms submenu
        JMenu algorithmsMenu = createStyledMenu("Sorting Algorithms");

        // Add all available sorting algorithms
        addAlgorithmMenuItem(algorithmsMenu, "Bubble Sort", new Bubble());
        addAlgorithmMenuItem(algorithmsMenu, "Heap Sort", new Heap());
        addAlgorithmMenuItem(algorithmsMenu, "Quick Sort", new Quick());
        addAlgorithmMenuItem(algorithmsMenu, "Merge Sort", new Merge());
        addAlgorithmMenuItem(algorithmsMenu, "Selection Sort", new Selection());
        addAlgorithmMenuItem(algorithmsMenu, "Bogo Sort", new Bogo());
        addAlgorithmMenuItem(algorithmsMenu, "Radix Sort", new Radix());

        // Restart menu item
        JMenuItem restartItem = createStyledMenuItem("Restart");
        restartItem.addActionListener(e -> {
            resetVisualization();
            timer.start();
            isRunning = true;
            chartPanel.repaint();
        });

        // Start/Pause menu item (dynamic text based on current state)
        JMenuItem startPauseItem = createStyledMenuItem(isRunning ? "Pause" : "Start");
        startPauseItem.addActionListener(e -> {
            toggleRunning();
            popupMenu.setVisible(false);
        });

        // Add all items to the popup menu
        popupMenu.add(settingsMenu);
        popupMenu.add(algorithmsMenu);

        // Custom separator with Apple-like styling
        popupMenu.add(createStyledSeparator());

        popupMenu.add(restartItem);
        popupMenu.add(startPauseItem);
    }

    // Helper method to create styled menu items
    private JMenuItem createStyledMenuItem(String text) {
        Color gray = new Color(50, 50, 50);
        JMenuItem item = new JMenuItem(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Check if the item is selected (hovered)
                if (getModel().isArmed()) {
                    // Highlight color when hovered
                    g2d.setColor(highlightColor);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }

                // Draw text
                g2d.setColor(gray);
                FontMetrics fm = g2d.getFontMetrics();
                int x = 10;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(getText(), x, y);

                g2d.dispose();
            }
        };

        // Set foreground color (not used in paintComponent but helps with UI manager)
        item.setForeground(gray);

        // Remove default background and border
        item.setOpaque(false);
        item.setBorderPainted(false);
        item.setContentAreaFilled(false);

        // Add some padding
        item.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        return item;
    }

    // Helper method to create styled menus
    private JMenu createStyledMenu(String text) {
        JMenu menu = getJMenu(text);

        // Style the popup menu that appears when clicking this menu
        JPopupMenu subMenu = menu.getPopupMenu();
        subMenu.setOpaque(false);
        subMenu.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Apply Apple-like styling to the submenu
        subMenu.setUI(new javax.swing.plaf.basic.BasicPopupMenuUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // white background
                g2d.setColor(new Color(255, 255, 255, 240));
                g2d.fillRect(0, 0, c.getWidth(), c.getHeight());

                // Add a subtle border
                g2d.setColor(new Color(220, 220, 220));
                g2d.drawRect(0, 0, c.getWidth() - 1, c.getHeight() - 1);

                g2d.dispose();

                // Paint the menu items
                super.paint(g, c);
            }
        });

        return menu;
    }

    private JMenu getJMenu(String text) {
        JMenu menu = new JMenu(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Check if the menu is selected (hovered)
                if (getModel().isArmed() || getModel().isSelected()) {
                    // Highlight color when hovered (Apple-like light blue highlight)
                    g2d.setColor(highlightColor);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }

                // Draw text
                g2d.setColor(new Color(50, 50, 50));
                FontMetrics fm = g2d.getFontMetrics();
                int x = 10;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(getText(), x, y);

                g2d.dispose();
            }
        };

        // Set foreground color
        menu.setForeground(new Color(50, 50, 50));

        // Remove default background and border
        menu.setOpaque(false);
        menu.setBorderPainted(false);
        menu.setContentAreaFilled(false);

        // Add some padding
        menu.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return menu;
    }

    // Helper method to create a styled separator
    private JSeparator createStyledSeparator() {
        JSeparator separator = new JSeparator() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw a subtle line
                g2d.setColor(new Color(200, 200, 200));
                g2d.drawLine(10, getHeight() / 2, getWidth() - 10, getHeight() / 2);

                g2d.dispose();
            }
        };

        // Set height for the separator
        separator.setPreferredSize(new Dimension(0, 10));
        separator.setOpaque(false);

        return separator;
    }

    private void addAlgorithmMenuItem(JMenu menu, String name, SortAlgorithm alg) {
        JMenuItem item = createStyledMenuItem(name);
        item.addActionListener(e -> {
            // Change the algorithm
            changeAlgorithm(alg);
        });
        menu.add(item);
    }

    private void changeAlgorithm(SortAlgorithm newAlgorithm) {
        // Stop current visualization
        timer.stop();
        isRunning = false;

        // Change algorithm
        algorithm = newAlgorithm;

        // Reset visualization with new algorithm
        resetVisualization();

        // Update title
        setTitle(algorithm.getName() + " Visualization");

        // Repaint
        chartPanel.repaint();
    }

    private void showPopupMenu(MouseEvent e) {
        // Recreate the popup menu to ensure it's updated with current state
        createPopupMenu();

        // Show the popup menu at the mouse position
        popupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    private void toggleRunning() {
        // If sorting is complete, restart
        if (isSorted()) {
            resetVisualization();
            timer.start();
            isRunning = true;
            return;
        }

        // Otherwise toggle between running and paused
        if (!isRunning) {
            // Start the visualization
            timer.start();
            isRunning = true;
        } else {
            // Stop the visualization
            timer.stop();
            isRunning = false;
        }
        chartPanel.repaint();
    }

    private void drawCircle(Graphics g) {
        int radius = 5;
        int x = 10; // chartPanel.getWidth() - radius - 10 => put this if top right
        int y = 10; // chartPanel.getHeight() - radius - 10 ==> put both if bottom right

        g.setColor(Color.GREEN);
        g.fillOval(x, y, radius * 2, radius * 2);
    }

    private void performMicroStep() {
        boolean stepCompleted = algorithm.microStep(array);

        if (stepCompleted) {
            currentStep++;
            if (isSorted()) {
                timer.stop();
                isRunning = false;
            } else {
                algorithm.resetForNewStep(array, currentStep);
            }
        }

        chartPanel.repaint();
    }

    private void resetVisualization() {
        timer.stop();
        isRunning = false;
        currentStep = 0;

        // Reset array
        int[] newArray = createShuffledArray(array.length);
        System.arraycopy(newArray, 0, array, 0, array.length);

        // Reset algorithm
        algorithm.resetForNewStep(array, currentStep);

        chartPanel.repaint();
    }

    private void drawChart(Graphics g) {
        int[] sums = Chart.getSums(array);
        int[] percentages = Chart.getPercentages(sums);

        int width = chartPanel.getWidth();
        int height = chartPanel.getHeight();
        int barWidth = width / sums.length;

        // Draw bars with current bar color
        for (int i = 0; i < percentages.length; i++) {
            int barHeight = (percentages[i] * height) / 100;
            g.setColor(barColor);
            g.fillRect(i * barWidth, height - barHeight, barWidth - 1, barHeight);
        }
    }

    private boolean isSorted() {
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) return false;
        }
        return true;
    }

    private int[] createShuffledArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) array[i] = i + 1; // Values from 1 to size

        // Shuffle the array (Fisher-Yates algorithm)
        java.util.Random rnd = new java.util.Random();
        for (int i = size - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Swap
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return array;
    }
}
