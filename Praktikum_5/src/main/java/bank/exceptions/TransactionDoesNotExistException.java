package bank.exceptions;

public class TransactionDoesNotExistException extends Exception{

    public TransactionDoesNotExistException() {
        super("Transaction does not exist.");
    }
}
