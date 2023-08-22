package com.orestislef.simplelogin.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.orestislef.simplelogin.R;
import com.orestislef.simplelogin.api.RetrofitClient;
import com.orestislef.simplelogin.api.interfaces.ApiInterface;
import com.orestislef.simplelogin.api.models.ApiResponse;
import com.orestislef.simplelogin.api.models.ChangePasswordRequest;
import com.orestislef.simplelogin.handlers.UserHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);


        TextView welcomeUser = view.findViewById(R.id.welcome_user);
        String welcomeText = "Welcome user: " + UserHandler.getInstance().getUsername();
        welcomeUser.setText(welcomeText);

        Button changePasswordBTN = view.findViewById(R.id.change_passwordBTN);
        changePasswordBTN.setOnClickListener(this::OnChangePasswordClicked);

        return view;
    }

    private void OnChangePasswordClicked(View view) {
        showChangePasswordDialog();
    }

    private void showChangePasswordDialog() {
        UserHandler userHandler = UserHandler.getInstance();
        // Create a dialog instance
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_change_password);

        // Initialize dialog views
        EditText editTextNewPassword = dialog.findViewById(R.id.editTextNewPassword);
        Button btnChangePassword = dialog.findViewById(R.id.btnChangePassword);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        // Set a click listener for the change password button
        btnChangePassword.setOnClickListener(view -> {
            String newPassword = editTextNewPassword.getText().toString();

            changePassword(userHandler.getToken(), newPassword);

            // Dismiss the dialog when the password change is complete
            dialog.dismiss();
        });

        // Set a click listener for the cancel button
        btnCancel.setOnClickListener(view -> {
            // Dismiss the dialog when the cancel button is clicked
            dialog.dismiss();
        });

        // Show the dialog
        dialog.show();
    }

    private void changePassword(String token, String newPassword) {
        // Create an instance of your API service
        ApiInterface apiInterface = RetrofitClient.getClient().create(ApiInterface.class);

        // Make the API call to change the password
        Call<ApiResponse> call = apiInterface.changePassword(new ChangePasswordRequest(token, newPassword));

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getStatus().equals("success")) {
                        // Password change was successful
                        // Handle UI updates or navigate to another screen
                        Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                        UserHandler.getInstance().setData(apiResponse.getUserData());

                    } else {
                        // Password change failed, handle the error
                        String message = apiResponse != null ? apiResponse.getMessage() : "Password change failed";
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle API call failure
                    Toast.makeText(getContext(), "Failed to change password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                // Handle network or other errors
                Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
