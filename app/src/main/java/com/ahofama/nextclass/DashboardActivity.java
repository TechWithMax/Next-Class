package com.ahofama.nextclass;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ahofama.nextclass.databinding.DasboardActivityBinding;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity {

    DasboardActivityBinding binding;
    DrawerLayout drawerLayout;
    ImageButton buttonDrawerToggle;
    NavigationView navigationView;
    TextView txtUsername,txtEmail;
    private int lastSelectedItemId = -1; // -1 means nothing selected


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DasboardActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            Intent intent = new Intent(DashboardActivity.this, LoginPageActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                // ✅ We are back to main fragments → show bottom nav
                binding.bottomNavigationView.setVisibility(View.VISIBLE);
                findViewById(R.id.frame_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.nav_frame_layout).setVisibility(View.GONE);
            }
        });





        // Load Home fragment by default
        loadFragment(new NavDashboardFragment());

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

        // Side navigation Bar items click
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId =  item.getItemId();

                if(itemId == R.id.navDashboard){
                    loadFragment(new NavDashboardFragment());
                }

                if(itemId == R.id.navProfile){
                    loadFragment(new ProfileFragment());
                }

                if(itemId == R.id.navPdf){
                    loadFragment(new NavPdfFragment());
                }

                if(itemId == R.id.navContactus){
                    loadFragment(new NavContactFragment());
                }

                if(itemId == R.id.navQuestions){
                    loadFragment(new NavQuestionFragment());
                }

                if(itemId == R.id.navSettings) {
                    loadFragment(new NavSettingFragment());
                }

                if(itemId == R.id.navTerms) {
                    loadFragment(new NavTermFragment());
                }

                if(itemId == R.id.navLogout) {
                    if (itemId == R.id.navLogout) {
                        new AlertDialog.Builder(DashboardActivity.this)
                                .setTitle("Logout")
                                .setMessage("Are you sure you want to log out?")
                                .setPositiveButton("Yes", (dialog, which) -> {
                                    // ✅ Clear session
                                    SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.clear();
                                    editor.apply();

                                    // ✅ Navigate to login
                                    Intent intent = new Intent(DashboardActivity.this, LoginPageActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                })
                                .setNegativeButton("No", (dialog, which) -> {
                                    dialog.dismiss(); // ✅ stay in dashboard
                                })
                                .show();
                    }

                }

                drawerLayout.close();

                return false;
            }
        });



        // Handle bottom nav item clicks
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment selectedFragment = null;

            // ✅ First clear all highlights
            for (int i = 0; i < binding.bottomNavigationView.getMenu().size(); i++) {
                binding.bottomNavigationView.getMenu().getItem(i).setChecked(false);
            }

            // ✅ Highlight only the current one
            item.setChecked(true);


            if (item.getItemId() == R.id.explore) {
                selectedFragment = new ExploreFragment();
            } else if (item.getItemId() == R.id.courses) {
                selectedFragment = new CourseFragment();
            } else if (item.getItemId() == R.id.wishlist) {
                selectedFragment = new WishlistFragment();
            }

            if (selectedFragment != null) {
                lastSelectedItemId = id; // ✅ safe here
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, selectedFragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            }
            return false;
        });




        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();

                    getSupportFragmentManager().executePendingTransactions();
                    Fragment current = getSupportFragmentManager()
                            .findFragmentById(R.id.frame_layout);

                    if (current != null && !isBottomNavFragment(current)) {
                        // If we’re on a drawer fragment, restore last bottom nav item
                        if (lastSelectedItemId != -1) {
                            binding.bottomNavigationView.setSelectedItemId(lastSelectedItemId);
                        } else {
                            clearBottomNavSelection();
                        }
                    }
                } else {
                    finish();
                }
            }
        });


    }

    private boolean isBottomNavFragment(Fragment fragment) {
        return (fragment instanceof ExploreFragment ||
                fragment instanceof CourseFragment ||
                fragment instanceof WishlistFragment);
    }



    private void clearBottomNavSelection() {
        binding.bottomNavigationView.getMenu().setGroupCheckable(0, true, false);
        for (int i = 0; i < binding.bottomNavigationView.getMenu().size(); i++) {
            binding.bottomNavigationView.getMenu().getItem(i).setChecked(false);
        }
    }


    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .addToBackStack(null)
                .commit();

        // ✅ Clear bottom nav highlight
        clearBottomNavSelection();
    }

    private void loadNavFragment(Fragment fragment) {
        // Hide bottom nav and main frame
        binding.bottomNavigationView.setVisibility(View.GONE);
        findViewById(R.id.frame_layout).setVisibility(View.GONE);
        findViewById(R.id.nav_frame_layout).setVisibility(View.VISIBLE);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_frame_layout, fragment)
                .commit();
    }






}