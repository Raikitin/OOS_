package bank;

import java.awt.image.BandCombineOp;
import java.util.*;
import bank.exceptions.*;
import java.io.*;
import java.nio.file.*;
import com.google.gson.*;
import com.google.gson.reflect.*;
import java.lang.reflect.*;

/**
 * class for a private bank
 */
public class PrivateBank implements Bank{

    private String name;
    private double incomingInterest;
    private double outgoingInterest;
    public Map<String, List<Transaction>> accountsToTransactions = new HashMap<>();
    public String directoryName;

    /**
     * @param name Name of the bank
     * @param incomingInterest incomingInterest of the bank
     * @param outgoingInterest outgoingInterest of the bank
     */
    public PrivateBank(String name, double incomingInterest, double outgoingInterest, String directoryName){
        this.setName(name);
        this.setIncomingInterest(incomingInterest);
        this.setOutgoingInterest(outgoingInterest);
        this.setDirectoryName(directoryName);

        try{
            readAccounts();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param orig original bank that gets copied
     */
    public PrivateBank(PrivateBank orig) throws IOException, AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException {
        this(orig.getName(), orig.getIncomingInterest(), orig.getOutgoingInterest(), orig.getDirectoryName());

        try{
            orig.readAccounts();
        } catch (IOException exp){
            System.out.println(exp.getMessage());
        }

        for(String account : orig.accountsToTransactions.keySet()){
            this.createAccount(account, orig.getTransactions(account));

            try{
                this.writeAccount(account);
            } catch (IOException exp){
                System.out.println(exp.getMessage());
            }
        }
    }


    /**
     * @return name of the bank
     */
    public String getName(){
        return name;
    }

    /**
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return IncomingInterest of the bank
     */
    public double getIncomingInterest() {
        return incomingInterest;
    }

    /**
     * @param incomingInterest new IncomingInterest
     */
    public void setIncomingInterest(double incomingInterest) {
        this.incomingInterest = incomingInterest;
    }

    /**
     * @return OutgoingInterest of the bank
     */
    public double getOutgoingInterest() {
        return outgoingInterest;
    }

    /**
     * @param outgoingInterest new OutgoingInterest
     */
    public void setOutgoingInterest(double outgoingInterest) {
        this.outgoingInterest = outgoingInterest;
    }

    /**
     * @return directory name of the bank
     */
    public String getDirectoryName(){ return directoryName;}

    /**
     * Sets the directory of the bank
     * @param directoryName name of the new directory
     */
    public void setDirectoryName(String directoryName) { this.directoryName = directoryName;}

    /**
     * @return String of bank data
     */
    @Override
    public String toString(){
        String ret = "Name: " + this.getName() + ", IncomingInterest: " + this.getIncomingInterest() + ", OutgoingInterest: " + this.getOutgoingInterest() + ", Accounts to Transactions: " + this.accountsToTransactions.toString();
        return ret;
    }

    /**
     * @param obj object for comparison
     * @return true if they are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        PrivateBank other = (PrivateBank) obj;
        return this.name.equals(other.name) && (Double.compare(this.outgoingInterest, other.outgoingInterest) == 0 && Double.compare(this.incomingInterest, other.incomingInterest) == 0) && this.accountsToTransactions.equals(other.accountsToTransactions);
    }

    /**
     * Adds an account to the bank.
     *
     * @param account the account to be added
     * @throws AccountAlreadyExistsException if the account already exists
     */
    @Override
    public void createAccount(String account) throws AccountAlreadyExistsException {
        if(accountsToTransactions.containsKey(account)){
            throw new AccountAlreadyExistsException();
        }
        accountsToTransactions.put(account, new ArrayList<>());
        try {
            this.writeAccount(account);
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }
    }

    /**
     * Adds an account (with specified transactions) to the bank.
     * Important: duplicate transactions must not be added to the account!
     *
     * @param account      the account to be added
     * @param transactions a list of already existing transactions which should be added to the newly created account
     * @throws AccountAlreadyExistsException    if the account already exists
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     */
    @Override
    public void createAccount(String account, List<Transaction> transactions) throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException {
        if(accountsToTransactions.containsKey(account)){
            throw new AccountAlreadyExistsException();
        }
        Set<Transaction> duplicatedTransactions = new HashSet();
        for(Transaction transaction : transactions){
            if(!duplicatedTransactions.add(transaction)){
                throw new TransactionAlreadyExistException();
            }
            if(!transaction.validationTransaction()){
                throw new TransactionAttributeException();
            }
        }
        accountsToTransactions.put(account, transactions);

        try {
            this.writeAccount(account);
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }
    }

    /**
     * Deletes the given account in the private including the serialized file
     *
     * @param account account which should be deleted
     * @throws AccountDoesNotExistException if account doesn't exist
     * @throws IOException  if input or output is invalid
     */
    @Override
    public void deleteAccount(String account) throws AccountDoesNotExistException, IOException {
        if(!accountsToTransactions.containsKey(account)){
            throw new AccountDoesNotExistException();
        }
        accountsToTransactions.remove(account);
        Files.deleteIfExists(Path.of(getDirectoryName() + "\\Konto " + account + ".json"));
    }

    /**
     * Adds a transaction to an already existing account.
     *
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which should be added to the specified account
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     */
    @Override
    public void addTransaction(String account, Transaction transaction) throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException{
        if(!accountsToTransactions.containsKey(account)){
            throw new AccountDoesNotExistException();
        }
        if(containsTransaction(account, transaction)){
            throw new TransactionAlreadyExistException();
        }
        if(!transaction.validationTransaction()){
            throw new TransactionAttributeException();
        }
        accountsToTransactions.get(account).add(transaction);

        try {
            this.writeAccount(account);
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }
    }

    /**
     * Removes a transaction from an account. If the transaction does not exist, an exception is
     * thrown.
     *
     * @param account     the account from which the transaction is removed
     * @param transaction the transaction which is removed from the specified account
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionDoesNotExistException if the transaction cannot be found
     */
    @Override
    public  void removeTransaction(String account, Transaction transaction) throws AccountDoesNotExistException, TransactionDoesNotExistException{
        if(!accountsToTransactions.containsKey(account)){
            throw new AccountDoesNotExistException();
        }
        if(!containsTransaction(account, transaction)){
            throw new TransactionDoesNotExistException();
        }
        accountsToTransactions.get(account).remove(transaction);

        try {
            this.writeAccount(account);
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }
    }

    /**
     * Checks whether the specified transaction for a given account exists.
     *
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction to search/look for
     * @return True if already exist
     */
    @Override
    public boolean containsTransaction(String account, Transaction transaction){
        List<Transaction> transactions =  accountsToTransactions.get(account);
        for(Transaction t: transactions)
        {
            if(t.equals(transaction))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates and returns the current account balance.
     *
     * @param account the selected account
     * @return the current account balance
     */
    @Override
    public double getAccountBalance(String account){
        double balance = 0.0;
        for(Transaction transaction : accountsToTransactions.get(account)){
            balance += transaction.calculate();
        }
        return balance;
    }

    /**
     * @return list of all accounts
     */
    public List<String> getAllAccounts(){
        return new ArrayList<>(accountsToTransactions.keySet());
    }

    /**
     * Returns a list of transactions for an account.
     *
     * @param account the selected account
     * @return the list of all transactions for the specified account
     */
    @Override
    public List<Transaction> getTransactions(String account){
        return accountsToTransactions.get(account);
    }

    /**
     * Returns a sorted list (-> calculated amounts) of transactions for a specific account. Sorts the list either in ascending or descending order
     * (or empty).
     *
     * @param account the selected account
     * @param asc     selects if the transaction list is sorted in ascending or descending order
     * @return the sorted list of all transactions for the specified account
     */
    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc){
        List<Transaction> transactions = getTransactions(account);
        transactions.sort(Comparator.comparingDouble(Transaction::getAmount));
        if(!asc) {
            Collections.reverse(transactions);
        }
        return transactions;
    }

    /**
     * Returns a list of either positive or negative transactions (-> calculated amounts).
     *
     * @param account  the selected account
     * @param positive selects if positive or negative transactions are listed
     * @return the list of all transactions by type
     */
    @Override
    public List<Transaction> getTransactionsByType(String account, boolean positive){
        List<Transaction> transactions = getTransactions(account);
        List<Transaction> ret = new ArrayList<>();
        for(Transaction transaction: transactions){
            if(transaction.calculate() >= 0 && positive || transaction.calculate() < 0 && !positive)
            {
                ret.add(transaction);
            }
        }
        return ret;
    }

    /**
     * reading account jsons
     * @throws IOException
     */
    private void readAccounts() throws IOException {
        Path fileDirectory = Paths.get(directoryName);

        try {
            // Get all files in the directory
            List<Path> files = Files.walk(fileDirectory).filter(Files::isRegularFile).toList();

            // Populate the map with file names as keys and empty lists as values
            for (Path file : files) {
                String fileName = file.getFileName().toString();
                accountsToTransactions.put(fileName.replaceAll("Konto (.+?)\\.json", "$1"), new ArrayList<>());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //iterate over account data
        for (String account : accountsToTransactions.keySet()) {
            String fileName = "Konto " + account + ".json";
            Path filePath = Paths.get(directoryName, fileName);

            if (Files.exists(filePath)) {
                try (Reader reader = Files.newBufferedReader(filePath)) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeHierarchyAdapter(Transaction.class, new TransactionSerializer());
                    Gson gson = gsonBuilder.create();

                    Type transactionListType = new TypeToken<List<Transaction>>() {}.getType();
                    List<Transaction> transactions = gson.fromJson(reader, transactionListType);

                    accountsToTransactions.put(account, transactions);
                }
            }
        }
    }

    /**
     *
     * @param account account which will be written
     * @throws IOException
     */
    private void writeAccount(String account) throws IOException {
        String fileName = "Konto " + account + ".json";
        Path filePath = Paths.get(directoryName, fileName);


        Path directoryPath = filePath.getParent();
        Files.createDirectories(directoryPath);

        try(Writer writer = Files.newBufferedWriter(filePath)) {

            Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeHierarchyAdapter(Transaction.class, new TransactionSerializer()).create();

            List<Transaction> transactions = accountsToTransactions.get(account);

            if(transactions != null) {
                gson.toJson(transactions, writer);
            }
        } catch(IOException exp){
            throw new IOException();
        }
    }
}
