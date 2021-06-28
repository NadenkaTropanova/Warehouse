package com.salesregister.controller;

import com.salesregister.Main;
import com.salesregister.controller.popup.PopupWindow;
import com.salesregister.domain.Operation;
import com.salesregister.service.OperationService;
import com.salesregister.service.UserService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

@Controller
public class OperationController {
    private final OperationService service;
    private final UserService userService;

    @FXML
    private TableView<Operation> table;

    @FXML
    private TextField query;

    @Autowired
    public OperationController(OperationService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @FXML
    void add(ActionEvent event) throws IOException {
        AddOperationController.load();
    }

    @FXML
    void search(ActionEvent event) throws IOException {
        System.out.println(query.getText());
        if (StringUtils.isEmpty(query.getText())) {
            updateTable(service.getOperationsForCurrentUser());
        } else {
            updateTable(service.getOperationsForCurrentUser(query.getText()));
        }

    }

    private void updateTable(List<Operation> operations) {
        TableColumn<Operation, Operation> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Operation, Operation> nameCol = new TableColumn<>("Название");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Operation, Operation> descriptionCol = new TableColumn<>("Описание");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Operation, Operation> placeCol = new TableColumn<>("Операция");
        placeCol.setCellValueFactory(new PropertyValueFactory<>("operation"));

        TableColumn<Operation, Operation> durationCol = new TableColumn<>("Кол-во");
        durationCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Operation, Operation> priceCol = new TableColumn<>("Цена");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Operation, Operation> dateCol = new TableColumn<>("Дата регистрации");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Operation, Operation> delete = new TableColumn<>("");
        delete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        delete.setCellFactory(param -> new TableCell<Operation, Operation>() {
            private final Button deleteButton = new Button("Удалить");

            @Override
            protected void updateItem(Operation operation, boolean empty) {
                super.updateItem(operation, empty);

                if (operation == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    try {
                        service.deleteOperation(operation.getId());
                        operations.removeIf(o -> o.getId().equals(operation.getId()));
                        updateTable(operations);
                    } catch (Exception e) {
                        PopupWindow.openWindow("Ошибка", "Запись не найдена");
                    }
                });
            }
        });

        ObservableList<Operation> values = FXCollections
                .observableArrayList(operations);

        table.setItems(values);
        table.getColumns().clear();

        table.getColumns().addAll(
                idCol, nameCol,
                descriptionCol, placeCol,
                durationCol, priceCol,
                dateCol, delete
        );
    }

    @FXML
    public void initialize() {
        updateTable(service.getOperationsForCurrentUser());
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        userService.logout();
        AuthenticationController.load();
    }

    public static void load() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/operations.fxml"));
        loader.setControllerFactory(Main.getApplicationContext().getBeanFactory()::getBean);
        Parent view = loader.load();
        Main.getStage().setScene(new Scene(view));
        Main.getStage().show();
    }
}
