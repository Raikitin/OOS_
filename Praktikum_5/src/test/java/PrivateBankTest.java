import bank.*;
import bank.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test cases for the private bank, testing different methods with correct and false inputs
 */
@DisplayName("Test methods for PrivateBank")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PrivateBankTest {

    static PrivateBank privateBank;
    static PrivateBank copyPrivateBank;
    static String dir = "F:\\Dokumente\\GitHub\\OOS\\Praktikum_5\\test_data";

    @DisplayName("Set up a PrivateBank")
    @BeforeAll
    public static void setUp() throws AccountAlreadyExistsException, IOException, TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException {

        File folder = new File(dir);
        if (folder.exists()) {
            File[] listOfFiles = folder.listFiles();
            if( listOfFiles != null) {
                for (File file : listOfFiles)
                    file.delete();
                Files.deleteIfExists(Path.of(dir));
            }
        }

        privateBank = new PrivateBank("JUnit Bank", 0, 0.12, dir);

        privateBank.createAccount("Adam");
        privateBank.createAccount("Eva");
        privateBank.addTransaction("Adam", new Payment("19.01.2011", -500, "Payment", 0.9, 0.25));
        privateBank.addTransaction("Eva", new IncomingTransfer("03.03.2000", 500, "IncomingTransfer from Adam to Eva", "Adam", "Eva"));
        copyPrivateBank = new PrivateBank(privateBank);

    }

    /*@DisplayName("Cleaner")
    @AfterAll
    public static void clean(){
        File directory = new File(dir);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            directory.delete();
        }
        }*/


    @DisplayName("Testing constructor")
    @Test
    @Order(0)
    public void constructorTest() {
        assertAll("PrivateBank",
                () -> assertEquals("JUnit Bank", privateBank.getName()),
                () -> assertEquals(dir, privateBank.getDirectoryName()),
                () -> assertEquals(0, privateBank.getIncomingInterest()),
                () -> assertEquals(0.12, privateBank.getOutgoingInterest()));
    }

    @DisplayName("Testing copy constructor")
    @Test
    @Order(1)
    public void copyConstructorTest() {
        assertAll("CopyPrivateBank",
                () -> assertEquals(privateBank.getName(), copyPrivateBank.getName()),
                () -> assertEquals(privateBank.getDirectoryName(), copyPrivateBank.getDirectoryName()),
                () -> assertEquals(privateBank.getIncomingInterest(), copyPrivateBank.getIncomingInterest()),
                () -> assertEquals(privateBank.getOutgoingInterest(), copyPrivateBank.getOutgoingInterest()));
    }

    @DisplayName("Create a duplicate account")
    @ParameterizedTest
    @Order(2)
    @ValueSource(strings = {"Adam", "Eva"})
    public void createDuplicateAccountTest(String account) {
        Exception exp = assertThrows(AccountAlreadyExistsException.class,
                () -> privateBank.createAccount(account));
        System.out.println(exp.getMessage());
    }

    @DisplayName("Create a valid account")
    @ParameterizedTest
    @Order(3)
    @ValueSource(strings = {"Kain", "Abel", "Set"})
    public void createValidAccountTest(String account) {
        assertDoesNotThrow(
                () -> privateBank.createAccount(account)
        );
    }

    @DisplayName("Create a valid account with a transactions list")
    @ParameterizedTest
    @Order(4)
    @ValueSource(strings = {"Ismael", "Isaak"})
    public void createValidAccountWithTransactionsListTest(String account) {
        assertDoesNotThrow(
                () -> privateBank.createAccount(account, List.of(
                        new Payment("23.11.2023", -2500, "Payment 2", 0.8, 0.5),
                        new OutgoingTransfer("08.06.2023", 1500, "OutgoingTransfer to Adam", account, "Adam")
                ))
        );
    }

    @DisplayName("Create a duplicate account with a transactions list")
    @ParameterizedTest
    @Order(5)
    @ValueSource(strings = {"Adam", "Eva", "Kain", "Abel", "Set", "Ismael", "Isaak"})
    public void createInvalidAccountWithTransactionsListTest(String account) {
        Exception exp = assertThrows(AccountAlreadyExistsException.class,
                () -> privateBank.createAccount(account, List.of(
                        new Payment("23.11.2023", -2500, "Payment 2", 0.8, 0.5)
                ))
        );
        System.out.println(exp.getMessage());
    }

    @DisplayName("Add a valid transaction to a valid account")
    @ParameterizedTest
    @Order(6)
    @ValueSource(strings = {"Adam", "Kain", "Abel", "Set"})
    public void addValidTransactionValidAccountTest(String account) {
        assertDoesNotThrow(
                () -> privateBank.addTransaction(account, new IncomingTransfer("08.06.2023", 1500, "OutgoingTransfer to Adam", "Eva", account))
        );
    }

    @DisplayName("Add a duplicate transaction to a valid account")
    @ParameterizedTest
    @Order(7)
    @ValueSource(strings = {"Ismael", "Isaak"})
    public void addDuplicateTransactionTest(String account) {
        Exception exp = assertThrows(TransactionAlreadyExistException.class,
                () -> privateBank.addTransaction(account, new Payment("23.11.2023", -2500, "Payment 2", 0.8, 0.5))
        );
        System.out.println(exp.getMessage());
    }

    @DisplayName("Add a valid transaction to an invalid account")
    @ParameterizedTest
    @Order(8)
    @ValueSource(strings = {"Maria", "Josef", "Moses"})
    public void addTransactionInvalidAccountTest(String account) {
        Exception exp = assertThrows(AccountDoesNotExistException.class,
                () -> privateBank.addTransaction(account, new Payment("19.01.2011", -500, "Payment", 0.9, 0.25))
        );
        System.out.println(exp.getMessage());
    }

    @DisplayName("Remove a valid transaction")
    //@ParameterizedTest
    @Test
    @Order(9)
    //@ValueSource(strings = {"Ismael", "Isaak"}) // needed a workaround to check if func is working
    public void removeValidTransactionTest() {
        //Payment pay = new Payment("23.11.2023", -2500, "Payment 2", 0.8, 0.5);
        //privateBank.addTransaction(account, pay);
        //assertDoesNotThrow(
        //        () -> privateBank.removeTransaction(account, new Payment("23.11.2023", -2500, "Payment 2", 0.8, 0.5))
        //);
        assertDoesNotThrow(() -> privateBank.createAccount("account"));
        Transaction validTransaction = new Payment("23.11.2023", -2500, "Payment 2", 0.8, 0.5);

        assertDoesNotThrow(() -> privateBank.addTransaction("account", validTransaction));
        assertTrue(privateBank.containsTransaction("account", validTransaction));
        assertDoesNotThrow(() -> privateBank.removeTransaction("account", validTransaction));
    }

    @DisplayName("Remove an invalid transaction")
    @ParameterizedTest
    @Order(10)
    @ValueSource(strings = {"Ismael", "Isaak"})
    public void removeInvalidTransactionTest(String account) {
        Exception exp = assertThrows(TransactionDoesNotExistException.class,
                () -> privateBank.removeTransaction(account, new Payment("19.01.2011", -500, "Payment", 0.9, 0.25))
        );
        System.out.println(exp.getMessage());
    }

    @DisplayName("Contains a transaction is true")
    @ParameterizedTest
    @Order(11)
    @ValueSource(strings = {"Ismael", "Isaak"})
    public void containsTransactionTrueTest(String account) {
        assertTrue(privateBank.containsTransaction(account, new OutgoingTransfer("08.06.2023", 1500, "OutgoingTransfer to Adam", account, "Adam")));
        System.out.println("containsTransactionTrueTest in <" + account + "> is correct.");
    }

    @DisplayName("Contains a transaction is false")
    @ParameterizedTest
    @Order(12)
    @ValueSource(strings = {"Adam", "Kain", "Abel", "Set", "Eva"})
    public void containsTransactionFalseTest(String account) {
        assertFalse(privateBank.containsTransaction(account, new OutgoingTransfer("08.06.2023", 1500, "OutgoingTransfer to Adam", account, "Adam")));
        System.out.println("containsTransactionFalseTest in <" + account + "> is correct.");
    }

    @DisplayName("Get account balance")
    @Test
    @Order(14)
    public void getAccountBalanceTest() {

        assertDoesNotThrow(() -> privateBank.createAccount("payment"));
        assertEquals(0., privateBank.getAccountBalance("payment"));

        Transaction validTransaction1 = new Payment("date", 1000, "pay", 0.5, 0.5);
        Transaction validTransaction2 = new Payment("date", 500, "pay", 0.5, 0.5);

        assertDoesNotThrow(() -> privateBank.addTransaction("payment", validTransaction1));
        assertEquals(500.0, privateBank.getAccountBalance("payment"));

        assertDoesNotThrow(() -> privateBank.addTransaction("payment", validTransaction2));
        assertEquals(750.0, privateBank.getAccountBalance("payment"));
    }

    @DisplayName("Get transactions list")
    @Test
    @Order(15)
    public void getTransactionTest() {
        List<Transaction> transactionList = List.of(
                new Payment("19.01.2011", -500, "Payment",  0.9, 0.25),
                new IncomingTransfer("08.06.2023", 1500, "OutgoingTransfer to Adam", "Eva", "Adam"));
        assertEquals(transactionList, privateBank.getTransactions("Adam"));
        System.out.println("getTransactionTest in <Adam> is correct.");
    }

    @DisplayName("Get transactions list by type")
    @Test
    @Order(16)
    public void getTransactionsByTypeTest() {
        List<Transaction> transactionList = List.of(
                new OutgoingTransfer("08.06.2023", 1500, "OutgoingTransfer to Adam", "Eva", "Adam"));
        assertDoesNotThrow(() -> privateBank.addTransaction("Eva", new OutgoingTransfer("08.06.2023", 1500, "OutgoingTransfer to Adam", "Eva", "Adam") ));
        assertEquals(transactionList, privateBank.getTransactionsByType("Eva", false));
        System.out.println("getTransactionByTypeTest in <Eva> is correct.");
    }

    @DisplayName("Get sorted transactions list")
    @Test
    @Order(17)
    public void getTransactionsSortedTest() {
        assertDoesNotThrow(() -> privateBank.removeTransaction("Eva", new OutgoingTransfer("08.06.2023", 1500.0, "OutgoingTransfer to Adam", "Eva", "Adam")));
        assertEquals(List.of(
                        new IncomingTransfer("03.03.2000", 500, "IncomingTransfer from Adam to Eva", "Adam", "Eva"))
                , privateBank.getTransactionsSorted("Eva", true));

    }

    @DisplayName("To String Test")
    @Test
    @Order(18)
    public void toStringTest() {
        PrivateBank dummyBank = new PrivateBank("Dummy Bank", 0.1, 0.1, dir);
        assertDoesNotThrow(() -> dummyBank.createAccount("Account"));
        assertDoesNotThrow(() -> dummyBank.addTransaction("Account", new IncomingTransfer("26.11.2023", 1000, "test", "Donor", "Account")));
        assertEquals(dummyBank.toString(), "Name: Dummy Bank, IncomingInterest: 0.1, OutgoingInterest: 0.1, Accounts to Transactions: {Account=[Date: 26.11.2023, Amount: 1000.0, Description: test, Sender: Donor, Recipient: Account]}");
    }

    @DisplayName("Equality Test")
    @Test
    @Order(19)
    public void equalsTest() throws IOException, AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException {
        PrivateBank copyBank = new PrivateBank(privateBank);
        /*System.out.println(privateBank.toString());
        System.out.println(copyBank.toString());*/
        assertTrue(privateBank.equals(copyBank));
    }

    @DisplayName("Deletion Test")
    @Test
    @Order(20)
    public void deleteTest() throws AccountAlreadyExistsException, IOException{
        assertDoesNotThrow(() -> privateBank.deleteAccount("Set"));
        Path path = Paths.get(privateBank.getDirectoryName() + "\\Konto Set.json");
        assertFalse(Files.exists(path));
    }

    @DisplayName("Get all accounts Test")
    @Test
    @Order(20)
    public void allAccountsTest(){
        List<String> accountList = new ArrayList<>();
        accountList.addAll(privateBank.accountsToTransactions.keySet());
        assertEquals(accountList, privateBank.getAllAccounts());
    }
}