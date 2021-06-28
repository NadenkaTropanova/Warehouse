package com.salesregister.controller;

import com.salesregister.Main;
import com.salesregister.controller.popup.PopupWindow;
import com.salesregister.request.OperationRequest;
import com.salesregister.service.OperationService;
import com.salesregister.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.math.BigDecimal;

@Controller
public class AddOperationController {
    @Autowired
    private OperationService service;


    @FXML
    private TextField name;

    @FXML
    private TextField operation;

    @FXML
    private TextField price;

    @FXML
    private TextField amount;

    @FXML
    private TextArea description;

    @FXML
    void add(ActionEvent event) {
        if (name.getText().length() == 0) {
            PopupWindow.openWindow("Ошибка", "Заполните название");
            return;
        }

        if (description.getText().length() == 0) {
            PopupWindow.openWindow("Ошибка", "Заполните описание");
            return;
        }

        if (operation.getText().length() == 0) {
            PopupWindow.openWindow("Ошибка", "Заполните операцию");
            return;
        }

        if (!amount.getText().matches("[0-9]+")) {
            PopupWindow.openWindow("Ошибка", "Кол-во должно содержать только цифры");
            return;
        }

        if (!price.getText().matches("[0-9]+[.,]?[0-9]*")) {
            PopupWindow.openWindow("Ошибка", "Цена может содержать только цифры и точку(если требуется)");
            return;
        }

        OperationRequest request = new OperationRequest();
        request.setName(name.getText());
        request.setOperation(operation.getText());
        request.setDescription(description.getText());
        request.setAmount(Integer.valueOf(amount.getText()));
        request.setPrice(new BigDecimal(price.getText().replace(',', '.')));

        service.addOperation(request);

        name.setText("");
        description.setText("");
        operation.setText("");
        amount.setText("");
        price.setText("");
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        OperationController.load();
    }

    public static void load() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/add_operation.fxml"));
        loader.setControllerFactory(Main.getApplicationContext().getBeanFactory()::getBean);
        Parent view = loader.load();
        Main.getStage().setScene(new Scene(view));
        Main.getStage().show();
    }
}
