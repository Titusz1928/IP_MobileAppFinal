package com.example.ip_mobileapp.ui.Chat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ip_mobileapp.LoginActivity;
import com.example.ip_mobileapp.Model.EmailNamePair;
import com.example.ip_mobileapp.Model.User;
import com.example.ip_mobileapp.Model.UserSession;
import com.example.ip_mobileapp.R;
import com.example.ip_mobileapp.databinding.FragmentChatBinding;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;

    String url;
    private static final String TAG = "MyTag";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //declaring buttons
        ImageView searchButton = binding.strImageView;
        EditText searchInputField = binding.strEditText;

        LinearLayout parentLayout = binding.cltrLinearLayout;


        //LOADING IN EXISTING CHATS
        url = getString(R.string.CLOUD_SERVER2) + getString(R.string.URLchats);
        JSONObject userData = new JSONObject();

        UserSession userSession = UserSession.getInstance(requireActivity());
        User user = userSession.getUser();

        Integer userId = user.getId();
        if(userId!=null){
            try{
                new Thread(() -> {
                    try {
                        RestTemplate restTemplate = new RestTemplate();
                        Map<String, Integer> requestParams = new HashMap<>();
                        requestParams.put("id", userId);
                        Log.d("MyTag","userId: "+String.valueOf(user.getId()));
                        String url = getString(R.string.CLOUD_SERVER2) + getString(R.string.URLchats);
                        Log.d("MyTag","url: "+url);

                        // Using ParameterizedTypeReference to handle generic types
                        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                                url,
                                HttpMethod.POST,
                                new HttpEntity<>(requestParams),
                                new ParameterizedTypeReference<Map<String, Object>>() {});

                               if (responseEntity.getStatusCode().is2xxSuccessful()) {
                                   List<EmailNamePair> chatHistory = createEmailNamePairList(responseEntity.getBody());
                                   Log.d(TAG,chatHistory.toString());

                                   Handler handler = new Handler(Looper.getMainLooper());
                                   handler.post(() -> {
                                               parentLayout.removeAllViews();
//                                       Sort emailNamePairsExisting list by date
//                                       we do this so that the newest message appears first
                                       Collections.sort(chatHistory, new Comparator<EmailNamePair>() {
                                           @Override
                                           public int compare(EmailNamePair pair1, EmailNamePair pair2) {
                                               return pair2.getDate().compareTo(pair1.getDate());
                                           }
                                       });

                                       //we go through the list
                                       for (EmailNamePair pair : chatHistory) {
                                           // Create a CardView for each pair and add it to the parent LinearLayout
                                           CardView cardView = createCardViewExisting(pair.getEmail(), pair.getPrenume(), pair.getDate(), pair.getLast_msg(), pair.getId_conv());
                                           parentLayout.addView(cardView);
                                           Log.d(TAG, "object created: " + pair.toString());

                                       }
                                   });


                               }




                    }catch (Exception e) {
                        Log.e("MyTag", "Error fetching chats", e);
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(() -> Toast.makeText(getActivity(), getString(R.string.ERROR), Toast.LENGTH_LONG).show());
                    }
                }).start();
            }catch (Exception e) {
                // Handle exceptions
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    Toast.makeText(getActivity(), getString(R.string.ERROR),
                            Toast.LENGTH_LONG).show();
                });
            }
        }else{
                redirectToLogin();
        }
        Log.d(TAG, userData.toString());




            //executed when search button is pressed
            searchButton.setOnClickListener(v -> {

                url = getString(R.string.CLOUD_SERVER2)+getString(R.string.URLsearch);

                String search = searchInputField.getText().toString();
                if(userId!=null){
                    try{
                        new Thread(() -> {
                            try {
                                RestTemplate restTemplate = new RestTemplate();
                                Map<String, Object> requestParams = new HashMap<>();
                                requestParams.put("searched_email", search);
                                requestParams.put("user_id", userId);
                                Log.d("MyTag","userId: "+String.valueOf(user.getId()));
                                String url = getString(R.string.CLOUD_SERVER2) + getString(R.string.URLsearch);
                                Log.d("MyTag","url: "+url);

                                // Using ParameterizedTypeReference to handle generic types
                                ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                                        url,
                                        HttpMethod.POST,
                                        new HttpEntity<>(requestParams),
                                        new ParameterizedTypeReference<Map<String, Object>>() {});

                                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                                    List<EmailNamePair> chatsFound = createEmailNamePairList(responseEntity.getBody());
                                    Log.d(TAG,chatsFound.toString());

                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(() -> {
                                        parentLayout.removeAllViews();
//                                       Sort emailNamePairsExisting list by date
//                                       we do this so that the newest message appears first
                                        Collections.sort(chatsFound, new Comparator<EmailNamePair>() {
                                            @Override
                                            public int compare(EmailNamePair pair1, EmailNamePair pair2) {
                                                return pair2.getDate().compareTo(pair1.getDate());
                                            }
                                        });

                                        //we go through the list
                                        for (EmailNamePair pair : chatsFound) {
                                            // Create a CardView for each pair and add it to the parent LinearLayout
                                            CardView cardView = createCardViewExisting(pair.getEmail(), pair.getPrenume(), pair.getDate(), pair.getLast_msg(), pair.getId_conv());
                                            parentLayout.addView(cardView);
                                            Log.d(TAG, "object created: " + pair.toString());

                                        }
                                    });


                                }




                            }catch (Exception e) {
                                Log.e("MyTag", "Error fetching chats", e);
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(() -> Toast.makeText(getActivity(), getString(R.string.ERROR), Toast.LENGTH_LONG).show());
                            }
                        }).start();
                    }catch (Exception e) {
                        // Handle exceptions
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(() -> {
                            Toast.makeText(getActivity(), getString(R.string.ERROR),
                                    Toast.LENGTH_LONG).show();
                        });
                    }
                }else{
                    redirectToLogin();
                }
                Log.d(TAG, userData.toString());

            });


            return root;
        }

    private List<EmailNamePair> createEmailNamePairList(Map<String, Object> serverResponse) throws JSONException {
        List<String> emailArray = (List<String>) serverResponse.get("email_values");
        List<String> prenumeArray = (List<String>) serverResponse.get("prenume_values");
        List<String> dateArray = (List<String>) serverResponse.get("data_values");
        List<String> lastMsgArray = (List<String>) serverResponse.get("last_msg_values");
        List<String> idConvArray = (List<String>) serverResponse.get("id_conv_values");

        // Create a list containing all the arrays
        return parseLists(emailArray, prenumeArray, dateArray, lastMsgArray, idConvArray);
    }

    private List<EmailNamePair> parseLists(List<String> emailArray, List<String> prenumeArray, List<String> dateArray, List<String> lastMsgArray, List<String> idConvArray) {
        List<EmailNamePair> emailNamePairs = new ArrayList<>();

        Integer length = Math.min(emailArray.size(), prenumeArray.size());

        for (int i = 0; i < length; i++) {
            String email = emailArray.get(i);
            String prenume = prenumeArray.get(i);
            String date = String.valueOf(dateArray.get(i));
            String lastMsg = String.valueOf(lastMsgArray.get(i));
            String idConv = String.valueOf(idConvArray.get(i));

            // Truncate lastMsg to 10 characters and add ellipsis if there was truncation
            if (lastMsg.length() > 10) {
                lastMsg = lastMsg.substring(0, 10) + "...";
            }

            EmailNamePair emailNamePair = new EmailNamePair(email, prenume, date, lastMsg, idConv);
            emailNamePairs.add(emailNamePair);
        }

        return emailNamePairs;
    }



