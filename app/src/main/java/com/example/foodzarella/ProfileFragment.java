package com.example.foodzarella;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.foodzarella.databinding.FragmentProfileBinding;
import com.google.android.material.navigation.NavigationView;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        NavigationView navigationView = binding.navigatorLayout;

        // Set up NavController
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_home);

        // Set up AppBarConfiguration
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_gallery, R.id.nav_home, R.id.nav_slideshow)
                .setOpenableLayout(binding.drawerLayout)
                .build();

        // Set up ActionBar with NavController
        if (requireActivity() instanceof HomeActivity) {
            ((HomeActivity) requireActivity()).setSupportActionBar(binding.myToolbar);
        }

        NavigationUI.setupActionBarWithNavController((AppCompatActivity) requireActivity(), navController, mAppBarConfiguration);

        // Set up navigation menu with NavController
        NavigationUI.setupWithNavController(navigationView, navController);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
