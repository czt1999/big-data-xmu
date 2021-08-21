import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;

import static com.mongodb.client.model.Filters.eq;

public class TaskMongoDB {

    public static void main(String[] args) {
        String user = "test";
        String dbname = "test";
        String password = "";
        MongoCredential credential = MongoCredential.createCredential(user, dbname, password.toCharArray());
        ServerAddress serverAddress = new ServerAddress("192.168.3.9", 27017);
        MongoClient client = new MongoClient(serverAddress, Arrays.asList(credential));
        MongoDatabase db = client.getDatabase(dbname);
        MongoCollection<Document> studentCollection = db.getCollection("student");
        // 1:
        String json = "{ name: 'scofield', score: { English: 45, Math: 89, Computer: 100 } }";
        studentCollection.insertOne(Document.parse(json));
        // 2:
        MongoCursor<Document> cursor = studentCollection.find(eq("name", "scofield")).iterator();
        System.out.println("========== scofield ==========");
        while (cursor.hasNext()) {
            Document score = cursor.next().get("score", Document.class);
            System.out.println("score.English >> " + score.get("English"));
            System.out.println("score.Math >> " + score.get("Math"));
            System.out.println("score.Computer >> " + score.get("Computer"));
            System.out.println("==============================");
        }
    }
}
