import bank.Payment;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * test cases for the payments, testing different methods
 */
public class PaymentTest {

    static Payment payment1;
    static Payment payment2;
    static Payment copyPayment;

    @BeforeAll
    @DisplayName("Set up for Payment objects")
    public static void setUp() {
        //System.out.println("Set up for Payment objects");
        payment1 = new Payment("21.11.2023", 1000, "Test Payment 1");
        payment2 = new Payment("08.06.2023", -2000, "Test Payment 2", 0.8, 0.5);
        copyPayment = new Payment(payment2);
    }


    @Test
    public void defaultConstructorTest() {
        Payment defaultPay = new Payment();
        assertEquals("01.01.2023", defaultPay.getDate());
        assertEquals(1000, defaultPay.getAmount());
        assertEquals("test", defaultPay.getDescription());
        assertEquals(0.0, defaultPay.getIncomingInterest());
        assertEquals(0.0, defaultPay.getOutgoingInterest());
    }

    @Test
    public void smallConstructorTest() {
        assertEquals("21.11.2023", payment1.getDate());
        assertEquals("Test Payment 1", payment1.getDescription());
        assertEquals(1000, payment1.getAmount());
    }

    @Test
    public void normalConstructorTest() {
        assertEquals("08.06.2023", payment2.getDate());
        assertEquals("Test Payment 2", payment2.getDescription());
        assertEquals(-2000, payment2.getAmount());
        assertEquals(0.8, payment2.getIncomingInterest());
        assertEquals(0.5, payment2.getOutgoingInterest());
    }

    @Test
    public void copyConstructorTest() {
        assertEquals(payment2.getDate(), copyPayment.getDate());
        assertEquals(payment2.getDescription(), copyPayment.getDescription());
        assertEquals(payment2.getAmount(), copyPayment.getAmount());
        assertEquals(payment2.getIncomingInterest(), copyPayment.getIncomingInterest());
        assertEquals(payment2.getOutgoingInterest(), copyPayment.getOutgoingInterest());
    }


    @Test
    public void calculateIncomingInterestTest() {
        double expected = payment1.getAmount() - payment1.getIncomingInterest() * payment1.getAmount();
        assertTrue(payment1.getAmount() >= 0);
        assertEquals(expected, payment1.calculate());
    }

    @Test
    public void calculateOutgoingInterestTest() {
        double expected = payment2.getAmount() + payment2.getOutgoingInterest() * payment2.getAmount();
        assertTrue(payment2.getAmount() < 0);
        assertEquals(expected, payment2.calculate());
    }

    @Test
    public void equalsTrueTest() {
        assertEquals(payment2, copyPayment);
    }

    @Test
    public void equalsFalseTest() {
        assertNotEquals(payment1, payment2);
    }

    @Test
    public void toStringTester() {
        String string = "Date: 08.06.2023, Amount: -3000.0, Description: Test Payment 2, Incoming Interest: 0.8, Outgoing Interest: 0.5";
        assertEquals(string, payment2.toString());
    }

}
