package com.zhejian.utils;

import java.util.HashMap;
import java.util.Map;

import com.mongodb.util.JSON;

/**
*Title:
*Description:
*@author chengshoufu
*@date 2018年6月28日 上午10:56:23 
*/
public class JsonStrToMap {
	/**
     * json 字符串转化为map格式
     * @param jsonString
     * @return
     */

    public static Map<String, Object> jsonStrToMap(String jsonString) {
        Object parseObj = JSON.parse(jsonString); // 反序列化 把json 转化为对象
        Map<String, Object> map = (HashMap<String, Object>) parseObj; // 把对象转化为map
        return map;
    }
}
