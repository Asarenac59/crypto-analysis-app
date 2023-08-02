package com.example.group18;

/**
 * @author Aleksander , Felopater 
 * @date 2021-12-04
 * **/

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

/**
 * this class handles the creation of the main userinterface
 * providing the user drop-down menus to select the required
 * forms of analysis
 */
public class main {

    @FXML
    private ChoiceBox<String> dropdown = null;
    @FXML
    private ListView<String> list;
    @FXML
    private DatePicker date;
    @FXML
    private ChoiceBox<String> metrics;
    @FXML
    private ChoiceBox<String> intervals;
    @FXML
    private Label warning;
    @FXML
    private Button refresh;
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
     * initializes the main userinterface and hides the result objects
     * from the user until analysis criteria have been selected and performed.
     * set the objects used to display the main UI.
     *
     */

    private String api = "https://api.coingecko.com/api/v3/";

    // add text to choice box
    public void initialize() {
        table.setVisible(false);
        linechart.setVisible(false);
        scatterchart.setVisible(false);
        barchart.setVisible(false);

        // get coinGecko name list and add to choice box
        dropdown.getItems().addAll("Bitcoin", "Ethereum", "Binance Coin", "Tether", "Solana", "Cardano", "XRP",
                "USD Coin", "Polkadot", "Dogecoin", "Avalanche", "Shiba Inu", "Crypto.com Coin", "Terra",
                "Wrapped Bitcoin", "Litecoin", "Binance USD", "Chainlink", "Polygon", "Bitcoin Cash", "Algorand", "Dai",
                "Uniswap", "Elrond", "Axie Infinity", "Stellar", "Internet Computer", "VeChain", "TerraUSD", "Cosmos",
                "Filecoin", "cETH", "FTX Token", "TRON", "Theta Network", "Decentraland", "Lido Staked Ether",
                "Ethereum Classic", "OKB", "Hedera", "The Sandbox", "Fantom", "The Graph", "Gala", "Near", "cDAI",
                "Monero", "cUSDC", "Tezos", "Helium", "Flow", "EOS", "Olympus", "Radix", "IOTA", "Klaytn",
                "Magic Internet Money", "Loopring", "Enjin Coin", "THORChain", "Amp", "PancakeSwap", "Aave", "Kusama",
                "LEO Token", "eCash", "Zcash", "Arweave", "Harmony", "Bitcoin SV", "Maker", "Quant", "Kadena", "NEO",
                "Basic Attention Token", "Bitcoin Cash ABC", "Chiliz", "Huobi BTC", "Holo", "BitTorrent", "Stacks",
                "Waves", "Theta Fuel", "Dash", "Curve DAO Token", "KuCoin Token", "SafeMoon", "Wonderland",
                "Celsius Network", "Compound", "Celo", "Ethereum Name Service", "Huobi Token", "e-Radix", "LINK",
                "Qtum", "NEM", "IoTeX", "Synthetix Network Token", "Immutable X");
        intervals.getItems().addAll("Daily", "weekly", "monthly", "yearly");
        metrics.getItems().addAll("Price", "Market Cap", "Volume", "coins in circulation", "% change of unit price",
                "% change of market cap", "% change of volume", "% change of coins in circulation");
    }

    /**
     * this method handles the drop-down selection menu when the user
     * attempts to a cryptocurrency to the list. If the currency chosen is invalid
     * the user is displayed a warning message.
     * 
     * @param event is triggered when user wishes to add currency
     * @throws Exception to handle any errors
     */

    public void addCrypto(ActionEvent event) throws Exception {
        // get dropdown selection
        String selection = null;

        if (dropdown.getValue() != null) {
            selection = dropdown.getValue().toString();

            // add selection to list if its not there
            if (!list.getItems().contains(selection)) {
                if (confirmCrypto(selection)) {
                    list.getItems().add(selection);
                } else {
                    warning.setText("Unable to add " + selection + " to list. Please try again later.");
                }

            }

        }
    }

    /**
     * this method handles the drop-down selection menu when the user and removes
     * coin from the list.
     * 
     * @param event triggered when removing currency from list
     * @throws Exception to handle errors
     */
    public void removeCrypto(ActionEvent event) throws Exception {
        Object item = list.getSelectionModel().getSelectedItem();

        if (item != null) {
            // get selected item in listView
            list.getItems().remove(item);
        }
    }

    /**
     * this method analyses the user's input and determines if its valid
     * 
     * @param event in the case user has not selected a valid option
     *              from the drop-down menus
     * @throws Exception to handle errors
     */
    public void analyze(ActionEvent event) throws Exception {
        if (list.getItems().size() == 0) {
            warning.setText("You dont have any coins in your list. Please add some coins.");
        } else if (date.getValue() != null && date.getValue().isAfter(LocalDate.now())) {
            // if date.getValue is greater than today, set warning to "Please select a date
            // in the past."
            warning.setText("Please select a date in the past.");
        } else if (date.getValue() == null) {
            warning.setText("Please select a date.");
        } else if (metrics.getValue() == null) {
            warning.setText("Please select an analysis.");
        } else if (intervals.getValue() == null) {
            warning.setText("Please select an interval.");
        } else {
            // fetch data from coinGecko
            warning.setText("");

            Analysis analysis = new Analysis(list, date, metrics, intervals, barchart, linechart, table, scatterchart);
            analysis.getData();
        }

    }

    /**
     * this method creates a connection to CoinGecko's API to
     * retrieve and add a cryptocurrency to the user's list
     * 
     * @param selection is the list of cryptocurrencies the user selects
     * @return to show if user picked a valid currency
     * @throws Exception to handle errors
     */
    private boolean confirmCrypto(String selection) throws Exception {
        // get dropdown selection
        String url = api + "coins/" + selection.toLowerCase() + "/";
        // get coinGecko data
        URL obj = new URL(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();

        if (responseCode == 200) {
            return true;
        } else {
            return false;
        }

    }

}
