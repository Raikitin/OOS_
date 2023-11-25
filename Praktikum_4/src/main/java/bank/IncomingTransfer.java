package bank;

/**
 * own subclass for incoming transfers
 */
public class IncomingTransfer extends Transfer{

    /**
     * using the super constructor of transfer
     * @param date Date of transfer
     * @param amount    Amount of Transfer
     * @param description   Description of Transfer
     * @param sender    Sender of Transfer
     * @param recipient Recipient of Transfer
     */
    public IncomingTransfer(String date, double amount, String description, String sender, String recipient)
    {
        super(date, amount, description, sender, recipient);
    }

    /**
     * @param orig Original object
     */
    public IncomingTransfer(Transfer orig)
    {
        super(orig);
    }

    /**
     * @return add the amount
     */
    @Override
    public double calculate(){
        return +super.calculate();
    }
}
