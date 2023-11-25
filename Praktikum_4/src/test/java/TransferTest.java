import bank.IncomingTransfer;
import bank.OutgoingTransfer;

import bank.Transfer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


public class TransferTest {

    static Transfer incomingTransfer;
    static Transfer outgoingTransfer;
    static Transfer copyTransfer;

    @BeforeAll
    public static void setUp() {
        incomingTransfer = new IncomingTransfer("21.11.2023", 1000, "Test Incoming Transfer", "you", "me");
        outgoingTransfer = new OutgoingTransfer("08.06.2023", 500, "Test Outgoing Transfer", "me", "you");
        copyTransfer = new OutgoingTransfer(outgoingTransfer);
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
    public void copyConstructorTester() {
        assertEquals(outgoingTransfer.getAmount(), copyTransfer.getAmount());
        assertEquals(outgoingTransfer.getDate(), copyTransfer.getDate());
        assertEquals(outgoingTransfer.getRecipient(), copyTransfer.getRecipient());
        assertEquals(outgoingTransfer.getAmount(), copyTransfer.getAmount());
        assertEquals(outgoingTransfer.getSender(), copyTransfer.getSender());
        assertEquals(outgoingTransfer.getDescription(), copyTransfer.getDescription());
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
        assertEquals(outgoingTransfer, copyTransfer);
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