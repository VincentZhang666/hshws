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
 * Json������
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
	 * ���÷��ص�json�ַ���(����)
	 * 
	 * @param isSucc
	 *            �Ƿ�ɹ�
	 * @param msg
	 *            ������Ϣ
	 * @param obj
	 *            ��Ҫ���صĶ���
	 * @param totalRecord
	 *            �ܼ�¼��,-1��ʾ����Ҫ����
	 *            
	 * @return json�ַ���
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
	 * ���÷��ص�json�ַ���(����)
	 * 
	 * @param obj
	 *            ��Ҫ���صĶ���
	 * @return json�ַ���
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
	 * ���÷��ص�json�ַ���(����)
	 * 
	 * @param obj
	 *            ��Ҫ���صĶ���
	 * @return json�ַ���
	 * @throws IOException 
	 */
	public static String returnStrForEsb(Object obj) throws IOException {
		StringBuffer retStr = new StringBuffer();
		String retJson = objToJsonStr(obj);
		retStr.append(retJson);
		return retStr.toString();
	}

	/**
	 * ���л�����
	 * 
	 * @param obj
	 *            ��Ҫ�����л��Ķ���
	 * @return ���л�֮����ַ���
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
	 * �����л�����(��Ե�������)��
	 * 
	 * @param jsonStr
	 *            ��Ҫ�����л����ַ���
	 * @param clazz
	 *            ת�ɵĶ�������
	 * @return ����
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
     * ���ַ��������л���ʵ����  ��Ե���һ��ʵ�壬��ʵ���е����԰����Զ�������ͣ���Teacher���ͣ�����List<Teacher>����
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
     * ��stringת����listBean 
     * @param jsonArrStr ��Ҫ�����л����ַ��� 
     * @param clazz �������л�֮����� 
     * @return ʵ��list 
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
     * ��stringת����listBean �����а���ʵ����� ��List<Student> ��Student�к�������List<Teacher>
     * @param jsonArrStr ��Ҫ�����л����ַ���
     * @param clazz �����л������
     * @param classMap �������а�������Teacher���뵽һ��Map�У���ʽ��map.put("teacher",Teacher.class)
     * @return �����л�����ַ���
     * ʹ��ʾ����
        Map classMap = new HashMap();
        //����Ҫ��Parent���г�ʼ�� ����ʶ��
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
//	 * ����key��������json�����ֵ
//	 * @param condition ������json�ַ���
//	 * @param key ��������
//	 * @return ����ֵ
//	 */
//	public static String getConParaByKey(String condition, String key){
//		JSONObject json = JSONObject.fromObject(condition);
//		 return json.getString(key);
//		
//	}
}
