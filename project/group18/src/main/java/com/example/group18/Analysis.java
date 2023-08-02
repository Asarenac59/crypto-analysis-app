package com.example.group18;

/**
 * @author Aleksander , Felopater 
 * @date 2021-12-04
 * **/

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;

/**
 * This class takes selected input from the user-interface
 * and implements the DataAdapter class method's which handle the computations
 * for the user-input selected.
 */

public class Analysis {
    private ListView<String> list;
    @FXML
    private DatePicker date;
    @FXML
    private ChoiceBox<String> metrics;
    @FXML
    private ChoiceBox<String> intervals;
    @FXML
    private TableView table;
    @FXML
    private LineChart linechart;
    @FXML
    private ScatterChart scatterchart;
    @FXML
    private BarChart barchart;

    /**
     *
     * @param list
     * @param date
     * @param metrics
     * @param intervals
     * @param barchart
     * @param linechart
     * @param tableview
     * @param scatterchart
     *
     *                     above parameters store the drop-down selection menu
     *                     options
     *                     so that analysis can be performed based on the input
     *                     provided
     */
    public Analysis(ListView<String> list, DatePicker date, ChoiceBox<String> metrics, ChoiceBox<String> intervals,
            BarChart barchart, LineChart linechart, TableView tableview, ScatterChart scatterchart) {
        this.list = list;
        this.date = date;
        this.metrics = metrics;
        this.intervals = intervals;
        this.barchart = barchart;
        this.linechart = linechart;
        this.table = tableview;
        this.scatterchart = scatterchart;

    }

    /**
     * This class handles basic input from the user and
     * will store input from user based on the user selection from
     * various drop down menus and implement DataAdapter class methods which
     * handle the core analysis.
     * 
     * @throws Exception
     */

    public void getData() throws Exception {
        DataAdapter dataAdapter = new DataAdapter(list, date, metrics, intervals);
        dataAdapter.fetch();

        switch (metrics.getValue()) {
            case "Price": {
                List<List> price = new ArrayList<>();

                for (Object coin : list.getItems()) {
                    String coinName = coin.toString();
                    price.add(dataAdapter.getPrice(coinName));
                }

                showData(list, price);
                break;
            }
            case "Market Cap": {
                List<List> marketCap = new ArrayList<>();

                for (Object coin : list.getItems()) {
                    String coinName = coin.toString();
                    marketCap.add(dataAdapter.getMarketCapForCoin(coinName));
                }
                showData(list, marketCap);
                break;
            }
            case "Volume": {
                List<List> volume = new ArrayList<>();

                for (Object coin : list.getItems()) {
                    String coinName = coin.toString();
                    volume.add(dataAdapter.getVolumeForCoin(coinName));
                }
                showData(list, volume);
                break;
            }
            case "coins in circulation": {
                List<List> change = new ArrayList<>();

                for (Object coin : list.getItems()) {
                    String coinName = coin.toString();
                    change.add(dataAdapter.getCirculation(coinName));

                }
                showData(list, change);
                break;
            }
            case "% change of unit price": {
                List<List> change = new ArrayList<>();

                for (Object coin : list.getItems()) {
                    String coinName = coin.toString();
                    change.add(dataAdapter.getPercentageUnitprice(coinName));
                }
                showData(list, change);
                break;
            }

            case "% change of market cap": {
                List<List> change = new ArrayList<>();
                for (Object coin : list.getItems()) {
                    String coinName = coin.toString();
                    change.add(dataAdapter.getChangeInMarketCap(coinName));
                }
                showData(list, change);
                break;
            }
            case "% change of volume": {
                List<List> change = new ArrayList<>();
                for (Object coin : list.getItems()) {
                    String coinName = coin.toString();
                    change.add(dataAdapter.getChangeInVolume(coinName));
                }
                showData(list, change);
                break;
            }
            case "% change of coins in circulation": {
                List<List> change = new ArrayList<>();
                for (Object coin : list.getItems()) {
                    String coinName = coin.toString();
                    change.add(dataAdapter.getChangeInCoinCirculation(coinName));
                }
                showData(list, change);
                break;
            }
        }
    }

    /**
     *
     * method to render the results to the user after analysis has been completed
     * 
     * @param data  is the data for each coin in the cryptocurrency list
     * @param listv is the name for each coin in the list
     */
    public void showData(ListView<String> listv, List<List> data) {
        graphs graph = new graphs(listv, data, table, linechart, scatterchart, barchart, intervals);
        graph.showData(metrics.getValue(), intervals.getValue());
    }

}
