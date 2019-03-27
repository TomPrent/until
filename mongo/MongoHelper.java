package com.zhejian.utils;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
*Title:
*Description:
*@author chengshoufu
*@date 2018年6月28日 上午10:48:33 
*/
public class MongoHelper {
	static final String DBName = "zhejian";
    static final String ServerAddress = "127.0.0.1"; 
    static final int PORT = 27017;

    public MongoHelper(){
    }

    public MongoClient getMongoClient( ){
        MongoClient mongoClient = null;
        try {
              // 连接到 mongodb 服务
            mongoClient = new MongoClient(ServerAddress, PORT); 
            System.out.println("Connect to mongodb successfully");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return mongoClient;
    }

    public MongoDatabase getMongoDataBase(MongoClient mongoClient) {  
        MongoDatabase mongoDataBase = null;
        try {  
            if (mongoClient != null) {  
                  // 连接到数据库
                mongoDataBase = mongoClient.getDatabase(DBName);  
                System.out.println("Connect to DataBase successfully");
            } else {  
                throw new RuntimeException("MongoClient不能够为空");  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return mongoDataBase;
    }  

    public void closeMongoClient(MongoDatabase mongoDataBase,MongoClient mongoClient ) {  
        if (mongoDataBase != null) {  
            mongoDataBase = null;  
        }  
        if (mongoClient != null) {  
            mongoClient.close();  
        }  
        System.out.println("CloseMongoClient successfully");  

    } 
}
