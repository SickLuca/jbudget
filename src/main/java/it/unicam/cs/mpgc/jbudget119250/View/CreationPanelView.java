package it.unicam.cs.mpgc.jbudget119250.View;

import it.unicam.cs.mpgc.jbudget119250.Controller.Controller;
import it.unicam.cs.mpgc.jbudget119250.Controller.DefaultJpaController;
import it.unicam.cs.mpgc.jbudget119250.Model.Abstractions.AbstractCategory;
import it.unicam.cs.mpgc.jbudget119250.Model.Abstractions.Profile;
import it.unicam.cs.mpgc.jbudget119250.Model.Entities.DefaultCategory;
import it.unicam.cs.mpgc.jbudget119250.Model.Entities.DefaultTag;
import it.unicam.cs.mpgc.jbudget119250.Model.Entities.DefaultUser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;

import java.util.ResourceBundle;

/**
 * Class responsible for managing the graphical user interface
 * for creating new categories and user profiles. It implements the JavaFX {@link Initializable}
 * interface to initialize its components and set up event handling logic.

 * Functionalities of this class include:
 * - Setting up event handlers for user interaction events such as button clicks.
 * - Validating user input to ensure compliance with specified criteria.
 * - Interacting with a database via generic JPA controllers for saving and fetching entities.
 */

public class CreationPanelView implements Initializable {

    @FXML
    private TextField categoryNameTextField;

    @FXML
    private ComboBox<AbstractCategory> parentComboBox;

    @FXML
    private Button createNewCategoryButton;

    @FXML
    private TextField profileNameTextField;

    @FXML
    private TextField profileSurnameTextField;

    @FXML
    private Button createNewProfileButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeComboBox();

        createNewCategoryButton.setOnAction(event -> handleCreateCategory());
        createNewProfileButton.setOnAction(event -> handleCreateProfile());
    }

    private void initializeComboBox() {
        parentComboBox.getItems().addAll(
                new DefaultJpaController<>(AbstractCategory.class).getAll());
    }

    private void handleCreateProfile() {
        DefaultUser user = new DefaultUser();
        user.setName(profileNameTextField.getText());
        user.setSurname(profileSurnameTextField.getText());

        if (!validateProfile(user)) {
            AlertView.showAlert("Invalid profile", "Name and surname must not be blank and must not contain spaces, numbers or special characters", Alert.AlertType.ERROR);
            return;
        }

        Controller<DefaultUser> userController = new DefaultJpaController<>(DefaultUser.class);
        userController.save(user);
        AlertView.showAlert("Profile created", "Profile successfully created", Alert.AlertType.INFORMATION);
    }

    private void handleCreateCategory() {
        Controller<DefaultCategory> categoryController = new DefaultJpaController<>(DefaultCategory.class);

        DefaultCategory category = new DefaultCategory();
        category.setName(categoryNameTextField.getText());
        category.setParent(parentComboBox.getSelectionModel().getSelectedItem());

        if (!validateCategory(category, categoryController)) {
            AlertView.showAlert("Invalid category", "Category name must not be blank and must not already exists", Alert.AlertType.ERROR);
            return;
        }

        categoryController.save(category);
        initializeComboBox();

        createAssociatedTag(category);
    }

    private void createAssociatedTag(DefaultCategory category) {
        DefaultTag tag = new DefaultTag();
        tag.setCategory(category);
        tag.setName(category.getFullPath());

        Controller<DefaultTag> tagController = new DefaultJpaController<>(DefaultTag.class);
        tagController.save(tag);
        AlertView.showAlert("Category created", "Category successfully created", Alert.AlertType.INFORMATION);
    }

    private boolean validateCategory(DefaultCategory category, Controller<DefaultCategory> categoryController) {
        if (category.getName().isBlank()) {
            AlertView.showAlert("Blank Fields", "Category name must not be blank", Alert.AlertType.ERROR);
            return false;
        }

        if (categoryController.getAll().stream()
                .map(AbstractCategory::getName)
                .anyMatch(c -> c.equalsIgnoreCase(category.getName()) ) ) {
            AlertView.showAlert("Category already exists", "Category already exists", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private boolean validateProfile(Profile profile) {
        if (profile.getName().isBlank() || profile.getSurname().isBlank()) {
            AlertView.showAlert("Blank Fields", "Name and surname must not be blank", Alert.AlertType.ERROR);
            return false;
        }
        if (!profile.getName().matches("[a-zA-Z]+") || !profile.getSurname().matches("[a-zA-Z]+")) {
            AlertView.showAlert("Invalid fields", "Name and surname must not contain spaces, numbers or special characters", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }
}
