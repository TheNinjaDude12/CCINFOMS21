package com.example.gamerecordgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class RefundGameView {

    @FXML
    private ComboBox<?> comboBox;

    @FXML
    private TableView<Transaction> table;

    @FXML
    private TextField textField;

    @FXML
    private TextField textFieldGame;

    public static class Transaction{
        private int orderId;
        private int gameId;
        private String title;
        private String purchaseDate;
        private String paymentMethod;
        private float amount;
        private String status;

        public Transaction(int orderId, int gameId, String title, String purchaseDate,
                           String paymentMethod, float amount, String status) {
            this.orderId = orderId;
            this.gameId = gameId;
            this.title = title;
            this.purchaseDate = purchaseDate;
            this.paymentMethod = paymentMethod;
            this.amount = amount;
            this.status = status;
        }

        public int getOrderId() {return orderId;}
        public int getGameId() {return gameId;}
        public String getTitle() {return title;}
        public String getPurchaseDate() {return purchaseDate;}
        public String getPaymentMethod() {return paymentMethod;}
        public float getAmount() {return amount;}
        public String getStatus() {return status;}

        public void setOrderId(int orderId) {this.orderId = orderId;}
        public void setGameId(int gameId) {this.gameId = gameId;}
        public void setTitle(String title) {this.title = title;}
        public void setPurchaseDate(String purchaseDate) {this.purchaseDate = purchaseDate;}
        public void setPaymentMethod(String paymentMethod) {this.paymentMethod = paymentMethod;}
        public void setAmount(float amount) {this.amount = amount;}
        public void setStatus(String status) {this.status = status;}
    }

    public void initialize(){
        TableColumn<Transaction, Integer> orderIdCol = new TableColumn<>("Order ID");
        TableColumn<Transaction, Integer> gameIdCol = new TableColumn<>("Game ID");
        TableColumn<Transaction, String> titleCol = new TableColumn<>("Title");
        TableColumn<Transaction, String> purchaseDateCol = new TableColumn<>("Purchase Date");
        TableColumn<Transaction, String> paymentMethodCol = new TableColumn<>("Payment Method");
        TableColumn<Transaction, Integer> amountCol = new TableColumn<>("Amount");
        TableColumn<Transaction, String> statusCol = new TableColumn<>("Status");
    }

    public void refundGame(){

    }

    public void updateTransaction(){

    }

    public void back(ActionEvent event) throws IOException {
        System.out.println("Returning to Game Menu");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("gameView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
