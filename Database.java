import static com.mongodb.client.model.Filters.eq;

import org.bson.types.ObjectId;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.mongodb.client.result.InsertOneResult;

import com.mongodb.client.model.Projections;

import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

import com.mongodb.client.result.DeleteResult;

public class Database {
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public Database() {
        String uri = "mongodb+srv://Linkkeeper2:admin@cluster0.hae2g3k.mongodb.net/?retryWrites=true&w=majority";

        try (this.client = MongoClients.create(uri)) {
            this.database = this.client.getDatabase("Tetris");
            this.collection = this.database.getCollection("Accounts");
        }
    }

    public void changeCollection(String collection) {
        this.collection = this.database.getCollection(collection);
    }
}