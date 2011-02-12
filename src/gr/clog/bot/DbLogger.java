package gr.clog.bot;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class DbLogger 
{
	DBCollection dbCollection;
	
	public DbLogger(String dbHost, int dbPort, String dbName, String collectionName)
	{
		Mongo mongo;
		try 
		{
			mongo = new Mongo(dbHost, dbPort);
			DB db = mongo.getDB(dbName);
			dbCollection = db.getCollection(collectionName);
		} 
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		} catch (MongoException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void logMsg(String nick, String channel, String msg, long time)
	{
		BasicDBObject bdo = new BasicDBObject();
		bdo.put("n", nick);
		bdo.put("c", channel);
		bdo.put("m", msg);
		bdo.put("t", time);
		dbCollection.insert(bdo);
	}
}
