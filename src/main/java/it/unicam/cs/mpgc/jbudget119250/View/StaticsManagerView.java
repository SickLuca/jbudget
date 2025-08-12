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
import javafx.scene.control.Label;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * This class is responsible for managing and displaying statistical
 * data in a JavaFX application. It uses a date range selected by the user to filter
 * financial movements and visualizes the summary data using a PieChart.
 * <p>
 * This class acts as the controller for the associated FXML file {@code StaticsManagerScene.fxml}. The main functionalities include:
 * - Date range selection using DatePicker components.
 * - Fetching and filtering data from a JPA controller based on the date range.
 * - Calculating totals by tags for visualization.
 * - Updating a PieChart UI component with the filtered and grouped data.
 * - Updating labels that show relevant statistics based on the filtered data.
 */
public class StaticsManagerView implements Initializable {

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private PieChart tagStatisticsPieChart;

    @FXML
    private Label totalMovementsLabel;

    @FXML
    private Label maxMovementLabel;

    @FXML
    private Label minMovementLabel;

    @FXML
    private Label averageMovementLabel;


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
            // Get selected dates
            LocalDateTime fromDate = fromDatePicker.getValue().atStartOfDay();
            LocalDateTime toDate = toDatePicker.getValue().atTime(LocalTime.MAX);

            //Movements filtered by dates
            List<AbstractMovement> filteredMovements = getFilteredMovements(fromDate, toDate);

            updateLabels(filteredMovements);

            // Group them by tag and calculate amount results
            Map<String, BigDecimal> tagTotals = calculateTagTotals(filteredMovements);

            // Create data for pie chart
            ObservableList<PieChart.Data> pieChartData = createPieChartData(tagTotals);

            // Update pie chart
            tagStatisticsPieChart.setData(pieChartData);

        } catch (Exception e) {
            AlertView.showAlert("Error during data loading: ", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private List<AbstractMovement> getFilteredMovements(LocalDateTime fromDate, LocalDateTime toDate) {
        return movementController.getAll().stream()
                .filter(movement -> movement.getDate() != null)
                .filter(movement ->
                        !movement.getDate().isBefore(fromDate) &&
                                !movement.getDate().isAfter(toDate))
                .collect(Collectors.toList());
    }

    private Map<String, BigDecimal> calculateTagTotals(List<AbstractMovement> movements) {
        Map<String, BigDecimal> tagTotals = new HashMap<>();

        for (AbstractMovement movement : movements) {
            List<DefaultTag> tags = movement.getTag();
            BigDecimal amount = movement.getAmount();

            // If the movement has no tag, group it by "No Tag"
            if (tags == null || tags.isEmpty()) {
                String noTagKey = "No Tag";
                tagTotals.merge(noTagKey, amount, BigDecimal::add);
            } else {
                // For each movement tag, add the amount
                for (DefaultTag tag : tags) {
                    tagTotals.merge(tag.getName(), amount, BigDecimal::add);
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

        // Create data chart
        for (Map.Entry<String, BigDecimal> entry : tagTotals.entrySet()) {
            String label = String.format("%s (€%.2f)",
                    entry.getKey(),
                    entry.getValue());
            pieChartData.add(new PieChart.Data(label, entry.getValue().doubleValue()));
        }

        return pieChartData;
    }

    private void updateLabels(List<AbstractMovement> movements) {
        totalMovementsLabel.setText(getTotalMovements(movements));
        maxMovementLabel.setText(getMaxMovement(movements));
        minMovementLabel.setText(getMinMovement(movements));
        averageMovementLabel.setText(getAverageMovement(movements));
    }

    private String getAverageMovement(List<AbstractMovement> movements) {
        return  movements.stream()
                .map(AbstractMovement::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(movementController.getAll().size()), RoundingMode.HALF_UP)
                .toString();
    }

    private String getMinMovement(List<AbstractMovement> movements) {
        return movements.stream()
                .map(AbstractMovement::getAmount)
                .min(BigDecimal::compareTo)
                .map(BigDecimal::toString)
                .orElse("No movements");
    }

    private String getTotalMovements(List<AbstractMovement> movements) {
        return movements.size() + " movements";
    }

    private String getMaxMovement(List<AbstractMovement> movements) {
        return movements.stream()
                .map(AbstractMovement::getAmount)
                .max(BigDecimal::compareTo)
                .map(BigDecimal::toString)
                .orElse("No movements");
    }

}