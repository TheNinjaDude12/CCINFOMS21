package com.example.demo22;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class UserLibrary {
    @FXML
    private Text gameRelease;
    @FXML
    private Text gameGenre;
    @FXML
    private Text gameTitle;
    @FXML
    private Text welcomeText;
    @FXML
    private TableView<Game> gamesTable;
    @FXML
    public TextField filterText;
    public class Game {
        private String title;

        Game(String title) {
            this.title = title;
        }
        public  String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
    }
    public static int user_id = UserLogIn.user_id;




    public void initialize() {
        TableColumn gameName = new TableColumn("Games");
        gamesTable.getColumns().addAll(gameName);
        ObservableList<UserLibrary.Game> games = FXCollections.observableArrayList();
        gamesTable.setEditable(false);
        gamesTable.setStyle(
                "-fx-background-color: #1b2838;" +  // Dark blue-gray background
                        "-fx-control-inner-background: #16202d;" +  // Darker row background
                        "-fx-table-cell-border-color: transparent;" +
                        "-fx-text-fill: white;"  // White text
        );


        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/gamemanagementdatabase",
                    "root", "thunder1515");

            PreparedStatement pstmt = connection.prepareStatement(
                        "SELECT title from game_record gr \n" +
                            "JOIN transaction_log tl ON gr.game_id = tl.game_id\n" +
                            "JOIN customer_record cr ON cr.customer_id = tl.customer_id " + "WHERE tl.customer_id =" + user_id);

            ResultSet resultSet = pstmt.executeQuery();
            while(resultSet.next()) {
                games.add(new Game(resultSet.getString("title")));
            }

            PreparedStatement pstmt2 = connection.prepareStatement("SELECT first_name FROM customer_record WHERE customer_id = " + user_id);
            ResultSet resultSet2 = pstmt2.executeQuery();
            if (resultSet2.next()) {
                welcomeText.setText("Welcome " + resultSet2.getString("first_name"));
            }





            connection.close();

        } catch(SQLException e) {
            e.printStackTrace();
        }
        gameName.setCellValueFactory(new PropertyValueFactory<>("title"));
        gameName.prefWidthProperty().bind(gamesTable.widthProperty());
        gamesTable.setItems(games);
        gameName.setCellFactory(TextFieldTableCell.forTableColumn());
        gamesTable.setOnMouseClicked(event -> {
            Game selectedGame = gamesTable.getSelectionModel().getSelectedItem();
            if (selectedGame != null && !selectedGame.getTitle().equals("Games")) {
                gameTitle.setText(selectedGame.getTitle());


                try {
                    Connection connection = DriverManager.getConnection(
                            "jdbc:mysql://127.0.0.1:3306/gamemanagementdatabase",
                            "root", "thunder1515");

                    PreparedStatement pstmt2 = connection.prepareStatement(
                            "SELECT genre, release_date FROM game_record WHERE title = '" + selectedGame.getTitle() + "'"  );

                    ResultSet resultSet2 = pstmt2.executeQuery();
                    if (resultSet2.next()) {
                        gameGenre.setText(resultSet2.getString("genre"));
                        gameRelease.setText(resultSet2.getString("release_date"));
                    }

                    connection.close();

                } catch(SQLException e) {
                    System.out.println("Error");
                }
            }

        });

        FilteredList<Game> filteredData = new FilteredList<>(games, b -> true);
         filterText.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(game -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();
                if(game.getTitle().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                }

                return false;
            });



        });
        SortedList<UserLibrary.Game> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(gamesTable.comparatorProperty());
        gamesTable.setItems(sortedData);


    }

    public void back(ActionEvent event) throws IOException {
        System.out.println("works");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("userMenuOptions.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
