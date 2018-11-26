package com.thtf.app.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

public class HttpClient {
	/**
	 * skip AllHttpsCertificates
	 * 
	 */
	static {
		try {
			trustAllHttpsCertificates();
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String urlHostName, SSLSession session) {
					return true;
				}
			});
		} catch (Exception e) {
		}
	}

	private Logger logger = Logger.getLogger(HttpClient.class);

	private String responseContent;

	private Map<String, List<String>> responseHeaders = new HashMap<String, List<String>>();

	public void last() throws Exception {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept",
				"text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
		headers.put("Accept-Encoding", "gzip, deflate, br");
		headers.put("Accept-Language", "zh-CN,zh;q=0.9,zh-TW;q=0.8,en-US;q=0.7,en;q=0.6");
		// headers.put("Authorization",
		// " Bearer
		// eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTUxNDYwMDUzMX0.MQtYugZcc8uy2V-okKZa5KyxYw4OjiwNO5ENwVII8VJmgzeKOXqGHdQjF_foduPnbtfwcaFDs8KquB28bNPhUA");
		// headers.put("Content-Type", "application/x-www-form-urlencoded");
		headers.put("Cookie",
				"BAIDUID=DB80AE9332DB0B5A30CF54B0C0AB4463:FG=1; PSTM=1515120501; BIDUPSID=660511998CA52E928036E9E79F9426FE; ispeed_lsm=2; __cfduid=d503be984fb5465de57dd382e7d39ae471515401409; H_PS_645EC=03basU5dghdalzJg9multSF8i%2FHuv8P%2F0ydfxhKstWIpBRBBJI1WFxf1JHY; BD_CK_SAM=1; PSINO=2; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; BD_HOME=0; H_PS_PSSID=1463_19037_21124_20930; BD_UPN=12314353");
		String response0 = doGet(
				"https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=0&rsv_idx=1&tn=baidu&wd=123&rsv_pq=c0a4676a00007b61&rsv_t=8561f2T5ORZ27MMrileCuqH4I8Scuo6acrG2nGCK4b4Vx5FntFyFH%2FMh9eU&rqlang=cn&rsv_enter=0&rsv_sug3=4&rsv_sug1=3&rsv_sug7=100&inputT=1636&rsv_sug4=3241",
				headers);
		System.out.println(response0);
	}

	public String doGet(String path, Map<String, String> headers) throws Exception {

		this.logRequestInfo("GET", path, headers, null);
		URL url = new URL(path.trim());
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		// System.setProperty("https.protocols", "TLSv1.1");
		urlConnection.setInstanceFollowRedirects(false);
		setConnProperties(headers, urlConnection);

		logger.info("Request URL ... " + url);

		urlConnection = redirect(headers, url, urlConnection);

		this.responseHeaders.putAll(urlConnection.getHeaderFields());// 保持头部信息
		System.out.println("content................:" + urlConnection.getContent());
		// this.logResponseInfo(this.responseHeaders);
		if (200 == urlConnection.getResponseCode()) {

			InputStream is = urlConnection.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while (-1 != (len = is.read(buffer))) {
				baos.write(buffer, 0, len);
				baos.flush();
			}
			this.responseContent = baos.toString("utf-8");
			return this.responseContent;
		}
		return null;
	}

	public String doPost(String path, Map<String, String> headers, String param) throws Exception {
		this.logRequestInfo("POST", path, headers, param);
		URL url = null;

		// System.setProperty("https.protocols", "TLSv1.1");
		url = new URL(path);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestMethod("POST");
		// httpURLConnection.setConnectTimeout(10000);
		httpURLConnection.setReadTimeout(2000000);
		httpURLConnection.setInstanceFollowRedirects(false);
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		httpURLConnection.setUseCaches(false);
		setConnProperties(headers, httpURLConnection);

		PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());

		printWriter.write(param);// post request body param format:
									// xx=xx&yy=yy

		printWriter.flush();

		System.out.println("Request URL ... " + url);

		httpURLConnection = redirect(headers, url, httpURLConnection);

		// get response header info
		this.responseHeaders.putAll(httpURLConnection.getHeaderFields());
		this.logResponseInfo(this.responseHeaders);
		BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		int len;
		byte[] arr = new byte[1024];
		while ((len = bis.read(arr)) != -1) {
			bos.write(arr, 0, len);
			bos.flush();
		}
		bos.close();
		System.out.println(bos.toString("utf-8"));
		this.responseContent = bos.toString("utf-8");
		return this.responseContent;
	}

	private HttpURLConnection redirect(Map<String, String> headers, URL url, HttpURLConnection urlConnection)
			throws IOException, MalformedURLException {
		while (isRedirected(urlConnection)) {

			// get redirect url from "location" header field
			// String newUrl = "http://localhost:8081"+
			// urlConnection.getHeaderField("Location");
			String newUrl = urlConnection.getHeaderField("Location");

			// get the cookie if need, for login
			this.responseHeaders.putAll(urlConnection.getHeaderFields());
			String cookies = urlConnection.getHeaderField("Set-Cookie");
			if (cookies == null) {
				cookies = headers.get("Cookie");
			}

			// open the new connnection again
			urlConnection = (HttpURLConnection) new URL(newUrl).openConnection();
			urlConnection.setRequestProperty("Cookie", cookies);
			urlConnection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
			urlConnection.addRequestProperty("User-Agent", "Mozilla");
			urlConnection.addRequestProperty("Referer", "google.com");

			System.out.println("Redirect to URL : " + url);

		}
		return urlConnection;
	}

	private boolean isRedirected(HttpURLConnection httpURLConnection) throws IOException {
		// normally, 3xx is redirect
		boolean redirect = false;
		int status = httpURLConnection.getResponseCode();
		if (status != HttpURLConnection.HTTP_OK) {
			if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM
					|| status == HttpURLConnection.HTTP_SEE_OTHER)
				redirect = true;
		}

		System.out.println("Response Code ... " + status);
		return redirect;
	}

	private static void setConnProperties(Map<String, String> headers, HttpURLConnection urlConnection) {
		Iterator<String> key = headers.keySet().iterator();
		while (key.hasNext()) {
			String keyStr = key.next();
			urlConnection.setRequestProperty(keyStr, headers.get(keyStr));
			System.out.println(keyStr + ":" + headers.get(keyStr));

		}
	}

	private void logRequestInfo(String method, String path, Map<String, String> headers, String param) {
		logger.info(
				"Request----(Method:" + method + "," + "URI:" + path + ",Header:" + headers + "param:" + param + ")");
	}

	private void logResponseInfo(Map<String, List<String>> headers) {
		logger.info("Response---(Header:" + headers + ")");
	}

	public String getResponseContent() {
		return responseContent;
	}

	public Map<String, List<String>> getResponseHeaders() {
		return responseHeaders;
	}

	private static void trustAllHttpsCertificates() throws NoSuchAlgorithmException, KeyManagementException {
		TrustManager[] trustAllCerts = new TrustManager[1];
		trustAllCerts[0] = new TrustAllManager();
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	private static class TrustAllManager implements X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
		}

		public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
		}
	}

//	public static void main(String[] args) throws Exception {
//
//		// String response =
//		// doPost("http://localhost:9000/api/authenticate","{\r\n" +
//		// " \"username\": \"admin\",\r\n" +
//		// " \"password\": \"admin\"\r\n" +
//		// "}");
//		// System.out.println(response);
//		HttpClient client = new HttpClient();
//		client.last();
//
//	}

}
