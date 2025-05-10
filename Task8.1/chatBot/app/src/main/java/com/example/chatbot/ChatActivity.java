package com.example.chatbot;

import android.os.Bundle;
import android.text.Html;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChatActivity extends AppCompatActivity {
    RecyclerView chatRecyclerView;
    EditText messageInput;
    ImageButton sendButton;
    ChatAdapter chatAdapter;
    List<ChatMessage> messages = new ArrayList<>();
    String username;
    private boolean isWaitingForResponse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        username = getIntent().getStringExtra("username");

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        chatAdapter = new ChatAdapter(messages, username);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // Add welcome message as bot message
        addMessage("Welcome " + username + "!", false);

        sendButton.setOnClickListener(v -> {
            String userMessage = messageInput.getText().toString().trim();
            if (!userMessage.isEmpty() && !isWaitingForResponse) {
                addMessage(userMessage, true);
                fetchBotResponse(userMessage);
                messageInput.setText("");
            }
        });
    }

    private void addMessage(String text, boolean isUser) {
        runOnUiThread(() -> {
            String formattedText = isUser ? text : Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString();

            messages.add(new ChatMessage(formattedText, isUser));
            chatAdapter.notifyItemInserted(messages.size() - 1);
            chatRecyclerView.scrollToPosition(messages.size() - 1);
        });
    }

    private void fetchBotResponse(String message) {
        isWaitingForResponse = true;
        runOnUiThread(() -> {
            sendButton.setEnabled(false);
            messageInput.setEnabled(false);
            addMessage("...", false);  // Add loading indicator
        });

        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:5001/chat");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // Sending form data instead of JSON
                String urlParameters = "userMessage=" + URLEncoder.encode(message, "UTF-8");

                // Send the request data
                OutputStream os = conn.getOutputStream();
                os.write(urlParameters.getBytes("UTF-8"));
                os.close();

                InputStream in = new BufferedInputStream(conn.getInputStream());
                String response = new BufferedReader(new InputStreamReader(in))
                        .lines().collect(Collectors.joining("\n"));

                // Remove the loading indicator and show response
                runOnUiThread(() -> {
                    messages.remove(messages.size() - 1);
                    chatAdapter.notifyItemRemoved(messages.size());
                    addMessage(response, false);
                });

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    messages.remove(messages.size() - 1);
                    chatAdapter.notifyItemRemoved(messages.size());
                    addMessage("Sorry, I couldn't get a response. Please try again.", false);
                });
            } finally {
                isWaitingForResponse = false;
                runOnUiThread(() -> {
                    sendButton.setEnabled(true);
                    messageInput.setEnabled(true);
                });
            }
        }).start();
    }

}