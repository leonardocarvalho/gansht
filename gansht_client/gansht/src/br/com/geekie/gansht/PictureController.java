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

	private static int PHOTO_REQUEST_CODE = 3479;
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
    	if (requestCode != PHOTO_REQUEST_CODE) return;
    	
    	String feedback;
    	if (resultCode == RESULT_OK) {    		
    		String testGroup = ((EditText) findViewById(R.id.test_group)).getText().toString();
    		EditText userId = (EditText) findViewById(R.id.user_id);
    		String testName = this.getSelectedTest();
    		 
    		int statusCode = PhotoSender.sendImage(
    			new PhotoEncoder().extractImage((Bitmap) data.getExtras().get("data")), 
    			testGroup, 
    			userId.getText().toString(),
    			testName
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
