package com.example.ip_mobileapp.ui.ResetPassword;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ip_mobileapp.LoginActivity;
import com.example.ip_mobileapp.Model.User;
import com.example.ip_mobileapp.Model.UserSession;
import com.example.ip_mobileapp.R;
import com.example.ip_mobileapp.databinding.FragmentChangePasswordBinding;
import com.example.ip_mobileapp.databinding.FragmentChatBinding;
import com.example.ip_mobileapp.ui.Sensors.SensorFragment;
import com.example.ip_mobileapp.ui.Settings.SettingsFragment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


public class ChangePasswordFragment extends Fragment {

    private FragmentChangePasswordBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button sendCode = binding.CPAifcvConfirmButton;

        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ChangePasswordFragment.this)
                       .navigate(R.id.to_change_password2);
            }
        });


        /*
            newPasswordBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //CHANGE THE PASSWORD DIRECTLY WITHOUT EMAIL CODE
                    UserSession userSession = UserSession.getInstance(requireActivity());
                    User user = userSession.getUser();
                    Thread thread = new Thread(() -> {
                        try {
                            if (user != null) {
                                Log.d("MyTAg",user.getPassword().toString()+" "+oldPasswordText.getText().toString());
                                if (user.getPassword().equals(oldPasswordText.getText().toString())) {
                                    if (newPasswordText.getText().toString().equals(confirmPasswordText.getText().toString())) {

                                        RestTemplate restTemplate = new RestTemplate();

                                        Map<String, String> requestBody = new HashMap<>();
                                        requestBody.put("emailAddress", user.getEmailAddress());
                                        requestBody.put("newPassword", newPasswordText.getText().toString());

                                        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(
                                                getString(R.string.CLOUD_SERVER) + getString(R.string.CHANGE_PASSWORD),
                                                requestBody, Void.class);

                                        if (responseEntity.getStatusCode().is2xxSuccessful()) {
                                            toastMessage("Parola a fost schimbată cu succes!");
                                        } else {
                                            toastMessage("A apărut o eroare la schimbarea parolei.");
                                        }

                                    } else {
                                        toastMessage("Parolele nu se potrivesc!");
                                    }
                                } else {
                                    toastMessage("Parola este incorectă!");
                                }
                            } else {
                                redirectToLogin();
                            }
                        } catch (Exception e) {
                            // Handle exceptions
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(() -> {
                                Log.d("MyTag", e.getMessage());
                                Toast.makeText(getActivity(), "A apărut o eroare: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            });
                        }
                    });
                    thread.start();
                }

                private void toastMessage(String mesaj) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> {
                        Toast.makeText(getActivity(), mesaj, Toast.LENGTH_LONG).show();
                    });
                }

                private void redirectToLogin() {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> {
                        Toast.makeText(getActivity(), "Eroare!",
                                Toast.LENGTH_SHORT).show();
                    });
                    Intent intent = new Intent(requireActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
            */





        return root;
    }

    private void redirectToLogin() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            Toast.makeText(getActivity(), "Eroare!",
                    Toast.LENGTH_SHORT).show();
        });
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}