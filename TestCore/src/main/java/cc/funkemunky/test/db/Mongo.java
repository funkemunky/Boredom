package cc.funkemunky.test.db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import lombok.Getter;

@Getter
public class Mongo {
    private final MongoDatabase database;
    private final MongoCollection<Document> settings;

    public Mongo() {
        MongoClient client;
        ConnectionString cs = new ConnectionString(MongoConfig.mongoDBURL);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        client = MongoClients.create(settings);

        database = client.getDatabase("testcoreInfo");
        
        this.settings = database.getCollection("settings");
    }
}
