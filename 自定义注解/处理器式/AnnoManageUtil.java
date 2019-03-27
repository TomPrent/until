package com.zhejian.until;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.zhejian.annotation.ApiMethod;
import com.zhejian.annotation.ApiServer;
import com.zhejian.model.ExecutorBean;

/**
*Title:
*Description:
*@author chengshoufu
*@date 2019年3月26日 上午9:20:54 
*/
public final class AnnoManageUtil {

	 /**
     * 获取指定文件下面的注解方法方法保存在mapp中
     *
     * @param packageName
     * @return
     */
    public static Map<String, ExecutorBean> getRequestMappingMethod(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(ApiServer.class);

        // 存放url和ExecutorBean的对应关系
        Map<String, ExecutorBean> mapp = new HashMap<String, ExecutorBean>();
        for (Class classes : classesList) {
            //得到该类下面的所有方法
            Method[] methods = classes.getDeclaredMethods();

            for (Method method : methods) {
                //得到该类下面的RequestMapping注解
            	ApiMethod apiMethod = method.getAnnotation(ApiMethod.class);
                if (null != apiMethod) {
                    ExecutorBean executorBean = new ExecutorBean();
                    try {
                        executorBean.setObject(classes.newInstance());
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    executorBean.setMethod(method);
                    mapp.put(apiMethod.name(), executorBean);

                }
            }
        }
        return mapp;
    }
	
}
