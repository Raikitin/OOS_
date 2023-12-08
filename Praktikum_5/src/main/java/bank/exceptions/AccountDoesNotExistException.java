package bank.exceptions;

public class AccountDoesNotExistException extends Exception{

    public AccountDoesNotExistException() {
        super("Account does not exist.");
    }
}
