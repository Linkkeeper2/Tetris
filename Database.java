import java.net.InetAddress;
import java.net.UnknownHostException;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;

public class Database {
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public Database() {
        // Replace the uri string with your MongoDB deployment's connection string
        try {
            String uri = "mongodb+srv://Linkkeeper2:admin@cluster0.hae2g3k.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
            
            client = MongoClients.create(uri);
            database = client.getDatabase("Tetris");
            collection = database.getCollection("Servers");
        } catch (Exception e) {}
    }

    public void createServer(String address, String hostName) {
        collection = database.getCollection("Servers");

        try {
            // Inserts a sample document describing a movie into the collection
            collection.insertOne(new Document()
                    .append("_id", new ObjectId())
                    .append("address", address)
                    .append("name", hostName));
        
        // Prints a message if any exceptions occur during the operation
        } catch (MongoException me) {}
    }

    public FindIterable<Document> getServers() {
        collection = database.getCollection("Servers");

        Bson projectionFields = Projections.fields(
                Projections.include("address", "name"),
                Projections.excludeId());
        
        FindIterable<Document> servers = collection.find()
                .projection(projectionFields);

        return servers;
    }

    public void joinServer(String address) {

    }

    public void closeServer() throws UnknownHostException {
        collection = database.getCollection("Servers");
        
        String address = InetAddress.getLocalHost().getHostAddress();
        Bson query = Filters.eq("address", address);

        try {
            collection.deleteOne(query);
        } catch (MongoException me) {}
    }
}