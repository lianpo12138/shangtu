package com.dubu.turnover.component.http;

import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BestMatchSpec;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.dubu.turnover.utils.JsonUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;
public class HttpUtil {
	
	public static final String CALL_METHOD_POST = "POST";
	
	public static final String CALL_METHOD_PUT = "PUT";
	
	public static final String CALL_METHOD_GET = "GET";

	private static final Logger logger = Logger.getLogger(HttpUtil.class);

	private static int bufferSize= 1024;

	private static volatile HttpUtil instance;

	private PoolingHttpClientConnectionManager connManager;

	private volatile HttpClient client;

	private volatile BasicCookieStore cookieStore;

	public static String defaultEncoding= "utf-8";

	

	private HttpUtil(){
		//??????????????????
		ConnectionConfig connConfig = ConnectionConfig.custom().setCharset(Charset.forName(defaultEncoding)).build();
		SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(100000).build();
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
		ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
		registryBuilder.register("http", plainSF);
		//??????????????????????????????????????????????????????
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, new AnyTrustStrategy()).build();
			LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			registryBuilder.register("https", sslSF);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		} catch (KeyManagementException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		Registry<ConnectionSocketFactory> registry = registryBuilder.build();
		//?????????????????????
		connManager = new PoolingHttpClientConnectionManager(registry);
		connManager.setDefaultConnectionConfig(connConfig);
		connManager.setDefaultSocketConfig(socketConfig);
		//??????cookie????????????
		cookieStore = new BasicCookieStore();
		//???????????????
		client= HttpClientBuilder.create().setDefaultCookieStore(cookieStore).setConnectionManager(connManager).build();
	}

	public static HttpUtil getInstance(){
		synchronized (HttpUtil.class) {
			if (HttpUtil.instance == null){
				instance = new HttpUtil();
			}
			return instance;
		}
	}
	
	private static List<NameValuePair> paramsConverter(Map<String, String> params){
		List<NameValuePair> nvps = new LinkedList<NameValuePair>();
		Set<Entry<String, String>> paramsSet= params.entrySet();
		for (Entry<String, String> paramEntry : paramsSet) {
			nvps.add(new BasicNameValuePair(paramEntry.getKey(), paramEntry.getValue()));
		}
		return nvps;
	}

	public static String readStream(InputStream in, String encoding){
		if (in == null){
			return null;
		}
		try {
			InputStreamReader inReader= null;
			if (encoding == null){
				inReader= new InputStreamReader(in, defaultEncoding);
			}else{
				inReader= new InputStreamReader(in, encoding);
			}
			char[] buffer= new char[bufferSize];
			int readLen= 0;
			StringBuffer sb= new StringBuffer();
			while((readLen= inReader.read(buffer))!=-1){
				sb.append(buffer, 0, readLen);
			}
			inReader.close();
			return sb.toString();
		} catch (IOException e) {
			logger.error("????????????????????????", e);
		}
		return null;
	}

	public InputStream doGet(String url) throws URISyntaxException, ClientProtocolException, IOException{
		HttpResponse response= this.doGet(url, null, null);
		return response!=null ? response.getEntity().getContent() : null;
	}

	public String doGetForString(String url) throws URISyntaxException, ClientProtocolException, IOException{
		return HttpUtil.readStream(this.doGet(url), null);
	}

	public InputStream doGetForStream(String url, Map<String, String> queryParams) throws URISyntaxException, ClientProtocolException, IOException{
		return this.doGetForStream(url, queryParams, null);
	}
	
	public InputStream doGetForStream(String url, Map<String, String> queryParams, Map<String,String> headers) throws URISyntaxException, ClientProtocolException, IOException{
		HttpResponse response= this.doGet(url, queryParams, headers);
		return response!=null ? response.getEntity().getContent() : null;
	}

	public String doGetForString(String url, Map<String, String> queryParams) throws URISyntaxException, ClientProtocolException, IOException{
		return doGetForString(url, queryParams, null);
	}
	
	public String doGetForString(String url, Map<String, String> queryParams, Map<String,String> headers) throws URISyntaxException, ClientProtocolException, IOException{
		return HttpUtil.readStream(this.doGetForStream(url, queryParams, headers), null);
	}

	/**
	 * ?????????Get??????
	 * @param url ??????url
	 * @param queryParams ????????????????????????
	 * @return
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws ClientProtocolException
	 */
	public HttpResponse doGet(String url, Map<String, String> queryParams,
			Map<String,String> headers) throws URISyntaxException, ClientProtocolException, IOException{
		
		HttpGet gm = (HttpGet) initHttpMethod(headers, CALL_METHOD_GET);
		
		URIBuilder builder = new URIBuilder(url);
		//??????????????????
		if (queryParams!=null && !queryParams.isEmpty()){
			builder.setParameters(HttpUtil.paramsConverter(queryParams));
		}
		
		logger.debug(builder.build());
		
		gm.setURI(builder.build());
		
		return client.execute(gm);
	}
	/**
	 * ?????????Put??????
	 * @param url ??????url
	 * @param queryParams ????????????????????????
	 * @param formParams post???????????????
	 * @return
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws ClientProtocolException
	 */
	public HttpResponse doPut(String url, Map<String, String> queryParams, Map<String, String> formParams, Map<String,String> headers) throws URISyntaxException, ClientProtocolException, IOException{
		HttpPut pm = (HttpPut) initHttpMethod(headers, CALL_METHOD_PUT);
		URIBuilder builder = new URIBuilder(url);
		//??????????????????
		if (queryParams!=null && !queryParams.isEmpty()){
			builder.setParameters(HttpUtil.paramsConverter(queryParams));
		}
		
		logger.debug(builder.build());
		
		pm.setURI(builder.build());
		//??????????????????
		if (formParams!=null && !formParams.isEmpty()){
			pm.setEntity(new UrlEncodedFormEntity(HttpUtil.paramsConverter(formParams)));
		}
		
		return client.execute(pm);
	}
	/**
	 * ??????HTTP???????????????
	 * @param headers
	 * @param invokeType
	 * @return
	 */
	private HttpMessage initHttpMethod(Map<String, String> headers, String invokeType) {
		
		HttpMessage httpMessage = null;
		
		if(CALL_METHOD_POST.equals(invokeType)) {
			httpMessage = new HttpPost();
		} else if(CALL_METHOD_GET.equals(invokeType)) {
			httpMessage = new HttpGet();
		} else if(CALL_METHOD_PUT.equals(invokeType)) {
			httpMessage = new HttpPut();
		} else {
			return null;
		}
		
		if(headers != null && !headers.isEmpty()) {
			Set<Entry<String, String>> entrys = headers.entrySet();
			for(Entry<String, String> entry : entrys) {
				httpMessage.addHeader(entry.getKey(), entry.getValue());
				httpMessage.addHeader(entry.getKey(), entry.getValue());
			}
		}
		return httpMessage;
	}

	public InputStream doPostForStream(String url, Map<String, String> queryParams) throws URISyntaxException, ClientProtocolException, IOException {
		HttpResponse response = this.doPost(url, queryParams, null, null);
		return response!=null ? response.getEntity().getContent() : null;
	}

	public String doPostForString(String url, Map<String, String> queryParams) throws URISyntaxException, ClientProtocolException, IOException {
		return HttpUtil.readStream(this.doPostForStream(url, queryParams), null);
	}
	
	public InputStream doPutForStream(String url, Map<String, String> queryParams,Map<String, String> headers) throws URISyntaxException, ClientProtocolException, IOException {
		HttpResponse response = this.doPut(url, queryParams, null, headers);
		return response!=null ? response.getEntity().getContent() : null;
	}
	public String doPutForString(String url, Map<String, String> queryParams,Map<String, String> headers) throws URISyntaxException, ClientProtocolException, IOException {
		return HttpUtil.readStream(this.doPutForStream(url, queryParams, headers), null);
	}
	public InputStream doPutForStream(String url, Map<String, String> queryParams,Map<String, String> formParams, Map<String, String> headers) throws URISyntaxException, ClientProtocolException, IOException {
		HttpResponse response = this.doPut(url, queryParams, formParams, headers);
		return response!=null ? response.getEntity().getContent() : null;
	}
	public String doPutForString(String url, Map<String, String> queryParams, Map<String, String> formParams, Map<String, String> headers) throws URISyntaxException, ClientProtocolException, IOException {
		return HttpUtil.readStream(this.doPutForStream(url, queryParams, formParams, headers), null);
	}

	public InputStream doPostForStream(String url, Map<String, String> queryParams, Map<String, String> formParams, Map<String, String> headers) throws URISyntaxException, ClientProtocolException, IOException{
		HttpResponse response = this.doPost(url, queryParams, formParams, headers);
		return response!=null ? response.getEntity().getContent() : null;
	}

	public String doPostRetString(String url, Map<String, String> queryParams, Map<String, String> formParams) throws URISyntaxException, ClientProtocolException, IOException{
		return doPostRetString(url, queryParams, formParams, null);
	}
	
	public String doPostRetString(String url, Map<String, String> queryParams, Map<String, String> formParams,
			Map<String,String> headers) throws URISyntaxException, ClientProtocolException, IOException{
		return HttpUtil.readStream(this.doPostForStream(url, queryParams, formParams, headers), null);
	}

	/** 
	 * ?????????Post??????
	 * @param url ??????url
	 * @param queryParams ????????????????????????
	 * @param formParams post???????????????
	 * @return
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws ClientProtocolException
	 */
	public HttpResponse doPost(String url, Map<String, String> queryParams, Map<String, String> formParams, Map<String,String> headers) throws URISyntaxException, ClientProtocolException, IOException{
		HttpPost pm = (HttpPost) initHttpMethod(headers, CALL_METHOD_POST);
		URIBuilder builder = new URIBuilder(url);
		//??????????????????
		if (queryParams!=null && !queryParams.isEmpty()){
			builder.setParameters(HttpUtil.paramsConverter(queryParams));
		}
		
		logger.debug(builder.build());
		
		pm.setURI(builder.build());
		//??????????????????
		if (formParams!=null && !formParams.isEmpty()){
//			pm.setEntity(new UrlEncodedFormEntity(HttpUtil.paramsConverter(formParams)));
			pm.setEntity(new UrlEncodedFormEntity(HttpUtil.paramsConverter(formParams), "utf-8"));
		}
		return client.execute(pm);
	}

	
	
	public String multipartPut(String url, Map<String, String> queryParams,Map<String,String> headers) throws URISyntaxException, ClientProtocolException, IOException{
		HttpPut pm = (HttpPut) initHttpMethod(headers, CALL_METHOD_PUT);
		URIBuilder builder = new URIBuilder(url);
		//??????????????????
		if (queryParams!=null && !queryParams.isEmpty()){
			builder.setParameters(HttpUtil.paramsConverter(queryParams));
		}
		
		logger.debug(builder.build());
		
		pm.setURI(builder.build());
		
		
		StringEntity entity = new StringEntity(JsonUtils.toJson(queryParams),"utf-8");
		pm.setEntity(entity);

		HttpResponse response = client.execute(pm);
		InputStream in = response!=null ? response.getEntity().getContent() : null;
		return HttpUtil.readStream(in, null);
	}
	
	public String multipartPost(String url, Map<String, String> queryParams, Map<String,String> headers) throws URISyntaxException, ClientProtocolException, IOException{
		HttpPost pm = (HttpPost) initHttpMethod(headers, CALL_METHOD_POST);
		URIBuilder builder = new URIBuilder(url);

		logger.debug(builder.build());
		
		pm.setURI(builder.build());
		
		
		StringEntity entity = new StringEntity(JsonUtils.toJson(queryParams),"utf-8");
		pm.setEntity(entity);

		HttpResponse response = client.execute(pm);
		InputStream in = response!=null ? response.getEntity().getContent() : null;
		return HttpUtil.readStream(in, null);
	}

	/**
	 * ????????????Http?????????????????????Cookie
	 * @param domain ?????????
	 * @param port ?????? ???null ??????80
	 * @param path Cookie?????? ???null ??????"/"
	 * @param useSecure Cookie???????????????????????? ???null ??????false
	 * @return
	 */
	public Map<String, Cookie> getCookie(String domain, Integer port, String path, Boolean useSecure){
		if (domain == null){
			return null;
		}
		if (port==null){
			port= 80;
		}
		if (path==null){
			path="/";
		}
		if (useSecure==null){
			useSecure= false;
		}
		List<Cookie> cookies = cookieStore.getCookies();
		if (cookies==null || cookies.isEmpty()){
			return null;
		}

		CookieOrigin origin= new CookieOrigin(domain, port, path, useSecure);
		BestMatchSpec cookieSpec = new BestMatchSpec();
		Map<String, Cookie> retVal= new HashMap<String, Cookie>();
		for (Cookie cookie : cookies) {
			if(cookieSpec.match(cookie, origin)){
				retVal.put(cookie.getName(), cookie);				
			}
		}
		return retVal;
	}

	/**
	 * ????????????Cookie
	 * @param cookies cookie????????????
	 * @param domain ????????? ????????????
	 * @param path ?????? ???null?????????"/"
	 * @param useSecure ???????????????????????? ???null ?????????false
	 * @return ??????????????????cookie
	 */
	public boolean setCookie(Map<String, String> cookies, String domain, String path, Boolean useSecure){
		synchronized (cookieStore) {
			if (domain==null){
				return false;
			}
			if (path==null){
				path= "/";
			}
			if (useSecure==null){
				useSecure= false;
			}
			if (cookies==null || cookies.isEmpty()){
				return true;
			}
			Set<Entry<String, String>> set= cookies.entrySet();
			String key= null;
			String value= null;
			for (Entry<String, String> entry : set) {
				key= entry.getKey();
				if (key==null || key.isEmpty() || value==null || value.isEmpty()){
					throw new IllegalArgumentException("cookies key and value both can not be empty");
				}
				BasicClientCookie cookie= new BasicClientCookie(key, value);
				cookie.setDomain(domain);
				cookie.setPath(path);
				cookie.setSecure(useSecure);
				cookieStore.addCookie(cookie);
			}
			return true;
		}
	}

	/**
	 * ????????????Cookie
	 * @param key Cookie???
	 * @param value Cookie???
	 * @param domain ????????? ????????????
	 * @param path ?????? ???null?????????"/"
	 * @param useSecure ???????????????????????? ???null ?????????false
	 * @return ??????????????????cookie
	 */
	public boolean setCookie(String key, String value, String domain, String path, Boolean useSecure){
		Map<String, String> cookies= new HashMap<String, String>();
		cookies.put(key, value);
		return setCookie(cookies, domain, path, useSecure);
	}

}