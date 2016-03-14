package spbau.mit.divan.foodhunter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.net.UserInfo;

import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.showToast;
import static spbau.mit.divan.foodhunter.net.UserInfo.EMPTY_LOGIN;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        if (!UserInfo.getLogin(this).equals(EMPTY_LOGIN)) {
            goToMainMenu();
        }

    }

    public void onSignInClick(View view) {
        String login = ((EditText)findViewById(R.id.loginText)).getText().toString();
        if (login.equals(EMPTY_LOGIN)) {
            showToast(getApplicationContext(), getResources().getString(R.string.incorrect_login));
        } else {
            UserInfo.setLogin(this, login);
            goToMainMenu();
        }
    }

    private void goToMainMenu() {
        Intent intent = new Intent(Login.this, MainMenu.class);
        startActivity(intent);
        finish();
    }
}
