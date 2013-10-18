package br.com.geekie.gansht.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import br.com.geekie.gansht.R;
import br.com.geekie.gansht.core.PictureController;

public class Login extends Activity {

    public static final String loginAuthKey = "loginAuth";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isLogged()) {
            this.nextActivity();
        } else {
            setContentView(R.layout.login);
        }
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

    public void loginFailure() {
        ((TextView) findViewById(R.id.login_failure)).setText("Falha. Verifique se usuário e senha conferem.");
    }

    public void loginSuccess(String auth) {
        ((TextView) findViewById(R.id.login_failure)).setText("");
        SharedPreferences preferences = getSharedPreferences("gansht", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(loginAuthKey, auth);
        editor.commit();

        this.nextActivity();
    }

    private void nextActivity() {
        Intent i = new Intent(this, PictureController.class);
        startActivity(i);
    }

    private boolean isLogged() {
    	SharedPreferences preferences = getSharedPreferences("gansht", 0);
        String auth = preferences.getString(loginAuthKey, null);
        return auth != null;
    }
 }
