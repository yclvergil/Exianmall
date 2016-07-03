package com.cnmobi.exianmall.base;
//
//package com.cnmobi.exianmall;
//
//import java.io.IOException;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.params.HttpParams;
//import org.apache.http.params.HttpProtocolParams;
//import org.apache.http.util.EntityUtils;
//
//public class CommonJSONHelper {
//	public static String getJSON(String uri) {
//		String json = null;
//		try {
//			HttpClient client = new DefaultHttpClient();
//			HttpParams httpParams = client.getParams();
//			String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
//			HttpProtocolParams.setUserAgent(httpParams, userAgent);
//			HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
//			HttpGet httpGet = new HttpGet(uri);
//			HttpResponse httpResponse = client.execute(httpGet);
//			if (httpResponse != null) {
//				int code = httpResponse.getStatusLine().getStatusCode();
//				if (code == HttpStatus.SC_OK) {
//		 			HttpEntity entity = httpResponse.getEntity();
//					json = EntityUtils.toString(entity);
//				}
//			}
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return json;
//	}
//}
//
