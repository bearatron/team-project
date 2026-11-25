package app;

import javax.swing.*;

/**
 * The entry point of the application.
 */
public class Main {

    /**
     * Builds the application window and displays it.
     *
     * @param args the command-line arguments (unused)
     */
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();

        // add additional builder method calls when use cases are implemented
        JFrame application = appBuilder
                .addTruckListView()
                .build();
        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
