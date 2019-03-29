package com.zhejian.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class CheckBlankUtil {
	
	/**
     * 检查数据空 <p>
     * String null -> true; "" -> true; " " -> true; <p>
     * List null -> true; size()<1 -> true; <p>
     * Ojbect null -> true; <p>
     * other -> false; <p>
     * 
     * true 返回params中的errCode <p>
     * false 返回null
     * @param obj
     * @param params
     * @return
     */
	public static String check(Object obj, Map<String, String> params) {
		return checks(obj, params);
	}

    /**
     * 检查数据空 <p>
     * String null -> true; "" -> true; " " -> true; <p>
     * List null -> true; size()<1 -> true; <p>
     * Ojbect null -> true; <p>
     * other -> false; <p>
     * 
     * true 返回params中的errCode <p>
     * false 返回null
     * @param obj
     * @param params
     * @param tokenType token类型，传入且类型为AD时，自动不校验did和docId的空值
     * @return
     */
    public static String checks(Object obj, Map<String, String> params) {
        Class<? extends Object> clazz = obj.getClass();
        
        Iterator<String> it = params.keySet().iterator();
        while(it.hasNext()) {
            String mtdName = it.next();
            String errCode = params.get(mtdName);
            boolean isBlank = true;
            
            try {
                Method m = clazz.getMethod("get" + getMethodName(mtdName), null);
                if(m.getReturnType() == String.class) {
                    isBlank = StringUtils.isBlank((String) m.invoke(obj, null));
                } else if(List.class.isAssignableFrom(m.getReturnType())) {
                    List list = (List) m.invoke(obj, null);
                    if(list != null) {
                        isBlank = list.isEmpty();
                    } else {
                        isBlank = true;
                    }
                } else if(Object.class.isAssignableFrom(m.getReturnType())){
                    isBlank = m.invoke(obj, null) == null;
                } else {
                    isBlank = false;
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if(isBlank)
                return errCode;
        }
        return null;
    }
    
    /**
     * 获取getter setter的名称，即首字母大写形式 <p>
     * 
     * 注意：AAA -> AAA; aAa -> aAa; aaa -> Aaa; Aaa -> Aaa;
     * @param name
     * @return
     */
    public static String getMethodName(String name) {
        char c1 = name.charAt(0);
        char c2 = name.charAt(1);
        if(Character.isUpperCase(c2)) {
            return name;
        } else {
            return Character.toUpperCase(c1) + name.substring(1);
        }
    }
}
