package com.example.ip_mobileapp.ui.Registration;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ip_mobileapp.R;
import com.example.ip_mobileapp.databinding.FragmentLoginBinding;
import com.example.ip_mobileapp.databinding.FragmentRegistrationBinding;


public class RegistrationFragment extends Fragment {

    private FragmentRegistrationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentRegistrationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}