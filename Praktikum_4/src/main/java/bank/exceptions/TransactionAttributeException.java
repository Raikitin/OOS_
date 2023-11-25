package bank.exceptions;

public class TransactionAttributeException extends Exception{

    public TransactionAttributeException() {
        super("Transaction attribute error.");
    }
}
