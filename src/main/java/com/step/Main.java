package com.step;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {
    /*
    ObservableList. Our data source.
    */
    private final ObservableList<Employee> employees =
            FXCollections.observableArrayList(
                    new Employee("John Smith"),
                    new Employee("Peter Smith"),
                    new Employee("Jane Smith"));
    private int selectedIndex = -1;

    /*
    Main method
    */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /*
    JavaFx Start Method
    */
    @Override
    public void start(Stage stage) throws Exception {
        final Label titleLabel = new Label("Employees");
        final TextField employeeNameTxtField = new TextField();
        final Button addButton = new Button("Add");
        final Button updateBtn = new Button("Update");
        final Button deleteBtn = new Button("Delete");
        final Button removeAll = new Button("Remove all");
        final Button clearSelection = new Button("Clear selection");


        Scene scene = new Scene(new Group());
        stage.setTitle("JavaFX CRUD");
        stage.setWidth(515);
        stage.setHeight(550);
        // Title
        titleLabel.setFont(new Font("Arial", 20));
        employeeNameTxtField.setPromptText("Name");

        ListView myListView = new ListView();
        myListView.setItems(employees);

        addButton.setOnAction((ActionEvent e) -> {
            employees.add(new Employee(employeeNameTxtField.getText()));
            employeeNameTxtField.clear();
        });


        myListView.setOnMouseClicked((MouseEvent event) -> {
            if (myListView.getSelectionModel().getSelectedItem() != null) {
                String selectedItem = myListView.getSelectionModel().getSelectedItem().toString();
                selectedIndex = myListView.getSelectionModel().getSelectedIndex();
                employeeNameTxtField.setText(selectedItem);
                addButton.setDisable(true);
            } else {
                addButton.setDisable(false);
            }
        });


        updateBtn.setOnAction((ActionEvent e) -> {
            if (myListView.getSelectionModel().getSelectedItem() != null) {
                Object selectedItem = myListView.getSelectionModel().getSelectedItem();
                Dialog d = new Alert(Alert.AlertType.INFORMATION, String.format("Selected employee (%s) has been updated to (%s)", selectedItem, employeeNameTxtField.getText()));
                d.show();
                employees.remove(selectedIndex);
                employees.add(selectedIndex, new Employee(employeeNameTxtField.getText()));
                employeeNameTxtField.clear();
            }
        });

        deleteBtn.setOnAction((ActionEvent e) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This action cannot be undone");
            alert.setTitle("Please confirm");
            alert.setHeaderText("Are you sure you want to delete this employee?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                employees.remove(selectedIndex);
                employeeNameTxtField.clear();
            }
        });

        removeAll.setOnAction((ActionEvent e) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This action cannot be undone");
            alert.setTitle("Please confirm");
            alert.setHeaderText("Are you sure you want to remove all the employees?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                employees.clear();
                employeeNameTxtField.clear();
            }
        });

        clearSelection.setOnAction((ActionEvent e) -> {
            myListView.getSelectionModel().clearSelection();
            employeeNameTxtField.clear();
            addButton.setDisable(false);
        });

        HBox myHBox = new HBox();
        myHBox.getChildren().addAll(employeeNameTxtField, addButton, updateBtn, deleteBtn, clearSelection, removeAll);
        myHBox.setSpacing(3);

        VBox myVBox = new VBox();
        myVBox.setSpacing(5);
        myVBox.setPadding(new Insets(10, 0, 0, 10));
        myVBox.getChildren().addAll(titleLabel, myListView, myHBox);

        ((Group) scene.getRoot()).getChildren().addAll(myVBox);

        stage.setScene(scene);
        stage.show();
    }

    /*
    Our data Object
    */
    public static class Employee {

        private final SimpleStringProperty name;

        private Employee(String name) {
            this.name = new SimpleStringProperty(name);
        }

        public String getName() {
            return name.get();
        }

        public void setName(String mName) {
            name.set(mName);
        }

        @Override
        public String toString() {
            return getName();
        }
    }
}