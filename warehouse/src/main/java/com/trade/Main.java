package com.trade;

import com.trade.controller.AuthenticationController;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.nio.file.Files;

@SpringBootApplication
public class Main extends Application {

    private static ConfigurableApplicationContext applicationContext;
    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    public static ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        File file = new File("");
        String path = file.getAbsolutePath() + "/src/main/resources/icon.jpg";
        file = new File(path);
        String localUrl = file.toURI().toURL().toString();
        Image image = new Image(localUrl);
        stage.getIcons().add(image);
        AuthenticationController.load();
    }

    @Override
    public void init() throws Exception {
        applicationContext = SpringApplication.run(Main.class);
    }

    @Override
    public void stop() throws Exception {
        applicationContext.stop();
    }
}