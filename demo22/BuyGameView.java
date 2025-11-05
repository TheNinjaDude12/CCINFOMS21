package com.example.demo22;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class BuyGameView {
    @FXML
    private ChoiceBox<Integer> customerIdSelector;
    public void initialize() {
       Integer[] intArray = {1,2,3};
        ObservableList<Integer> observableIntList = FXCollections.observableArrayList(intArray);

        customerIdSelector.setItems(observableIntList);

    }
}
