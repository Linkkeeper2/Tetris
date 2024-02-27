import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Database {
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public Database() {
        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "mongodb+srv://Linkkeeper2:admin@cluster0.hae2g3k.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        client = MongoClients.create(uri);
        database = client.getDatabase("Tetris");
        collection = database.getCollection("Servers");
    }

    public void createServer(String address, String hostName) {
        try {
            // Inserts a sample document describing a movie into the collection
            collection.insertOne(new Document()
                    .append("_id", new ObjectId())
                    .append("address", address)
                    .append("name", hostName));
        
        // Prints a message if any exceptions occur during the operation
        } catch (MongoException me) {
            MyGame.status.addMessage("Unable to insert due to an error: " + me);
        }
    }
}