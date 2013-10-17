package br.com.geekie.gansht.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

@SuppressLint("UseSparseArrays")
@SuppressWarnings("unchecked")
public class LoginSender extends AsyncTask<Object, Object, HttpResponse> {

	private static final String serverURI = "http://gansht.herokuapp.com/login";
	private Login loginActivity;
	
	public void doLogin(Login activity, String username, String password) {
		this.loginActivity = activity;
		List<NameValuePair> contents = new ArrayList<NameValuePair>();
		contents.add(new BasicNameValuePair("username", username));
		contents.add(new BasicNameValuePair("password", password));
		this.execute(contents);
	}

	@Override
	protected HttpResponse doInBackground(Object... contents) {		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(serverURI);

		try {
			post.setEntity(new UrlEncodedFormEntity((List<NameValuePair>) contents[0]));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
				
		try {
			return httpClient.execute(post);
		} catch (IOException e) {
			return null;
		}		
	}
	
	@Override
	protected void onPostExecute(HttpResponse response) {
		try {
			String auth = EntityUtils.toString(response.getEntity());
			System.out.println("Auth: " + auth);			
			boolean success = false;
			this.loginActivity.loginSuccess(success);
		} catch (Exception ex) {
			ex.printStackTrace();
			this.loginActivity.loginSuccess(false);
		} 
	}
}
