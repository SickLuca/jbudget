package it.unicam.cs.mpgc.jbudget119250.View;

import it.unicam.cs.mpgc.jbudget119250.Controller.*;
import it.unicam.cs.mpgc.jbudget119250.Model.Abstractions.AbstractMovement;
import it.unicam.cs.mpgc.jbudget119250.Model.Entities.DefaultBalance;
import it.unicam.cs.mpgc.jbudget119250.Model.Entities.DefaultTag;
import it.unicam.cs.mpgc.jbudget119250.Model.Entities.DefaultUser;
import it.unicam.cs.mpgc.jbudget119250.Model.Entities.OperationType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the main home view user interface in a financial management application.
 * It handles the initialization of UI components and their interaction with the
 * underlying data model.
 * <p>
 * This class is responsible for:
 * - Managing user input and interactions in the home view.
 * - Displaying and updating the current financial balance.
 * - Adding and removing tags for financial movements.
 * - Collecting user input data to create new financial movements.
 * - Updating and displaying a list of movements in a table.
 */
public class HomeView implements Initializable {


    // Header elements

    @FXML
    private Label balanceValueLabel;

    // Form elements (left side)

    @FXML
    private ComboBox<MovementType> typeOfMovementComboBox;


    @FXML
    private ComboBox<OperationType> typeOfOperationComboBox;


    @FXML
    private DatePicker dateOfMovementPicker;

    @FXML
    private TextField amountTextField;

    @FXML
    private ComboBox<DefaultTag> addTagComboBox;

    @FXML
    private Button addTagButton;

    @FXML
    private ComboBox<DefaultUser> userComboBox;

    @FXML
    private Button createMovementButton;

    // Table and right side elements

    @FXML
    private TableView<AbstractMovement> movementsTableView;

    @FXML
    private TableColumn<AbstractMovement, Long> idColumn;

    @FXML
    private TableColumn<AbstractMovement, BigDecimal> amountColumn;

    @FXML
    private TableColumn<AbstractMovement, String> typeColumn;

    @FXML
    private TableColumn<AbstractMovement, String> operationColumn;

    @FXML
    private TableColumn<AbstractMovement, LocalDateTime> dateColumn;

    @FXML
    private TableColumn<AbstractMovement, List<String>> tagsColumn;

    @FXML
    private TableColumn<AbstractMovement, String> userColumn;

    @FXML
    private ListView<DefaultTag> selectedTagsListView;

