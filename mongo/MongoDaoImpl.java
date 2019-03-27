package com.zhejian.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.zhejian.model.PageForm;
import com.zhejian.model.mongo.LogOne;
import com.zhejian.model.mongo.LogThree;
import com.zhejian.utils.JsonStrToMap;

/**
*Title:
*Description:
*@author chengshoufu
*@date 2018年6月28日 上午10:53:39 
*/
@Repository
public class MongoDaoImpl implements MongoDao{
	/**
     * 根据id，来检索
     * @param db
     * @param table
     * @param doc
     */
	@Override
    public Map<String,Object> queryByID(MongoDatabase db, String table, Object Id) throws Exception{
        MongoCollection<Document> collection = db.getCollection(table);
        BasicDBObject query = new BasicDBObject("_id", Id);
        //  DBObject接口和BasicDBObject对象：表示一个具体的记录，BasicDBObject实现了DBObject，是key-value的数据结构，用起来和HashMap是基本一致的。
        FindIterable<Document> iterable = collection.find(query);

        Map<String,Object> jsonStrToMap = null;
        MongoCursor<Document> cursor = iterable.iterator();
        while (cursor.hasNext()) {
            Document user = cursor.next();
            String jsonString = user.toJson();
            jsonStrToMap = JsonStrToMap.jsonStrToMap(jsonString);//这里用到我自己写的方法,主要是包json字符串转换成map格式,为后面做准备,方法放在后面
        }
        System.out.println("检索ID完毕");

        return jsonStrToMap;
    }

    /**
     * 根据一个doc，来检索，当doc是空的时候检索全部
     * @param db
     * @param table
     * @param doc
     */
    public FindIterable<Document>  queryByDoc(MongoDatabase db, String table, BasicDBObject doc,PageForm page) {
        MongoCollection<Document> collection = db.getCollection(table);
        int number=(page.getPage()-1)*page.getLimit();
    	FindIterable<Document> iterable = collection.find(doc).limit(page.getLimit()).skip(number);
    	return iterable;
    }
    
    @Override
    public List<LogOne>  mongoGroup1(MongoDatabase db, String table,Document sub_match) {
        MongoCollection<Document> dbCollection = db.getCollection(table);
        
        Document sub_group = new Document();		
        sub_group.put("_id", "$hosId");		
        sub_group.put("count", new Document("$sum", 1));
        sub_group.put("putNumber", new Document("$sum", "$putNumber"));
        sub_group.put("hosName", new Document("$last", "$hosName"));	
        
        Document group = new Document("$group", sub_group);		
        Document match = new Document("$match", sub_match);
        
        List<Document> aggregateList = new ArrayList<Document>();			
        aggregateList.add(group);	
        aggregateList.add(match);
        
        AggregateIterable<Document> results = dbCollection.aggregate(aggregateList);
        List<LogOne> list =new ArrayList<LogOne>();
        MongoCursor<Document> cursor = results.iterator();
    	try {			
    		while(cursor.hasNext()) {	
    			LogOne one=new LogOne();
    			Document item_doc = cursor.next();	
    			String leaveMethod = item_doc.getString("_id");				
    			int count = item_doc.getInteger("count");
    			int putNumber = item_doc.getInteger("putNumber");
    			String hosName = item_doc.getString("hosName");
    			one.setHosId(leaveMethod);
    			one.setCount(count+"");
    			one.setPutNumber(putNumber+"");
    			one.setHosName(hosName);
    			list.add(one);
    			}		
		} finally {			
			cursor.close();		
		}	
    	return list;
    }
    
    @Override
    public List<LogThree>  mongoGroup3(MongoDatabase db, String table,Document sub_match) {
        MongoCollection<Document> dbCollection = db.getCollection(table);
        Document id = new Document();
        id.put("hosId", "$hosId");
        id.put("date", "$date");
        
        Document sub_group = new Document();		
        sub_group.put("_id", id);
        sub_group.put("count", new Document("$sum", 1));
        sub_group.put("requestTime", new Document("$avg", "$requestTime1"));
        sub_group.put("responseTime", new Document("$avg", "$responseTime1"));
        sub_group.put("putNumber", new Document("$sum", "$putNumber"));
        sub_group.put("successNumber", new Document("$sum","$successNumber"));
        
        Document group = new Document("$group", sub_group);		
        Document match = new Document("$match", sub_match);
        
        List<Document> aggregateList = new ArrayList<Document>();			
        aggregateList.add(group);	
        aggregateList.add(match);
        
        AggregateIterable<Document> results = dbCollection.aggregate(aggregateList);
        List<LogThree> list =new ArrayList<LogThree>();
        MongoCursor<Document> cursor = results.iterator();
    	try {			
    		while(cursor.hasNext()) {				
    			Document item_doc = cursor.next();	
    			Document leaveMethod = item_doc.get("_id",Document.class);	
    			System.out.println(leaveMethod.getString("hosId"));
    			System.out.println(leaveMethod.getString("date"));
    			int count = item_doc.getInteger("count");
    			int putNumber = item_doc.getInteger("putNumber");
    			int successNumber = item_doc.getInteger("successNumber");
    			
    			double requestTime = item_doc.getDouble("requestTime");
    			double responseTime = item_doc.getDouble("responseTime");
    			
    			LogThree three=new LogThree();
    			three.setHosId(leaveMethod.getString("hosId"));
    			three.setDate(leaveMethod.getString("date"));
    			three.setEffectiveRate((float)successNumber/putNumber+"");
    			three.setAverageTime((responseTime-requestTime)+"");
    			three.setSuccessRate(count+"");
    			list.add(three);
    			}		
		} finally {			
			cursor.close();		
		}	
    	return list;
    }
    
