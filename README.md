# Crypto Currency Metrics Application developed in Java and JavaFX

## This application displays cryptocurrency data in graphical format for a list of user-selected cryptocurrencies

This project implements CoinGecko's REST API to retrieve and process data for cryptocurrencies. The application is developed in Java and uses JavaFX to create the user-interface and graph representations of the data

## How to run the program

Java and JSON files are all in src/main/java/com/example/group18. The program will start by launching the user login window which will prompt for a username and password, "admin" is the default for both username and password and can be modified within userData.json.

Any warnings can be ignored and are mostly due to JavaFX generic type declarations that are handled on run-time.

1. To begin, run the file login.java. The login window will appear prompting for the username and password.

<img src="https://github.com/Asarenac59/crypto-analysis-app/blob/bfcfd362370977228e3ec34f695dc0a8073bd00a/images/crypto-app-login.png"](https://github.com/Asarenac59/crypto-analysis-app/blob/61f982a76e7d38503d5bffa9ab4e1a764691823d/images/crypto-app-login.PNG" alt="login window"/>

2. If login credentials are correct the main menu will be displayed. Cryptocurrencies are selected at the drop-down list located at the very top of the main menu and can be added or removed by clicking the "+" and "-" buttons to the very right. The selected cryptocurrencies are listed on the far right.

<img src="https://github.com/Asarenac59/crypto-analysis-app/blob/bfcfd362370977228e3ec34f695dc0a8073bd00a/images/crypto-app-select.png" alt="main menu"/>

3. A date from which data will be displayed must be selected from the left-most drop-down list at the bottom of the main menu. Note: CoinGecko does not list data before 4/26/2013, any date selected prior to then will not be displayed by the application.

<img src="https://github.com/Asarenac59/crypto-analysis-app/blob/bfcfd362370977228e3ec34f695dc0a8073bd00a/images/crypto-app-date.png" alt="date selection"/>

4. The next step is to select one of eight metrics from the middle drop-down list at the bottom of the main menu as shown below.

<img src="https://github.com/Asarenac59/crypto-analysis-app/blob/bfcfd362370977228e3ec34f695dc0a8073bd00a/images/crypto-app-metric.png"](https://github.com/Asarenac59/crypto-analysis-app/blob/61f982a76e7d38503d5bffa9ab4e1a764691823d/images/crypto-app-metrics.png" alt="metric selection"/>

5. After a metric is selected, the "Choose Interval" drop-down list allows for data to be displayed in one of the four options

<img src="https://github.com/Asarenac59/crypto-analysis-app/blob/bfcfd362370977228e3ec34f695dc0a8073bd00a/images/crypto-app-interval.png" alt="interval selection"/>
 
6. Clicking the "Refresh" button under the list of currencies on the right will display the selected metric up to the present day in several chart formats for each cryptocurrencies.

<img src="https://github.com/Asarenac59/crypto-analysis-app/blob/bfcfd362370977228e3ec34f695dc0a8073bd00a/images/crypto-app-refresh.png" alt="data displayed"/>

Data is shown in a table, line chart, scatter chart, and bar chart. Current-day data is the left-most value for each chart.
