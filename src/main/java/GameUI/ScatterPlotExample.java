package GameUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class ScatterPlotExample extends Application {

    @Override
    public void start(Stage stage) {

        // Define the x-axis and y-axis
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("X-axis");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Y-axis");

        // Create the scatter chart
        ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.setTitle("Scatter Plot Example");

        // Create the first series of data
        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        series1.setName("Series 1");
        series1.getData().add(new XYChart.Data<>(1, 2));
        series1.getData().add(new XYChart.Data<>(2, 3));
        series1.getData().add(new XYChart.Data<>(3, 4));

        // Create the second series of data
        XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
        series2.setName("Series 2");
        series2.getData().add(new XYChart.Data<>(4, 5));
        series2.getData().add(new XYChart.Data<>(5, 6));
        series2.getData().add(new XYChart.Data<>(6, 7));

        // Change the color of the series
        series1.getNode().setStyle("-fx-color: blue;");
        series2.getNode().setStyle("-fx-color: green;");

        // Add the series to the chart
        scatterChart.getData().addAll(series1, series2);

        // Add the chart to the scene and show the stage
        Scene scene = new Scene(scatterChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