    private static final long DEFAULT_BALANCE_ID = 1L;
    //pattern observer, la lista si aggiorna automaticamente
    Controller<DefaultBalance> balanceController = new DefaultJpaController<>(DefaultBalance.class);
    private final ObservableList<DefaultTag> selectedTags = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeBalance();
        initializeUI();
        refreshTableView();
    }

    private void initializeUI() {
        initializeComboBox();
        initializeTable();
        initializeListView();

        addTagButton.setOnAction(event -> handleAddTag());
        createMovementButton.setOnAction(event -> handleCreateMovement());
    }

    private void initializeListView() {
        selectedTagsListView.setItems(selectedTags);

        selectedTagsListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleRemoveTag();
            }
        });
    }

    private void initializeBalance() {
        DefaultBalance balance = balanceController.getById(DEFAULT_BALANCE_ID);
        if (balance == null) {
            DefaultBalance defaultBalance = new DefaultBalance();
            defaultBalance.setBalance(BigDecimal.ZERO);
            balanceController.save(defaultBalance);
            
            balanceValueLabel.setText(defaultBalance.getBalance().toString());

        } else balanceValueLabel.setText(calculateBalance().toString());
    }

    private void refreshTableView() {
        try {
            Controller<AbstractMovement> movementController = new DefaultJpaController<>(AbstractMovement.class);
            ObservableList<AbstractMovement> movements = FXCollections.observableList(movementController.getAll());

            movementsTableView.setItems(movements);
        } catch (Exception e) {
            AlertView.showAlert("Error during data loading", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private void initializeComboBox() {
        typeOfMovementComboBox.getItems().addAll((MovementType.values()));
        typeOfOperationComboBox.getItems().addAll(OperationType.values());
        addTagComboBox.getItems().addAll(
                new DefaultJpaController<>(DefaultTag.class).getAll()
        );
        userComboBox.getItems().addAll(
                new DefaultJpaController<>(DefaultUser.class).getAll()
        );

        addTagComboBox.setItems(addTagComboBox.getItems().sorted(Comparator.comparing(DefaultTag::getName)));
    }
    
    private void initializeTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("movementType"));
        operationColumn.setCellValueFactory(new PropertyValueFactory<>("operationType"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        tagsColumn.setCellValueFactory(new PropertyValueFactory<>("tag"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
    }


    @FXML
    private void handleAddTag() {
        DefaultTag tag = addTagComboBox.getSelectionModel().getSelectedItem();
        if (selectedTagsListView.getItems().contains(tag)) {
            AlertView.showAlert("Tag already exists", "Please select a tag which was not previously added to the list.", Alert.AlertType.ERROR);
        }

        if (tag != null) {
            selectedTags.add(tag);
            addTagComboBox.setValue(null);
        }  else {
            AlertView.showAlert("Tag not selected", "Please select a tag from the box", Alert.AlertType.ERROR);
        }

    }

    private void handleRemoveTag() {
        DefaultTag selectedTag = selectedTagsListView.getSelectionModel().getSelectedItem();
        if (selectedTag != null) {
            selectedTags.remove(selectedTag);
        }
    }

    @FXML
    private void handleCreateMovement() {
        try {
            if (collectAndBuildFormData()) {
            updateBalance();
            refreshTableView();
            clearForm();
            AlertView.showAlert("Movement created", "Movement successfully created", Alert.AlertType.INFORMATION);
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            //Bisogna fare un Alert!
            AlertView.showAlert("Error during data validation", e.getMessage(), Alert.AlertType.ERROR);
        } catch (MovementCreationException e) {
            AlertView.showAlert("Error during movement saving", e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    private void clearForm() {
        typeOfMovementComboBox.setValue(null);
        typeOfOperationComboBox.setValue(null);
        amountTextField.clear();
        dateOfMovementPicker.setValue(null);
        userComboBox.setValue(null);

        selectedTags.clear();
        addTagComboBox.setValue(null);
    }

    private boolean validateMovement(AbstractMovement newMovement) {
        return newMovement.getAmount() != null &&
                !newMovement.getAmount().equals(BigDecimal.ZERO) &&
                newMovement.getOperationType() != null &&
                newMovement.getDate() != null &&
                newMovement.getUser() != null;
    }

    private boolean collectAndBuildFormData () {
        MovementType typeOfMovement= typeOfMovementComboBox.getSelectionModel().getSelectedItem();
        OperationType typeOfOperation = typeOfOperationComboBox.getSelectionModel().getSelectedItem();
        BigDecimal amountValue = amountToBigDecimal(amountTextField.getText());
        LocalDateTime dateTime = dateOfMovementPicker.getValue().atTime(LocalTime.now());
        DefaultUser user = userComboBox.getSelectionModel().getSelectedItem();

        MovementFactory factory = new MovementFactory();

        AbstractMovement movement = factory.builderFor(typeOfMovement)
                .withOperation(typeOfOperation)
                .withAmount(amountValue)
                .withDate(dateTime)
                .withUser(user)
                .withTag(selectedTags)
                .build();

        if (!validateMovement(movement)) {
            AlertView.showAlert("Data not valid", "All fields must be compiled", Alert.AlertType.ERROR);
            return false;
        }

        typeOfMovement.save(movement);
        return true;
    }

    private void updateBalance() {
        DefaultBalance balance = balanceController.getById(DEFAULT_BALANCE_ID);
        if (balance != null) {
            balance.setBalance(calculateBalance());
            balanceController.update(balance);
            balanceValueLabel.setText(balance.getBalance().toString());
        }
    }

    private BigDecimal calculateBalance() {
        Controller<AbstractMovement> movementController = new DefaultJpaController<>(AbstractMovement.class);
        return movementController.getAll().stream()
                .filter(movement -> movement.getAmount() != null)
                .map(movement -> {
                    BigDecimal amount = movement.getAmount();
                    return movement.getOperationType() == OperationType.INCOME
                            ? amount
                            : amount.negate();
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }
    private BigDecimal amountToBigDecimal(String amount) {
        try {
            return BigDecimal.valueOf(Double.parseDouble(amount));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount format");

        }
    }
}