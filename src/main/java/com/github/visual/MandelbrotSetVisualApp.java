package com.github.visual;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;
/**
 * Written by Tim Kochetkov and Michael Bilibin.
 */

// TODO: 06/09/2022  Move all logic to model, following MVC pattern for JavaFX applications
public class MandelbrotSetVisualApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setResizable(false);
        primaryStage.setTitle("Mandelbrot set visual test");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/main_window.fxml")));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
