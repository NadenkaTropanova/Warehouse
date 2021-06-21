package com.trade.controller;

import com.trade.Main;
import com.trade.controller.popup.PopupWindow;
import com.trade.request.AuthenticationRequest;
import com.trade.request.RegistrationRequest;
import com.trade.service.UserService;
import com.trade.service.exception.AuthenticationException;
import com.trade.service.exception.UserAlreadyExistsException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class AuthenticationController {
    private final UserService userService;

    @FXML
    private TextField login;

    @FXML
    private TextField password;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @FXML
    void signIn(ActionEvent event) throws IOException {
        String login = this.login.getText();
        String password = this.password.getText();

        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail(login);
        request.setPassword(password);

        try {
            userService.authenticate(request);
            TradeController.load();
        } catch (AuthenticationException e) {
            PopupWindow.openWindow("Ошибка", "Неверные данные");
        }
    }

    @FXML
    void toRegistration(ActionEvent event) throws IOException {
        RegistrationController.load();
    }

    public static void load() throws IOException {
        FXMLLoader loader = new FXMLLoader(RegistrationController.class.getResource("/authentication.fxml"));
        loader.setControllerFactory(Main.getApplicationContext().getBeanFactory()::getBean);
        Parent view = loader.load();
        Main.getStage().setScene(new Scene(view));
        Main.getStage().show();
    }
}
