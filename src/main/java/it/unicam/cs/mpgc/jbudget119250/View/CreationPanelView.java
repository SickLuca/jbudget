package it.unicam.cs.mpgc.jbudget119250.View;

import it.unicam.cs.mpgc.jbudget119250.Controller.Controller;
import it.unicam.cs.mpgc.jbudget119250.Controller.DefaultJpaController;
import it.unicam.cs.mpgc.jbudget119250.Model.Abstractions.AbstractCategory;
import it.unicam.cs.mpgc.jbudget119250.Model.Abstractions.AbstractMovement;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Class responsible for managing the graphical user interface
 * for creating new categories and user profiles. It implements the JavaFX {@link Initializable}
 * interface to initialize its components and set up event handling logic.

 * Functionalities of this class include:
 * - Setting up event handlers for user interaction events such as button clicks.
 * - Validating user input to ensure compliance with specified criteria.
 * - Interacting with a database via generic JPA controllers for saving, deleting and fetching entities.
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

    @FXML
    private ComboBox<Profile> removeProfileComboBox;

    @FXML
    private Button removeProfileButton;

    @FXML
    private ComboBox<AbstractCategory> removeCategoryComboBox;

    @FXML
    private Button removeCategoryButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeComboBox();
        setUpEventHandlers();
    }

    private void setUpEventHandlers() {
        createNewCategoryButton.setOnAction(event -> handleCreateCategory());
        createNewProfileButton.setOnAction(event -> handleCreateProfile());
        removeProfileButton.setOnAction(event -> handleRemoveProfile());
        removeCategoryButton.setOnAction(event -> handleRemoveCategory());
    }

    private void initializeComboBox() {
        parentComboBox.getItems().clear();
        parentComboBox.getItems().addAll(
                new DefaultJpaController<>(AbstractCategory.class).getAll());

        removeCategoryComboBox.getItems().clear();
        removeCategoryComboBox.getItems().addAll(parentComboBox.getItems());

        removeProfileComboBox.getItems().clear();
        removeProfileComboBox.getItems().addAll(
                new DefaultJpaController<>(DefaultUser.class).getAll());
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

        initializeComboBox();
        AlertView.showAlert("Profile created", "Profile successfully created", Alert.AlertType.INFORMATION);
    }

    private void handleCreateCategory() {
        Controller<AbstractCategory> categoryController = new DefaultJpaController<>(AbstractCategory.class);

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

    private boolean validateCategory(DefaultCategory category, Controller<AbstractCategory> categoryController) {
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

    private void handleRemoveProfile() {
        Profile selectedProfile = removeProfileComboBox.getSelectionModel().getSelectedItem();
        Controller<AbstractMovement> movementController = new DefaultJpaController<>(AbstractMovement.class);

        boolean hasMovements = movementController.getAll().stream()
                .map(AbstractMovement::getUser)
                .map(DefaultUser::getId)
                .anyMatch(id -> id.equals(selectedProfile.getId()));

        if (hasMovements) {
            AlertView.showAlert("User actually in use", "Selected user has associated movements", Alert.AlertType.ERROR);
            return;
        }

        Controller<DefaultUser> profileController = new DefaultJpaController<>(DefaultUser.class);
        profileController.delete(selectedProfile.getId());

        initializeComboBox();
        AlertView.showAlert("Profile removed", "Profile successfully deleted", Alert.AlertType.INFORMATION);
    }


    private void handleRemoveCategory() {
        AbstractCategory selectedCategory = removeCategoryComboBox.getSelectionModel().getSelectedItem();

        if (selectedCategory == null) {
            AlertView.showAlert("No category selected", "Please select a category to remove", Alert.AlertType.ERROR);
            return;
        }

        Controller<AbstractMovement> movementController = new DefaultJpaController<>(AbstractMovement.class);

        // Raccoglie tutte le categorie utilizzate dai movimenti
        List<Long> usedCategoriesIds = getUsedCategoriesIds(movementController);

        // Controllo se la categoria o le sue sottocategorie sono in uso
        if (isCategoryInUse(selectedCategory, usedCategoriesIds)) {
            AlertView.showAlert("Category actually in use", "Selected category or its subcategories have associated movements", Alert.AlertType.ERROR);
            return;
        }

        removeCategoryRecursively(selectedCategory);

        initializeComboBox();
        AlertView.showAlert("Category successfully removed", "Category successfully deleted", Alert.AlertType.INFORMATION);
    }

    private List<Long> getUsedCategoriesIds(Controller<AbstractMovement> movementController) {
        return movementController.getAll().stream()
                .flatMap(movement -> movement.getTag().stream()) // Appiattisce la lista dei tag per ogni movimento
                .map(DefaultTag::getCategory) // Ottiene la categoria per ogni tag
                .filter(Objects::nonNull) // Filtra eventuali categorie nulle
                .map(AbstractCategory::getId)
                .distinct() // Rimuove duplicati
                .toList();// Raccoglie in una lista
    }

    private boolean isCategoryInUse(AbstractCategory category, List<Long> usedCategoriesIds) {
        // Controlla se la categoria stessa è in uso
        if (usedCategoriesIds.contains(category.getId())) {
            return true;
        }

        // Controlla ricorsivamente le sottocategorie
        for (AbstractCategory child : category.getChildren()) {
            if (isCategoryInUse(child, usedCategoriesIds)) {
                return true;
            }
        }

        return false;
    }

    private void removeCategoryRecursively(AbstractCategory category) {
        // Prima elimina ricorsivamente tutte le sottocategorie
        List<AbstractCategory> children = new ArrayList<>(category.getChildren()); // Copia per evitare ConcurrentModificationException
        for (AbstractCategory child : children) {
            removeCategoryRecursively(child);
        }

        // Poi elimina il tag associato a questa categoria
        removeTagByCategory(category);

        Controller<AbstractCategory> categoryController = new DefaultJpaController<>(AbstractCategory.class);
        //Se la categoria ha un parent, dobbiamo aggiornare la relazione con i suoi children
        updateParentsRelations(category, categoryController);

        // Infine elimina la categoria stessa
        categoryController.delete(category.getId());
    }

    private void updateParentsRelations(AbstractCategory category, Controller<AbstractCategory> categoryController) {
        if (category.getParent() != null) {
            AbstractCategory parent = categoryController.getById(category.getParent().getId());
            if (parent != null) {
                parent.getChildren().removeIf(child -> child.getId().equals(category.getId()));
                // Aggiorna il parent nel database
                categoryController.update(parent);
            }
        }
    }

    private void removeTagByCategory(AbstractCategory category) {
        Controller<DefaultTag> tagController = new DefaultJpaController<>(DefaultTag.class);

        // Trova il tag associato a questa categoria
        List<DefaultTag> allTags = tagController.getAll();
        allTags.stream()
                .filter(tag -> tag.getCategory().getId().equals(category.getId()))
                .findFirst().ifPresent(tagToDelete -> tagController.delete(tagToDelete.getId()));

    }
}
