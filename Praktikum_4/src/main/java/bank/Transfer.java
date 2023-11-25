package bank;

import bank.exceptions.*;
import com.google.gson.annotations.SerializedName;

/**
 * <p>Used for the transfer between two account in a transaction</p>
 *
 * @see bank.Transaction
 */
public class Transfer extends Transaction {

    private String sender;
    private String recipient;

    /**
     * <p>creates a transfer object</p>
     *
     * @param date        Date in format DD.MM.YYYY
     * @param amount      Amount of money
     * @param description Description of the transaction
     */
    public Transfer(String date, double amount, String description) {
        super(date, 0, description);
        setAmount(amount);
    }

    /**
     * <p>creates a transfer object</p>
     *
     * @param date        Date in format DD.MM.YYYY
     * @param amount      Amount of money
     * @param description Description of the transaction
     * @param sender      Sender of the transaction
     * @param recipient   Recipient of the transaction
     */
    public Transfer(String date, double amount, String description, String sender, String recipient) {
        this(date, amount, description);
        setSender(sender);
        setRecipient(recipient);
    }

    /**
     * <p>creates a new transfer object identical to orig</p>
     *
     * @param orig orig gets copied into the new obj
     */
    public Transfer(Transfer orig) {
        this(orig.getDate(), orig.getAmount(), orig.getDescription(), orig.getSender(), orig.getRecipient());
    }


    /**
     * throws exception if the amount is less then 0
     * @param amount sets the amount of the transaction
     */
    @Override
    public void setAmount(double amount) {
        try{
            if(amount < 0){
                throw new TransferAmountException();
            }
            else{
                this.amount = amount;
            }
        } catch (TransferAmountException exc)
        {
            this.amount = 0;
        }
    }

    /**
     * <p>returns the sender</p>
     *
     * @return sender
     */
    public String getSender() {
        return this.sender;
    }

    /**
     * <p>returns the sender</p>
     *
     * @param sender Sets the sender
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * <p>returns the recipient</p>
     *
     * @return recipient
     */
    public String getRecipient() {
        return this.recipient;
    }

    /**
     * <p>returns the recipient</p>
     *
     * @param recipient Sets the recipient
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }


    /**
     * <p>Calculates the new amount of your balance</p>
     *
     * @return calculated balance
     */
    public double calculate() {
        return this.amount;
    }

    /**
     * <p>Creates a String from the object data</p>
     *
     * @return ret Transfer data as string
     */
    public String toString() {
        //String ret = super.toString() + ", Sender: " + sender + ", Recipient: " + recipient;
        //return ret;
        return super.toString() + ", Sender: " + sender + ", Recipient: " + recipient;
    }

    /**
     * <p>Checks for equality of two objects</p>
     *
     * @return true on equal, false on not equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        final Transfer other = (Transfer) obj;
        return super.equals(other) && this.sender.equals(other.sender) && this.recipient.equals(other.recipient);
    }

}
