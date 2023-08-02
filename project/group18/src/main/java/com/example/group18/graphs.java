package com.example.group18;

/**
 * @author Aleksander , Felopater 
 * @date 2021-12-04
 * **/

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Graph class used to display results computed from Analysis and DataAdapter
 * class
 * in various forms. This class handles the methods to create the charts and
 * table
 * and creates the display provided to the main user-interface.
 */
public class graphs {
    @FXML
    private TableView table;
    @FXML
    private LineChart linechart;
    @FXML
    private ScatterChart scatterchart;
    @FXML
    private BarChart barchart;
    @FXML
    private ChoiceBox intervals;

    public ListView<String> list;
    public List<List> data;

    /**
     *
     * @param list
     * @param data
     * @param table
     * @param linechart
     * @param scatterchart
     * @param barchart
     * @param intervals
     *
     *                     all parameters above are taken as
     *                     input from Main user-interface drop-down menus
     *                     and used to handle graph creations
     */
    public graphs(ListView<String> list, List<List> data, TableView table, LineChart linechart,
            ScatterChart scatterchart, BarChart barchart, ChoiceBox<String> intervals) {
        this.list = list;
        this.data = data;
        this.table = table;
        this.linechart = linechart;
        this.scatterchart = scatterchart;
        this.barchart = barchart;
        this.intervals = intervals;
    }

    /**
     * this method handles the instantiation of the
     * of graphs that display the results of our
     * cryptocurrency analysis
     * 
     * @param title1 first name for chart/table
     * @param title2 second name for chart/table
     */
    public void showData(String title1, String title2) {

        createBarChart(title1, title2);
        createScatterChart(title1, title2);
        createLineChart(title1, title2);
        createTable(title1, title2);

    }

    /**
     *
     * Provides results of analysis in a table format
     * by creating columns for the date and list of currencies
     * the user has selected. Each row cell stores
     * the resulting data for the specific analysis
     * selected (i.e price, marketcap, volume, etc.)
     *
     */
    private void createTable(String title1, String title2) {
        table.setEditable(true);
        // clear table
        table.getColumns().clear();
        // set title

        LocalDate today = LocalDate.now();
        String dateString = today.toString();

        int sizeee = data.get(0).size();
        String[] columnNames = new String[sizeee + 2];
        String[][] inputData = new String[data.size()][columnNames.length + 1];

        for (int i = 0; i < inputData.length; i++) {
            inputData[i][0] = list.getItems().get(i).toUpperCase();
        }

        for (int i = 0; i < inputData.length; i++) {
            List<Double> coin = data.get(i);
            // convert coin to array
            Double[] coinArray = coin.stream().map(d -> d).toArray(Double[]::new);
            for (int j = 1; j < inputData[i].length - 2; j++) {
                // make coinArray[j-1] to two decimal places
                inputData[i][j] = String.format("%.4f", coinArray[j - 1]);
            }
        }

        columnNames[0] = "Cryptocurrency";

        // this handle date changes when intervals are selected
        for (int i = 1; i < sizeee + 1; i++) {
            columnNames[i] = dateString;

            if (intervals.getValue().equals("weekly")) {
                today = today.minusDays(7);
                dateString = today.toString();
            } else if (intervals.getValue().equals("monthly")) {
                today = today.minusDays(30);
                dateString = today.toString();
            } else if (intervals.getValue().equals("yearly")) {
                today = today.minusDays(365);
                dateString = today.toString();
            } else {
                today = today.minusDays(1);
                dateString = today.toString();
            }

        }

        table.setEditable(false);

        for (int i = 0; i < columnNames.length; i++) {
            // create columns for each date
            TableColumn<List<String>, String> column = new TableColumn<>(columnNames[i]);
            // set column width
            column.setMinWidth(100);

            // create cell for each column
            final int colIndex = i;
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(colIndex)));

