import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class DatabaseUpdates {
    public static void main(String[] args) {
        String uri = "mongodb+srv://Linkkeeper2:admin@cluster0.hae2g3k.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";

        MongoClient client = MongoClients.create(uri);
        MongoDatabase database = client.getDatabase("Tetris");
        MongoCollection<Document> collection = database.getCollection("Accounts");

        Bson query = Filters.eq("prestige", 0);
        // Creates instructions to update the values of two document fields
        Bson updates = Updates.combine(
                Updates.set("skinRows", 10));

        try {
            collection.updateMany(query, updates);
        } catch (MongoException me) {
        }
    }
}
