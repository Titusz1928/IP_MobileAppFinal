package com.example.ip_mobileapp.ui.Alarms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ip_mobileapp.databinding.FragmentAlarmBinding;


public class AlarmFragment extends Fragment {

    private FragmentAlarmBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentAlarmBinding.inflate(inflater, container, false);
        View root = binding.getRoot();




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}