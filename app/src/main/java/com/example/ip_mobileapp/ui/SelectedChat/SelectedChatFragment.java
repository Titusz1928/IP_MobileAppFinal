package com.example.ip_mobileapp.ui.SelectedChat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
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
import com.example.ip_mobileapp.Model.Message;
import com.example.ip_mobileapp.Model.MessageCardView;
import com.example.ip_mobileapp.Model.User;
import com.example.ip_mobileapp.Model.UserSession;
import com.example.ip_mobileapp.R;
import com.example.ip_mobileapp.databinding.FragmentChatBinding;
import com.example.ip_mobileapp.databinding.FragmentSelectedChatBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SelectedChatFragment extends Fragment {

    private FragmentSelectedChatBinding binding;
    String url;
    private static final String TAG = "MyTag";
    private static final long DELAY_MS = 10000;
    private Handler handler = new Handler();
    private long mLastClickTime = 0;
    private NestedScrollView nestedScrollView;
    private LinearLayout parentLayout;
    private String idConv;
    boolean firstLoad=true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSelectedChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        NestedScrollView nestedScrollView = binding.clNestedScrollView;

        String name = getArguments().getString("prenume");
        idConv = getArguments().getString("id_conv");
        Log.d(TAG,"ID CONVERSATIE: "+idConv);
        String receiverEmail = getArguments().getString("email");

        EditText messageToSend = binding.mscvMessageEditText;


        Log.d("MyTag",name);
        TextView nameText = binding.tcvNameText;
        if(name!= null){
            nameText.setText(name);
        }else{
            Toast.makeText(requireContext(), getString(R.string.ERROR), Toast.LENGTH_LONG).show();
        }
        UserSession userSession = UserSession.getInstance(requireActivity());
        User user = userSession.getUser();

        Integer userId = user.getId();
        if(userId!=null) {
            loadMessages(nestedScrollView, parentLayout, idConv);
            handler.postDelayed(loadMessagesTask, DELAY_MS);
        }else{
            redirectToLogin();
        }

        ImageView sendButton = binding.mscvImageView;

        sendButton.setOnClickListener(v -> {
            Log.d(TAG,"sending");
            //used for preventing double clicks
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            new Thread(() -> {
                try {
                    RestTemplate restTemplate = new RestTemplate();
                    Map<String, Object> requestParams = new HashMap<>();
                    requestParams.put("user_id", userId);
                    String message = messageToSend.getText().toString();
                    requestParams.put("message", message);
                    requestParams.put("remail", receiverEmail);
                    Log.d("MyTag", "userId: " + String.valueOf(user.getId()));
                    String urlSend = getString(R.string.CLOUD_SERVER2) + getString(R.string.URLsend);
                    Log.d("MyTag", "url: " + urlSend);

                    ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                            urlSend,
                            HttpMethod.POST,
                            new HttpEntity<>(requestParams),
                            new ParameterizedTypeReference<Map<String, Object>>() {
                            });

                    if (responseEntity.getStatusCode().is2xxSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(() -> {
                            Log.d(TAG, "Response: " + responseEntity.getBody().get("message"));
                            //convert non existing conversation to existing so that it can load the messages
                            if (idConv.equals("-1")) {
                                String idConvNew = String.valueOf(responseEntity.getBody().get("id_conv"));

                                Toast.makeText(requireContext(), "New Conversation started!", Toast.LENGTH_SHORT).show();
                                idConv = idConvNew;
                                Log.d(TAG,"NEW ID CONV VALUE: "+idConv);
                                loadMessages(nestedScrollView, parentLayout, idConv);
                                handler.postDelayed(loadMessagesTask, DELAY_MS);
                            } else {
                                loadMessages(nestedScrollView, parentLayout, idConv);
                            }
                            messageToSend.setText("");
                            loadMessages(nestedScrollView, parentLayout, idConv);
                        });
                    }
                }catch (Exception e) {
                    Log.e("MyTag", "Error fetching chats", e);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> Toast.makeText(getActivity(), getString(R.string.ERROR), Toast.LENGTH_LONG).show());
                }
            }).start();

        });



        return root;
    }

    Runnable loadMessagesTask = new Runnable() {
        @Override
        public void run() {

            Log.d(TAG, "loadMessagesTask running");
            if (idConv != null && !idConv.equals("-1")) {
                Log.d(TAG, "idConv is not null and has a valid value: " + idConv);
                loadMessages(nestedScrollView, parentLayout, idConv); // Call the function to load messages
            } else {
                Log.d(TAG, "idConv is null or empty or equals -1: ");
            }
            handler.postDelayed(this, DELAY_MS); // Schedule the task to run again after the delay
        }
    };

    private void loadMessages(NestedScrollView nestedScrollView, LinearLayout parentLayout, String idConv) {
        Log.d(TAG,"idconv: "+idConv);

        //loading previous messages
        url=getString(R.string.CLOUD_SERVER2)+getString(R.string.URLmessages);

        UserSession userSession = UserSession.getInstance(requireActivity());
        User user = userSession.getUser();

        parentLayout = binding.nsvLinearLayout;

        Integer userId=user.getId();

        if(userId!=null){
            try {
                LinearLayout finalParentLayout = parentLayout;
                new Thread(() -> {
                    try {
                        RestTemplate restTemplate = new RestTemplate();
                        Map<String, Object> requestParams = new HashMap<>();
                        requestParams.put("user_id", userId);
                        requestParams.put("id_conv", idConv);
                        Log.d("MyTag", "userId: " + String.valueOf(user.getId()));
                        String url = getString(R.string.CLOUD_SERVER2) + getString(R.string.URLmessages);
                        Log.d("MyTag", "url: " + url);

                        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                                url,
                                HttpMethod.POST,
                                new HttpEntity<>(requestParams),
                                new ParameterizedTypeReference<Map<String, Object>>() {});

                        Log.d(TAG,responseEntity.getBody().toString());
                        Log.d(TAG,"ok");

                        if (responseEntity.getStatusCode().is2xxSuccessful()) {
                            List<Message> messages= createMessagesList(responseEntity.getBody());
                            Log.d(TAG,messages.toString());

                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(() -> {
                                finalParentLayout.removeAllViews();

                                //for (int i = messageBoxList.size() - 1; i >= 0; i--) {
                                for (Message message : messages) {
                                    //MessageBox messagebox = messageBoxList.get(i);

                                    // Declare cardView variable outside the if-else block
                                    CardView cardView;

                                    // Create a CardView for each pair and add it to the parent LinearLayout
                                    String userIdString = "1";                                                              //!
                                    Log.d(TAG,"[i] Comparing:"+message.getSender_id()+userIdString);

                                    if (message.getSender_id().equals(userIdString)) {
                                        //currenlty logged in user message
                                        cardView = createCardViewRight(message.getContinut(), message.getData());
                                    } else {
                                        //other user message
                                        cardView = createCardViewLeft(message.getContinut(), message.getData());
                                    }

                                    if (firstLoad) {
                                        //puts scorll view to the bottom (always the newest message is at the bottom)
                                        nestedScrollView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                                            }
                                        });
                                        firstLoad = false;
                                    }

                                    finalParentLayout.addView(cardView);
                                    Log.d(TAG, "Object created: " + message.toString());
                                }
                            });

                        }


                    } catch (Exception e) {
                        Log.e("MyTag", getString(R.string.ERROR), e);
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


    }

    private List<Message> createMessagesList(Map<String, Object> serverResponse) throws JSONException {
        List<String> idArray = (List<String>) serverResponse.get("id_sender_values");
        List<String> continutArray = (List<String>) serverResponse.get("continut_values");
        List<String> dateArray = (List<String>) serverResponse.get("data_values");

        // Create a list containing all the arrays
        return parseLists(idArray, continutArray, dateArray);
    }

    private List<Message> parseLists(List<String> idArray, List<String> continutArray, List<String> dateArray) throws JSONException {
        List<Message> messageList = new ArrayList<>();

        int length = Math.min(continutArray.size(), dateArray.size());

        for (int i = 0; i < length; i++) {
            String continut = continutArray.get(i);
            String data = String.valueOf(dateArray.get(i));
            String idSender = String.valueOf(idArray.get(i));

            Message message = new Message(continut,data,idSender);
            messageList.add(message);
        }

        return messageList;
    }

    private CardView createCardViewLeft(String continut, String data) {
        // Create a new CardView
        MessageCardView cardView = new MessageCardView(requireContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, // Width
                ViewGroup.LayoutParams.WRAP_CONTENT  // Height
        );
        layoutParams.setMargins(16, 16, 16, 16);
        cardView.setLayoutParams(layoutParams);
        cardView.setRadius(40);
        cardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBackground));
        cardView.setContentPadding(10, 10, 10, 10);

        // Create a LinearLayout for the card view contents
        LinearLayout linearLayout = new LinearLayout(requireContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // Create the TextView for 'continut'
        TextView textViewContinut = new TextView(requireContext());
        textViewContinut.setText(continut);
        textViewContinut.setTextColor(Color.BLACK);
        textViewContinut.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textViewContinut.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Create the TextView for 'data'
        TextView textViewData = new TextView(requireContext());
        textViewData.setText(data);
        textViewData.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        textViewData.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Add TextViews to LinearLayout
        linearLayout.addView(textViewContinut);
        linearLayout.addView(textViewData);

        // Add LinearLayout to CardView
        cardView.addView(linearLayout);

        // Return the constructed CardView
        return cardView;
    }



    private CardView createCardViewRight(String continut, String data) {
        // Create a new CardView
        MessageCardView cardView = new MessageCardView(requireContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, // Width
                ViewGroup.LayoutParams.WRAP_CONTENT  // Height
        );
        layoutParams.setMargins(16, 16, 16, 16);
        layoutParams.gravity = Gravity.END;
        cardView.setLayoutParams(layoutParams);
        cardView.setRadius(40);
        cardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
        cardView.setContentPadding(10, 10, 10, 10);


        // Create a LinearLayout for the card view contents
        LinearLayout linearLayout = new LinearLayout(requireContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // Create the TextView for 'continut'
        TextView textViewContinut = new TextView(requireContext());
        textViewContinut.setText(continut);
        textViewContinut.setTextColor(Color.BLACK);
        textViewContinut.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textViewContinut.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Create the TextView for 'data'
        TextView textViewData = new TextView(requireContext());
        textViewData.setText(data);
        textViewData.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        textViewData.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Add TextViews to LinearLayout
        linearLayout.addView(textViewContinut);
        linearLayout.addView(textViewData);

        // Add LinearLayout to CardView
        cardView.addView(linearLayout);

        // Return the constructed CardView
        return cardView;
    }

    @Override
    public void onDestroyView() {
        handler.removeCallbacks(loadMessagesTask);
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