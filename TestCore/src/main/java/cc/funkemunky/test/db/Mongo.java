package cc.funkemunky.test.db;

import cc.funkemunky.api.utils.com.mongodb.ConnectionString;
import cc.funkemunky.api.utils.com.mongodb.MongoClientSettings;
import cc.funkemunky.api.utils.com.mongodb.client.MongoClient;
import cc.funkemunky.api.utils.com.mongodb.client.MongoClients;
import cc.funkemunky.api.utils.com.mongodb.client.MongoCollection;
import cc.funkemunky.api.utils.com.mongodb.client.MongoDatabase;
import cc.funkemunky.api.utils.org.bson.Document;
import lombok.Getter;

@Getter
public class Mongo {
    private final MongoDatabase database;
    private final MongoCollection<Document> settings;

    public Mongo() {
        ConnectionString cs = new ConnectionString(MongoConfig.mongoDBURL);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();

        try(MongoClient client = MongoClients.create(settings)) {
            database = client.getDatabase("testcoreInfo");

            this.settings = database.getCollection("settings");
        }
    }
}
