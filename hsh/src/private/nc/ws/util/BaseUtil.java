package nc.ws.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ����������
 * 
 * 
 */
public class BaseUtil {

	/**
	 * ����key����ȡ�����ļ���ֵ
	 * 
	 * @param key
	 * @return ֵ
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
