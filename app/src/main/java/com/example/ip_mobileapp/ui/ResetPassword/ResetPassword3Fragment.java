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
import com.example.ip_mobileapp.databinding.FragmentResetPassword3Binding;
import com.example.ip_mobileapp.ui.Login.LoginFragment;


public class ResetPassword3Fragment extends Fragment {

    private FragmentResetPassword3Binding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentResetPassword3Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button toLogin = binding.RPA3ifcvBackButton;
        Button toLoginWithReset = binding.RPA3ifcvConfirmButton;


        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity) getActivity()).switchFragment(new LoginFragment());
            }
        });

        toLoginWithReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity) getActivity()).switchFragment(new LoginFragment());
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