package br.com.geekie.gansht.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Base64;

@SuppressLint("UseSparseArrays")
@SuppressWarnings("unchecked")
public class PhotoSender extends AsyncTask<Object, Object, HttpResponse> {

    private static final String serverURI = "http://gansht.herokuapp.com/upload_data";
    private static Random random = new Random();
    private static HashMap<Integer, PictureController> waitingActivities = new HashMap<Integer, PictureController>();

    public void sendImage(PictureController postRequester, byte[] imageData, String testGroup, String userId, String testName, String authToken) {
        Integer postId = random.nextInt();
        List<NameValuePair> contents = new ArrayList<NameValuePair>();
        contents.add(new BasicNameValuePair("post_id", postId.toString()));
        contents.add(new BasicNameValuePair("user_id", userId));
        contents.add(new BasicNameValuePair("test_id", testId));
        contents.add(new BasicNameValuePair("image", Base64.encodeToString(imageData, Base64.DEFAULT)));
        contents.add(new BasicNameValuePair("auth", authToken));

        waitingActivities.put(postId, postRequester);
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line);

            String[] params = stringBuilder.toString().split(";");
            Integer postId = Integer.valueOf(params[0]);

            waitingActivities.get(postId).photoPosted(response.getStatusLine().getStatusCode());
            waitingActivities.remove(postId);
        } catch (Exception ex) {
            ex.printStackTrace();
            waitingActivities.values().iterator().next().photoPosted(500);
        }
    }
}
