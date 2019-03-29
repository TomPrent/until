package com.zhejian.utils;

import com.google.gson.Gson;
import com.zhejian.exception.CustomizedException;

/** 
 * Jaxb2工具类 
 */ 
public class JsonUtil {  

    /** 
     * JavaBean转换成json 
     * @param obj 
     * @return  
     */  
    public static String convertToJson(Object obj) { 
    	Gson gson = new Gson(); 
        String s = gson.toJson(obj); 
        return s;  
    }  
    
    /** 
     * json转换成JavaBean 
     * @param json 
     * @param c 
     * @return 
     * @throws CustomizedException 
     */  
    public static <T> T convertToJavaBean(String json, Class<T> c) throws CustomizedException {  
        T t = null; 
    	Gson gson = new Gson(); 

        try {  
        	t = gson.fromJson(json, c);    
        } catch (Exception e) {  
            e.printStackTrace(); 
            throw new CustomizedException("The input is not a correct json.");
        }  
  
        return t;  
    }  


}
