package com.example.melb_go.repository;

import com.example.melb_go.api.ApiService;
import com.example.melb_go.ui.Settings.AuthResponse;
import com.example.melb_go.api.RetrofitClient;

import retrofit2.Callback;

public class AuthRepository {
    private final ApiService apiService;

    public AuthRepository() {
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
    }

    public void login(String email, String password, Callback<AuthResponse> callback) {
        com.example.melb_go.AuthRequest request = new com.example.melb_go.AuthRequest(email, password);
        apiService.login(request).enqueue(callback);
    }

    public void signup(String email, String password, Callback<AuthResponse> callback) {
        com.example.melb_go.AuthRequest request = new com.example.melb_go.AuthRequest(email, password);
        apiService.signup(request).enqueue(callback);
    }
}

