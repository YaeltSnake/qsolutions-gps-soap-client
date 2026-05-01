package com.qsolutions.gpsclient;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Test mínimo para verificar que JavaFX 21 está correctamente
 * configurado en el proyecto NetBeans + Ant.
 *
 * Esta clase NO forma parte del producto final. Es una validación
 * técnica del entorno antes de comenzar v2.0.0.
 *
 * @author YaeltSnake
 */
public class HelloJavaFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Crear un label con un mensaje
        Label label = new Label("¡JavaFX funciona! 🎉");
        label.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-text-fill: #2D3748;" +
            "-fx-font-family: 'Segoe UI';"
        );

        // Layout simple — un StackPane centra el contenido automáticamente
        StackPane root = new StackPane(label);
        root.setStyle("-fx-background-color: #F7FAFC;");

        // Crear la escena (ventana visual)
        Scene scene = new Scene(root, 400, 200);

        // Configurar la ventana principal
        primaryStage.setTitle("Hello JavaFX — QSolutions Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // launch() es un método estático heredado de Application
        // Inicia el thread de JavaFX y llama a start()
        launch(args);
    }
}