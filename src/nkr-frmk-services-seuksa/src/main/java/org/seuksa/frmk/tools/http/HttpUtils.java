/**
 * 
 */
package org.seuksa.frmk.tools.http;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.seuksa.frmk.tools.log.Log;

/**
 * @author malin
 * @version $Revision: 57 $
 */
public class HttpUtils {

	/**
	 * 
	 * @param url
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static HttpResponse httpConnect(String url) throws HttpException, IOException {
		return httpConnect(url, null, null, null);
	}
	/**
	 * 
	 * @param url
	 * @param content
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static HttpResponse httpConnect(String url, String content) throws HttpException, IOException {
		return httpConnect(url, content, null, null);
	}

	/**
	 * 
	 * @param url
	 * @param requestData
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static HttpResponse httpConnect(
										String url, 
										String content, 
										String contentType, 
										String charset)
			throws HttpException, IOException {
		Log.getInstance(HttpUtils.class).enter("httpConnect - start"
				+ "\nURL : " + url
				+ "\ncontent : " + content
				+ "\ncontentType : " + contentType
				+ "\ncharset : " + charset
				);
		
		PostMethod post = null;
		
		try {
			post = new PostMethod(url);
	
			if (content == null) {
				content = "";
			}
			// ISO-8859-1 
			// UTF-8
//			RequestEntity entity = new StringRequestEntity(content, "text/xml", "ISO-8859-1");
			RequestEntity entity = new StringRequestEntity(content, contentType, charset);
			post.setRequestEntity(entity);

			HttpClient httpclient = new HttpClient();

			Log.getInstance(HttpUtils.class).debug("Execute method POST ..");
			httpclient.executeMethod(post);

			// Display status code
			Log.getInstance(HttpUtils.class).debug("Response status code: " + post.getStatusCode());
			Log.getInstance(HttpUtils.class).debug("Response charset: " + post.getResponseCharSet());
			
			
			HttpResponse response = new HttpResponse(post);

			return response;
		} finally {
			// Release current connection to the connection pool once done
			if (post != null) {
				post.releaseConnection();
			}
			Log.getInstance(HttpUtils.class).exit("httpConnect - end");
		}

	}
}
