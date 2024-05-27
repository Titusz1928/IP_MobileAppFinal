package com.example.ip_mobileapp.ui.SelectedChat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ip_mobileapp.R;
import com.example.ip_mobileapp.databinding.FragmentChatBinding;
import com.example.ip_mobileapp.databinding.FragmentSelectedChatBinding;


public class SelectedChatFragment extends Fragment {

    private FragmentSelectedChatBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSelectedChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}