            table.getColumns().add(column);

        }

        List<List<String>> data = new ArrayList<List<String>>();

        // input data into table
        for (int i = 0; i < inputData.length; i++) {
            List<String> row = new ArrayList<String>();
            for (int j = 0; j < inputData[0].length; j++) {

                row.add(inputData[i][j]);

            }

            data.add(row);

        }

        ObservableList<List<String>> inpData = FXCollections.observableArrayList(data);

        table.setItems(inpData);
        table.setVisible(true);
    }

    /**
     *
     * method to create the results in the form of
     * a linechart.
     * 
     * @param title1 is name for axis given to linechart
     * @param title2 second axis for linechart
     *
     */
    private void createLineChart(String title1, String title2) {
        linechart.setAnimated(false);
        linechart.getData().clear();
        linechart.setTitle(title2 + " " + title1 + " Line Chart");

        for (String s : list.getItems()) {
            // get todays date
            LocalDate today = LocalDate.now();
            String dateString = today.toString();

            // get index of s in list
            int index = list.getItems().indexOf(s);
            // create series

            XYChart.Series series = new XYChart.Series();
            series.setName(s);

            List<Double> newData = data.get(index);
            for (Double d : newData) {
                series.getData().add(new XYChart.Data(dateString, d));

                if (intervals.getValue().equals("weekly")) {
                    today = today.minusDays(7);
                    dateString = today.toString();
                } else if (intervals.getValue().equals("monthly")) {
                    today = today.minusDays(30);
                    dateString = today.toString();
                } else if (intervals.getValue().equals("yearly")) {
                    today = today.minusDays(365);
                    dateString = today.toString();
                } else {
                    today = today.minusDays(1);
                    dateString = today.toString();
                }

            }
            linechart.getData().add(series);
        }
        linechart.setVisible(true);
    }

    /**
     * method that handles the creation of the
     * scatter chart to be displayed
     * 
     * @param title1 first axis name
     * @param title2 second axis name
     */
    private void createScatterChart(String title1, String title2) {
        scatterchart.setAnimated(false);
        scatterchart.getData().clear();
        scatterchart.setTitle(title2 + " " + title1 + " Scatter Chart");

        for (String s : list.getItems()) {
            // get todays date
            LocalDate today = LocalDate.now();
            String dateString = today.toString();

            // get index of s in list
            int index = list.getItems().indexOf(s);
            // create series

            XYChart.Series series = new XYChart.Series();
            series.setName(s);

            List<Double> newData = data.get(index);
            for (Double d : newData) {
                series.getData().add(new XYChart.Data(dateString, d));

                if (intervals.getValue().equals("weekly")) {
                    today = today.minusDays(7);
                    dateString = today.toString();
                } else if (intervals.getValue().equals("monthly")) {
                    today = today.minusDays(30);
                    dateString = today.toString();
                } else if (intervals.getValue().equals("yearly")) {
                    today = today.minusDays(365);
                    dateString = today.toString();
                } else {
                    today = today.minusDays(1);
                    dateString = today.toString();
                }

            }
            scatterchart.getData().add(series);
        }
        scatterchart.setVisible(true);
    }

    /**
     * this method handles the creation of the
     * barchart displayed to the user-interface
     * 
     * @param title1 first axis name
     * @param title2 second axis name
     */
    private void createBarChart(String title1, String title2) {
        barchart.setAnimated(false);
        barchart.getData().clear();
        barchart.setTitle(title2 + " " + title1 + " Bar Chart");

        for (String s : list.getItems()) {
            // get todays date
            LocalDate today = LocalDate.now();
            String dateString = today.toString();

            // get index of s in list
            int index = list.getItems().indexOf(s);
            // create series

            XYChart.Series series = new XYChart.Series();
            series.setName(s);

            List<Double> newData = data.get(index);
            for (Double d : newData) {
                series.getData().add(new XYChart.Data(dateString, d));

                if (intervals.getValue().equals("weekly")) {
                    today = today.minusDays(7);
                    dateString = today.toString();
                } else if (intervals.getValue().equals("monthly")) {
                    today = today.minusDays(30);
                    dateString = today.toString();
                } else if (intervals.getValue().equals("yearly")) {
                    today = today.minusDays(365);
                    dateString = today.toString();
                } else {
                    today = today.minusDays(1);
                    dateString = today.toString();
                }

            }
            barchart.getData().add(series);
        }
        barchart.setVisible(true);
    }
}