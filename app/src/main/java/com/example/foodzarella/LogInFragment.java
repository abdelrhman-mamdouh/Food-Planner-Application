package com.example.foodzarella;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class LogInFragment extends Fragment {

    private static final String TAG = "LogInFragment";
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.log_in_frament, container, false);

        editTextEmail = rootView.findViewById(R.id.et_email);
        editTextPassword = rootView.findViewById(R.id.et_password);
        buttonLogin = rootView.findViewById(R.id.btn_login);

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                editTextEmail.setError("Please enter your email");
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("Invalid email format");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                editTextPassword.setError("Please enter a password");
                return;
            }
            checkIfUserExists(email, password,rootView);
        });


        return rootView;
    }

    private void checkIfUserExists(String email, String password, View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        Query query = usersRef.whereEqualTo("email", email);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    SnackbarUtils.showSnackbar(requireContext(), view, "User not found");
                } else {
                    loginUser(email, password,view);
                }
            } else {
                SnackbarUtils.showSnackbar(requireContext(), view, "Error checking user");
            }
        });
    }

    private void loginUser(String email, String password,View view) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    SnackbarUtils.showSnackbar(requireContext(), view, "Login Successful");
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.navigateToHome();
                })
                .addOnFailureListener(e -> {
                    SnackbarUtils.showSnackbar(requireContext(), view, "Login Unsuccessful");
                });
    }

}