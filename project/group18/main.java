package application;

import org.jfree.chart.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cryptoAnalyzer.utils.DataFetcher;

import org.json.JSONArray;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;
  
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class main {

@FXML
private ChoiceBox dropdown = null;
@FXML
private ListView list;
@FXML
private DatePicker date;
@FXML
private ChoiceBox metrics;
@FXML
private ChoiceBox intervals;
@FXML
private Label warning;

private Button refresh;



//save coinGecko api to api variable
private String api = "https://api.coingecko.com/api/v3/";

    //add text to choice box
public void initialize() {
    //dropdown.getItems().addAll("BTC", "ETH", "XRP", "BCH", "LTC", "EOS", "XLM", "ADA", "TRX", "NEO", "XMR", "DASH", "XEM", "ETC", "ZEC", "BTG", "ICX", "LSK", "OMG", "VEN", "QTUM", "BNB", "BCD", "PPT", "BTX", "ZRX", "MCO", "GNT", "ICN", "WAVES", "STRAT", "KNC", "GAS", "ZIL", "SNT", "BAT", "REP", "ELF", "MDA", "MTH", "MFT", "FUN", "ZEN", "SALT", "XZC", "GTO", "XVG", "DCR", "LRC", "QASH", "SUB", "SNGLS", "ENJ", "KMD", "RCN", "RLC", "RDN", "POA", "MANA", "DGD", "DNT", "STORJ", "BCN", "GRS", "BCPT", "XRB", "WAX", "WTC", "LINK", "SALT", "TNB", "SUB", "TUSD", "PAY", "GTO", "XLM", "XMR", "XEM", "XRP", "XLM", "XMR", "XEM", "XRP", "XLM", "XMR", "XEM", "XRP", "XLM", "XMR", "XEM", "XRP", "XLM", "XMR", "XEM", "XRP", "XLM", "XMR", "XEM", "XRP", "XLM", "XMR", "XEM", "XRP", "XLM", "XMR", "XEM", "XRP", "XLM", "XMR", "XEM", "XRP", "XLM", "XMR", "XEM", "XRP", "XLM", "XMR", "XEM", "XRP", "XLM", "XMR", "XEM", "XRP");
    //get coinGecko name list and add to choice box
    dropdown.getItems().addAll("Bitcoin", "Ethereum", "Binance Coin", "Tether", "Solana", "Cardano", "XRP", "USD Coin", "Polkadot", "Dogecoin", "Avalanche", "Shiba Inu", "Crypto.com Coin", "Terra", "Wrapped Bitcoin", "Litecoin", "Binance USD", "Chainlink", "Polygon", "Bitcoin Cash", "Algorand", "Dai", "Uniswap", "Elrond", "Axie Infinity", "Stellar", "Internet Computer", "VeChain", "TerraUSD", "Cosmos", "Filecoin", "cETH", "FTX Token", "TRON", "Theta Network", "Decentraland", "Lido Staked Ether", "Ethereum Classic", "OKB", "Hedera", "The Sandbox", "Fantom", "The Graph", "Gala", "Near", "cDAI", "Monero", "cUSDC", "Tezos", "Helium", "Flow", "EOS", "Olympus", "Radix", "IOTA", "Klaytn", "Magic Internet Money", "Loopring", "Enjin Coin", "THORChain", "Amp", "PancakeSwap", "Aave", "Kusama", "LEO Token", "eCash", "Zcash", "Arweave", "Harmony", "Bitcoin SV", "Maker", "Quant", "Kadena", "NEO", "Basic Attention Token", "Bitcoin Cash ABC", "Chiliz", "Huobi BTC", "Holo", "BitTorrent", "Stacks", "Waves", "Theta Fuel", "Dash", "Curve DAO Token", "KuCoin Token", "SafeMoon", "Wonderland", "Celsius Network", "Compound", "Celo", "Ethereum Name Service", "Huobi Token", "e-Radix", "LINK", "Qtum", "NEM", "IoTeX", "Synthetix Network Token", "Immutable X");
    intervals.getItems().addAll("Daily", "weekly", "monthly", "yearly");
    metrics.getItems().addAll("Price", "Market Cap", "Volume", "coins in circulation", "% change of unit price", "% change of market cap", "% change of volume", "% change of coins in circulation");
}


//done
public void addCrypto(ActionEvent event) throws Exception {
    //get dropdown selection
    String selection = null;

    if (dropdown.getValue() != null) {
         selection = dropdown.getValue().toString();

        //add selection to list if its not there
        if (!list.getItems().contains(selection)) {
            if(confirmCrypto(selection)) {
                list.getItems().add(selection);
            } else {
                warning.setText("Unable to add " + selection + " to list. Please try again later.");
            }

        }

    }
}

//done
public void removeCrypto(ActionEvent event) throws Exception {
    Object item = list.getSelectionModel().getSelectedItem();

    if(item != null) {
        //get selected item in listView
        list.getItems().remove(item);
    }
}

public void analyze(ActionEvent event) throws Exception {
    if(list.getItems().size() == 0) {
        warning.setText("You dont have any coins in your list. Please add some coins.");
    } else if (date.getValue() == null) {
        warning.setText("Please select a date.");
    } else if (metrics.getValue() == null) {
        warning.setText("Please select an analysis.");
    } else if (intervals.getValue() == null) {
        warning.setText("Please select an interval.");
    } else {
        warning.setText("");
        //get date
        fetch();
    }

}


private boolean confirmCrypto(String selection) throws Exception {
    //get dropdown selection
    String url = api + "coins/" + selection.toLowerCase() + "/";
    //get coinGecko data
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


private void fetch() throws Exception {
    //get date
    for (Object coin : list.getItems()) {
        String selection = coin.toString();
        //change date format
        String date = this.date.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        String url = api + "coins/" + selection.toLowerCase() + "/history?date=" +date;

        //get url
        URL obj = new URL(url);
        //open connection
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        //set request method
        con.setRequestMethod("GET");
        //set request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        //get response code
        int responseCode = con.getResponseCode();
        //if response code is 200
        if (responseCode == 200) {
            //get response
            String inline = "";
            //get input stream
            Scanner in = new Scanner(con.getInputStream());
            //while there is a line
            while (in.hasNextLine()) {
                //add line to string
                inline += in.nextLine();

            }
            in.close();
            //parse json and save it cryptoData.json
            FileWriter file = new FileWriter("src/main/java/com/example/group18/cryptoData.json");
            file.write(inline);
            file.close();

        }
    }
}

public double getPriceForCoin() throws FileNotFoundException, IOException, ParseException {
	
	Object obj = new JSONParser().parse(new FileReader("cryptoData.json"));
    JSONObject jo = (JSONObject) obj;
    
    JsonObject id = ((JsonElement) jo.get("id")).getAsJsonObject();
    
    //get marketdata
	JsonObject marketData = ((JsonElement) jo.get("market_data")).getAsJsonObject();
	
	//price
	JsonObject currentPrice = marketData.get("current_price").getAsJsonObject();
	double price = currentPrice.get("cad").getAsDouble();
    
    return price;

}
    
public double getMarketCapForCoin() {

	Object obj = new JSONParser().parse(new FileReader("cryptoData.json"));
    JSONObject jo = (JSONObject) obj;
 
	double marketCap = 0.0;
	
	JsonObject marketData = ((JsonElement) jo.get("market_data")).getAsJsonObject();
	JsonObject currentPrice = marketData.get("market_cap").getAsJsonObject();
	marketCap = currentPrice.get("cad").getAsDouble();
	
	return marketCap;
}

public double getVolumeForCoin() {
	double volume = 0.0;
	Object obj = new JSONParser().parse(new FileReader("cryptoData.json"));
    JSONObject jo = (JSONObject) obj;
    
	JsonObject marketData = ((JsonElement) jo.get("market_data")).getAsJsonObject();
	JsonObject currentPrice = marketData.get("total_volume").getAsJsonObject();
	volume = currentPrice.get("cad").getAsDouble();
	
	return volume;
}


public double getCirculation() {
	
	double cap = getMarketCapForCoin();
	double price = getPriceForCoin();
	double circulation = cap/price;
	
	return circulation;
}

	
}

}
