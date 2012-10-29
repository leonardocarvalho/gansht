package br.com.geekie.gansht;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class PictureController extends Activity {

	public static final int PHOTO_REQUEST_CODE = 3479;
	private static int SUCCESS = 200;
	
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
    	startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), PHOTO_REQUEST_CODE);
    }
    
    private String getSelectedTest() {
    	RadioGroup testGroup = (RadioGroup) findViewById(R.id.selected_test); 
    	int testId = testGroup.getCheckedRadioButtonId();
    	RadioButton test = (RadioButton) testGroup.findViewById(testId);
    	return test.getText().toString();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (requestCode != PHOTO_REQUEST_CODE)	return;
 
    	if (resultCode == RESULT_OK) {    		
    		String testGroup = ((EditText) findViewById(R.id.test_group)).getText().toString();
    		EditText userId = (EditText) findViewById(R.id.user_id);
    		String testName = getSelectedTest();
    		 
    		new PhotoSender().sendImage(
    			this,
    			new PhotoEncoder().extractImage((Bitmap) data.getExtras().get("data")), 
    			testGroup, 
    			userId.getText().toString(),
    			testName
    		);    		
    	} else {
    		((TextView) findViewById(R.id.result_feedback)).setText("Picture not taken");
    	}
    }
    
    public void photoPosted(int statusCode) {
    	TextView feedback = (TextView) findViewById(R.id.result_feedback);
    	if (statusCode == SUCCESS) {
    		feedback.setText("Data saved");
    	} else {
    		feedback.setText("Failure sending to server. Return code: " + statusCode);
    	}
    }
}
