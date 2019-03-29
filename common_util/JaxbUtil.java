package com.zhejian.utils;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.zhejian.exception.CustomizedException;

//import com.zhejian.exception.CustomizedException;

/** 
 * Jaxb2工具类 
 */ 
public class JaxbUtil {  
  
    /** 
     * JavaBean转换成xml 
     * 默认编码UTF-8 
     * @param obj 
     * @return  
     */  
    public static String convertToXml(Object obj) {  
        return convertToXml(obj, "UTF-8");  
    }  
  
    /** 
     * JavaBean转换成xml 
     * @param obj 
     * @param encoding  
     * @return  
     */  
    private static String convertToXml(Object obj, String encoding) {  
        String result = null;  
        try {  
            JAXBContext context = JAXBContext.newInstance(obj.getClass());  
            Marshaller marshaller = context.createMarshaller(); 
            //TODO
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);  
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);  
  
            StringWriter writer = new StringWriter();  
            marshaller.marshal(obj, writer);  
            result = writer.toString();
            result = result.replaceFirst(" standalone=\"yes\"", "");
            
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return result;  
    }  
  
    /** 
     * xml转换成JavaBean 
     * @param xml 
     * @param c 
     * @return 
     * @throws CustomizedException 
     */  
    @SuppressWarnings("unchecked")  
    public static <T> T convertToJavaBean(String xml, Class<T> c) throws CustomizedException {  
        T t = null;  
        try {  
            JAXBContext context = JAXBContext.newInstance(c);  
            Unmarshaller unmarshaller = context.createUnmarshaller();  
            t = (T) unmarshaller.unmarshal(new StringReader(xml));  
        } catch (Exception e) {  
            e.printStackTrace(); 
            throw new CustomizedException("The input is not a correct json.");
        }  
  
        return t;  
    }  


}
