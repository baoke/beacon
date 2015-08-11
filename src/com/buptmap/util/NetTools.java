package com.buptmap.util;


import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
 
import javax.activation.MimetypesFileTypeMap;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class NetTools {
	/**
	 * post进行网络连接
	 * @param urlStr
	 * @return
	 */
	public static String doPostWithParams(String urlStr, List<BasicNameValuePair> params) {
		String result = null;
		if (urlStr == null) {
			return null;
		}
		HttpPost httpRequest = new HttpPost(urlStr);
//			List<BasicNameValuePair> params =new ArrayList<BasicNameValuePair>(); 
//			for(int i = 0; i < p.length; i++){
//				params.add(new BasicNameValuePair(p[i][0], p[i][1]));
//			}
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity( params));
			HttpResponse httpResponse = (new DefaultHttpClient())
					.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				if(true){
				result = EntityUtils.toString(httpResponse
						.getEntity());
			} else {
				System.err.println("---HttpStatus is not ok!---");
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			System.err.println("---网络异常---");
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.err.println("---内容处理异常---");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("---IO异常---");
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * post进行网络连接
	 * @param urlStr
	 * @return
	 */
	public static String doPost(String urlStr, final String postStr) {

			if (urlStr == null) {
				return null;
			}
			HttpPost httpRequest = new HttpPost(urlStr);
			
			byte[] data = null;
			try {
				if(postStr != null){
					data = postStr.getBytes("UTF-8");
				}else{
					System.err.println("No data!!");
//					data = SystemParam.AUTONAVI_TEST_XML.getBytes("UTF-8");
				}
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				System.err.println("---Post String Encoding Error---");
				e1.printStackTrace();
			}
			ByteArrayEntity entity = new ByteArrayEntity(data);
			httpRequest.setEntity(entity);
			
			try {
				HttpResponse httpResponse = (new DefaultHttpClient())
						.execute(httpRequest);
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				if(true){
					String resultStr = EntityUtils.toString(httpResponse
							.getEntity());
					System.out.println(resultStr);
					return resultStr;
				} else {
					System.err.println("---HttpStatus is not ok!---");
					return null;
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				System.err.println("---ClientProtocolException---");
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				System.err.println("---ParseException---");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("---IOException---");
				e.printStackTrace();
			}

		return null;
	}
	
	/**
	 * get方法进行网络连接
	 * @param urlStr
	 * @return
	 */
	public static String doGet(String urlStr) {
		//System.out.println("url:"+urlStr);
		try {
			if (urlStr == null) {
				return null;
			}
			HttpGet httpRequest = new HttpGet(urlStr);
			HttpResponse httpResponse = (new DefaultHttpClient())
					.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
				String resultStr = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
				return resultStr;
			} else {
				System.err.println("<StatusCode: " + httpResponse.getStatusLine().getStatusCode() + " >");
				System.err.println(EntityUtils.toString(httpResponse.getEntity()));
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取SSL方式get方法连接返回字符串
	 * @param urlStr
	 * @return
	 */
	public static String doHttpsGet(String urlStr){
        try {
			trustAllHttpsCertificates();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HostnameVerifier hv = new HostnameVerifier() {
			@Override
			public boolean verify(String urlHostName, SSLSession session) {
				System.out.println("Warning: URL Host: " + urlHostName
						+ " vs. " + session.getPeerHost());
				return true;
			}
		};

        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        
        StringBuffer str = new StringBuffer();
		try {
			URL url = new URL(urlStr);
			HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
//			System.out.println("Header:" +urlConn.getHeaderField(0).toString().split(" ")[1]);
			//非正常返回立刻返回null退出
			String status = urlConn.getHeaderField(0).toString().split(" ")[1];
			if(!status.equals("200")){
				System.err.println("<StatusCode: " + status + " >Https not right header~!");
				InputStream is = urlConn.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				System.err.println(br.readLine());
				return null;
			}
			
			InputStream is = urlConn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while((line = br.readLine()) != null){
				System.out.println(line);
				str.append(line).append("\r\n");
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str.toString();
        
	}

	public static String doHttpsPost(String urlStr, String param){
		String result = null;
		try {
			result = new String(post(urlStr, param, "utf-8"));
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("---IO Exception---");
			e.printStackTrace();
		}
		return result;
	}
    /**
     * post 方式连接
     * @param url
     * @param content
     * @param charset
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws IOException
     */
    public static byte[] post(String url, String content, String charset)
            throws NoSuchAlgorithmException, KeyManagementException,
            IOException {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
                new java.security.SecureRandom());
 
        URL console = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
        conn.setSSLSocketFactory(sc.getSocketFactory());
        conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
        conn.setDoOutput(true);
        conn.connect();
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.write(content.getBytes(charset));

        out.flush();
        out.close();
        InputStream is = conn.getInputStream();
        if (is != null) {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            is.close();
            return outStream.toByteArray();
        }
        return null;
    }

	/**
	 * 将字符串转换成MD5
	 * @param source
	 * @return
	 */
	public static String getMD5(String source) {
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
				'E', 'F' };

		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source.getBytes("UTF-8"));
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
										// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
											// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
											// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
															// >>>
															// 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			System.out.println("<NoSuchAlgorithmException>:" + e.getMessage());
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			System.out.println("<UnsupportedEncodingException>:" + e.getMessage());
			e.printStackTrace();
		}

		return s;
	}
	
	/** 
     * 上传图片 
     *  
     * @param urlStr 
     * @param textMap 
     * @param fileMap 
     * @return 
     */  
    public static String formUpload(String urlStr, Map<String, String> textMap,  
            Map<String, String> fileMap) {  
	        String res = "";  
	        HttpURLConnection conn = null;  
	        String BOUNDARY = "----WebKitFormBoundaryFCg4KhjGeQbzlgiR"; //boundary就是request头和上传文件内容的分隔符  
	        try {  
	            URL url = new URL(urlStr);  
	            conn = (HttpURLConnection) url.openConnection();  
	            conn.setConnectTimeout(5000);  
	            conn.setReadTimeout(30000);  
	            conn.setDoOutput(true);  
	            conn.setDoInput(true);  
	            conn.setUseCaches(false);  
	            conn.setRequestMethod("POST");  
	            conn.setRequestProperty("Connection", "Keep-Alive");  
	            conn  
	                    .setRequestProperty("User-Agent",  
	                            "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");  
	            conn.setRequestProperty("Content-Type",  
	                    "multipart/form-data; boundary=" + BOUNDARY);  
	  
	            OutputStream out = new DataOutputStream(conn.getOutputStream());  
	            // text  
	            if (textMap != null) {  
	                StringBuffer strBuf = new StringBuffer();  
	                Iterator iter = textMap.entrySet().iterator();  
	                while (iter.hasNext()) {  
	                    Map.Entry entry = (Map.Entry) iter.next();  
	                    String inputName = (String) entry.getKey();  
	                    String inputValue = (String) entry.getValue();  
	                    if (inputValue == null) {  
	                        continue;  
	                    }  
	                    strBuf.append("\r\n").append("--").append(BOUNDARY).append(  
	                            "\r\n");  
	                    strBuf.append("Content-Disposition: form-data; name=\""  
	                            + inputName + "\"\r\n\r\n");  
	                    strBuf.append(inputValue);  
	                }  
	                out.write(strBuf.toString().getBytes());  
	            }  
	  
	            // file  
	            if (fileMap != null) {  
	                Iterator iter = fileMap.entrySet().iterator();  
	                while (iter.hasNext()) {  
	                    Map.Entry entry = (Map.Entry) iter.next();  
	                    String inputName = (String) entry.getKey();  
	                    String inputValue = (String) entry.getValue();  
	                    if (inputValue == null) {  
	                        continue;  
	                    }  
	                    File file = new File(inputValue);  
	                    String filename = file.getName();  
	                    String contentType = new MimetypesFileTypeMap()  
	                            .getContentType(file);  
	                    if (filename.endsWith(".png")) {  
	                        contentType = "image/png";  
	                    }  
	                    if(filename.endsWith(".jpg")) {
	                    	contentType = "image/jpeg";
	                    }
	                    if(filename.endsWith(".jpeg")) {
	                    	contentType = "image/jpeg";
	                    }
	                    if(filename.endsWith(".gif")) {
	                    	contentType = "image/gif";
	                    }
	                    if (contentType == null || contentType.equals("")) {  
	                        contentType = "application/octet-stream";  
	                    }  
	  
	                    StringBuffer strBuf = new StringBuffer();  
	                    strBuf.append("\r\n").append("--").append(BOUNDARY).append(  
	                            "\r\n");  
	                    strBuf.append("Content-Disposition: form-data; name=\""  
	                            + inputName + "\"; filename=\"" + filename  
	                            + "\"\r\n");  
	                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");  
	                    System.out.println(strBuf);
	                    out.write(strBuf.toString().getBytes());  
	  
	                    DataInputStream in = new DataInputStream(  
	                            new FileInputStream(file));  
	                    int bytes = 0;  
	                    byte[] bufferOut = new byte[1024];  
	                    while ((bytes = in.read(bufferOut)) != -1) {  
	                        out.write(bufferOut, 0, bytes);  
	                    }  
	                    in.close();  
	                }  
	            }  
	  
	            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();  
	            out.write(endData);  
	            out.flush();  
	            out.close();  
	  
	            // 读取返回数据  
	            StringBuffer strBuf = new StringBuffer();  
	            BufferedReader reader = new BufferedReader(new InputStreamReader(  
	                    conn.getInputStream()));  
	            String line = null;  
	            while ((line = reader.readLine()) != null) {  
	                strBuf.append(line).append("\n");  
	            }  
	            res = strBuf.toString();  
	            reader.close();  
	            reader = null;  
	        } catch (Exception e) {  
	            System.out.println("发送POST请求出错。" + urlStr);  
	            e.printStackTrace();  
	            return null;
	        } finally {  
	            if (conn != null) {  
	                conn.disconnect();  
	                conn = null;  
	            }  
	        }  
	        return res;  
	    }  
	
	/**
	 * 
	 * @throws Exception
	 */
    private static void trustAllHttpsCertificates() throws Exception {

        //  Create a trust manager that does not validate certificate chains:

        javax.net.ssl.TrustManager[] trustAllCerts =
                new javax.net.ssl.TrustManager[1];

        javax.net.ssl.TrustManager tm = new miTM();

        trustAllCerts[0] = tm;

        javax.net.ssl.SSLContext sc =
                javax.net.ssl.SSLContext.getInstance("SSL");

        sc.init(null, trustAllCerts, null);

        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(
                sc.getSocketFactory());

    }

    public static class miTM implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {
        @Override
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        @Override
		public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType) throws
                java.security.cert.CertificateException {
            return;
        }

        @Override
		public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType) throws
                java.security.cert.CertificateException {
            return;
        }
    }
    
    private static class TrustAnyTrustManager implements X509TrustManager {
    	 
        @Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
 
        @Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
 
        @Override
		public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }
    
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
		public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
 
}

