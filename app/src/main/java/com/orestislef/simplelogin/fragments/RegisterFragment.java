package com.orestislef.simplelogin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.orestislef.simplelogin.R;
import com.orestislef.simplelogin.api.RetrofitClient;
import com.orestislef.simplelogin.api.interfaces.ApiInterface;
import com.orestislef.simplelogin.api.models.ApiResponse;
import com.orestislef.simplelogin.api.models.RegisterRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Find the EditText fields
        EditText usernameEditText = view.findViewById(R.id.RusernameEditText);
        EditText passwordEditText = view.findViewById(R.id.RpasswordEditText);

        // Find the Register button
        Button registerButton = view.findViewById(R.id.RregisterButton);
        registerButton.setOnClickListener(v -> {
            // Get the entered username and password
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Validate the input (e.g., check for empty fields, strong password requirements, etc.)
            if (username.isEmpty() || password.isEmpty()) {
                // Handle validation error (show a message to the user)
            } else {
                // Make the API call for registration
                Thread thread = new Thread(() -> performRegistration(username, password));
                thread.start();
            }
        });

        return view;
    }

    private void performRegistration(String username, String password) {
        // Create an instance of your Retrofit service interface for registration
        ApiInterface apiService = RetrofitClient.getClient().create(ApiInterface.class);

        // Create a RegisterRequest object with the username and password
        RegisterRequest request = new RegisterRequest(username, password);

        // Make the API call using Retrofit
        Call<ApiResponse> call = apiService.register(request);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getStatus().equals("success")) {
                        // Registration was successful, you can handle this accordingly
                        // For example, navigate to the login fragment
                        loadLoginFragment();
                    } else {
                        if (apiResponse != null)
                            Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle API call failure (e.g., network error)
                    Toast.makeText(getContext(), "network error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                // Handle network or other errors
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadLoginFragment() {
        // Create an instance of LoginFragment
        LoginFragment loginFragment = new LoginFragment();

        // Replace the current fragment with the LoginFragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, loginFragment);
        transaction.addToBackStack(null); // Add to the back stack, so back button works
        transaction.commit();
    }
}
