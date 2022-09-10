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
// TODO: 10/09/2022 implement JNA GMP Library for improved from https://github.com/square/jna-gmp
//    GMP is a free library for arbitrary precision arithmetic, operating on signed integers, rational numbers, and floating-point numbers.
//    There is no practical limit to the precision except the ones implied by the available memory in the machine GMP runs on.
//    GMP has a rich set of functions, and the functions have a regular interface.
//    The main target applications for GMP are cryptography applications and research, Internet security applications, algebra systems, computational algebra research, etc.
//    GMP is carefully designed to be as fast as possible, both for small operands and for huge operands.
//    The speed is achieved by using fullwords as the basic arithmetic type, by using fast algorithms,
//    with highly optimised assembly code for the most common inner loops for a lot of CPUs, and by a general emphasis on speed.
//    The first GMP release was made in 1991. It is continually developed and maintained, with a new release about once a year.
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
