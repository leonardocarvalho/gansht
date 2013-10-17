package br.com.geekie.gansht.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import br.com.geekie.gansht.R;

public class Login extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_picture_controller, menu);
        return true;
    }
    
    public void submitLogin(View view) {
    	EditText username = (EditText) findViewById(R.id.username);
    	EditText password = (EditText) findViewById(R.id.password);
    	new LoginSender().doLogin(this, username.getText().toString(), password.getText().toString());
    }
    
    public void loginSuccess(boolean success) {
    	//System.out.println("Callback " + success);
    }
	
}
