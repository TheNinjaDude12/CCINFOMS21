package com.example.demo22;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class customerTable {

    @FXML
    private TableView customerTable;
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
        public void setSpent(float spent) {
            this.spent = spent;
        }
        public void setGames(int games) {
            this.games = games;
        }


    }
    public void initialize() {
        TableColumn customerID = new TableColumn("ID");
        TableColumn customerFirstName = new TableColumn("First Name");
        TableColumn customerLastName = new TableColumn("Last Name");
        TableColumn customerEmail = new TableColumn("Email");
        TableColumn customerDate = new TableColumn("Registration Date");
        TableColumn customerCountry = new TableColumn("Country");
        TableColumn customerPlatform = new TableColumn("Preferred Platform");
        TableColumn customerSpent = new TableColumn("Total Spent");
        TableColumn customerGames = new TableColumn("Games Owned");

        customerTable.getColumns().addAll(customerID, customerFirstName, customerLastName,
                customerEmail, customerDate, customerCountry, customerPlatform, customerSpent, customerGames);

        ObservableList<customerTable.Person> data = FXCollections.observableArrayList();




        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/gamemanagementdatabase",
                    "root", "thunder1515");

            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT * FROM customer_record");

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                data.add(new customerTable.Person(resultSet.getInt("customer_id"), resultSet.getString("first_name"),
                        resultSet.getString("last_name"), resultSet.getString("email"),
                        resultSet.getString("registration_date"), resultSet.getString("country"),
                        resultSet.getString("preferred_platform"),
                        resultSet.getDouble("total_spent"), resultSet.getInt("games_owned")));

            }

            connection.close();

        } catch(SQLException e) {
            e.printStackTrace();
        }

        customerID.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        customerLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        customerEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        customerDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        customerCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        customerPlatform.setCellValueFactory(new PropertyValueFactory<>("platform"));
        customerSpent.setCellValueFactory(new PropertyValueFactory<>("spent"));
        customerGames.setCellValueFactory(new PropertyValueFactory<>("games"));
        customerTable.setItems(data);

    }
}
