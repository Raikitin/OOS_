package bank.exceptions;

public class TransferAmountException extends Exception{

    public TransferAmountException() {
        super("Invalid Amount");
    }
}
