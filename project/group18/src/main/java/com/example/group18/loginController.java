package com.example.group18;

/**
 * @author Aleksander , Felopater 
 * @date 2021-12-04
 * **/

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @interface mainController
 *            login proxy to the server
 **/
interface mainController {
    void setStage() throws IOException;
}

/***
 * @class loginController
 * @extends mainController
 * @implements mainController
 *
 *             main controller for the login page
 *
 */
public class loginController implements mainController {

    // variables
    @FXML
    private Label wrongLogin;
    @FXML
    private Button button;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    /***
     * @method setStage
     * @throws IOException
     *
     *                     Sets the stage to switch to the main menu
     */
    @Override
    public void setStage() throws IOException {
        // load main menu
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

        // create a new scene with the loaded fxml
        Stage stage = (Stage) button.getScene().getWindow();

        // set window name
        stage.setTitle("Main Menu");

        // set scene
        stage.setResizable(false);
        stage.setScene(new Scene(root, 1559, 894));
        stage.show();
    }

    /**
     * @method authenticates
     * @param event
     * @throws IOException
     *
     *                     method to authenticate the user from javaFX button
     *
     **/
    public void authenticate(ActionEvent event) throws IOException {
        checkLogin();
    }

    /**
     * @method checkLogin
     * @throws IOException
     *
     *                     method to check the login credentials
     *
     **/
    private void checkLogin() throws IOException {
        // read userData.json
        if (checkData(username.getText(), password.getText())) {
            // go to main menu
            wrongLogin.setText("");
            // set stage
            setStage();
        } else {
            // display error when login is incorrect
            wrongLogin.setText("Wrong username or password");
        }

    }

    /**
     * @method checkData
     * @param username
     * @param password
     * @return boolean
     *
     *         method to check the login credentials from the json file which stores
     *         user data
     *
     **/
    private boolean checkData(String username, String password) throws FileNotFoundException {
        // read userData.json
        JSONObject jsonObject = new JSONObject(
                new JSONTokener(new FileReader(
                        "../crypto-analysis-app/project/group18/src/main/java/com/example/group18/userData.json")));
        JSONArray jsonArray = jsonObject.getJSONArray("users1");

        // loop through the json array and compare the username and password
        if (jsonArray.getJSONObject(0).getString("username").equals(username)
                && jsonArray.getJSONObject(0).getString("password").equals(password)) {
            jsonArray.getJSONObject(0).put("online", true);
            // return true if the username and password are correct
            return true;
        }
        return false;
    }

}