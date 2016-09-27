package nc.ws.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import nc.ws.entity.DataAPIEntity;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

public class WSUtil {

	/**
	 * Web Service���ӣ��������ݣ������ؽ��
	 * 
	 * @param urlStr
	 *            �ӿڵ�ַ
	 * @param data
	 *            ���͵�����
	 * @return ���
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static String connect(DataAPIEntity dataEntity)
			throws MalformedURLException, IOException {
		// �ж���Ҫ���͵������Ƿ�Ϊnull
		//String dataSub = data.substring(data.indexOf("&") + 1, data.length());
		//if (!StringUtils.isEmpty(dataSub) && !dataSub.equals("result=null")) {
			URL url = new URL(
					"http://mcloud.yonyou.com/eccloud-inter/servlet/BaseHttpServlet");// ��������
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("POST"); // ��������ʽ
			connection.setRequestProperty("Accept", "application/json"); // ���ý������ݵĸ�ʽ
			connection.setRequestProperty("Content-Type", "application/json"); // ���÷������ݵĸ�ʽ
			connection.connect();
			
			// ������д����
			ObjectOutputStream oos = null;
			
			String datas = JsonUtil.returnRequestJson(dataEntity);
			System.out.println(datas);
			oos = new ObjectOutputStream(connection.getOutputStream());
			oos.writeObject(datas);
			oos.flush();
			oos.close();

			// ��ȡ��������
			InputStream ois = connection.getInputStream();
			InputStreamReader inread = new InputStreamReader(ois, "UTF-8");
			StringBuffer postResult = new StringBuffer();
			char[] b = new char[1024];
			for (int n; (n = inread.read(b)) != -1;) {
				postResult.append(new String(b, 0, n));
			}
			if (null != inread) {
				try {
					inread.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (null != ois) {
				try {
					ois.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			String str = new String(postResult.toString().getBytes("utf-8"),
					"utf-8");
			// sysout��������
			//System.out.println(str);
			return str;
		/*} else {
			return "{\"ret\":0,\"msg\":\"���͵�����Ϊ�գ�\"}";
		}*/
	}

	/**
	 * ��Web Service���󷵻ص�����
	 * 
	 * @param response
	 *            ����
	 * @param retJson
	 *            ���ص�����
	 * @throws IOException
	 */
	public static void responStream(HttpServletResponse response, String retJson)
			throws IOException {
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		OutputStream stream = response.getOutputStream();
		stream.write(retJson.getBytes("UTF-8"));
	}
	
	public static void main(String[] args) {
		
		try {
			DataAPIEntity datas = new DataAPIEntity();
			datas.setPlat("top");
			datas.setAppKey("222");
			datas.setAppSecret("222");
			datas.setSession("222");
			datas.setMethod("searchLogisticsTrace");
			
			/*Map<String, Object> mapRequest = new HashMap<String, Object>();
			mapRequest.put("isSplit", "222");
			mapRequest.put("sellerNick", "222");
			mapRequest.put("subTid", "222");
			mapRequest.put("tid", "222");
			datas.setRequest(mapRequest);*/
			
			String str = WSUtil.connect(datas);
			System.out.println(str);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
