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
import com.example.ip_mobileapp.databinding.FragmentResetPassword1Binding;
import com.example.ip_mobileapp.databinding.FragmentResetPassword2Binding;
import com.example.ip_mobileapp.ui.Login.LoginFragment;


public class ResetPassword2Fragment extends Fragment {

    private FragmentResetPassword2Binding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentResetPassword2Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button toLogin = binding.RPA2ifcvBackButton;
        Button toResetPassword3 = binding.RPA2ifcvConfirmButton;


        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity) getActivity()).switchFragment(new LoginFragment());
            }
        });

        toResetPassword3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity) getActivity()).switchFragment(new ResetPassword3Fragment());
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