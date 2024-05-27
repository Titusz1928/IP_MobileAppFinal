package com.example.ip_mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ip_mobileapp.Model.User;
import com.google.android.material.textfield.TextInputEditText;

import org.springframework.web.client.RestTemplate;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button toRegistration = findViewById(R.id.LOGifcvRegisterButton);
        Button loginButton = findViewById(R.id.LOGifcvLoginButton);
        TextView toResetPassword1 = findViewById(R.id.LOGifcvForgotPasswordText);

        TextInputEditText cnpText = findViewById(R.id.tilCNP);
        TextInputEditText passwordText = findViewById(R.id.tilPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cnpString = cnpText.getText().toString();
                String passwordString = passwordText.getText().toString();

                Thread thread = new Thread(() -> {
                    try {
                        if (cnpString != null && passwordString != null) {
                            User user = new User(cnpString, passwordString);
                            RestTemplate restTemplate = new RestTemplate();
                            Log.d("MyTag", getString(R.string.CLOUD_SERVER) + getString(R.string.LOGIN));

                            user = restTemplate.postForObject(getString(R.string.CLOUD_SERVER) + getString(R.string.LOGIN),
                                    user, User.class);

                            if (Objects.equals(user.getId(), -1)) {
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(() -> {
                                    Toast.makeText(LoginActivity.this, "Datele sunt incorecte",
                                            Toast.LENGTH_SHORT).show();
                                });
                            } else {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    } catch (Exception e) {
                        // Handle exceptions
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(() -> {
                            Toast.makeText(LoginActivity.this, "An error occurred: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        });
                    }
                });
                thread.start();
            }
        });



    }
}