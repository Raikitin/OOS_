package bank;


import bank.exceptions.*;
import com.google.gson.annotations.SerializedName;

public class Payment extends Transaction {

    private double incomingInterest;
    private double outgoingInterest;

    /**
     * <p>creates a payment object</p>
     *
     * @param date        Date in format DD.MM.YYYY
     * @param amount      Amount of money
     * @param description Description of the transaction
     */
    public Payment(String date, double amount, String description) {
        super(date, amount, description);
    }

    /**
     * <p>creates a payment object</p>
     *
     * @param date             Date in format DD.MM.YYYY
     * @param amount           Amount of money
     * @param description      Description of the transaction
     * @param incomingInterest Incoming interest of that transaction, Value in %, 0 to 1
     * @param outgoingInterest Outgoing interest of that transaction, Value in %, 0 to 1
     */
    public Payment(String date, double amount, String description, double incomingInterest, double outgoingInterest) {
        this(date, amount, description);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
    }

    /**
     * <p>creates a new payment object identical to orig</p>
     *
     * @param orig orig gets copied into the new obj
     */
    public Payment(Payment orig) {
        this(orig.getDate(), orig.getAmount(), orig.getDescription());
        setIncomingInterest(orig.getIncomingInterest());
        setOutgoingInterest(orig.getOutgoingInterest());
    }

    /**
     * @return incomingInterest
     */
    public double getIncomingInterest() {
        return incomingInterest;
    }

    /**
     * throws exception if the interest is not between 0 and 1
     * @param incomingInterest given incomingInterest
     */
    public void setIncomingInterest(double incomingInterest) {
        try{
            if (incomingInterest < 0 || incomingInterest > 1) {
                throw new IncomingInterestException();
            }
            else {
                this.incomingInterest = incomingInterest;
            }
        } catch (IncomingInterestException exc)
        {
            this.incomingInterest = 0;
        }
    }

    /**
     * @return outgoingInterest
     */
    public double getOutgoingInterest() {
        return outgoingInterest;
    }

    /**
     * throws exception if interest is not between 0 and 1
     * @param outgoingInterest given outgoingInterest
     */
    public void setOutgoingInterest(double outgoingInterest) {
        try{
            if (outgoingInterest < 0 || outgoingInterest > 1) {
                throw new OutgoingInterestException();
            }
            else {
                this.outgoingInterest = outgoingInterest;
            }
        } catch (OutgoingInterestException exc)
        {
            this.outgoingInterest = 0;
        }
    }

    /**
     * <p>Calculates the new amount of your balance</p>
     *
     * @return calculated balance
     */
    public double calculate() {
        if (this.amount > 0) {
            return (this.amount - this.amount * incomingInterest);
        } else {
            return (this.getAmount() + this.getAmount() * this.outgoingInterest);
        }
    }

    /**
     * <p>Creates a String from the object data</p>
     *
     * @return ret Payment data as string
     */
    @Override
    public String toString() {
        //String ret = super.toString() + ", InInterest: " + incomingInterest + ", OutInterest: " + outgoingInterest;
        //ret;
        return super.toString() + ", Incoming Interest: " + incomingInterest + ", Outgoing Interest: " + outgoingInterest;
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
        final Payment other = (Payment) obj;
        return super.equals(other) && (Double.compare(this.outgoingInterest, other.outgoingInterest) == 0 && Double.compare(this.incomingInterest, other.incomingInterest) == 0);
    }
}
