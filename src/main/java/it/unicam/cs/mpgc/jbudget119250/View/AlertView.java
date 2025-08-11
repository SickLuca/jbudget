package it.unicam.cs.mpgc.jbudget119250.View;

import javafx.scene.control.Alert;

/**
 * Utility for displaying alert messages within the application.
 * It provides a static method to create and display an alert dialog with a specified
 * title, message, and type.
 */
public class AlertView {

    /**
     * This method creates and configures an alert based on the provided parameters
     * and shows it to the user, waiting for the user to acknowledge it.
     *
     * @param title the title of the alert dialog
     * @param message the content of the alert dialog
     * @param type the type of the alert (e.g., INFORMATION, WARNING, ERROR)
     */
    public static void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
