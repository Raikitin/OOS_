package bank.exceptions;

public class TransactionAlreadyExistException extends Exception{

    public TransactionAlreadyExistException() {
        super("Transaction already exists.");
    }
}
