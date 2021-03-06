package com.trade.controller;

import com.trade.Main;
import com.trade.controller.popup.PopupWindow;
import com.trade.domain.Trade;
import com.trade.domain.Type;
import com.trade.request.TradeRequest;
import com.trade.service.TradeService;
import com.trade.service.UserService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TradeController {
    private final TradeService tradeService;
    private final UserService userService;
    private Type currentType;
    @FXML
    private ComboBox<Type> type;
    @FXML
    private TableView<Trade> table;

    @FXML
    private TextField time;

    @FXML
    private TextField company;

    @FXML
    private TextField amount;

    @FXML
    private TextField price;

    @FXML
    private TextField product;

    @Autowired
    public TradeController(TradeService tradeService, UserService userService) {
        this.tradeService = tradeService;
        this.userService = userService;
    }

    @FXML
    public void initialize() {
        ObservableList<Type> items = FXCollections.observableArrayList(
                Type.CANCELLED, Type.SOLD, Type.ORDERED, Type.DELIVERED
        );

        type.setItems(items);

        ChangeListener<Type> changeListener = (observable, oldValue, newValue) -> currentType = newValue;

        type.getSelectionModel()
                .selectedItemProperty()
                .addListener(changeListener);
        updateTable();
    }

    @FXML
    void add(ActionEvent event) {
        if (company.getText().length() == 0) {
            PopupWindow.openWindow("????????????", "?????????????????? ?????? ????????????????");
            return;
        }

        if (product.getText().length() == 0) {
            PopupWindow.openWindow("????????????", "?????????????????? ???????????????????????? ????????????");
            return;
        }

        if (!amount.getText().matches("[0-9]+")) {
            PopupWindow.openWindow("????????????", "???????????????????? ???????????? ?????????????????? ???????????? ??????????");
            return;
        }


        if (!price.getText().matches("[0-9]+[.,]?[0-9]*")) {
            PopupWindow.openWindow("????????????", "???????? ?????????? ?????????????????? ???????????? ?????????? ?? ??????????(???????? ??????????????????)");
            return;
        }
        System.out.println(currentType);
        if (currentType == null) {
            PopupWindow.openWindow("????????????", "???????????????? ?????? ??????????????????");
            return;
        }

        TradeRequest request = new TradeRequest();
        request.setTime(time.getText());
        request.setType(currentType);
        request.setCompanyName(company.getText());
        request.setProductName(product.getText());
        request.setAmount(Integer.valueOf(amount.getText()));
        request.setPrice(new BigDecimal(price.getText().replace(',', '.')));

        tradeService.addTrade(request);

        company.setText("");
        product.setText("");
        amount.setText("");
        price.setText("");
        time.setText("");

        updateTable();
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        userService.logout();
        AuthenticationController.load();
    }

    private void updateTable() {
        TableColumn<Trade, Trade> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Trade, Trade> companyCol = new TableColumn<>("????????????????");
        companyCol.setCellValueFactory(new PropertyValueFactory<>("companyName"));

        TableColumn<Trade, Trade> productCol = new TableColumn<>("??????????");
        productCol.setCellValueFactory(new PropertyValueFactory<>("productName"));

        TableColumn<Trade, Trade> timeCol = new TableColumn<>("??????????");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<Trade, Trade> amountCol = new TableColumn<>("??????-????");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Trade, Trade> typeCol = new TableColumn<>("?????? ??????????????????");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Trade, Trade> priceCol = new TableColumn<>("????????");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Trade, Trade> dateCol = new TableColumn<>("???????? ??????????????????????");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Trade, Trade> delete = new TableColumn<>("");
        delete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        delete.setCellFactory(param -> new TableCell<Trade, Trade>() {
            private final Button deleteButton = new Button("??????????????");

            @Override
            protected void updateItem(Trade trade, boolean empty) {
                super.updateItem(trade, empty);

                if (trade == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    try {
                        tradeService.deleteTrade(trade.getId());
                        updateTable();
                    } catch (Exception e) {
                        PopupWindow.openWindow("????????????", "???????????????? ???? ??????????????");
                    }
                });
            }
        });

        TableColumn<Trade, Trade> open = new TableColumn<>("");
        open.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        open.setCellFactory(param -> new TableCell<Trade, Trade>() {
            private final Button openButton = new Button("??????????????????");

            @Override
            protected void updateItem(Trade trade, boolean empty) {
                super.updateItem(trade, empty);

                if (trade == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(openButton);
                openButton.setOnAction(event -> {
                    try {
                        CountController.load(trade.getProductName());
                    } catch (Exception e) {
                        System.out.println(e);
                        PopupWindow.openWindow("????????????", "???????????????? ???? ??????????????");
                    }
                });
            }
        });

        ObservableList<Trade> trades = FXCollections
                .observableArrayList(tradeService.getTradesForCurrentUser());

        table.setItems(trades);
        table.getColumns().clear();

        table.getColumns().addAll(
                idCol, companyCol,
                productCol, typeCol, timeCol, amountCol,
                priceCol, dateCol, delete, open
        );
    }

    public static void load() throws IOException {
        FXMLLoader loader = new FXMLLoader(RegistrationController.class.getResource("/trades.fxml"));
        loader.setControllerFactory(Main.getApplicationContext().getBeanFactory()::getBean);
        Parent view = loader.load();
        Main.getStage().setScene(new Scene(view));
        Main.getStage().show();
    }
}
