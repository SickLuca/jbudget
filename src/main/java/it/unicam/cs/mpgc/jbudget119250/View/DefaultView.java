package it.unicam.cs.mpgc.jbudget119250.View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * This class is a controller for managing navigation between different views
 * in an application. It implements the {@link Initializable} interface*/
public class DefaultView implements Initializable {

@FXML
private Button goToMovementsButton;

@FXML
private Button goToStatisticsManagerButton;

@FXML
private Button goToCreationPanelButton;

@FXML
private StackPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupEventHandlers();
        goToMovements();
    }

    private void setupEventHandlers() {
        goToCreationPanelButton.setOnAction(event -> goToCreationPanel());
        goToStatisticsManagerButton.setOnAction(event -> goToStaticsManager());
        goToMovementsButton.setOnAction((event -> goToMovements()));
    }

    private void goPage(String path) throws IOException {
        URL fxmlUrl = getClass().getResource("/" + path);
        if (fxmlUrl == null) {
            throw new IllegalArgumentException("FXML file not found: " + path);
        }
        Parent root = FXMLLoader.load(fxmlUrl);
        rootPane.getChildren().setAll(root);
    }

    public void goToMovements() {
        try {
            goPage("Scenes/HomeScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToStaticsManager() {
        try {
            goPage("Scenes/StaticsManagerScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToCreationPanel(){
        try {
            goPage("Scenes/CreationPanelScene.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
