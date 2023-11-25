package bank;

/**
 * <p>Super Class for all Transaction</p>
 */
public abstract class Transaction implements CalculateBill {

    protected String date;
    protected double amount;
    protected String description;

    public Transaction(){
        this.setDate("01.01.2023");
        this.setAmount(1000);
        this.setDescription("test");
    }

    /**
     * <p>creates a Transaction object</p>
     *
     * @param date        Date in format DD.MM.YYYY
     * @param amount      Amount of Money
     * @param description Description of the Transaction
     */
    public Transaction(String date, double amount, String description) {
        setDate(date);
        setAmount(amount);
        setDescription(description);
    }

    /**
     * @param orig orig gets copied into the new obj
     */
    public Transaction(Transaction orig) {
        this(orig.getDate(), orig.getAmount(), orig.getDescription());
    }

    /**
     * @return date
     */
    public String getDate() {
        return this.date;
    }

    /**
     * <p>Format DD.MM.YYYY</p>
     *
     * @param date sets the date of the transaction
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return amount
     */
    public double getAmount() {
        return this.amount;
    }

    /**
     * <p>Amount must be positiv</p>
     *
     * @param amount sets the amount of the transaction
     */
    public void setAmount(double amount) {

        this.amount = amount;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @param description sets the description of the transaction
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * <p>Creates a String from the object data</p>
     *
     * @return ret object data as string
     */
    public String toString() {
        //String ret = "Date: " + date + ", Amount: " + calculate() + ", Description: " + description;
        //return ret;
        return "Date: " + date + ", Amount: " + calculate() + ", Description: " + description;
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
        final Transaction other = (Transaction) obj;
        return this.description.equals(other.description) && Double.compare(this.amount, other.amount) == 0 && this.date.equals(other.date); // Error amount compare resulting in always false
    }

    public boolean validationTransaction(){
        return !this.getDate().isEmpty() && !this.getDescription().isEmpty() && this.getAmount() != 0.;
    }
}
