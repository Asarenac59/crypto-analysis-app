# Crypto Currency Metrics Application developed in Java and JavaFX

## This application displays crypotocurrency data in graphical format for various user-selected cryptocurrencies

This project implements CoinGecko's REST API to retrieve and process data for cryptocurrencies. The application is developed in Java and uses JavaFX to create the user-interface and graph

Java files are located under src/main/java/com/example/group18. To execute, run the file login.java. The program will start by launching the user login window which will prompt for a username and password, "admin" is the default for both username and password and can be modified within userData.json.

After login information is verified, a new windows opens allowing the user to select several cryptocurrencies, desired metric (price, market cap, etc.) and time frame.

Clicking "Refresh" will display the user-selceted metrics in several chart formats for all cryptocurrencies in the list.
