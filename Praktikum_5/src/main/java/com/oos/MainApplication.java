package com.oos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent fxmlLoader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Mainview.fxml")));
        Scene scene = new Scene(fxmlLoader);
        primaryStage.setTitle("GuiBank - Willkommen");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
