package nc.ws.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.vo.pub.lang.UFBoolean;
import nc.ws.entity.DataAPIEntity;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import nc.ms.tb.oba.jackson.map.DeserializationConfig;
//import nc.ms.tb.oba.jackson.map.ObjectMapper;

/**
 * Json工具类
 * 
 * 
 */
public class JsonUtil {

	static ObjectMapper mapper = new ObjectMapper();

	public static String returnRequestJson(DataAPIEntity datas) throws JsonGenerationException, JsonMappingException, IOException {
		StringBuffer retJson = new StringBuffer();
		
		
		retJson.append("{\"plat\": \""+datas.getPlat()+"\"," +
				"\"appKey\": \""+datas.getAppKey()+"\"," +
				"\"appSecret\": \""+datas.getAppSecret()+"\"," +
				"\"session\": \""+datas.getSession()+"\"," +
				"\"method\": \""+datas.getMethod()+"\"," +
				"\"format\": \"json\"," +
				"\"access\": \"1\",");
		
		if(datas.getRequest() != null){
			if(datas.getRequest().size() >0){
				retJson.append("\"request\":"+objToJsonStr(datas.getRequest())+"}");
			}
			else{
				retJson.append("\"request\":\"\"}");
			}
		}else{
			retJson.append("\"request\":\"\"}");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("params", retJson);
		String result = objToJsonStr(map);
		
		return result.toString();
	}
		
	/**
	 * 设置返回的json字符串(重载)
	 * 
	 * @param isSucc
	 *            是否成功
	 * @param msg
	 *            错误信息
	 * @param obj
	 *            需要返回的对象
	 * @param totalRecord
	 *            总记录数,-1表示不需要输送
	 *            
	 * @return json字符串
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public static String returnJson(UFBoolean isSucc, String msg, Object obj, int totalRecord) throws JsonGenerationException, JsonMappingException, IOException {
		StringBuffer retJson = new StringBuffer();
		if (isSucc.booleanValue()) {
			String result = objToJsonStr(obj);
			String totalRec = new String();
			if(totalRecord != -1){
				totalRec = "\"totalrecord\":" + totalRecord + ",";
			}
			retJson.append("{\"ret\":0,\"msg\":\"\","+ totalRec +"\"result\":" + result + "}");
		} else {
			retJson.append("{\"ret\":1,\"msg\":\"" + msg
					+ "\",\"result\":\"\"}");
		}
		return retJson.toString();
	}

	/**
	 * 设置返回的json字符串(重载)
	 * 
	 * @param obj
	 *            需要返回的对象
	 * @return json字符串
	 * @throws IOException 
	 */
	public static String returnStr(Object obj) throws IOException {
		StringBuffer retStr = new StringBuffer();
		String retJson = objToJsonStr(obj);
		String key = BaseUtil.readConValByKey("key");
		retStr.append("key=" + key + "&result=" + retJson);
		return retStr.toString();
	}
	
	/**
	 * 设置返回的json字符串(重载)
	 * 
	 * @param obj
	 *            需要返回的对象
	 * @return json字符串
	 * @throws IOException 
	 */
	public static String returnStrForEsb(Object obj) throws IOException {
		StringBuffer retStr = new StringBuffer();
		String retJson = objToJsonStr(obj);
		retStr.append(retJson);
		return retStr.toString();
	}

	/**
	 * 序列化操作
	 * 
	 * @param obj
	 *            需要被序列化的对象
	 * @return 序列化之后的字符串
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public static String objToJsonStr(Object obj) throws JsonGenerationException, JsonMappingException, IOException {

		String result = null;
		if (obj != null) {
			result = mapper.writeValueAsString(obj);
		}
		return result;
	}

	/**
	 * 反序列化操作(针对单个对象)型
	 * 
	 * @param jsonStr
	 *            需要反序列化的字符串
	 * @param clazz
	 *            转成的对象类型
	 * @return 对象
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object readJsonObject(String jsonStr, Class clazz) throws JsonParseException, JsonMappingException, IOException {
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Object result = mapper.readValue(jsonStr, clazz);
		return result;
	}
	
	/**
     * 由字符串反序列化成实体类  针对的是一个实体，此实体中的属性包括自定义的类型，如Teacher类型，或者List<Teacher>类型
     * @param jsonArrStr
     * @param clazz
     * @param classMap
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static Object getObjFromJsonArrStr(String jsonArrStr, Class clazz, Map classMap) 
    {  
        JSONObject jsonObj = JSONObject.fromObject(jsonArrStr);  
        return JSONObject.toBean(jsonObj, clazz, classMap);            
    }
    
    /** 
     * 将string转换成listBean 
     * @param jsonArrStr 需要反序列化的字符串 
     * @param clazz 被反序列化之后的类 
     * @return 实体list 
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static List getListFromJsonArrStr(String jsonArrStr, Class clazz) {   
        JSONArray jsonArr = JSONArray.fromObject(jsonArrStr);   
        List list = new ArrayList();   
        for (int i = 0; i < jsonArr.size(); i++)  
        {   
            list.add(JSONObject.toBean(jsonArr.getJSONObject(i), clazz));   
        }   
        return list;   
    } 
	
	/**
     * 将string转换成listBean 属性中包含实体类等 如List<Student> 而Student中含有属性List<Teacher>
     * @param jsonArrStr 需要反序列化的字符串
     * @param clazz 反序列化后的类
     * @param classMap 将属性中包含的如Teacher加入到一个Map中，格式如map.put("teacher",Teacher.class)
     * @return 反序列化后的字符串
     * 使用示例：
        Map classMap = new HashMap();
        //必须要对Parent进行初始化 否则不识别
        Teacher p = new Teacher();
        classMap.put("teacher", p.getClass());
        List mlist = JSONTransfer.getListFromJsonArrStr(resultStr, Student.class, classMap);
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static List getListFromJsonArrStr(String jsonArrStr, Class clazz, Map classMap) 
    {  
       JSONArray jsonArr = JSONArray.fromObject(jsonArrStr);  
       List list = new ArrayList();  
       for (int i = 0; i < jsonArr.size(); i++) 
       {           
           list.add(JSONObject.toBean(jsonArr.getJSONObject(i), clazz, classMap));  
       }  
       return list;  
    }
	
//	/**
//	 * 根据key查找条件json里面的值
//	 * @param condition 条件的json字符串
//	 * @param key 条件参数
//	 * @return 参数值
//	 */
//	public static String getConParaByKey(String condition, String key){
//		JSONObject json = JSONObject.fromObject(condition);
//		 return json.getString(key);
//		
//	}
}
