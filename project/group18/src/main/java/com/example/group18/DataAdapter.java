package com.example.group18;

/**
 * @author Aleksander , Felopater 
 * @date 2021-12-04
 * **/

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @class DataAdapter
 * @brief this class is the adapter to handle data from CryptoData.json and
 *        fetch data from the API
 *
 **/
public class DataAdapter {
	private ListView<String> list;
	@FXML
	private DatePicker date;
	@FXML
	private ChoiceBox<String> metrics;
	@FXML
	private ChoiceBox<String> intervals;

	// api url
	private String url = "https://api.coingecko.com/api/v3/";

	/**
	 * @constructor DataAdapter
	 * @brief constructor for DataAdapter
	 * @param list      listview
	 * @param date      date picker
	 * @param metrics   choicebox
	 * @param intervals choicebox
	 *
	 *                  Main constructor for DataAdapter that sets the listview,
	 *                  date picker, choiceboxes and calls the fetch method
	 *
	 *
	 **/
	public DataAdapter(ListView<String> list, DatePicker date, ChoiceBox<String> metrics, ChoiceBox<String> intervals)
			throws FileNotFoundException {
		this.list = list;
		this.date = date;
		this.metrics = metrics;
		this.intervals = intervals;
	}

	/**
	 * this method creates a connection to CoinGecko's API
	 * results from call to API are stored in a database and used for analysis
	 * from other methods in the class
	 * 
	 * @throws Exception to handle errors in call to API
	 */
	public void fetch() throws Exception {
		JSONObject jsonouter = new JSONObject();

		for (Object coin : list.getItems()) {
			String selection = coin.toString();
			// change date format
			String date = this.date.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

			// get number of days from date to today
			int days = this.getDays(date);

			if (intervals.getValue().equals("weekly") && days < 7 || intervals.getValue().equals("monthly") && days < 30
					|| intervals.getValue().equals("yearly") && days < 365) {
				days = 1;
			}

			String api = url + "coins/" + selection.toLowerCase() + "/market_chart?vs_currency=cad&days=" + days
					+ "&interval=daily";

			// get url
			URL obj = new URL(api);
			// open connection
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			// set request method
			con.setRequestMethod("GET");
			// set request header
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			// get response code
			int responseCode = con.getResponseCode();
			// if response code is 200
			if (responseCode == 200) {
				// get response
				String inline = "";
				// get input stream
				Scanner in = new Scanner(con.getInputStream());
				// while there is a line
				while (in.hasNextLine()) {
					// add line to string
					inline += in.nextLine();

				}

				// close scanner
				in.close();
				jsonouter.append("coin", inline);

			}
		}
		FileWriter file = new FileWriter(
				"../crypto-analysis-app/project/group18/src/main/java/com/example/group18/cryptoData.json");
		file.write(jsonouter.toString());
		file.close();

	}

	/**
	 * @method getDays
	 * @param date taken from user-interface
	 * @return will return the total number of from the date selected and current
	 *         date
	 *         this method will return the number of days between the date selected
	 *         and current date
	 */
	private int getDays(String date) {
		// get current date
		LocalDate today = LocalDate.now();
		// get date from date picker
		LocalDate selectedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		// get number of days between dates
		long days = ChronoUnit.DAYS.between(today, selectedDate);

		// get absolute value of days
		int absDays = (int) Math.abs(days);

		return absDays;
	}

	/**
	 * method handles the implementation
	 * required to get price from @param coin
	 * 
	 * @param coin is the selected coin from user
	 * @return the price of the coin
	 * @throws FileNotFoundException to handle errors in reading from stored crypto
	 *                               data
	 */
	public List<Double> getPrice(String coin) throws FileNotFoundException {
		// get index of coin in list
		int index = list.getItems().indexOf(coin);
		int timing = 0;

		JSONObject jo = new JSONObject(
				new JSONTokener(
						new FileReader(
								"../crypto-analysis-app/project/group18/src/main/java/com/example/group18/cryptoData.json")));

		JSONArray ja = jo.getJSONArray("coin");

		String Array = ja.get(index).toString();
		JSONObject json = new JSONObject(Array);
		JSONArray jsonArray = json.getJSONArray("prices");

		if (intervals.getValue().equals("weekly")) {
			timing = 7;

		} else if (intervals.getValue().equals("monthly")) {
			timing = 30;

		} else if (intervals.getValue().equals("yearly")) {
			timing = 365;

		}

		List<Double> price = new ArrayList<>();

		for (int i = jsonArray.length() - 1; i >= 0; i--) {
			JSONArray jsonArray1 = jsonArray.getJSONArray(i);
			price.add(jsonArray1.getDouble(1));
			i = i - timing;
		}

		return price;
	}

	/**
	 * this method handles the retrieval of the marketcap analysis
	 * 
	 * @param coin is the coin to fetch market cap for
	 * @return will return the marketcap for @param coin
	 * @throws FileNotFoundException to catch file errors
	 */
	public List<Double> getMarketCapForCoin(String coin) throws FileNotFoundException {
		// get index of coin in list
		int index = list.getItems().indexOf(coin);
		int timing = 0;

		JSONObject jo = new JSONObject(
				new JSONTokener(
						new FileReader(
								"../crypto-analysis-app/project/group18/src/main/java/com/example/group18/cryptoData.json")));

		JSONArray ja = jo.getJSONArray("coin");
		//
		String Array = ja.get(index).toString();
		JSONObject json = new JSONObject(Array);
		JSONArray jsonArray = json.getJSONArray("market_caps");

		if (intervals.getValue().equals("weekly")) {
			timing = 7;

		} else if (intervals.getValue().equals("monthly")) {
			timing = 30;

		} else if (intervals.getValue().equals("yearly")) {
			timing = 365;

		}

		List<Double> market = new ArrayList<>();

		// traverse from end of array to start
		for (int i = jsonArray.length() - 1; i >= 0; i--) {
			JSONArray jsonArray1 = jsonArray.getJSONArray(i);
			market.add(jsonArray1.getDouble(1));
			i = i - timing;
		}

		return market;

	}

