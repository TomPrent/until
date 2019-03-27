package com.zhejian.aspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zhejian.utils.MemCachedManager;

/**
*Title:
*Description:
*@author chengshoufu
*@date 2018年8月28日 下午2:20:25 
*/
@Component
public class AspectMethod {
	@Autowired
	private MemCachedManager cachedManager;
	
	@MemCachedDao
	public Object getData(String key,String method,String name){
		return cachedManager.getValue(key);
	}
}