//        @NonNull
//        private static Message getFirstMessage (Chat chat){
//            Message firstMessage = null;
//            List<Message> messages = chat.getMessages();
//
//            // Check if the messages list is not empty and contains at least one message
//            if (messages != null && !messages.isEmpty()) {
//                // Get the first message from the list
//                firstMessage = messages.get(0);
//            }
//
//            // If firstMessage is still null, set a default message
//            if (firstMessage == null) {
//                // Create a default message with empty content and sendingDate
//                firstMessage = new Message();
//                firstMessage.setContent("");
//            }
//            return firstMessage;
//        }


    private CardView createCardViewExisting(String email, String prenume, String date, String lastMsg, String id_conv) {
        CardView cardView = new CardView(requireContext());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                950, // Width
                300  // Height
        );
        cardView.setRadius(30);
        layoutParams.setMargins(16, 16, 16, 16); // Left, top, right, bottom margins
        cardView.setLayoutParams(layoutParams);

        //cardView.setCardCornerRadius(30); // Card corner radius
        cardView.setCardBackgroundColor(Color.WHITE); // Card background color
        cardView.setContentPadding(10, 10, 10, 10); // Card content padding

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("prenume", prenume);
                //we pass the email, prenume and id_conv to the SelectedChat activity
                bundle.putString("email", email);
                bundle.putString("prenume", prenume);
                bundle.putString("id_conv",id_conv);

                // Navigate to the target fragment with the data
                NavHostFragment.findNavController(ChatFragment.this)
                        .navigate(R.id.to_selected_chat, bundle);
            }
        });


        // Create a ConstraintLayout for the card view
        ConstraintLayout constraintLayout = new ConstraintLayout(requireContext());

        // Set constraint layout properties
        ConstraintLayout.LayoutParams constraintLayoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        );
        constraintLayout.setLayoutParams(constraintLayoutParams);

        cardView.addView(constraintLayout);

        // Create and add TextView for prenume to the ConstraintLayout
        TextView textPrenume = new TextView(requireContext());
        textPrenume.setId(View.generateViewId());
        textPrenume.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));
        textPrenume.setText(prenume);
        textPrenume.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
        textPrenume.setTextColor(Color.BLACK);
        constraintLayout.addView(textPrenume);

        // Set constraints for prenume TextView
        ConstraintSet constraintSetPrenume = new ConstraintSet();
        constraintSetPrenume.clone(constraintLayout);
        constraintSetPrenume.connect(textPrenume.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSetPrenume.connect(textPrenume.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSetPrenume.applyTo(constraintLayout);

        if(!date.equals("-1")) {
            // Create and add TextView for email to the ConstraintLayout
            TextView textLastMsg = new TextView(requireContext());
            textLastMsg.setId(View.generateViewId());
            textLastMsg.setLayoutParams(new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            ));
            textLastMsg.setText(lastMsg);
            textLastMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textLastMsg.setTextColor(Color.BLACK);
            constraintLayout.addView(textLastMsg);

            // Set constraints for email TextView
            ConstraintSet constraintSetLastmsg = new ConstraintSet();
            constraintSetLastmsg.clone(constraintLayout);
            constraintSetLastmsg.connect(textLastMsg.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSetLastmsg.connect(textLastMsg.getId(), ConstraintSet.TOP, textPrenume.getId(), ConstraintSet.BOTTOM);
            constraintSetLastmsg.applyTo(constraintLayout);

            // Create and add TextView for date to the ConstraintLayout
            TextView textDate = new TextView(requireContext());
            textDate.setId(View.generateViewId());
            textDate.setLayoutParams(new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            ));
            textDate.setText(date);
            textDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textDate.setTextColor(Color.GRAY);
            constraintLayout.addView(textDate);
            // Set constraints for date TextView
            ConstraintSet constraintSetDate = new ConstraintSet();
            constraintSetDate.clone(constraintLayout);
            constraintSetDate.connect(textDate.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END); // Align to the right side of the ConstraintLayout
            constraintSetDate.connect(textDate.getId(), ConstraintSet.TOP, textLastMsg.getId(), ConstraintSet.BOTTOM); // Position below textEmail
            constraintSetDate.applyTo(constraintLayout);
        }

        // Create and add TextView for email to the ConstraintLayout
        TextView textEmail = new TextView(requireContext());
        textEmail.setId(View.generateViewId());
        textEmail.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));
        textEmail.setText(email);
        textEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textEmail.setTextColor(Color.GRAY);
        constraintLayout.addView(textEmail);

        // Set constraints for email TextView
        ConstraintSet constraintSetEmail = new ConstraintSet();
        constraintSetEmail.clone(constraintLayout);
        constraintSetEmail.connect(textEmail.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSetEmail.connect(textEmail.getId(), ConstraintSet.TOP, textPrenume.getId(), ConstraintSet.BOTTOM);
        constraintSetEmail.applyTo(constraintLayout);



        return cardView;
    }


        @Override
        public void onDestroyView () {
            super.onDestroyView();
            binding = null;
        }
        private void redirectToLogin() {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                Toast.makeText(getActivity(), getString(R.string.ERROR),
                        Toast.LENGTH_SHORT).show();
            });
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

