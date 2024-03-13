import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimerTask;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;
import org.bson.types.ObjectId;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;

import java.awt.Color;

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
        } catch (Exception e) {
        }
    }

    public void createServer(String address, String hostName) {
        collection = database.getCollection("Servers");

        try {
            collection.insertOne(new Document()
                    .append("_id", new ObjectId())
                    .append("address", address)
                    .append("name", hostName)
                    .append("chat", Arrays.asList("Chat (/ to chat)"))
                    .append("lobby", Arrays.asList("Lobby"))
                    .append("status", Arrays.asList())
                    .append("attacks", Arrays.asList())
                    .append("results", Arrays.asList()));
        } catch (MongoException me) {
        }
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
        } catch (MongoException me) {
        }
    }

    public void chatMessage(String name, String message) {
        if (MyGame.client == null)
            return;

        collection = database.getCollection("Servers");

        Document query = new Document().append("address", MyGame.client.host);

        Bson updates = Updates.combine(
                Updates.addToSet("chat", name + ": " + message));

        try {
            collection.updateOne(query, updates);
        } catch (MongoException me) {
        }
    }

    public ArrayList<Menu.Text> readChat() {
        if (MyGame.client == null)
            return null;

        collection = database.getCollection("Servers");

        Bson projectionFields = Projections.fields(
                Projections.include("chat"),
                Projections.excludeId());

        Document doc = collection.find(Filters.eq("address", MyGame.client.host))
                .projection(projectionFields)
                .first();

        if (doc == null) {
            MyGame.leaveGame();
            return null;
        }

        @SuppressWarnings("unchecked")
        ArrayList<String> chats = (ArrayList<String>) doc.get("chat");

        ArrayList<Menu.Text> messages = new ArrayList<>();

        for (Object e : chats)
            messages.add(new Menu().new Text((String) e, 0, 0, Color.WHITE));

        return messages;
    }

    public void addAttack(String name, String target, int lines) {
        if (MyGame.client == null)
            return;

        collection = database.getCollection("Servers");

        Document query = new Document().append("address", MyGame.client.host);

        Bson updates = Updates.combine(
                Updates.addToSet("attacks", name + " sent " + lines + " lines to " + target));

        try {
            collection.updateOne(query, updates);
        } catch (MongoException me) {
        }
    }

    public ArrayList<String> readAttacks() {
        if (MyGame.client == null)
            return null;

        collection = database.getCollection("Servers");

        Bson projectionFields = Projections.fields(
                Projections.include("attacks"),
                Projections.excludeId());

        Document doc = collection.find(Filters.eq("address", MyGame.client.host))
                .projection(projectionFields)
                .first();

        if (doc == null) {
            MyGame.leaveGame();
            return null;
        }

        @SuppressWarnings("unchecked")
        ArrayList<String> chats = (ArrayList<String>) doc.get("attacks");

        return chats;
    }

    public void removeAttack(String attack) {
        if (MyGame.client == null)
            return;

        collection = database.getCollection("Servers");

        Document query = new Document().append("address", MyGame.client.host);

        Bson updates = Updates.combine(
                Updates.pull("attacks", attack));

        try {
            collection.updateOne(query, updates);
        } catch (MongoException me) {
        }
    }

    public void addStatus(String status) {
        if (MyGame.client == null)
            return;

        collection = database.getCollection("Servers");

        Document query = new Document().append("address", MyGame.client.host);

        Bson updates = Updates.combine(
                Updates.addToSet("status", status));

        try {
            collection.updateOne(query, updates);

            MyGame.timer.schedule(new TimerTask() {
                public void run() {
                    removeStatus(status);
                }
            }, 3000);
        } catch (MongoException me) {
        } catch (IllegalStateException e) {
        }
    }

    public ArrayList<Menu.Text> readStatus() {
        if (MyGame.client == null)
            return null;

        collection = database.getCollection("Servers");

        Bson projectionFields = Projections.fields(
                Projections.include("status"),
                Projections.excludeId());

        Document doc = collection.find(Filters.eq("address", MyGame.client.host))
                .projection(projectionFields)
                .first();

        if (doc == null) {
            MyGame.leaveGame();
            return null;
        }

        @SuppressWarnings("unchecked")
        ArrayList<String> chats = (ArrayList<String>) doc.get("status");

        ArrayList<Menu.Text> messages = new ArrayList<>();

        for (Object e : chats)
            messages.add(new Menu().new Text((String) e, 0, 0, Color.WHITE));

        return messages;
    }

    public void clearStatus() {
        if (MyGame.client == null)
            return;

        collection = database.getCollection("Servers");

        Document query = new Document().append("address", MyGame.client.host);

        Bson updates = Updates.combine(
                Updates.set("status", Arrays.asList()));

        try {
            collection.updateOne(query, updates);
        } catch (MongoException me) {
        }
    }

    public void removeStatus(String status) {
        if (MyGame.client == null)
            return;

        collection = database.getCollection("Servers");

        Document query = new Document().append("address", MyGame.client.host);

        Bson updates = Updates.combine(
                Updates.pull("status", status));

        try {
            collection.updateOne(query, updates);
        } catch (MongoException me) {
        }
    }

    public void addLobby(String name) {
        if (MyGame.client == null)
            return;

        collection = database.getCollection("Servers");

        Document query = new Document().append("address", MyGame.client.host);

        Bson updates = Updates.combine(
                Updates.addToSet("lobby", name));

        try {
            collection.updateOne(query, updates);
        } catch (MongoException me) {
        }
    }

    public ArrayList<Menu.Text> readLobby() {
        if (MyGame.client == null)
            return null;

        collection = database.getCollection("Servers");

        Bson projectionFields = Projections.fields(
                Projections.include("lobby"),
                Projections.excludeId());

        Document doc = collection.find(Filters.eq("address", MyGame.client.host))
                .projection(projectionFields)
                .first();

        if (doc == null) {
            MyGame.leaveGame();
            return null;
        }

        @SuppressWarnings("unchecked")
        ArrayList<String> gameLobby = (ArrayList<String>) doc.get("lobby");

        ArrayList<Menu.Text> lobby = new ArrayList<>();

        for (Object e : gameLobby)
            lobby.add(new Menu().new Text((String) e, 0, 0, Color.WHITE));

        return lobby;
    }

    public void removeLobby(String name) {
        if (MyGame.client == null)
            return;

        collection = database.getCollection("Servers");

        Document query = new Document().append("address", MyGame.client.host);

        Bson updates = Updates.combine(
                Updates.pull("lobby", name));

        try {
            collection.updateOne(query, updates);
        } catch (MongoException me) {
        }
    }

    public void addResult(String result) {
        if (MyGame.client == null)
            return;

        collection = database.getCollection("Servers");

        Document query = new Document().append("address", MyGame.client.host);

        Bson updates = Updates.combine(
                Updates.addToSet("results", result));

        try {
            collection.updateOne(query, updates);
        } catch (MongoException me) {
        }
    }

    public ArrayList<String> readResults() {
        if (MyGame.client == null)
            return null;

        collection = database.getCollection("Servers");

        Bson projectionFields = Projections.fields(
                Projections.include("results"),
                Projections.excludeId());

        Document doc = collection.find(Filters.eq("address", MyGame.client.host))
                .projection(projectionFields)
                .first();

        if (doc == null) {
            MyGame.leaveGame();
            return null;
        }

        @SuppressWarnings("unchecked")
        ArrayList<String> chats = (ArrayList<String>) doc.get("results");

        return chats;
    }

    public void createAccount(String name, String password) {
        collection = database.getCollection("Accounts");

        try {
            collection.insertOne(new Document()
                    .append("_id", new ObjectId())
                    .append("name", name)
                    .append("password", password.getBytes())
                    .append("level", 0)
                    .append("exp", 0)
                    .append("highestLvl", 0)
                    .append("prestige", 0)
                    .append("inputDelay", 150)
                    .append("controls", Arrays.asList(32, 37, 38, 39, 40, 47, 67, 90)));

            MyGame.status.addMessage("Account created successfully!", 2500);
        } catch (MongoException me) {
        }
    }

    @SuppressWarnings("unchecked")
    public void linkAccount(String name, String password) {
        collection = database.getCollection("Accounts");

        Bson projectionFields = Projections.fields(
                Projections.include("name", "password", "level", "exp", "highestLvl", "prestige", "inputDelay", "controls"),
                Projections.excludeId());

        FindIterable<Document> iterable = collection.find()
                .projection(projectionFields);

        ArrayList<Document> accounts = new ArrayList<>();
        iterable.into(accounts);

        for (int i = 0; i < accounts.size(); i++) {
            Document doc = accounts.get(i);

            Binary bytes = (Binary) doc.get("password");
            String pass = new String(bytes.getData(), StandardCharsets.UTF_8);

            if (doc.getString("name").equals(name) && pass.equals(password)) {
                MyGame.account.name = name;
                MyGame.account.level = doc.getInteger("level");
                MyGame.account.exp = doc.getInteger("exp");
                MyGame.account.prestige = doc.getInteger("prestige");
                MyGame.account.highestLevel = doc.getInteger("highestLvl");
                Account.inputDelay = doc.getInteger("inputDelay");
                Account.controls = (ArrayList<Integer>) doc.get("controls");
                MyGame.status.addMessage("Logged in successfully!", 2500);

                try {
                    FileWriter myWriter = new FileWriter("Account.txt");

                    myWriter.write(name + "\n" + password);
                    myWriter.close();
                } catch (IOException e) {
                }
                return;
            } else if (doc.getString("name").equals(name)) {
                MyGame.status.addMessage("Username is taken.", 2500);
                return;
            }
        }

        createAccount(name, password);

        try {
            FileWriter myWriter = new FileWriter("Account.txt");
            myWriter.write(name + "\n" + password);
            myWriter.close();
        } catch (IOException e) {
        }

        MyGame.account.login();
    }

    public void updateAccount(String name) {
        collection = database.getCollection("Accounts");

        Document query = new Document().append("name", name);

        Bson updates = Updates.combine(
                Updates.set("level", MyGame.account.level),
                Updates.set("exp", MyGame.account.exp),
                Updates.set("highestLvl", MyGame.account.highestLevel),
                Updates.set("prestige", MyGame.account.prestige),
                Updates.set("controls", Account.controls),
                Updates.set("inputDelay", Account.inputDelay));
        try {
            collection.updateOne(query, updates);
        } catch (MongoException me) {
        }
    }

    public FindIterable<Document> getAccounts() {
        collection = database.getCollection("Accounts");

        Bson projectionFields = Projections.fields(
                Projections.include("name", "level", "highestLvl", "prestige"),
                Projections.excludeId());

        FindIterable<Document> accounts = collection.find()
                .projection(projectionFields);

        return accounts;
    }

    public Document getAccount(String name) {
        collection = database.getCollection("Accounts");

        Bson projectionFields = Projections.fields(
                Projections.include("name"),
                Projections.excludeId());

        Document account = collection.find(Filters.eq("name", name))
            .projection(projectionFields)
            .first();

        return account;
    }

    public void readAll() {
        if (MyGame.client != null) {
            MyGame.client.updateLobby();
            MyGame.chat.updateChat(readChat());
            MyGame.status.updateStatus(readStatus());

            ArrayList<String> results = readResults();

            if (MyGame.menu instanceof Menus.ResultsMenu) {
                Menus.ResultsMenu menu = (Menus.ResultsMenu) MyGame.menu;

                menu.otherDeaths = new ArrayList<>();

                for (int i = 0; i < results.size(); i++)
                    menu.otherDeaths.add(new Menu().new Text(results.get(i), MyGame.SCREEN_WIDTH / 2 - 75,
                            144 + (i * 48) + 48, Color.WHITE));
            }

            ArrayList<String> attacks = readAttacks();

            if (attacks == null)
                return;

            for (int i = 0; i < attacks.size(); i++) {
                String s = attacks.get(i);

                if (s.contains("sent")) {
                    String[] split = s.split(" ");

                    if (split[5].equals(MyGame.client.name)) {
                        for (int k = 0; k < Integer.parseInt(split[2]); k++) {
                            if (MyGame.client.queue.size() < 10)
                                MyGame.client.queue.add(new TetriminoNode(Color.GRAY, 0, -2, -1));
                        }

                        removeAttack(s);
                        continue;
                    }
                }
            }
        }
    }
}