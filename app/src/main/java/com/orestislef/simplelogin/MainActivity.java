package com.orestislef.simplelogin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.orestislef.simplelogin.api.models.UserData;
import com.orestislef.simplelogin.fragments.LoginFragment;
import com.orestislef.simplelogin.fragments.MainFragment;
import com.orestislef.simplelogin.handlers.UserHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if savedInstanceState is null to avoid adding the fragment multiple times
        if (savedInstanceState == null) {
            // Call a method to replace the content with the LoginFragment
            loadLoginFragment();
        }
    }

    private void loadLoginFragment() {
        // Create an instance of the LoginFragment
        LoginFragment loginFragment = new LoginFragment();

        // Get the FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Start a FragmentTransaction to replace the content of the layout with the LoginFragment
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, loginFragment); // R.id.fragment_container is the ID of the layout container in your activity's XML
        transaction.addToBackStack(null); // Optionally, add the transaction to the back stack
        transaction.commit();
    }

    public void OnUserLoggedIn(UserData userData) {
        UserHandler.getInstance().setUsername(userData.getUsername());
        UserHandler.getInstance().setToken(userData.getToken());
        loadMainFragment();
    }

    private void loadMainFragment() {
        // Create an instance of the LoginFragment
        MainFragment mainFragment = new MainFragment();

        // Get the FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Start a FragmentTransaction to replace the content of the layout with the LoginFragment
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, mainFragment); // R.id.fragment_container is the ID of the layout container in your activity's XML
        transaction.addToBackStack(null); // Optionally, add the transaction to the back stack
        transaction.commit();

    }
}