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

import com.google.android.material.button.MaterialButton;
import com.orestislef.simplelogin.MainActivity;
import com.orestislef.simplelogin.R;
import com.orestislef.simplelogin.api.RetrofitClient;
import com.orestislef.simplelogin.api.interfaces.ApiInterface;
import com.orestislef.simplelogin.api.models.ApiResponse;
import com.orestislef.simplelogin.api.models.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private EditText usernameEditText, passwordEditText;
    private MaterialButton loginButton;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        loginButton = view.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Call the method to make the login API request here
            Thread thread = new Thread(() -> loginUser(username, password));
            thread.start();
        });

        Button registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            // Inflate the RegisterFragment when the button is clicked
            loadRegisterFragment();
        });

        return view;
    }

    private void loadRegisterFragment() {
        // Create an instance of RegisterFragment
        RegisterFragment registerFragment = new RegisterFragment();

        // Replace the current fragment with the RegisterFragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, registerFragment);
        transaction.addToBackStack(null); // Add to the back stack, so back button works
        transaction.commit();
    }

    private void loginUser(String username, String password) {
        // Create a LoginRequest object
        LoginRequest loginRequest = new LoginRequest(username, password);

        // Get the Retrofit instance using the RetrofitClient
        ApiInterface apiInterface = RetrofitClient.getClient().create(ApiInterface.class);

        // Make the API request
        Call<ApiResponse> call = apiInterface.login(loginRequest);

        // Enqueue the call to handle the response asynchronously
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    // Handle a successful login response here
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null) {
                        String status = apiResponse.getStatus();
                        String message = apiResponse.getMessage();

                        // Check the status and message to determine the action to take
                        if ("success".equals(status)) {
                            MainActivity mainActivity = (MainActivity) getActivity();
                            if (mainActivity != null)
                                mainActivity.OnUserLoggedIn(apiResponse.getUserData());
                        } else {
                            // Login failed, show an error message or take appropriate action
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // Handle error response here, e.g., network issues, server errors, etc.
                    Toast.makeText(getContext(), "Network issues", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                // Handle network or other errors here
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}


