package com.example.foodzarella;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        EditText etName = view.findViewById(R.id.et_name);
        EditText etEmail = view.findViewById(R.id.et_email);
        EditText etPassword = view.findViewById(R.id.et_password);
        EditText etConfirmPassword = view.findViewById(R.id.et_confirm_password);
        Button btnSignUp = view.findViewById(R.id.btn_signup);

        btnSignUp.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                etName.setError("Please enter your name");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Please enter your email");
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Invalid email format");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                etPassword.setError("Please enter a password");
                return;
            }
            if (password.length() < 6) {
                etPassword.setError("Password must be at least 6 characters long");
                return;
            }
            if (!password.equals(confirmPassword)) {
                etConfirmPassword.setError("Passwords do not match");
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        String userId = user.getUid();
                        User newUser = new User(userId, name, email);
                        mDatabase.child("users").child(userId).setValue(newUser);
                        Navigation.findNavController(requireView()).navigate(R.id.logIn_frament);
                    }
                } else {
                    Navigation.findNavController(requireView()).navigate(R.id.logIn_frament);
                }
            });
        });
        return view;
    }
}
