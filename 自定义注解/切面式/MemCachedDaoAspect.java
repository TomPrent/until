package com.zhejian.aspect;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zhejian.utils.MemCachedManager;
import com.zhejian.constants.Constants;
import com.zhejian.dao.ConsultDao;
import com.zhejian.dao.HospitalDao;
import com.zhejian.entity.AuditStateCode;
import com.zhejian.entity.BodyCheckType;
import com.zhejian.entity.CHESTCode;
import com.zhejian.entity.ConclusionsCode;
import com.zhejian.entity.ECGCode;
import com.zhejian.entity.EmployingType;
import com.zhejian.entity.EnterpriseSize;
import com.zhejian.entity.HazardCode;
import com.zhejian.entity.Hospital;
import com.zhejian.entity.IndustryCategory;

/**
*Title:
*Description:
*@author chengshoufu
*@date 2018年8月27日 上午11:12:35 
*/
@Aspect //该注解标示该类为切面类 
@Component
public class MemCachedDaoAspect {
	private static final Logger log = Logger.getLogger(MemCachedDaoAspect.class);
	@Autowired
	private MemCachedManager cachedManager;
	@Autowired
	private ConsultDao ConsultDao;
	@Autowired
	private HospitalDao hospitalDao;
	
	//切入点
    @Pointcut("@annotation(com.zhejian.aspect.MemCachedDao)")
    private void pointcut() {
    }

    /**
     * 在方法执行前后
     *
     * @param point
     * @param emCachedDao
     * @return
     */
    @Around("pointcut() && @annotation(memCachedDao)")//
    public Object around(ProceedingJoinPoint point, MemCachedDao memCachedDao) {
        //String requestUrl = memCachedDao.requestName();
        try {
        	//访问目标方法的参数：
            Object[] args = point.getArgs();
            if (args != null && args.length > 0 && args[0].getClass() == String.class) {
                String key=(String) args[0];
                Object data=cachedManager.getValue(key);
                if(data == null){
                	String method=(String) args[1];
                	String name=(String) args[2];
                	Date now = new Date( );
                    SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd hh:mm:ss");
                	log.error(name+"没有缓存"+ft.format(now));
                	//获取方法  
    				Method m = ConsultDao.getClass().getDeclaredMethod(method);
    				if("ECGCode".equals(name)){
    					//调用方法  
        				List<ECGCode> list= (List<ECGCode>) m.invoke(ConsultDao);
        				for (ECGCode ecgCode : list) {
        					cachedManager.setValue("ECGCode" + ecgCode.getECGCode(), ecgCode.getECGName(), Constants.MEMCACHE_TIME);
        				}
    				}else if("CHESTCode".equals(name)){
    					List<CHESTCode> list= (List<CHESTCode>) m.invoke(ConsultDao);
    					for (CHESTCode chestCode : list) {
    						cachedManager.setValue("CHESTCode" + chestCode.getCHESTCode(), chestCode.getCHESTName(),
    								Constants.MEMCACHE_TIME);
    					}
    				}else if("ConclusionsCode".equals(name)){
    					List<ConclusionsCode> list= (List<ConclusionsCode>) m.invoke(ConsultDao);
    					for (ConclusionsCode conclusionsCode : list) {
    						cachedManager.setValue("ConclusionsCode" + conclusionsCode.getConclusionsCode(),
    								conclusionsCode.getConclusionsName(), Constants.MEMCACHE_TIME);
    					}
    				}else if("HazardCode".equals(name)){
    					List<HazardCode> list= (List<HazardCode>) m.invoke(ConsultDao);
    					for (HazardCode hazardCode : list) {
    						cachedManager.setValue("HazardCode" + hazardCode.getHazardCode(), hazardCode.getHazardName(),
    								Constants.MEMCACHE_TIME);
    					}
    				}else if("bodyCheckType".equals(name)){
    					List<BodyCheckType> list= (List<BodyCheckType>) m.invoke(ConsultDao);
    					for (BodyCheckType bodyCheckType : list) {
    						cachedManager.setValue("bodyCheckType" + bodyCheckType.getBodyCheckType(), bodyCheckType.getBodyCheckName(),
    								Constants.MEMCACHE_TIME);
    					}
    				}else if("employingCode".equals(name)){
    					List<EmployingType> list= (List<EmployingType>) m.invoke(ConsultDao);
    					for(EmployingType employingUnit2:list){
    						cachedManager.setValue("employingCode"+employingUnit2.getEconomicCode(), employingUnit2.getEconomicType(), Constants.MEMCACHE_TIME);
    					}
    				}else if("IndustryCateCode".equals(name)){
    					List<IndustryCategory> list= (List<IndustryCategory>) m.invoke(ConsultDao);
    					for(IndustryCategory industryCategory:list){
    						cachedManager.setValue("IndustryCateCode"+industryCategory.getIndustryCateCode(), industryCategory.getIndustryCateName(), Constants.MEMCACHE_TIME);
    					}
    				}else if("EnterpriseCode".equals(name)){
    					List<EnterpriseSize> list= (List<EnterpriseSize>) m.invoke(ConsultDao);
    					for(EnterpriseSize enterpriseSize:list){
    						cachedManager.setValue("EnterpriseCode"+enterpriseSize.getEnterpriseCode(), enterpriseSize.getEnterpriseName(), Constants.MEMCACHE_TIME);
    					}
    				}else if("AuditStateCode".equals(name)){
    					List<AuditStateCode> list= (List<AuditStateCode>) m.invoke(ConsultDao);
    					for(AuditStateCode auditStateCode:list){
    						String s=auditStateCode.getAuditStateCode()+","+auditStateCode.getAuditStateName();
    						cachedManager.setValue("AuditStateCode"+auditStateCode.getRoleId()+auditStateCode.getThrough(), s, Constants.MEMCACHE_TIME);
    					}
    				}else if("Hospital".equals(name)){
    					Hospital hospital=hospitalDao.selectHosById(Integer.parseInt(key.substring(8)));
    					hospital.setPublicKey("");
    					hospital.setHosPassWord("");
    					cachedManager.setValue("Hospital"+hospital.getHosId(), hospital, Constants.MEMCACHE_TIME);
    				}
                }
            }
            return point.proceed(); //执行程序
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return throwable.getMessage();
        }
    }
}
