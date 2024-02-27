import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
            collection.insertOne(new Document()
                    .append("_id", new ObjectId())
                    .append("address", address)
                    .append("name", hostName));
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

    public void closeServer() throws UnknownHostException {
        collection = database.getCollection("Servers");
        
        String address = InetAddress.getLocalHost().getHostAddress();
        Bson query = Filters.eq("address", address);

        try {
            collection.deleteOne(query);
        } catch (MongoException me) {}
    }

    public void createAccount(String name, String password) {
        collection = database.getCollection("Accounts");

        try {
            collection.insertOne(new Document()
                    .append("_id", new ObjectId())
                    .append("name", name)
                    .append("password", password)
                    .append("level", 0)
                    .append("exp", 0));

            Bson projectionFields = Projections.fields(
                Projections.include("name", "password", "level", "exp"),
                Projections.excludeId());
                
            Document doc = collection.find(Filters.eq("password", password))
                    .projection(projectionFields)
                    .first();

            MyGame.account.name = doc.getString("name");
            MyGame.account.level = doc.getInteger("level");
            MyGame.account.exp = doc.getInteger("exp");
            MyGame.status.addMessage("Account created successfully!");
        } catch (MongoException me) {}
    }

    public void linkAccount(String name, String password) {
        collection = database.getCollection("Accounts");

        Bson projectionFields = Projections.fields(
                Projections.include("name", "password", "level", "exp"),
                Projections.excludeId());
        
        FindIterable<Document> iterable = collection.find()
                .projection(projectionFields);

        ArrayList<Document> accounts = new ArrayList<>();
        iterable.into(accounts);

        for (int i = 0; i < accounts.size(); i++) {
            Document doc = accounts.get(i);

            if (doc.getString("name").equals(name) && doc.getString("password").equals(password)) {
                MyGame.account.name = name;
                MyGame.account.level = doc.getInteger("level");
                MyGame.account.exp = doc.getInteger("exp");
                MyGame.status.addMessage("Logged in successfully!");
                return;
            }
        }

        createAccount(name, password);
    }
}