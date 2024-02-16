package com.example.foodzarella;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.foodzarella.databinding.ActivityHomeBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar myToolbar;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigator_layout);

        myToolbar = findViewById(R.id.my_toolbar);
        sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);

        setSupportActionBar(myToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupWithNavController(navigationView, navController);
        setupNavigation();

        updateNavigationViewHeader();
    }

    private void setupNavigation() {
        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_search, R.id.navigation_meal_plan, R.id.navigation_favorites).setDrawerLayout(drawerLayout).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupWithNavController(navView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.nav_home) {

                } else if (id == R.id.nav_gallery) {

                } else if (id == R.id.nav_log_out) {
                    logout();
                }
                return true;
            }
        });

    }

    private void logout() {
        mAuth.signOut();
        String authMethod = sharedPreferences.getString("authMethod", "");
        if ("Google".equals(authMethod)) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
            googleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(HomeActivity.this, "Logged out successfully from google", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            mAuth.signOut();
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateNavigationViewHeader() {
        NavigationView navigationView = findViewById(R.id.navigator_layout);
        View headerView = navigationView.getHeaderView(0);

        String displayName = sharedPreferences.getString("displayName", "");
        String email = sharedPreferences.getString("email", "");
        String photoUriString = sharedPreferences.getString("photoUri", "");
        Uri photoUri = photoUriString != null ? Uri.parse(photoUriString) : null;

        TextView userNameTextView = headerView.findViewById(R.id.tv_user_name);
        TextView emailTextView = headerView.findViewById(R.id.tv_user_email);
        ImageView profileImageView = headerView.findViewById(R.id.iv_profile);

        userNameTextView.setText(displayName);
        emailTextView.setText(email);
        if (photoUri != null) {
            Glide.with(this).load(photoUri).into(profileImageView);
        } else {
            profileImageView.setImageResource(R.drawable.logo_one);
        }
    }

}