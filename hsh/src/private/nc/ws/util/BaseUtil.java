package nc.ws.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 基础工具类
 * 
 * 
 */
public class BaseUtil {

	/**
	 * 根据key，获取配置文件的值
	 * 
	 * @param key
	 * @return 值
	 * @throws IOException 
	 */
	public static String readConValByKey(String key) throws IOException {
		Properties props = new Properties();
			InputStream in = WSUtil.class.getClassLoader().getResourceAsStream("WSConfig.properties");
			props.load(in);
			String value = props.getProperty(key);
			return value;
	}
	
}
