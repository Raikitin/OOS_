package com.oos;

import bank.*;
import bank.exceptions.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

public class AccountController implements Initializable {

    private final ObservableList<Transaction> transactionsList = FXCollections.observableArrayList();
    private PrivateBank privateBank;

    @FXML
    public Text text;
    @FXML
    public MenuButton addButton;
    @FXML
    public MenuItem payment;
    @FXML
    public MenuItem transfer;
    @FXML
    public Parent root;
    @FXML
    public MenuButton optionsButton;
    @FXML
    public MenuItem allTransaction;
    @FXML
    public MenuItem ascending;
    @FXML
    public MenuItem descending;
    @FXML
    public MenuItem positive;
    @FXML
    public MenuItem negative;
    @FXML
    public ListView<Transaction> transactionsListView;
    @FXML
    public Text accountName;
    @FXML
    public Button backButton;

    private void updateListView(List<Transaction> listTransaction) {
        transactionsList.clear();
        transactionsList.addAll(listTransaction);
        transactionsListView.setItems(transactionsList);
    }

    private void setDialogAddTransaction(MenuItem menuItem, String name){

        Dialog<Transaction> dialog = new Dialog<>();
        GridPane gridPane = new GridPane();

        Label date = new Label("Datum: ");
        Label amount = new Label("Betrag: ");
        Label description = new Label("Beschreibung: ");
        Label incomingInterest_sender = new Label();
        Label outgoingInterest_recipient = new Label();

        TextField dateText = new TextField();
        TextField amountText = new TextField();
        TextField descriptionText = new TextField();
        TextField incomingInterest_senderText = new TextField();
        TextField outgoingInterest_recipientText = new TextField();

        gridPane.add(date, 1, 1);
        gridPane.add(dateText, 2, 1);
        gridPane.add(description, 1, 2);
        gridPane.add(descriptionText, 2, 2);
        gridPane.add(amount, 1, 3);
        gridPane.add(amountText, 2, 3);
        gridPane.add(incomingInterest_sender, 1, 4);
        gridPane.add(incomingInterest_senderText, 2, 4);
        gridPane.add(outgoingInterest_recipient, 1, 5);
        gridPane.add(outgoingInterest_recipientText, 2, 5);

        ButtonType okButton = new ButtonType("Hinzufügen", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().setContent(gridPane);
        dialog.setResizable(true);
        dialog.getDialogPane().getButtonTypes().add(okButton);

        Alert invalid = new Alert(Alert.AlertType.ERROR);
        dialog.show();
        //Payment fall
        if (Objects.equals(menuItem.getId(), "payment")) {
            dialog.setTitle("Payment");
            incomingInterest_sender.setText("Incoming interest: ");
            outgoingInterest_recipient.setText("Outgoing interest: ");
            dialog.setResultConverter(buttonType -> {
                if (buttonType == okButton) {
                    if (Objects.equals(dateText.getText(), "") ||
                            Objects.equals(amountText.getText(), "") ||
                            Objects.equals(descriptionText.getText(), "") ||
                            Objects.equals(incomingInterest_senderText.getText(), "") ||
                            Objects.equals(outgoingInterest_recipientText.getText(), "")) {
                        invalid.setContentText("Ungültige Werte");
                        Optional<ButtonType> optional = invalid.showAndWait();
                        if (optional.isPresent() && optional.get() == ButtonType.OK) {
                            text.setText("Es wurde nichts gemacht");
                        }
                    } else {
                        Payment payment = new Payment(dateText.getText(),
                                Double.parseDouble(amountText.getText()),
                                descriptionText.getText(),
                                Double.parseDouble(incomingInterest_senderText.getText()),
                                Double.parseDouble(outgoingInterest_recipientText.getText()));
                        try {
                            privateBank.addTransaction(name, payment);
                            text.setText("Neues Payment hinzugefügt");
                        } catch (TransactionAlreadyExistException | AccountDoesNotExistException |
                                 TransactionAttributeException e) {
                            throw new RuntimeException(e);
                        }

                        updateListView(privateBank.getTransactions(name));
                        accountName.setText(name + " [" + privateBank.getAccountBalance(name) + " €]");
                    }
                }
                return null;
            });
        } else if (Objects.equals(menuItem.getId(), "transfer")) { // Transfer
            incomingInterest_sender.setText("Sender: ");
            outgoingInterest_recipient.setText("Empfänger: ");

            dialog.setResultConverter(buttonType -> {
                if (buttonType == okButton) {
                    if (Objects.equals(dateText.getText(), "") ||
                            Objects.equals(amountText.getText(), "") ||
                            Objects.equals(descriptionText.getText(), "") ||
                            Objects.equals(incomingInterest_senderText.getText(), "") ||
                            Objects.equals(outgoingInterest_recipientText.getText(), "")) {
                        invalid.setContentText("Ungültige Werte");
                        Optional<ButtonType> optional = invalid.showAndWait();
                        if (optional.isPresent() && optional.get() == ButtonType.OK) {
                            text.setText("Es wurde nicht gemacht");
                        }
                    } else {
                        if (outgoingInterest_recipientText.getText().equals(name)) { // Incoming Transfer
                            dialog.setTitle("Incomingtransfer");
                            IncomingTransfer incomingTransfer = new IncomingTransfer(dateText.getText(),
                                    Double.parseDouble(amountText.getText()),
                                    descriptionText.getText(),
                                    incomingInterest_senderText.getText(),
                                    outgoingInterest_recipientText.getText());

                            try {
                                privateBank.addTransaction(name, incomingTransfer);
                                text.setText("Der Incomingtransfer wurde hinzugefügt");
                            } catch (TransactionAlreadyExistException | AccountDoesNotExistException |
                                     TransactionAttributeException e) {
                                throw new RuntimeException(e);
                            }
                            updateListView(privateBank.getTransactions(name));
                            accountName.setText(name + " [" + privateBank.getAccountBalance(name) + "€]");
                        } else if (incomingInterest_senderText.getText().equals(name)) {// Outgoing Transfer
                            dialog.setTitle("Outgoingtransfer");
                            OutgoingTransfer outgoingTransfer = new OutgoingTransfer(dateText.getText(),
                                    Double.parseDouble(amountText.getText()),
                                    descriptionText.getText(),
                                    incomingInterest_senderText.getText(),
                                    outgoingInterest_recipientText.getText());

                            try {
                                privateBank.addTransaction(name, outgoingTransfer);
                                text.setText("Outgoing Transfer hinzugefügt");
                            } catch (TransactionAlreadyExistException | AccountDoesNotExistException |
                                     TransactionAttributeException e) {
                                throw new RuntimeException(e);
                            }
                            updateListView(privateBank.getTransactions(name));
                            accountName.setText(name + " [" + privateBank.getAccountBalance(name) + "€]");
                        }
                    }
                }
                return null;
            });
        }
    }



    public void setupData(PrivateBank privateBank, String name) throws AccountDoesNotExistException{

        this.privateBank = privateBank;
        accountName.setText(name + " [" + privateBank.getAccountBalance(name) + "€]");
        updateListView(privateBank.getTransactions(name));

        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteTransaction = new MenuItem("Transaktion löschen");

        contextMenu.getItems().addAll(deleteTransaction);
        transactionsListView.setContextMenu(contextMenu);

        AtomicReference<Transaction> selectedTransaction = new AtomicReference<>();

        transactionsListView.setOnMouseClicked(mouseEvent -> {
            selectedTransaction.set(transactionsListView.getSelectionModel().getSelectedItem());
            System.out.println("[" + selectedTransaction.toString() + "]");
        });

        deleteTransaction.setOnAction(event -> {

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Transaktion löschen.");
            confirmation.setContentText("Möchten sie wirklich diese Transaktion löschen?");
            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try{
                    privateBank.removeTransaction(name, selectedTransaction.get());
                } catch (AccountDoesNotExistException | TransactionDoesNotExistException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("[" + selectedTransaction.toString() + "] wurde erfolgreich gelöscht");
                text.setText("[" + selectedTransaction.toString() + "] wurde erfolgreich gelöscht");

                try {
                    updateListView(privateBank.getTransactions(name));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                try {
                    accountName.setText(name + " [" + privateBank.getAccountBalance(name) + "€]");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        allTransaction.setOnAction(event -> {
            try{
                updateListView(privateBank.getTransactions(name));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        ascending.setOnAction(event -> {
            try{
                updateListView(privateBank.getTransactionsSorted(name, true));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        descending.setOnAction(event -> {
            try{
                updateListView(privateBank.getTransactionsSorted(name, false));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        positive.setOnAction(event -> {
            updateListView(privateBank.getTransactionsByType(name, true));
        });

        negative.setOnAction(event -> {
            updateListView(privateBank.getTransactionsByType(name, false));
        });

        payment.setOnAction(event -> {
            setDialogAddTransaction(payment, name);
        });

        transfer.setOnAction(event -> {
            setDialogAddTransaction(transfer, name);
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        backButton.setOnMouseClicked(mouseEvent -> {
            try{
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Mainview.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setTitle("GuiBank - Willkommen");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        });
    }

}
