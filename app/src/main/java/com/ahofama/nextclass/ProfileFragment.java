package com.ahofama.nextclass;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private ImageView ivProfileImage;
    private TextView tvUserName, tvUserEmail, tvUserId;
    private TextView tvEnrolledCoursesCount, tvWishlistCount, tvCompletedCount;
    private MaterialButton btnLogout, btnEditProfile;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Init Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Bind views
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        tvUserId = view.findViewById(R.id.tvUserId);

        tvEnrolledCoursesCount = view.findViewById(R.id.tvEnrolledCoursesCount);
        tvWishlistCount = view.findViewById(R.id.tvWishlistCount);
        tvCompletedCount = view.findViewById(R.id.tvCompletedCount);

        btnLogout = view.findViewById(R.id.btnLogout);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);

        // Load user profile
        loadUserProfile();

        // Logout
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            redirectToLogin();
        });

        // Edit Profile
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void loadUserProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            redirectToLogin();
            return;
        }

        // Basic info
        tvUserName.setText(user.getDisplayName() != null ? user.getDisplayName() : "Unknown User");
        tvUserEmail.setText(user.getEmail());
        tvUserId.setText(user.getUid());

        // Load profile image (Glide handles null case)
        Glide.with(this)
                .load(user.getPhotoUrl())
                .placeholder(R.drawable.ic_person)
                .circleCrop()
                .into(ivProfileImage);

        // Fetch stats from Firestore (assuming "users" collection)
        db.collection("users").document(user.getUid()).get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        updateStats(document);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Failed to load stats", Toast.LENGTH_SHORT).show()
                );
    }

    private void updateStats(DocumentSnapshot doc) {
        long enrolled = doc.contains("enrolledCount") ? doc.getLong("enrolledCount") : 0;
        long wishlist = doc.contains("wishlistCount") ? doc.getLong("wishlistCount") : 0;
        long completed = doc.contains("completedCount") ? doc.getLong("completedCount") : 0;

        tvEnrolledCoursesCount.setText(String.valueOf(enrolled));
        tvWishlistCount.setText(String.valueOf(wishlist));
        tvCompletedCount.setText(String.valueOf(completed));
    }

    private void redirectToLogin() {
        Intent intent = new Intent(requireContext(), LoginPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish(); // finish parent activity
    }


}
