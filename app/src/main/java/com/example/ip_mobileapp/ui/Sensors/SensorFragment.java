package com.example.ip_mobileapp.ui.Sensors;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ip_mobileapp.R;
import com.example.ip_mobileapp.databinding.FragmentFisaMedicalaBinding;
import com.example.ip_mobileapp.databinding.FragmentSensorBinding;


public class SensorFragment extends Fragment {
    private FragmentSensorBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSensorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        CardView toActualizare = binding.llActualizareCardView;
        CardView toVisualization = binding.llVizualizareCardView;

        toActualizare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Using NavController to navigate
                NavHostFragment.findNavController(SensorFragment.this)
                        .navigate(R.id.to_nav_actualization);
            }
        });

        toVisualization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Using NavController to navigate
                NavHostFragment.findNavController(SensorFragment.this)
                        .navigate(R.id.to_nav_visualization);
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