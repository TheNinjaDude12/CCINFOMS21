package com.example.demo22;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class customerTable {

    @FXML
    private TableView<Person> customerTable; // Use Person type
    public static class Person {
        private int id;
        private String firstName;
        private String lastName;
        private String email;
        private String date;
        private String country;
        private String platform;
        private double spent;
        private int games;

        Person(int id, String firstName, String lastName, String email,
               String date, String country, String platform, double spent, int games) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.date = date;
            this.country = country;
            this.platform = platform;
            this.spent = spent;
            this.games = games;
        }

        public int getId() {
            return id;
        }
        public String getFirstName() {
            return firstName;
        }
        public String getLastName() {
            return lastName;
        }
        public String getEmail() {
            return email;
        }
        public String getDate() {
            return date;
        }
        public String getCountry() {
            return country;
        }
        public String getPlatform() {
            return platform;
        }
        public double getSpent() {
            return spent;
        }
        public int getGames() {
            return games;
        }

        public void setId(int id) {
            this.id = id;
        }
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public void setDate(String date) {
            this.date = date;
        }
        public void setCountry(String country) {
            this.country = country;
        }
        public void setPlatform(String platform) {
            this.platform = platform;
        }
        public void setSpent(double spent) { // Changed to double
            this.spent = spent;
        }
        public void setGames(int games) {
            this.games = games;
        }
    }

    public void initialize() {

        TableColumn<Person, Integer> customerID = new TableColumn<>("ID");
        TableColumn<Person, String> customerFirstName = new TableColumn<>("First Name");
        TableColumn<Person, String> customerLastName = new TableColumn<>("Last Name");
        TableColumn<Person, String> customerEmail = new TableColumn<>("Email");
        TableColumn<Person, String> customerDate = new TableColumn<>("Registration Date");
        TableColumn<Person, String> customerCountry = new TableColumn<>("Country");
        TableColumn<Person, String> customerPlatform = new TableColumn<>("Preferred Platform");
        TableColumn<Person, Double> customerSpent = new TableColumn<>("Total Spent"); // Use Double
        TableColumn<Person, Integer> customerGames = new TableColumn<>("Games Owned");

        customerTable.getColumns().clear(); // Clear existing if any
        customerTable.getColumns().addAll(customerID, customerFirstName, customerLastName,
                customerEmail, customerDate, customerCountry, customerPlatform, customerSpent, customerGames);

        customerID.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        customerLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        customerEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        customerDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        customerCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        customerPlatform.setCellValueFactory(new PropertyValueFactory<>("platform"));
        customerSpent.setCellValueFactory(new PropertyValueFactory<>("spent"));
        customerGames.setCellValueFactory(new PropertyValueFactory<>("games"));

        loadCustomers(); // Load data
        
        customerTable.setEditable(true);
        setupEditableColumns(); // Setup editing
        setupActionButtonColumn(); // Setup buttons
    }

    private void setupEditableColumns() {
        // First Name
        TableColumn<Person, String> customerFirstName = (TableColumn<Person, String>) customerTable.getColumns().get(1);
        customerFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        customerFirstName.setOnEditCommit(t -> {
            if (checkIfValidName(t.getNewValue())) {
                Person person = t.getRowValue();
                person.setFirstName(t.getNewValue());
                alterCustomerData(person.getFirstName(), person.getId(), "first_name");
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "First name cannot be empty or a number.");
                t.getTableView().getItems().set(t.getTablePosition().getRow(), t.getRowValue()); // Revert
            }
        });

        // Last Name
        TableColumn<Person, String> customerLastName = (TableColumn<Person, String>) customerTable.getColumns().get(2);
        customerLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        customerLastName.setOnEditCommit(t -> {
            if (checkIfValidName(t.getNewValue())) {
                Person person = t.getRowValue();
                person.setLastName(t.getNewValue());
                alterCustomerData(person.getLastName(), person.getId(), "last_name");
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Last name cannot be empty or a number.");
                t.getTableView().getItems().set(t.getTablePosition().getRow(), t.getRowValue()); // Revert
            }
        });

        // Email
        TableColumn<Person, String> customerEmail = (TableColumn<Person, String>) customerTable.getColumns().get(3);
        customerEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        customerEmail.setOnEditCommit(t -> {
            if (t.getNewValue() != null && !t.getNewValue().isEmpty() && t.getNewValue().contains("@")) {
                Person person = t.getRowValue();
                person.setEmail(t.getNewValue());
                alterCustomerData(person.getEmail(), person.getId(), "email");
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Email cannot be empty and must be valid.");
                t.getTableView().getItems().set(t.getTablePosition().getRow(), t.getRowValue()); // Revert
            }
        });

        // Registration Date
        TableColumn<Person, String> customerDate = (TableColumn<Person, String>) customerTable.getColumns().get(4);
        customerDate.setCellFactory(column -> new TableCell<>() {
            private final DatePicker datePicker = new DatePicker();
            {
                datePicker.setOnAction(event -> {
                    if (datePicker.getValue() != null) {
                        commitEdit(datePicker.getValue().toString());
                    }
                });
                datePicker.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused && datePicker.getValue() != null) {
                        commitEdit(datePicker.getValue().toString());
                    }
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        datePicker.setValue(LocalDate.parse(item));
                        setText(null);
                        setGraphic(datePicker);
                    } else {
                        setText(item);
                        setGraphic(null);
                    }
                }
            }
            @Override
            public void startEdit() {
                super.startEdit();
                String currentValue = getItem();
                if (currentValue != null) {
                    datePicker.setValue(LocalDate.parse(currentValue));
                }
                setText(null);
                setGraphic(datePicker);
                datePicker.requestFocus();
            }
            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem());
                setGraphic(null);
            }
        });
        customerDate.setOnEditCommit(event -> {
            Person person = event.getRowValue();
            String newDate = event.getNewValue();
            person.setDate(newDate);
            alterCustomerData(person.getDate(), person.getId(), "registration_date");
        });

        // Country
        TableColumn<Person, String> customerCountry = (TableColumn<Person, String>) customerTable.getColumns().get(5);
        customerCountry.setCellFactory(TextFieldTableCell.forTableColumn());
        customerCountry.setOnEditCommit(t -> {
            if (checkIfValidName(t.getNewValue())) {
                Person person = t.getRowValue();
                person.setCountry(t.getNewValue());
                alterCustomerData(person.getCountry(), person.getId(), "country");
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Country cannot be empty or a number.");
                t.getTableView().getItems().set(t.getTablePosition().getRow(), t.getRowValue()); // Revert
            }
        });

        // Platform
        TableColumn<Person, String> customerPlatform = (TableColumn<Person, String>) customerTable.getColumns().get(6);
        customerPlatform.setCellFactory(TextFieldTableCell.forTableColumn());
        customerPlatform.setOnEditCommit(t -> {
            Person person = t.getRowValue();
            person.setPlatform(t.getNewValue()); // Platform can be empty
            alterCustomerData(person.getPlatform(), person.getId(), "preferred_platform");
        });
    }


    public void alterCustomerData(String text, int id, String field) {
        // Use String.format to safely insert the column name
        String sql = String.format("UPDATE customer_record SET %s = ? WHERE customer_id = ?", field);
        
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3307/gamemanagementdatabase",
                "root", "");
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, text);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

        } catch(SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update customer: " + e.getMessage());
        }
    }

    private void setupActionButtonColumn() {
        TableColumn<Person, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setPrefWidth(180);

        actionCol.setCellFactory(column -> new TableCell<>() {
            private final Button viewButton = new Button("👁️ View");
            private final Button deleteButton = new Button("🗑️");
            private final HBox buttons = new HBox(5, viewButton, deleteButton);

            {
                viewButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand;");
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
                buttons.setAlignment(Pos.CENTER);

                viewButton.setOnAction(event -> {
                    Person person = getTableView().getItems().get(getIndex());
                    showCustomerDetails(person);
                });

                deleteButton.setOnAction(event -> {
                    Person person = getTableView().getItems().get(getIndex());
                    handleDeleteCustomer(person);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        });

        customerTable.getColumns().add(actionCol);
    }

    private void handleDeleteCustomer(Person person) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Customer?");
        confirmAlert.setContentText(
                "Are you sure you want to delete:\n\n" +
                        "ID: " + person.getId() + "\n" +
                        "Name: " + person.getFirstName() + " " + person.getLastName() + "\n" +
                        "Email: " + person.getEmail() + "\n\n" +
                        "This action cannot be undone!"
        );

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteFromDatabase(person.getId());
            loadCustomers(); // Refresh table
        }
    }

    private void deleteFromDatabase(int customerId) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3307/gamemanagementdatabase",
                "root", "")) {

            // Check if customer has transactions
            try (PreparedStatement checkTx = connection.prepareStatement(
                    "SELECT COUNT(*) FROM transaction_log WHERE customer_id = ?")) {
                checkTx.setInt(1, customerId);
                try (ResultSet rs = checkTx.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        showAlert(Alert.AlertType.WARNING, "Cannot Delete",
                                "This customer has purchase history.\n" +
                                        "Cannot delete customers with transactions.");
                        return;
                    }
                }
            }

            // If no transactions, proceed with deletion
            try (PreparedStatement deleteStmt = connection.prepareStatement(
                    "DELETE FROM customer_record WHERE customer_id = ?")) {
                deleteStmt.setInt(1, customerId);
                int rowsAffected = deleteStmt.executeUpdate();

                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Customer deleted successfully!");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete customer: " + e.getMessage());
        }
    }

    private void loadCustomers() {
        ObservableList<Person> customerList = FXCollections.observableArrayList();
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3307/gamemanagementdatabase",
                "root", "");
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM customer_record ORDER BY customer_id");
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                customerList.add(new Person(
                        rs.getInt("customer_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("registration_date"),
                        rs.getString("country"),
                        rs.getString("preferred_platform"),
                        rs.getDouble("total_spent"),
                        rs.getInt("games_owned")
                ));
            }
            customerTable.setItems(customerList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void showCustomerDetails(Person person) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Customer Details");
        dialog.setHeaderText(null);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setPrefWidth(650);

        // ═══ CUSTOMER INFORMATION ═══
        Label customerInfoHeader = new Label("═══ CUSTOMER INFORMATION ═══");
        customerInfoHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane customerInfo = new GridPane();
        customerInfo.setHgap(10);
        customerInfo.setVgap(8);

        customerInfo.add(new Label("ID:"), 0, 0);
        customerInfo.add(new Label(String.valueOf(person.getId())), 1, 0);
        customerInfo.add(new Label("Name:"), 0, 1);
        customerInfo.add(new Label(person.getFirstName() + " " + person.getLastName()), 1, 1);
        customerInfo.add(new Label("Email:"), 0, 2);
        customerInfo.add(new Label(person.getEmail()), 1, 2);
        customerInfo.add(new Label("Registration Date:"), 0, 3);
        customerInfo.add(new Label(person.getDate()), 1, 3);
        customerInfo.add(new Label("Country:"), 0, 4);
        customerInfo.add(new Label(person.getCountry()), 1, 4);
        customerInfo.add(new Label("Preferred Platform:"), 0, 5);
        customerInfo.add(new Label(person.getPlatform() != null ? person.getPlatform() : "N/A"), 1, 5);
        customerInfo.add(new Label("Total Spent:"), 0, 6);
        customerInfo.add(new Label(String.format("$%.2f", person.getSpent())), 1, 6);
        customerInfo.add(new Label("Games Owned:"), 0, 7);
        customerInfo.add(new Label(String.valueOf(person.getGames())), 1, 7);

        // ═══ GAMES PURCHASED ═══
        Label gamesHeader = new Label("═══ GAMES PURCHASED ═══");
        gamesHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TableView<GamePurchase> gamesTable = new TableView<>();
        gamesTable.setPrefHeight(250);

        TableColumn<GamePurchase, String> titleCol = new TableColumn<>("Game Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(300);

        TableColumn<GamePurchase, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(100);

        TableColumn<GamePurchase, String> dateCol = new TableColumn<>("Purchase Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        dateCol.setPrefWidth(120);

        TableColumn<GamePurchase, String> paymentCol = new TableColumn<>("Payment");
        paymentCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        paymentCol.setPrefWidth(120);

        gamesTable.getColumns().addAll(titleCol, priceCol, dateCol, paymentCol);

        ObservableList<GamePurchase> games = getCustomerGamesDetailed(person.getId());
        gamesTable.setItems(games);

        if (games.isEmpty()) {
            Label noGames = new Label("No games purchased yet.");
            noGames.setStyle("-fx-font-style: italic; -fx-text-fill: gray;");
            content.getChildren().addAll(customerInfoHeader, customerInfo, gamesHeader, noGames);
        } else {
            content.getChildren().addAll(customerInfoHeader, customerInfo, gamesHeader, gamesTable);
        }

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    // GamePurchase class
    public static class GamePurchase {
        private String title;
        private double price;
        private String purchaseDate;
        private String paymentMethod;

        public GamePurchase(String title, double price, String purchaseDate, String paymentMethod) {
            this.title = title;
            this.price = price;
            this.purchaseDate = purchaseDate;
            this.paymentMethod = paymentMethod;
        }

        public String getTitle() { return title; }
        public double getPrice() { return price; }
        public String getPurchaseDate() { return purchaseDate; }
        public String getPaymentMethod() { return paymentMethod; }
    }

    private ObservableList<GamePurchase> getCustomerGamesDetailed(int customerId) {
        ObservableList<GamePurchase> games = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3307/gamemanagementdatabase",
                "root", "");
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT gr.title, tl.amount, DATE_FORMAT(tl.purchase_date, '%Y-%m-%d') as purchase_date_formatted, tl.payment_method " +
                             "FROM game_record gr " +
                             "JOIN transaction_log tl ON gr.game_id = tl.game_id " +
                             "WHERE tl.customer_id = ? " +
                             "ORDER BY tl.purchase_date DESC")) {

            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    games.add(new GamePurchase(
                            rs.getString("title"),
                            rs.getDouble("amount"),
                            rs.getString("purchase_date_formatted"),
                            rs.getString("payment_method")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    public boolean checkIfValidName(String text) {
        if(text == null || text.isEmpty()) {
            return false;
        }
        return !isInteger(text);
    }

    public static boolean isInteger(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void back(ActionEvent event) throws IOException {
        System.out.println("Returning to Customer Menu");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customerView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}