    /**
     * 根据一个doc，来检索多少条，当doc是空的时候检索全部
     * @param db
     * @param table
     * @param doc
     */
    public long countByDoc(MongoDatabase db, String table, BasicDBObject doc) {
        MongoCollection<Document> collection = db.getCollection(table);
        long count = collection.count(doc);
        return count;
    }
    /**
     *  检索全部并返回迭代器
     * @param db
     * @param table
     */
    public FindIterable<Document> queryAll(MongoDatabase db, String table) {
        MongoCollection<Document> collection = db.getCollection(table);
        FindIterable<Document> iterable = collection.find();
        /*List<Map<String,Integer>> list = new ArrayList<Map<String,Integer>>();
        MongoCursor<Document> cursor = iterable.iterator();
        while (cursor.hasNext()) {
            Document user = cursor.next();
            String jsonString = user.toJson();
            Map<String, Integer> jsonStrToMap = JsonStrToMap.jsonStrToMap(jsonString);
            list.add(jsonStrToMap);
        }
        System.out.println("检索全部完毕");*/
        return iterable;
    }

    /**
     * 便利迭代器FindIterable<Document> 
     */
    public List<Object> printFindIterable(FindIterable<Document> iterable){
        MongoCursor<Document> cursor = iterable.iterator();
        List<Object> list=new ArrayList<Object>();
        while (cursor.hasNext()) {
            Document user = cursor.next();
            Map<String, Object> map=JsonStrToMap.jsonStrToMap(user.toJson());
            list.add(map);
        }
        cursor.close();
        return list;
    }

    @Override
    public boolean insert(MongoDatabase db, String table, Document document) {
        MongoCollection<Document> collection = db.getCollection(table);
        collection.insertOne(document);
        long count = collection.count(document);
        
        if(count == 1){
            System.out.println("文档插入成功");
            return true;
        }else{
            System.out.println("文档插入成功");
            return false;
        }

    }

    /**
     * insert many
     * @param db
     * @param table
     * @param document
     */
    public boolean insertMany(MongoDatabase db, String table, List<Document> documents ) {

        MongoCollection<Document> collection = db.getCollection(table);
        long preCount = collection.count();
        collection.insertMany(documents);
        long nowCount = collection.count();
        System.out.println("插入的数量: "+(nowCount-preCount));
        if((nowCount-preCount) == documents.size() ){
            System.out.println("文档插入多个成功");
            return true;
        }else{
            System.out.println("文档插入多个失败");
            return false;
        }

    }

    @Override
    public boolean delete(MongoDatabase db, String table, BasicDBObject document) {
        MongoCollection<Document> collection = db.getCollection(table);
        DeleteResult deleteManyResult = collection.deleteMany(document);
        long deletedCount = deleteManyResult.getDeletedCount();
        System.out.println("删除的数量: "+deletedCount);
        if(deletedCount > 0){
            System.out.println("文档删除多个成功");
            return true;
        }else{
            System.out.println("文档删除多个失败");
            return false;
        }
    }

    /**
     * 删除一个
     * @param db
     * @param table
     * @param document
     */
    public boolean deleteOne(MongoDatabase db, String table, BasicDBObject document) {
        MongoCollection<Document> collection = db.getCollection(table);
        DeleteResult deleteOneResult = collection.deleteOne(document);
        long deletedCount = deleteOneResult.getDeletedCount();
        System.out.println("删除的数量: "+deletedCount);
        if(deletedCount == 1){
            System.out.println("文档删除一个成功");
            return true;
        }else{
            System.out.println("文档删除一个失败");
            return false;
        }
    }


    @Override
    public boolean update(MongoDatabase db, String table, BasicDBObject whereDoc,BasicDBObject updateDoc) {
            MongoCollection<Document> collection = db.getCollection(table);  
             UpdateResult updateManyResult = collection.updateMany(whereDoc, new Document("$set",updateDoc)); 
             long modifiedCount = updateManyResult.getModifiedCount();
             System.out.println("修改的数量: "+modifiedCount);

            if (modifiedCount > 0){
                System.out.println("文档更新多个成功");
                return true;
            }else{
                System.out.println("文档更新失败");
                return false;
            }
    }


    /**
     * update one Data
     * @param db
     * @param table
     * @param whereDoc
     * @param updateDoc
     */
    public boolean updateOne(MongoDatabase db, String table, BasicDBObject whereDoc,BasicDBObject updateDoc) {
            MongoCollection<Document> collection = db.getCollection(table);  
             UpdateResult updateOneResult = collection.updateOne(whereDoc, new Document("$set",updateDoc)); 
             long modifiedCount = updateOneResult.getModifiedCount();
             System.out.println("修改的数量: "+modifiedCount);
             if(modifiedCount == 1){
                System.out.println("文档更新一个成功");
                 return true;
             }else{
                System.out.println("文档更新失败");
                 return false;
             }
    }
    /**
     * create collection
     * @param db
     * @param table
     */
    public void createCol(MongoDatabase db, String table) {
         db.createCollection(table);
        System.out.println("集合创建成功");
    }

    /**
     * drop a collection
     * @param db
     * @param table
     */
    public void dropCol(MongoDatabase db, String table) {
        db.getCollection(table).drop();
        System.out.println("集合删除成功");

    }
}
