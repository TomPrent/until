package com.zhejian.api;

import com.zhejian.annotation.ApiMethod;
import com.zhejian.annotation.ApiServer;

/**
*Title:
*Description:
*@author chengshoufu
*@date 2019年3月26日 上午10:03:29 
*/
@ApiServer
public class test {
	
	@ApiMethod(name = "test1")
	public void name(String name) {
		System.out.println("hello:"+name);
	}
}
