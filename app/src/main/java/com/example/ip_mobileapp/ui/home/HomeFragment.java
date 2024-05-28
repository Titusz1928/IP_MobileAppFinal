package com.example.ip_mobileapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ip_mobileapp.Model.User;
import com.example.ip_mobileapp.Model.UserSession;
import com.example.ip_mobileapp.R;
import com.example.ip_mobileapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        UserSession userSession = UserSession.getInstance(requireActivity());
        User user = userSession.getUser();

        if (user != null) {
            String userName = user.getFirstName();

            // Generate the welcome message based on the user's name
            String welcomeMessage = getString(R.string.HOMtcvGreeetingTextView, userName);

            // Set the welcome message to the TextView
            TextView welcomeText = binding.tcvGreetingTextView;
            welcomeText.setText(welcomeMessage);
        }


        ImageView infoButton = binding.HOMInfoImageView;
        TextView infoText = binding.icvDescriptionTextView;

        infoButton.setOnClickListener(v -> {
            if (infoText.getVisibility() == View.GONE) {
                infoText.setVisibility(View.VISIBLE);
                infoButton.setImageResource(R.drawable.ic_minimize);
            } else {
                infoText.setVisibility(View.GONE);
                infoButton.setImageResource(R.drawable.ic_expand);
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