package com.dubu.turnover.utils;



import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(HttpClientUtils.class);

	public static final String get(final String url, final Map<String, Object> params,final Map<String,String> heads) {
		StringBuilder sb = new StringBuilder("");

		if (null != params && !params.isEmpty()) {
			int i = 0;
			for (String key : params.keySet()) {
				if (i == 0) {
					sb.append("?");
				} else {
					sb.append("&");
				}
				sb.append(key).append("=").append(params.get(key));
				i++;
			}
		}

		CloseableHttpClient httpClient = createSSLClientDefault();

		CloseableHttpResponse response = null;
		HttpGet get = new HttpGet(url + sb.toString());
		if(heads!=null && heads.size()>0) {
			for(Map.Entry<String, String> entry : heads.entrySet()) {
				get.addHeader(entry.getKey(), entry.getValue());
			}
		}
		String result = "";

		try {
			response = httpClient.execute(get);

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (null != entity) {
					result = EntityUtils.toString(entity, "UTF-8");
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(HttpClientUtils.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			if (null != response) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException ex) {
					Logger.getLogger(HttpClientUtils.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

		return result;
	}

	public static final String postJson(final String url, File params,final Map<String,String> heads) {

		CloseableHttpClient httpClient = createSSLClientDefault();
		HttpPost post = new HttpPost(url);

		CloseableHttpResponse response = null;

		if (null != params && params.exists()) {
			/*List<NameValuePair> nvpList = new ArrayList<NameValuePair>();
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				NameValuePair nvp = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
				nvpList.add(nvp);
			}
			post.setEntity(new UrlEncodedFormEntity(nvpList, Charset.forName("UTF-8")));*/
			MultipartEntityBuilder multipartEntityBuilder =MultipartEntityBuilder.create();
			multipartEntityBuilder.addBinaryBody("file", params).setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			post.setEntity(multipartEntityBuilder.build());
		}
		if(heads!=null && heads.size()>0) {
			for(Map.Entry<String, String> entry : heads.entrySet()) {
				post.addHeader(entry.getKey(), entry.getValue());
			}
		}
		String result = "";

		try {
			response = httpClient.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (null != entity) {
					result = EntityUtils.toString(entity, "UTF-8");
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(HttpClientUtils.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			if (null != response) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException ex) {
					Logger.getLogger(HttpClientUtils.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

		return result;
	}
	
	public static final String postJson(final String url, String params) {
		log.info("post request url:" + url);
		log.info("post request parms:" + params);

		CloseableHttpClient httpClient = createSSLClientDefault();

		HttpPost post = new HttpPost(url);

		CloseableHttpResponse response = null;

		if (null != params && !params.isEmpty()) {
			/*List<NameValuePair> nvpList = new ArrayList<NameValuePair>();
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				NameValuePair nvp = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
				nvpList.add(nvp);
			}
			post.setEntity(new UrlEncodedFormEntity(nvpList, Charset.forName("UTF-8")));*/
			post.setEntity(new StringEntity(params, Charset.forName("UTF-8")));
		}
		String result = "";

		try {
			response = httpClient.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (null != entity) {
					result = EntityUtils.toString(entity, "UTF-8");
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(HttpClientUtils.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			if (null != response) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException ex) {
					Logger.getLogger(HttpClientUtils.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

		log.info("post response:" + result);
		return result;
	}
	public static final String postJson2(final String url, String params) {
		CloseableHttpClient httpClient = createSSLClientDefault();

		HttpPost post = new HttpPost(url);
		post.addHeader("Content-Type", "application/json;charset=UTF-8");

		CloseableHttpResponse response = null;

		if (null != params && !params.isEmpty()) {
			/*List<NameValuePair> nvpList = new ArrayList<NameValuePair>();
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				NameValuePair nvp = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
				nvpList.add(nvp);
			}
			post.setEntity(new UrlEncodedFormEntity(nvpList, Charset.forName("UTF-8")));*/
			post.setEntity(new StringEntity(params, Charset.forName("UTF-8")));
		}
		String result = "";

		try {
			response = httpClient.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (null != entity) {
					result = EntityUtils.toString(entity, "UTF-8");
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(HttpClientUtils.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			if (null != response) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException ex) {
					Logger.getLogger(HttpClientUtils.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

		return result;
	}

	private static CloseableHttpClient createSSLClientDefault() {

		SSLContext sslContext;
		try {
			sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有
				@Override
				public boolean isTrusted(X509Certificate[] xcs, String string) {
					return true;
				}
			}).build();

			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);

			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (KeyStoreException ex) {
			Logger.getLogger(HttpClientUtils.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(HttpClientUtils.class.getName()).log(Level.SEVERE, null, ex);
		} catch (KeyManagementException ex) {
			Logger.getLogger(HttpClientUtils.class.getName()).log(Level.SEVERE, null, ex);
		}

		return HttpClients.createDefault();
	}
	public static final String postJson2(final String url, String params,final Map<String, String> header) {
		CloseableHttpClient httpClient = createSSLClientDefault();
		HttpPost post = new HttpPost(url);

		CloseableHttpResponse response = null;

		if (null != params && !params.isEmpty()) {
			/*List<NameValuePair> nvpList = new ArrayList<NameValuePair>();
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				NameValuePair nvp = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
				nvpList.add(nvp);
			}
			post.setEntity(new UrlEncodedFormEntity(nvpList, Charset.forName("UTF-8")));*/
			post.setEntity(new StringEntity(params, Charset.forName("UTF-8")));
		}

		String result = "";

		if(header!=null) {
			for (Map.Entry<String, String> entry : header.entrySet()) {  
				post.addHeader(entry.getKey(), entry.getValue());
			}  
		}
		
		try {
			response = httpClient.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (null != entity) {
					result = EntityUtils.toString(entity, "UTF-8");
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(HttpClientUtils.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			if (null != response) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException ex) {
					Logger.getLogger(HttpClientUtils.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

		return result;
	}

}