package br.com.geekie.gansht.core;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import br.com.geekie.gansht.R;
import br.com.geekie.gansht.login.Login;

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
            String testId = ((EditText) findViewById(R.id.test_id)).getText().toString();
            EditText userId = (EditText) findViewById(R.id.user_id);
            String testName = getSelectedTest();
            SharedPreferences preferences = getSharedPreferences("gansht", 0);
            String authToken = preferences.getString(Login.loginAuthKey, null);

            new PhotoSender().sendImage(
                this,
                new PhotoEncoder().extractImage((Bitmap) data.getExtras().get("data")),
                userId.getText().toString(),
                testId,
                testName,
                authToken
            );
        } else {
            ((TextView) findViewById(R.id.result_feedback)).setText("Foto não foi tirada");
        }
    }

    public void photoPosted(int statusCode) {
        TextView feedback = (TextView) findViewById(R.id.result_feedback);
        if (statusCode == SUCCESS) {
            feedback.setText("Foto salva");
            
            EditText userIdField = (EditText) findViewById(R.id.user_id);
            userIdField.setText("");
            userIdField.requestFocus();
        } else {
            feedback.setText("Falha ao enviar informações. Código de retorno: " + statusCode);
        }
    }
}
