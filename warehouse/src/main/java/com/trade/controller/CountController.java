package com.trade.controller;

import com.trade.Main;
import com.trade.domain.Type;
import com.trade.service.TradeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class CountController {
    @Autowired
    private TradeService service;
    private static String productName;

    @FXML
    private TextField name;

    @FXML
    private TextField sold;

    @FXML
    private TextField cancelled;

    @FXML
    private TextField delivered;

    @FXML
    private TextField ordered;

    @FXML
    public void initialize() {
        name.setText(productName);
        sold.setText(String.valueOf(service.getProductAmount(productName, Type.SOLD)));
        cancelled.setText(String.valueOf(service.getProductAmount(productName, Type.CANCELLED)));
        delivered.setText(String.valueOf(service.getProductAmount(productName, Type.DELIVERED)));
        ordered.setText(String.valueOf(service.getProductAmount(productName, Type.ORDERED)));
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        TradeController.load();
    }

    public static void load(String name) throws IOException {
        FXMLLoader loader = new FXMLLoader(RegistrationController.class.getResource("/count.fxml"));
        loader.setControllerFactory(Main.getApplicationContext().getBeanFactory()::getBean);
        productName = name;
        Parent view = loader.load();
        Main.getStage().setScene(new Scene(view));
        Main.getStage().show();
    }
}
