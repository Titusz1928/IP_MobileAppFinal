package com.example.ip_mobileapp.ui.Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ip_mobileapp.LoginActivity;
import com.example.ip_mobileapp.MainActivity;
import com.example.ip_mobileapp.Model.User;
import com.example.ip_mobileapp.R;
import com.example.ip_mobileapp.databinding.FragmentLoginBinding;
import com.example.ip_mobileapp.ui.Registration.RegistrationFragment;
import com.example.ip_mobileapp.ui.Sensors.SensorFragment;
import com.google.android.material.textfield.TextInputEditText;

import org.springframework.web.client.RestTemplate;

import java.util.Objects;

public class LoginFragment extends Fragment{

    private FragmentLoginBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button toRegistration = binding.LOGifcvRegisterButton;
        Button loginButton = binding.LOGifcvLoginButton;
        TextView toResetPassword1 = binding.LOGifcvForgotPasswordText;

        TextInputEditText cnpText = binding.tilCNP;
        TextInputEditText passwordText = binding.tilPassword;

        toRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.to_nav_registration);

            }
        });

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
                            user = restTemplate.postForObject("@string/CLOUD_SERVER"+"@string/LOGIN",
                                    user, User.class);

                            if (Objects.equals(user.getId(), -1)) {
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(() -> {
                                    Toast.makeText(getActivity(), "Datele sunt incorecte",
                                            Toast.LENGTH_SHORT).show();
                                });
                            } else {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    } catch (Exception e) {
                        // Handle exceptions
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(() -> {
                            Toast.makeText(getActivity(), "An error occurred: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        });
                    }
                });
                thread.start();
            }
        });

        toResetPassword1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Using NavController to navigate
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.to_nav_reset_password);
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
