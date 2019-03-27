package com.zhejian.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.zhejian.annotation.ApiMethod;
import com.zhejian.model.ExecutorBean;
import com.zhejian.until.AnnoManageUtil;

/**
*Title:
*Description:测试
*@author chengshoufu
*@date 2018年3月7日 上午9:42:50 7
*/
public class TestDemo {
	
	@Test
	public void game(){
		Map<String, ExecutorBean> mmap = new HashMap<String, ExecutorBean>();
        mmap = AnnoManageUtil.getRequestMappingMethod("com.zhejian.api");
        ExecutorBean bean = mmap.get("test1");

        try {
            bean.getMethod().invoke(bean.getObject(),"world");
            /*ApiMethod apiMethod = bean.getMethod().getAnnotation(ApiMethod.class);
            System.out.println("注解名称：" + apiMethod.name());*/
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
	}
}