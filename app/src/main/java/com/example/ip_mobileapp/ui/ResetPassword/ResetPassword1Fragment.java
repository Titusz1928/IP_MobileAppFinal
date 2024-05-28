package com.example.ip_mobileapp.ui.ResetPassword;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ip_mobileapp.LoginActivity;
import com.example.ip_mobileapp.R;
import com.example.ip_mobileapp.databinding.FragmentRegistrationBinding;
import com.example.ip_mobileapp.databinding.FragmentResetPassword1Binding;
import com.example.ip_mobileapp.ui.Login.LoginFragment;


public class ResetPassword1Fragment extends Fragment {

    private FragmentResetPassword1Binding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentResetPassword1Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button toLogin = binding.RPAifcvBackButton;
        Button toResetPassword2 = binding.RPAifcvConfirmButton;


        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity) getActivity()).switchFragment(new LoginFragment());
            }
        });

        toResetPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity) getActivity()).switchFragment(new ResetPassword2Fragment());
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