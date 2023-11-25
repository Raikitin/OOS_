import com.google.gson.*;
import bank.*;

import java.io.File;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionSerializerTest {
    static String dir = "F:\\Dokumente\\GitHub\\OOS_\\Praktikum_4\\test_data";

    @org.junit.jupiter.api.AfterAll
    public static void cleanup() {
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
    }

    @org.junit.jupiter.api.Test
    void testPaymentSerialization() {
        PrivateBank privateBank = new PrivateBank("you_will_never_get_me", 0.5, 0.5, dir);

        assertDoesNotThrow(() -> privateBank.createAccount("payment"));
        Transaction validTransaction = new Payment("23.11.2023", 1000, "test", 0.5, 0.5);
        assertDoesNotThrow(() -> privateBank.addTransaction("payment", validTransaction));

        //get transactions for account payment
        List<Transaction> transactions = privateBank.accountsToTransactions.get("payment");

        //check for one transaction
        assertEquals(1, transactions.size());

        //get first transaction
        Transaction transaction = transactions.get(0);

        //new gson builder. as long as transaction serializers are correctly registered there cannot be any errors
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Payment.class, new TransactionSerializer());
        Gson gson = gsonBuilder.create();

        //serialize
        String json = gson.toJson(transaction);

        String comparison = "{" +
                "\"CLASSNAME\":\"Payment\"," +
                "\"INSTANCE\":{" +
                "\"incomingInterest\":0.5," +
                "\"outgoingInterest\":0.5," +
                "\"date\":\"23.11.2023\"," +
                "\"amount\":1000.0," +
                "\"description\":\"test\"" +
                "}" + "}";

        //compare json strings
        assertEquals(comparison, json);

        //get json object
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        //check CLASSNAME attribute
        JsonElement classNameElement = jsonObject.get("CLASSNAME");
        assertNotNull(classNameElement, "CLASSNAME property is missing in the JSON");
        assertEquals("Payment", classNameElement.getAsString());
    }

    @org.junit.jupiter.api.Test
    void testPaymentDeserialization() {
        PrivateBank privateBank = new PrivateBank("you_will_never_get_me", 0.5, 0.5, dir);

        assertDoesNotThrow(() -> privateBank.createAccount("payment"));
        Payment validTransaction = new Payment("23.11.2023", 1000, "test", 0.5, 0.5);
        assertDoesNotThrow(() -> privateBank.addTransaction("payment", validTransaction));

        //get transactions for account payment
        List<Transaction> transactions = privateBank.accountsToTransactions.get("payment");

        //check for one transaction
        assertEquals(1, transactions.size());

        //get first transaction
        Transaction transaction = transactions.get(0);

        //new gson builder
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Payment.class, new TransactionSerializer());
        Gson gson = gsonBuilder.create();

        //serialize
        String json = gson.toJson(transaction);

        String comparison = "{" +
                "\"CLASSNAME\":\"Payment\"," +
                "\"INSTANCE\":{" +
                "\"incomingInterest\":0.5," +
                "\"outgoingInterest\":0.5," +
                "\"date\":\"23.11.2023\"," +
                "\"amount\":1000.0," +
                "\"description\":\"test\"" +
                "}" + "}";

        //compare json strings
        assertEquals(comparison, json);

        //deserialize
        Payment deserializedTransaction = gson.fromJson(json, Payment.class);

        //check attributes
        assertEquals(0.5, deserializedTransaction.getIncomingInterest());
        assertEquals(0.5, deserializedTransaction.getOutgoingInterest());
        assertEquals("23.11.2023", deserializedTransaction.getDate());
        assertEquals(1000, deserializedTransaction.getAmount());
        assertEquals("test", deserializedTransaction.getDescription());
    }
}