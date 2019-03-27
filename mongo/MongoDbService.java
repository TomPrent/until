package com.zhejian.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.zhejian.dao.MongoDaoImpl;
import com.zhejian.model.PageForm;
import com.zhejian.model.mongo.LogThree;
import com.zhejian.utils.JsonStrToMap;
import com.zhejian.utils.JsonUtil;
import com.zhejian.utils.MongoHelper;

/**
*Title:
*Description:
*@author chengshoufu
*@date 2018年6月28日 上午11:29:25 
*/
@Service
public class MongoDbService {
	
	/*@Autowired
	private MongoDaoImpl mongoDaoImpl;*/
	MongoDaoImpl mongoDaoImpl=new MongoDaoImpl();
	
	//查询条数
	public long count(Map<String, Object> areaMap,Map<String, String> timeMap,String areaTable){
		MongoHelper mongoHelper = new MongoHelper();
        MongoClient mongoClient = mongoHelper.getMongoClient();
        MongoDatabase mongoDataBase = mongoHelper.getMongoDataBase(mongoClient);
        
        BasicDBObject query =new BasicDBObject(areaMap);
        
        String key=timeMap.get("key");
        String startTime=timeMap.get("startTime");
        String endTime=timeMap.get("endTime");
        
        if(startTime!= null || endTime!= null){
        	BasicDBObject list = new BasicDBObject();
        	if(startTime!=null){
        		list.put("$gt", startTime);
            }
        	if(endTime!=null){
        		list.put("$lte", endTime);
            }
        	query.put(key, list);
        }
        long count = mongoDaoImpl.countByDoc(mongoDataBase, areaTable, query);
        return count;
	}
	
	public List<Object> mongoGroup(String method,Map<String, Object> areaMap,Map<String, String> timeMap,String areaTable){
		MongoHelper mongoHelper = new MongoHelper();
        MongoClient mongoClient = mongoHelper.getMongoClient();
        MongoDatabase mongoDataBase = mongoHelper.getMongoDataBase(mongoClient);
        
        Object date=areaMap.get("date");
        Object hosId=areaMap.get("hosId");
        Document sub_match = new Document();	
        if(date != null){
        	sub_match.put("date", date.toString());	
        }else{
        	Object startTime=areaMap.get("startTime");
        	Object endTime=areaMap.get("endTime");
        	if(startTime != null && endTime  != null){
        		sub_match.put("leaveTime", new Document("$gt", startTime).append("$lt", endTime));
        	}else if(startTime != null && endTime  == null){
        		sub_match.put("leaveTime", new Document("$gt", startTime));
        	}else if(startTime == null && endTime  != null){
        		sub_match.put("leaveTime", new Document("$lt", endTime));
        	}
        }
        if(hosId != null){
        	sub_match.put("hosId", hosId.toString());	
        }
        List list = new ArrayList();
        if("1".equals(method)){
        	list= mongoDaoImpl.mongoGroup1(mongoDataBase, areaTable,sub_match);
        }else if("3".equals(method)){
        	List<LogThree> l= mongoDaoImpl.mongoGroup3(mongoDataBase, areaTable,sub_match);
        	for(LogThree e:l){
				areaMap.put("responseData", null);
    			int count2=(int) count(areaMap,timeMap,"logData");
    			long count1=Long.parseLong(e.getSuccessRate());
        		e.setSuccessRate(count2/count1+"");
    			list.add(e);
        	}
        }
        return list;
	}
	
	//查询
	public List<Object> find(Map<String, Object> areaMap,Map<String, String> timeMap,PageForm page,String areaTable){
		MongoHelper mongoHelper = new MongoHelper();
        MongoClient mongoClient = mongoHelper.getMongoClient();
        MongoDatabase mongoDataBase = mongoHelper.getMongoDataBase(mongoClient);
        
        BasicDBObject query =new BasicDBObject(areaMap);
    	
        String key=timeMap.get("key");
        String startTime=timeMap.get("startTime");
        String endTime=timeMap.get("endTime");
        
        if(startTime!= null || endTime!= null){
        	BasicDBObject list = new BasicDBObject();
        	if(startTime!=null){
        		list.put("$gt", startTime);
            }
        	if(endTime!=null){
        		list.put("$lte", endTime);
            }
        	query.put(key, list);
        }
        FindIterable<Document> queryByDocResult = mongoDaoImpl.queryByDoc(mongoDataBase, areaTable, query,page);
        List<Object> list=mongoDaoImpl.printFindIterable(queryByDocResult);
        return list;
	}
	
	//添加
	public void insert(Object error,String areaTable){
		MongoHelper mongoHelper = new MongoHelper();
        MongoClient mongoClient = mongoHelper.getMongoClient();
        MongoDatabase mongoDataBase = mongoHelper.getMongoDataBase(mongoClient);
        
        String json=JsonUtil.convertToJson(error);
        Map<String, Object> areaMap=JsonStrToMap.jsonStrToMap(json);
        
        mongoDaoImpl.insert(mongoDataBase, areaTable, new Document(areaMap));
	}
	
	//其他
	public void Reserve(String areaTable){
		MongoHelper mongoHelper = new MongoHelper();
        MongoClient mongoClient = mongoHelper.getMongoClient();
        MongoDatabase mongoDataBase = mongoHelper.getMongoDataBase(mongoClient);
        
        //   插入map 到mongodb
        /*mongoDaoImpl.insert(mongoDataBase, areaTable, new Document(areaMap));

        Map<String, Object> areaMap2 = new HashMap<String,Object>();
        Map<String, Object> areaMap3 = new HashMap<String,Object>();
        areaMap2.put("_id", 10);
        areaMap2.put("北京", 5);

        areaMap3.put("_id", 11);
        areaMap3.put("北京", 5);
        List<Document> docList = new ArrayList<Document>();
        docList.add(new Document(areaMap2));
        docList.add(new Document(areaMap3));
        mongoDaoImpl.insertMany(mongoDataBase, areaTable, docList);*/
        
   //   根据map 删除mongodb
//      mongoDaoImpl.delete(mongoDataBase, areaTable, new BasicDBObject(areaMap1));
//      mongoDaoImpl.deleteOne(mongoDataBase, areaTable, new BasicDBObject(areaMap1));
        
        //根据map 更新mongodb
        /*Map<String, Object> updateDoc = new HashMap<String,Object>();
        Map<String, Object> wehereDoc = new HashMap<String,Object>();
        wehereDoc.put("_id", 4);
        updateDoc.put("上海",25);
        mongDaoImpl.update(mongoDataBase, areaTable, new BasicDBObject(wehereDoc), new BasicDBObject(updateDoc));
        mongoDaoImpl.updateOne(mongoDataBase, areaTable, new BasicDBObject(wehereDoc), new BasicDBObject(updateDoc));*/
        
        //检索全部
        FindIterable<Document> queryAllResult = mongoDaoImpl.queryAll(mongoDataBase, areaTable);
        System.out.println(">>>>>>>>>>>>>>>"+queryAllResult);
        mongoDaoImpl.printFindIterable(queryAllResult);
        mongoHelper.closeMongoClient(mongoDataBase,mongoClient);
	}
}
