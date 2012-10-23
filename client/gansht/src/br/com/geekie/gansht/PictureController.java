package br.com.geekie.gansht;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class PictureController extends Activity {

	private static int PHOTO_REQUEST_CODE = 3479;
	private static int SUCCESS = 200;
	
	private Uri photoUri;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_controller);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_picture_controller, menu);
        return true;
    }
    
    public void takePhoto(View view) {
    	File photo = new File(Environment.getExternalStorageDirectory(), "last_test.jpg");
    	Intent startCamera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    	photoUri = Uri.fromFile(photo);
    	startCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
    	startActivityForResult(startCamera, PHOTO_REQUEST_CODE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (requestCode != PHOTO_REQUEST_CODE) return;
    	
    	String feedback;
    	if (resultCode == RESULT_OK) {
    		String testGroup = ((EditText) findViewById(R.id.test_group)).getText().toString();
    		EditText userId = (EditText) findViewById(R.id.user_id);
    		 
    		int statusCode = PhotoSender.sendImage(
    			new PhotoExtractor().extractImage(photoUri), testGroup, userId.getText().toString()
    		);
    		
    		if (statusCode == SUCCESS) {
    			feedback = "Sent data for " + testGroup + ": " + userId.getText().toString();
        		userId.setText("");
    		} else {
    			feedback = "Failure sending to server. Return code: " + statusCode;
    		}   		
    	} else {
    		feedback = "Picture not taken";
    	}
    	((TextView) findViewById(R.id.result_feedback)).setText(feedback);
    }
}
