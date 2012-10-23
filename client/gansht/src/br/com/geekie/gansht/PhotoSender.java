package br.com.geekie.gansht;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Base64;

public class PhotoSender {

	private static String serverURI = "localhost:5000/upload_data";
	
	public static int sendImage(byte[] imageData, String testGroup, String userId) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(serverURI);
		
		List<NameValuePair> contents = new ArrayList<NameValuePair>();
		contents.add(new BasicNameValuePair("user_id", userId));
		contents.add(new BasicNameValuePair("test_group", testGroup));
		contents.add(new BasicNameValuePair("image", Base64.encodeToString(imageData, Base64.DEFAULT)));
		
		try {
			post.setEntity(new UrlEncodedFormEntity(contents));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return 400;
		}
		
		return 200;
		
//		try {
//			HttpResponse response = httpClient.execute(post);
//			return response.getStatusLine().getStatusCode();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return 500;
//		}
	}
	
}
