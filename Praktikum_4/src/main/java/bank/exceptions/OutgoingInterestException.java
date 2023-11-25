package bank.exceptions;

public class OutgoingInterestException extends Exception{

    public OutgoingInterestException() {
        super("Wrong Withdrawal");
    }
}
