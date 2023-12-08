import bank.IncomingTransfer;
import bank.OutgoingTransfer;

import bank.Transfer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * test cases for the transfers, testing different methods
 */
public class TransferTest {

    static Transfer incomingTransfer;
    static Transfer outgoingTransfer;
    static Transfer copyOutgoingTransfer;
    static Transfer copyIncomingTransfer;

    @BeforeAll
    public static void setUp() {
        incomingTransfer = new IncomingTransfer("21.11.2023", 1000, "Test Incoming Transfer", "you", "me");
        outgoingTransfer = new OutgoingTransfer("08.06.2023", 500, "Test Outgoing Transfer", "me", "you");
        copyOutgoingTransfer = new OutgoingTransfer(outgoingTransfer);
        copyIncomingTransfer = new IncomingTransfer(incomingTransfer);
    }


    @Test
    public void smallConstructorTest() {
        assertEquals("21.11.2023", incomingTransfer.getDate());
        assertEquals("Test Incoming Transfer", incomingTransfer.getDescription());
        assertEquals(1000, incomingTransfer.getAmount());

    }

    @Test
    public void normalConstructorTest() {
        assertEquals("08.06.2023", outgoingTransfer.getDate());
        assertEquals("Test Outgoing Transfer", outgoingTransfer.getDescription());
        assertEquals(500, outgoingTransfer.getAmount());
        assertEquals("me", outgoingTransfer.getSender());
        assertEquals("you", outgoingTransfer.getRecipient());
    }

    @Test
    public void copyOutgoingConstructorTester() {
        assertEquals(outgoingTransfer.getAmount(), copyOutgoingTransfer.getAmount());
        assertEquals(outgoingTransfer.getDate(), copyOutgoingTransfer.getDate());
        assertEquals(outgoingTransfer.getRecipient(), copyOutgoingTransfer.getRecipient());
        assertEquals(outgoingTransfer.getAmount(), copyOutgoingTransfer.getAmount());
        assertEquals(outgoingTransfer.getSender(), copyOutgoingTransfer.getSender());
        assertEquals(outgoingTransfer.getDescription(), copyOutgoingTransfer.getDescription());
    }

    @Test
    public void copyIncomingConstructorTester() {
        assertEquals(incomingTransfer.getAmount(), copyIncomingTransfer.getAmount());
        assertEquals(incomingTransfer.getDate(), copyIncomingTransfer.getDate());
        assertEquals(incomingTransfer.getRecipient(), copyIncomingTransfer.getRecipient());
        assertEquals(incomingTransfer.getAmount(), copyIncomingTransfer.getAmount());
        assertEquals(incomingTransfer.getSender(), copyIncomingTransfer.getSender());
        assertEquals(incomingTransfer.getDescription(), copyIncomingTransfer.getDescription());
    }

    @Test
    public void calculateIncomingTransferTest() {
        assertInstanceOf(IncomingTransfer.class, incomingTransfer);
        assertEquals(incomingTransfer.getAmount(), incomingTransfer.calculate());
    }

    @Test
    public void calculateOutgoingTransferTest() {
        assertInstanceOf(OutgoingTransfer.class, outgoingTransfer);
        assertEquals(-outgoingTransfer.getAmount(), outgoingTransfer.calculate());
    }

    @Test
    public void equalsTrueTest() {
        assertEquals(outgoingTransfer, copyOutgoingTransfer);
    }

    @Test
    public void equalsFalseTest() {
        assertNotEquals(incomingTransfer, outgoingTransfer);
    }

    @Test
    public void toStringTester() {
        assertEquals("Date: 08.06.2023, Amount: -500.0, Description: Test Outgoing Transfer, Sender: me, Recipient: you", outgoingTransfer.toString());
    }

    @Test
    public void toStringTester2() {
        assertEquals("Date: 21.11.2023, Amount: 1000.0, Description: Test Incoming Transfer, Sender: you, Recipient: me", incomingTransfer.toString());
    }
}