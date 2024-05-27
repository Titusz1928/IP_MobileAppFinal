package com.example.ip_mobileapp.ui.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ip_mobileapp.R;
import com.example.ip_mobileapp.databinding.FragmentLoginBinding;
import com.example.ip_mobileapp.ui.Registration.RegistrationFragment;
import com.example.ip_mobileapp.ui.Sensors.SensorFragment;

public class LoginFragment extends Fragment{

    private FragmentLoginBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button toRegistration = binding.LOGifcvRegisterButton;

        TextView toResetPassword1 = binding.LOGifcvForgotPasswordText;

        toRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Using NavController to navigate
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.to_nav_registration);
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
