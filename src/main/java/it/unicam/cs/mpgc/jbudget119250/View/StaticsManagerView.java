package it.unicam.cs.mpgc.jbudget119250.View;

import it.unicam.cs.mpgc.jbudget119250.Controller.DefaultJpaController;
import it.unicam.cs.mpgc.jbudget119250.Model.Abstractions.AbstractMovement;
import it.unicam.cs.mpgc.jbudget119250.Model.Entities.DefaultTag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * The StaticsManagerView class is responsible for managing and displaying statistical
 * data in a JavaFX application. It uses a date range selected by the user to filter
 * financial movements and visualizes the summary data using a PieChart. The class
 * implements the {@link Initializable} interface, allowing for custom initialization logic
 * when the user interface is loaded.
 * <p>
 * This class acts as the controller for the associated FXML file and is initialized when
 * the UI is loaded. The main functionalities include:
 * - Date range selection using DatePicker components.
 * - Fetching and filtering data from a JPA controller based on the date range.
 * - Calculating totals by tags for visualization.
 * - Updating a PieChart UI component with the filtered and grouped data.
 */
public class StaticsManagerView implements Initializable {

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private PieChart tagStatisticsPieChart;

    private DefaultJpaController<AbstractMovement> movementController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movementController = new DefaultJpaController<>(AbstractMovement.class);

        LocalDate today = LocalDate.now();
        toDatePicker.setValue(today);
        fromDatePicker.setValue(today.minusMonths(1));

        fromDatePicker.setOnAction(e -> updatePieChart());
        toDatePicker.setOnAction(e -> updatePieChart());

        updatePieChart();
    }

    private void updatePieChart() {
        try {
            // Ottieni le date selezionate
            LocalDateTime fromDate = fromDatePicker.getValue().atStartOfDay();
            LocalDateTime toDate = toDatePicker.getValue().atTime(LocalTime.MAX);

            // Ottieni tutti i movimenti
            List<AbstractMovement> allMovements = movementController.getAll();

            // Filtra i movimenti per intervallo di date
            List<AbstractMovement> filteredMovements = allMovements.stream()
                    .filter(movement -> movement.getDate() != null)
                    .filter(movement ->
                            !movement.getDate().isBefore(fromDate) &&
                                    !movement.getDate().isAfter(toDate))
                    .collect(Collectors.toList());

            // Raggruppa per tag e calcola i totali
            Map<String, BigDecimal> tagTotals = calculateTagTotals(filteredMovements);

            // Crea i dati per il pie chart
            ObservableList<PieChart.Data> pieChartData = createPieChartData(tagTotals);

            // Aggiorna il pie chart
            tagStatisticsPieChart.setData(pieChartData);

        } catch (Exception e) {
            AlertView.showAlert("Error during data loading: ", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Map<String, BigDecimal> calculateTagTotals(List<AbstractMovement> movements) {
        Map<String, BigDecimal> tagTotals = new HashMap<>();

        for (AbstractMovement movement : movements) {
            List<DefaultTag> tags = movement.getTag();
            BigDecimal amount = movement.getAmount();

            if (amount == null) {
                continue;
            }

            // Se il movimento non ha tag, raggruppalo sotto "No Tag"
            if (tags == null || tags.isEmpty()) {
                String noTagKey = "No Tag";
                tagTotals.merge(noTagKey, amount, BigDecimal::add);
            } else {
                // Per ogni tag del movimento, aggiungi l'importo
                for (DefaultTag tag : tags) {
                    String tagName = (tag.getName() != null && !tag.getName().trim().isEmpty())
                            ? tag.getName()
                            : "Unnamed Tag";
                    tagTotals.merge(tagName, amount, BigDecimal::add);
                }
            }
        }

        return tagTotals;
    }

    private ObservableList<PieChart.Data> createPieChartData(Map<String, BigDecimal> tagTotals) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        if (tagTotals.isEmpty()) {
            pieChartData.add(new PieChart.Data("No Data", 1));
            return pieChartData;
        }

        // Crea i dati del chart
        for (Map.Entry<String, BigDecimal> entry : tagTotals.entrySet()) {
            String label = String.format("%s (€%.2f)",
                    entry.getKey(),
                    entry.getValue());
            pieChartData.add(new PieChart.Data(label, entry.getValue().doubleValue()));
        }

        return pieChartData;
    }

}