package com.ahofama.nextclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ahofama.nextclass.databinding.DasboardActivityBinding;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity {

    DasboardActivityBinding binding;
    DrawerLayout drawerLayout;
    ImageButton buttonDrawerToggle;
    NavigationView navigationView;
    TextView txtUsername,txtEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DasboardActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Load Home fragment by default
        loadFragment(new HomeFragment());

        drawerLayout = findViewById(R.id.drawerLayout);
        buttonDrawerToggle =  findViewById(R.id.buttonDrawerToggle);
        navigationView =  findViewById(R.id.navigationView);

        // Get header view of navigation drawer
        View headerView = navigationView.getHeaderView(0);

        txtUsername = headerView.findViewById(R.id.txtUsername);
        txtEmail = headerView.findViewById(R.id.txtEmail);

// ✅ Get username & email from intent
        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");

        if (username != null) {
            txtUsername.setText(username);
        }
        if (email != null) {
            txtEmail.setText(email);
        }




        buttonDrawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId =  item.getItemId();

                if(itemId == R.id.navProfile){
                    Toast.makeText(DashboardActivity.this, "Menu Clicked", Toast.LENGTH_SHORT).show();
                }

                if(itemId == R.id.navInstructors){
                    Toast.makeText(DashboardActivity.this, "Instructors Clicked", Toast.LENGTH_SHORT).show();
                }

                if(itemId == R.id.navContactus){
                    Toast.makeText(DashboardActivity.this, "Contact Us Clicked", Toast.LENGTH_SHORT).show();
                }

                if(itemId == R.id.navShare){
                    Toast.makeText(DashboardActivity.this, "Share Clicked", Toast.LENGTH_SHORT).show();
                }

                if(itemId == R.id.navSettings){
                    Toast.makeText(DashboardActivity.this, "Setting Clicked", Toast.LENGTH_SHORT).show();
                }

                if(itemId == R.id.navTerms){
                    Toast.makeText(DashboardActivity.this, "Terms and Conditions Clicked", Toast.LENGTH_SHORT).show();
                }

                if(itemId == R.id.navLogout){
                    Toast.makeText(DashboardActivity.this, "Log Out Clicked", Toast.LENGTH_SHORT).show();
                }

                drawerLayout.close();

                return false;
            }
        });




        // Handle bottom nav item clicks
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.courses) {
                selectedFragment = new CourseFragment();
            } else if (item.getItemId() == R.id.wishlist) {
                selectedFragment = new WishlistFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }

}