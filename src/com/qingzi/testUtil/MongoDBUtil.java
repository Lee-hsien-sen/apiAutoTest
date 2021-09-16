package com.qingzi.testUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.qingzi.process.MongoConf;
import com.qingzi.system.system;

//mongodb 连接数据库工具类
public class MongoDBUtil {
	
	public static Map<String,Object> map=new XMLread().getSystem();// 读取XML配置文件
  
	private static MongoClient getClient(HashMap<String, Object> data) {
		
       /* MongoConf conf = new MongoConf(((system)map.get("qingziUsrmgr")).getMongoUrl(),
        		27017,
        		((system)map.get("qingziUsrmgr")).getAuthDb(), 
        		((system)map.get("qingziUsrmgr")).getMongoName(), 
        		((system)map.get("qingziUsrmgr")).getMongoPwd());*/
        MongoConf conf = new MongoConf(((system)map.get(data.get("system"))).getMongoUrl(),
//				port:30017, //北京
        		27018,//香港
        		((system)map.get(data.get("system"))).getAuthDb(),
        		((system)map.get(data.get("system"))).getMongoName(), 
        		((system)map.get(data.get("system"))).getMongoPwd());
        MongoClient client;
        if (Objects.isNull(conf.getUsername())||"".equals(conf.getUsername())) {
            client = new MongoClient(new MongoClientURI(String.format("mongodb://%s:%s/?authSource=%s&ssl=false",
                    conf.getHost(), conf.getPort(), conf.getAuthDB())));
        } else {
            client = new MongoClient(new MongoClientURI(String.format("mongodb://%s:%s@%s:%s/?authSource=%s&ssl=false",
                    conf.getUsername(), conf.getPassword(), conf.getHost(), conf.getPort(), conf.getAuthDB())));
        }
        return client;
    }
  
	private static Logger logger = Logger.getLogger(MongoDBUtil.class);
	
	public static void main(String[] args) {
//		String database = "crystal";
//      MongoConf to = new MongoConf("192.168.200.74", 29017, "admin", "matrx_test", "matrx_test");
//      MongoClient toClient = getClient(to);

//      MongoDatabase toDatabase = fromClient.getDatabase(database);
		/*MongoClient fromClient = getClient();
		MongoDatabase fromDatabase = fromClient.getDatabase("crystal");

        MongoCollection<Document> collection = fromDatabase.getCollection("MeetingRoom");
        BasicDBObject object = new BasicDBObject();
        object.put("_id", new BasicDBObject("$in", new ObjectId[]{new ObjectId("608553b69829430001f4ca79")}));
        collection.deleteMany(object);*/
		
		
//		String ids = "608553b69829430001f4ca79";
//		deleteMany(ids);
//		findByid("crystal","mtmgrMetting","mId","225868201");
//		System.out.println("----" + findByid("crystal","mtmgrMetting","mId","225868201"));
//		updateByid();
//		deleteByid("crystal","usrmgrAccount","BUid","feifei");
		
	}
	
	//按条件删除
	public static DeleteResult deleteByid(HashMap<String, Object> data,String dataBase,String table, String key, String value){
		MongoClient fromClient = getClient(data);
		MongoDatabase fromDatabase = fromClient.getDatabase(dataBase);
		MongoCollection<Document> collection = fromDatabase.getCollection(table);
		//删选条件
		Bson filter = Filters.eq(key, value);
		DeleteResult deleteOne = collection.deleteOne(filter);
		return deleteOne;	
	}
	//按条件删除
		public static DeleteResult deleteByidAll(HashMap<String, Object> data,String dataBase,String table, String key, String value){
			MongoClient fromClient = getClient(data);
			MongoDatabase fromDatabase = fromClient.getDatabase(dataBase);
			MongoCollection<Document> collection = fromDatabase.getCollection(table);
			//删选条件
			Bson filter = Filters.eq(key, value);
			DeleteResult deleteOne = collection.deleteMany(filter);
			return deleteOne;	
		}
	//按条件修改
	public static UpdateResult updateByid(HashMap<String, Object> data) {
		MongoClient fromClient = getClient(data);
		MongoDatabase fromDatabase = fromClient.getDatabase("crystal");
		MongoCollection<Document> collection = fromDatabase.getCollection("mtmgrMetting");
		//删选条件
		Bson filter = Filters.eq("mId", "225868201");
		//需要修改的内容
		Document document = new Document("$set",new Document("title","测试会议001"));
		UpdateResult result = collection.updateOne(filter, document);
		System.out.println(result);
		
		return result;
	}
	
	
	//单个条件查询
	public static Document findByid(HashMap<String, Object> data, String dataBase,String table, String key, String value) {
		Document docu = null; 
//		MongoCursor<Document> doc = coll.find(filter).iterator();
		
		MongoClient fromClient = getClient(data);
		MongoDatabase fromDatabase = fromClient.getDatabase(dataBase);
		MongoCollection<Document> collection = fromDatabase.getCollection(table);
//		FindIterable<Document> iterable =  collection.find(Filters.eq("_id",new ObjectId("1-M-42683355806564352")));
		Bson Filter = Filters.eq(key, value);
		FindIterable<Document> iterable =  collection.find(Filter);
		MongoCursor<Document> cursor = iterable.iterator();
		while(cursor.hasNext()){
			docu = cursor.next();
//			System.out.println(docu.get("title"));
//			System.out.println("结果：" + docu);
		}
		return docu;
	}

	//删除
	private static void deleteMany(HashMap<String, Object> data, String ids){
		//ids = "608553b69829430001f4ca79";
		MongoClient fromClient = getClient(data);
		MongoDatabase fromDatabase = fromClient.getDatabase("zayhu");
		MongoCollection<Document> collection = fromDatabase.getCollection("MeetingRoom");
        BasicDBObject object = new BasicDBObject();
        object.put("_id", new BasicDBObject("$in", new ObjectId[]{new ObjectId(ids)}));
        collection.deleteMany(object);
	}
	
	
}