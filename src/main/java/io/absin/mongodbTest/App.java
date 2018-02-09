package io.absin.mongodbTest;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

/**
 * Hello world!
 * 
 * Note that on the db console I ran the following commands:
 * 
 * use test
 * 
 * t = { "_id" : "5a3218bbdcb7c38fd3731232", "json" : {
 * "query_by_gtin_response:queryByGtinResponse" : { "xmlns" :
 * "urn:gs1:tsd:query_by_gtin_response:xsd:1", "productData" : {
 * "productDataRecord" : [ { "module" : [ {
 * "product_tracking_information_module" : { "createdDate" :
 * "2017-12-13T13:30:08.297Z", "updatedDate" : "2017-12-13T13:30:08.297Z" } } ]
 * } ] } } } }
 * 
 * db.testData.insert( t );
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");
		complex();
		// simple();
	}

	private static void simple() {
		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		DB database = mongoClient.getDB("test");
		DBObject query = new BasicDBObject(
				"json.query_by_gtin_response:queryByGtinResponse.productData.productDataRecord.module.product_tracking_information_module.updatedDate",
				"2017-12-13T13:30:08.297Z");
		DBCollection collection = database.getCollection("testData");
		DBCursor cursor = collection.find(query);
		DBObject jo = cursor.one();
		System.out.println((String) cursor.one().get("_id"));
	}

	private static void complex0() {
		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		DB database = mongoClient.getDB("test");
		DBCollection collection = database.getCollection("testData");
		DBObject obj = new BasicDBObject();
		obj.put("$where",
				"this.json.query_by_gtin_response:queryByGtinResponse.productData.productDataRecord.module.product_tracking_information_module.updatedDate == this.json.query_by_gtin_response:queryByGtinResponse.productData.productDataRecord.module.product_tracking_information_module.createdDate");
		System.out.println(collection.count(obj));
		mongoClient.close();
	}

	private static void complex() {
		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		MongoDatabase database = mongoClient.getDatabase("test");
		MongoCollection<Document> collection = database.getCollection("testData");
		Document document = collection
				.find(eq(
						"this.json.query_by_gtin_response:queryByGtinResponse.productData.productDataRecord.module.product_tracking_information_module.updatedDate",
						"this.json.query_by_gtin_response:queryByGtinResponse.productData.productDataRecord.module.product_tracking_information_module.createdDate"))
				.first();
		if (document == null) {
			// Document does not exist
			System.out.println("No");
		} else {
			// We found the document
			System.out.println("Yes");
		}
		mongoClient.close();
	}
}
