package com.oos;

import bank.PrivateBank;
import bank.exceptions.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


public class MainController implements Initializable {

    private Stage stage;
    private Scene scene;
    private final ObservableList<String> accountList = FXCollections.observableArrayList();
    private String dir = "F:\\Dokumente\\GitHub\\OOS\\Praktikum_5\\bank_data";


    private PrivateBank privateBank = new PrivateBank("GuiBank", 0.5, 0.5, dir);


    //{privateBank = new PrivateBank("GuiBank", 0.5, 0.5, dir);}

    @FXML
    private Text text;

    @FXML
    private Button addButton;

    @FXML
    private ListView<String> accountsListView;

    @FXML
    private Parent root;

    private void updateListView(){
        accountList.clear();
        accountList.addAll(privateBank.getAllAccounts());
        accountList.sort(Comparator.naturalOrder());
        accountsListView.setItems(accountList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        updateListView();

        ContextMenu contextMenu = new ContextMenu();
        MenuItem viewAccount = new MenuItem("Account anzeigen");
        MenuItem deleteAccount = new MenuItem("Account löschen");

        contextMenu.getItems().addAll(viewAccount, deleteAccount);
        accountsListView.setContextMenu(contextMenu);

        AtomicReference<String> selectedAccount = new AtomicReference<>();

        accountsListView.setOnMouseClicked(mouseEvent -> {
                    selectedAccount.set(String.valueOf(accountsListView.getSelectionModel().getSelectedItems()));
                    //String account = selectedAccount.toString().replace("[", "").replace("]", "");
                    System.out.println(selectedAccount + " wurde ausgewählt.");
                    text.setText("Account " + selectedAccount + " wurde ausgewählt.");
                });

        // Action on Rightclick -> Account löschen
        deleteAccount.setOnAction(event -> {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Bestätigung");
            confirmation.setContentText("Soll der Account '" + selectedAccount.toString() + "' wirklich gelöscht werden?");
            Optional<ButtonType> result = confirmation.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK){
                try{
                    privateBank.deleteAccount(selectedAccount.toString().replace("[", "").replace("]", ""));
                } catch(AccountDoesNotExistException | IOException e){
                   throw new RuntimeException(e);
                }

                System.out.println(selectedAccount + " erfolgreich gelöscht");
                text.setText(selectedAccount + " erfolgreich gelöscht");
                updateListView();
            }
        });

        // Action on Rightclick -> Account anzeigen
        viewAccount.setOnAction(event -> {

            stage = (Stage) root.getScene().getWindow();

            try{
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("Accountview.fxml")));
                root = loader.load();
                AccountController accountController = loader.getController();
                accountController.setupData(privateBank, selectedAccount.toString().replace("[", "").replace("]", ""));

            } catch (IOException | AccountDoesNotExistException e) {
                throw new RuntimeException(e);
            }

            scene = new Scene(root);
            stage.setTitle("GuiBank - Willkommen");
            stage.setScene(scene);
            stage.show();
        });

        // Action on Account hinzufügen
        addButton.setOnMouseClicked(event -> {
            text.setText("");
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Neuen Account hinzufügen");
            dialog.setHeaderText("Neuen Account hinzufügen");
            dialog.getDialogPane().setMinWidth(300);

            Label nameLabel = new Label("Name: ");
            TextField nameTextField = new TextField();

            GridPane grid = new GridPane();
            grid.add(nameLabel, 2, 1);
            grid.add(nameTextField, 3, 1);
            dialog.getDialogPane().setContent(grid);
            dialog.setResizable(true);

            ButtonType buttonOK = new ButtonType("Bestätigen", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(buttonOK);

            dialog.setResultConverter(buttonType -> {
                if(buttonType == buttonOK) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    if(!Objects.equals(nameTextField.getText(), "")){

                        try{
                            privateBank.createAccount(nameTextField.getText());
                            text.setText("Der Account: '" + nameTextField.getText() + "' wurde erfolgreich hinzugefügt");
                        } catch (AccountAlreadyExistsException e) {
                            alert.setContentText("Dieser Account existiert bereits und kann nicht hinzugefügt werden");
                            Optional<ButtonType> result = alert.showAndWait();
                            if(result.isPresent() && result.get() == ButtonType.OK){
                                text.setText("Der Account: '" + nameTextField.getText() + "' existiert bereits und kann nicht hinzugefügt werden");
                            }
                            //System.out.println(e.getMessage());
                        }
                        updateListView();
                    }
                    else{
                        alert.setContentText("Bitte einen korrekten Namen eingeben");
                        Optional<ButtonType> result = alert.showAndWait();
                        if(result.isPresent() && result.get() == ButtonType.OK){
                            text.setText("Es konnte kein Account hinzugefügt werden");
                        }
                    }
                }
                return null;
            });

            dialog.show();
        });


    }
}
