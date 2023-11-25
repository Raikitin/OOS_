package bank;

import com.google.gson.*;

import java.util.*;
import java.lang.reflect.*;


public class TransactionSerializer implements JsonSerializer<Transaction>, JsonDeserializer<Transaction> {

    /**
     * if its a list of transactions it will be stored here
     */
    private List<Transaction> transactionList = new ArrayList<>();

    /**
     * List of Deserilized Tranactions
     * @return Transaction
     */
    public List<Transaction> getTransactions(){ return transactionList; }

    /**
     * Resets Adapter
     */
    public void serializerReset(){
        transactionList = new LinkedList<Transaction>();

    }


    /**
     * Serializer for Transactions
     * @param src the Transaction to Serialize
     * @param typeOfSrc Type of src
     * @param context Serialize Context
     * @return the json Element
     */
    @Override
    public JsonElement serialize(Transaction src, Type typeOfSrc, JsonSerializationContext context) {
        System.out.println("Wird der Serialize ausgeführt?");
        JsonObject result = new JsonObject();
        JsonObject instance = new JsonObject();
        String classname = src.getClass().toString();
        String[] name = classname.split("\\.");
        classname = name[name.length-1];
        result.addProperty("CLASSNAME", classname);

        if(src instanceof Payment p){
            instance.addProperty("incomingInterest", p.getIncomingInterest());
            instance.addProperty("outgoingInterest", p.getOutgoingInterest());
        }
        else if(src instanceof Transfer t){
            instance.addProperty("sender", t.getSender());
            instance.addProperty("recipient", t.getRecipient());
        }

        instance.addProperty("date", src.getDate());
        instance.addProperty("amount", src.getAmount());
        instance.addProperty("description", src.getDescription());



        result.add("INSTANCE", instance);
        return result;
    }

    /**
     *
     * @param jsonElement The JSON to Deserialize
     * @param type The Type of the Json
     * @param jsonDeserializationContext The context of the JSON
     * @return the Deserialized Transaction
     * @throws JsonParseException if something went wrong: read message
     */
    @Override
    public Transaction deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String classname = jsonObject.get("CLASSNAME").getAsString();

        JsonObject instanceElement = jsonObject.get("INSTANCE").getAsJsonObject();

        if(classname.equals("Payment"))
        {
            return new Payment(
                    instanceElement.get("date").getAsString(),
                    instanceElement.get("amount").getAsDouble(),
                    instanceElement.get("description").getAsString(),
                    instanceElement.get("incomingInterest").getAsDouble(),
                    instanceElement.get("outgoingInterest").getAsDouble());

        } else if (classname.equals("IncomingTransfer")) {
            return new IncomingTransfer(
                    instanceElement.get("date").getAsString(),
                    instanceElement.get("amount").getAsDouble(),
                    instanceElement.get("description").getAsString(),
                    instanceElement.get("sender").getAsString(),
                    instanceElement.get("recipient").getAsString());

        } else if (classname.equals("OutgoingTransfer")){
            return new OutgoingTransfer(
                    instanceElement.get("date").getAsString(),
                    instanceElement.get("amount").getAsDouble(),
                    instanceElement.get("description").getAsString(),
                    instanceElement.get("sender").getAsString(),
                    instanceElement.get("recipient").getAsString());

        } else{
            throw new JsonParseException("Unsupported class: " + classname);
        }

    }

}
