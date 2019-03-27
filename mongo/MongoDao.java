package com.zhejian.dao;

import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;
import com.zhejian.model.mongo.LogOne;
import com.zhejian.model.mongo.LogThree;

/**
*Title:
*Description:
*@author chengshoufu
*@date 2018年6月28日 上午10:50:40 
*/
@Repository
public interface MongoDao {
	/**
     * Get Data BY ID
     * 
     * @param db
     * @param table
     * @param Id
     * @throws Exception 
     */
    public Map<String,Object> queryByID(MongoDatabase db, String table, Object Id) throws Exception;

    /**
     * Insert Data
     * 
     * @param db
     * @param table
     * @param document
     */
    public boolean insert(MongoDatabase db, String table, Document doc);

    /**
     * Delete Many Data.if doc is empty will delete all Data
     * 
     * @param db
     * @param table
     * @param document
     */
    public boolean delete(MongoDatabase db, String table, BasicDBObject doc);

    /**
     * Update All Data
     * 
     * @param db
     * @param table
     * @param oldDoc
     * @param newDoc
     */
    public boolean update(MongoDatabase db, String table, BasicDBObject oldDoc,
            BasicDBObject newDoc);
    public List<LogOne> mongoGroup1(MongoDatabase db, String table,Document sub_match);
    public List<LogThree> mongoGroup3(MongoDatabase db, String table,Document sub_match);
}
