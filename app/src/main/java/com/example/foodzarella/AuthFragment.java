package com.example.foodzarella;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodzarella.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class AuthFragment extends Fragment {

    private static final int RC_SIGN_IN = 1001;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    private Button btnLoginFacebook;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth, container, false);
        callbackManager = CallbackManager.Factory.create();
        btnLoginFacebook = view.findViewById(R.id.facebook_button);
        btnLoginFacebook.setOnClickListener(v -> signInWithFacebook());
        initializeFirebase();
        return view;
    }

    private void initializeFirebase() {
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

        FacebookSdk.sdkInitialize(requireContext());
        AppEventsLogger.activateApp(requireActivity().getApplication());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
    }

    private void initializeViews(View view) {
        Button btnLoginGoogle = view.findViewById(R.id.google_button);
        Button btnSignUpEmail = view.findViewById(R.id.email_button);
        TextView tvLoginLink = view.findViewById(R.id.log_in);

        btnSignUpEmail.setOnClickListener(v -> navigateToSignUpFragment());
        tvLoginLink.setOnClickListener(v -> navigateToLoginFragment());
        btnLoginGoogle.setOnClickListener(v -> signInWithGoogle());
    }

    private void navigateToSignUpFragment() {
        Navigation.findNavController(requireView()).navigate(R.id.signUp_fragment);
    }

    private void navigateToLoginFragment() {
        Navigation.findNavController(requireView()).navigate(R.id.action_authFragment_to_logIn_frament);
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signInWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        update(user);
                    } else {
                        Toast.makeText(getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            showToast("Google Authentication succeeded.");
            updateUI(account);
        } catch (ApiException e) {
            showToast("signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            String name = account.getDisplayName();
            String email = account.getEmail();
            String idToken = account.getIdToken();
            Toast.makeText(getContext(), name + " " + email + " " + idToken, Toast.LENGTH_SHORT).show();
        }
    }
    private void update(@Nullable FirebaseUser user) {
        if (user != null) {
            // User is signed in, you can update UI with user information
            String displayName = user.getDisplayName();
            String email = user.getEmail();
            Uri uri = user.getPhotoUrl();
            showToast(displayName+" "+email+" "+uri);
            // Update your UI elements with the user's information
            // For example:
            // displayNameTextView.setText(displayName);
            // emailTextView.setText(email);
            // Glide.with(this).load(photoUrl).into(profileImageView);

            // Example: navigate to another fragment upon successful login
         //   Navigation.findNavController(requireView()).navigate(R.id.logged_in_fragment);
        } else {
            // User is signed out, update UI accordingly
            // For example:
            // displayNameTextView.setText("");
            // emailTextView.setText("");
            // profileImageView.setImageResource(R.drawable.default_profile_image);
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
