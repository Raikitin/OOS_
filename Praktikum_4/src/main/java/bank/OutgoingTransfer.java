package bank;

/**
 * own subclass for outgoing transfers
 */
public class OutgoingTransfer extends Transfer{

    /**
     * using the super constructor of transfer
     * @param date Date of transfer
     * @param amount    Amount of Transfer
     * @param description   Description of Transfer
     * @param sender    Sender of Transfer
     * @param recipient Recipient of Transfer
     */
    public OutgoingTransfer(String date, double amount, String description, String sender, String recipient)
    {
        super(date, amount, description, sender, recipient);
    }

    /**
     * @param orig Original object
     */
    public OutgoingTransfer(Transfer orig)
    {
        super(orig);
    }

    /**
     * @return subs the amount
     */
    @Override
    public double calculate(){
        return -super.calculate();
    }
}
