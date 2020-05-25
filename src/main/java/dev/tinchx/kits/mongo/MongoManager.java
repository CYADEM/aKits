package dev.tinchx.kits.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dev.tinchx.root.RootPlugin;
import dev.tinchx.root.configuration.RootConfig;
import lombok.Getter;

public class MongoManager {

    private MongoClient client;
    @Getter
    private MongoCollection profiles, kits;

    public void load() {
        RootConfig configuration = RootPlugin.getInstance().getSettings();
        if (configuration.getBoolean("DATABASE.MONGO.AUTHENTICATION.ENABLED")) {
            ServerAddress serverAddress = new ServerAddress(configuration.getString("DATABASE.MONGO.HOST"),
                    configuration.getInt("DATABASE.MONGO.PORT"));

            MongoCredential credential = MongoCredential.createCredential(
                    configuration.getString("DATABASE.MONGO.AUTHENTICATION.USER"), configuration.getString("DATABASE.MONGO.AUTHENTICATION.DATABASE"),
                    configuration.getString("DATABASE.MONGO.AUTHENTICATION.PASSWORD").toCharArray());

            client = new MongoClient(serverAddress, credential, MongoClientOptions.builder().build());
        } else {
            client = new MongoClient(configuration.getString("DATABASE.MONGO.HOST"), configuration.getInt("DATABASE.MONGO.PORT"));
        }
        String databaseName = "Kits-" + RootPlugin.getInstance().getNmsManager().getServerNMS().getServerName().toLowerCase();
        MongoDatabase database = client.getDatabase(databaseName);
        this.profiles = database.getCollection("Profiles");
        this.kits = database.getCollection("Kits");
    }

    public void close() {
        client.close();
    }
}