	/**
	 *
	 * @param coin for volume of each coin in our analysis
	 * @return list of volumes
	 * @throws FileNotFoundException if file specified does not exist
	 *                               This method will return the volume of each coin
	 */
	public List<Double> getVolumeForCoin(String coin) throws FileNotFoundException {
		// get index of coin in list
		int index = list.getItems().indexOf(coin);
		int timing = 0;
		int num = 0;

		JSONObject jo = new JSONObject(
				new JSONTokener(
						new FileReader(
								"../crypto-analysis-app/project/group18/src/main/java/com/example/group18/cryptoData.json")));

		JSONArray ja = jo.getJSONArray("coin");
		//
		String Array = ja.get(index).toString();
		JSONObject json = new JSONObject(Array);
		JSONArray jsonArray = json.getJSONArray("total_volumes");

		if (intervals.getValue().equals("weekly")) {
			timing = 7;
			num = -2;
		} else if (intervals.getValue().equals("monthly")) {
			timing = 30;
		} else if (intervals.getValue().equals("yearly")) {
			timing = 365;
			num = -2;
		}

		List<Double> volume = new ArrayList<>();

		for (int i = jsonArray.length() - 1; i >= 0; i--) {
			JSONArray jsonArray1 = jsonArray.getJSONArray(i);
			volume.add(jsonArray1.getDouble(1)); // changed from 0 to 1 because volume was not showing
			i = i - timing;
		}

		return volume;
	}

	/**
	 * method to handle the retrieval of circulation
	 * 
	 * @param coin is the coin to be analyzed
	 * @return list of circulation for each coin
	 * @throws FileNotFoundException if file specified does not exist
	 *
	 */
	public List<Double> getCirculation(String coin) throws FileNotFoundException {

		List<Double> cap = getMarketCapForCoin(coin);
		List<Double> price = getPrice(coin);

		List<Double> circ = new ArrayList<>();
		for (int i = 0; i < cap.size(); i++) {
			circ.add(cap.get(i) / price.get(i));
		}
		return circ;
	}

	/**
	 * this method will handle the calculation of unit price change
	 * 
	 * @param coin for the change in price of each coin in the analysis
	 * @return for a list of prices changes in percentages
	 * @throws FileNotFoundException if file specified does not exist
	 */

	public List<Double> getPercentageUnitprice(String coin) throws FileNotFoundException {
		List<Double> price = getPrice(coin);

		List<Double> percentage = new ArrayList<>();

		if (price.size() == 1) {
			percentage.add(price.get(0));
		}

		for (int i = 1; i < price.size(); i++) {
			percentage.add(((price.get(i) - price.get(i - 1)) / price.get(i - 1)) * 100);
		}

		return percentage;
	}

	/**
	 * this method will handle calculations for the change in market cap
	 * 
	 * @param coin for the change in market cap of each coin in the analysis
	 * @return is list of percent changes in market cap
	 * @throws FileNotFoundException if file specified does not exist
	 */
	public List<Double> getChangeInMarketCap(String coin) throws FileNotFoundException {
		List<Double> MarketCap = getMarketCapForCoin(coin);
		// get percentage change in market cap
		List<Double> change = new ArrayList<>();

		if (MarketCap.size() == 1) {
			change.add(MarketCap.get(0));
		}

		for (int i = 1; i < MarketCap.size(); i++) {
			// get percentage change
			double changeInMarketCap = ((MarketCap.get(i) - MarketCap.get(i - 1)) / MarketCap.get(i - 1)) * 100;
			change.add(changeInMarketCap);

		}

		return change;
	}

	/**
	 * this method will handle the calculation change in volume
	 * 
	 * @param coin for the change in volume for coins in analysis
	 * @return is list of percent changes in volume
	 * @throws FileNotFoundException if file specified does not exist
	 */
	public List<Double> getChangeInVolume(String coin) throws FileNotFoundException {
		List<Double> volume = getVolumeForCoin(coin);
		// get percentage change in market cap
		List<Double> change = new ArrayList<>();

		if (volume.size() == 1) {
			change.add(volume.get(0));
		}

		for (int i = 1; i < volume.size(); i++) {
			// get percentage change
			double changeInVolume = ((volume.get(i) - volume.get(i - 1)) / volume.get(i - 1)) * 100;
			change.add(changeInVolume);

		}

		return change;
	}

	/**
	 * this method will handle the calculation change in circulation
	 * 
	 * @param coin is list of coins to analyze
	 * @return percent change in circulation for coin
	 * @throws FileNotFoundException if file specified does not exist
	 */
	public List<Double> getChangeInCoinCirculation(String coin) throws FileNotFoundException {
		List<Double> circulation = getCirculation(coin);
		// get percentage change in market cap
		List<Double> change = new ArrayList<>();

		if (circulation.size() == 1) {
			change.add(circulation.get(0));
		}

		for (int i = 1; i < circulation.size(); i++) {
			// get percentage change
			double changeInCirculation = ((circulation.get(i) - circulation.get(i - 1)) / circulation.get(i - 1)) * 100;
			change.add(changeInCirculation);

		}
		return change;
	}

}
