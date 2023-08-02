/**
 * @author Aleksander , Felopater 
 * @date 2021-12-04
 * **/

package com.example.group18;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Felopater michael, Aleksander Sarenac
 * @date 2019-12-04
 **/

public class login extends Application {

    /**
     * This method is the entry point of the application.
     * It is the first method that is called when the application is started.
     *
     * @param stage the stage of the application
     **/
    @Override
    public void start(Stage stage) throws IOException {
        stage.setResizable(false);

        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage.setScene(new Scene(root, 520, 260));
        stage.setTitle("Login");
        stage.show();
    }

    /**
     *
     * @param args the command line arguments
     *             Main method of the application
     **/
    public static void main(String[] args) {
        launch();
    }
}