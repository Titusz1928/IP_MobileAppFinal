package com.example.ip_mobileapp.ui.ResetPassword;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ip_mobileapp.R;
import com.example.ip_mobileapp.databinding.FragmentRegistrationBinding;
import com.example.ip_mobileapp.databinding.FragmentResetPassword1Binding;


public class ResetPassword1Fragment extends Fragment {

    private FragmentResetPassword1Binding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentResetPassword1Binding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}