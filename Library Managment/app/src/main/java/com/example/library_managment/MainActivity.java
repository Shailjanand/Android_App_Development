package com.example.library_managment;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.library_managment.ui.AboutFragment;
import com.example.library_managment.ui.BorrowedFragment;
import com.example.library_managment.ui.CatalogFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_main);

        ImageButton btnNavHome = findViewById(R.id.btnNavHome);
        ImageButton btnNavBorrowed = findViewById(R.id.btnNavBorrowed);
        ImageButton btnNavAbout = findViewById(R.id.btnNavAbout);
        ImageButton btnNavExit = findViewById(R.id.btnNavExit);

        if (savedInstanceState == null) {
            loadFragment(new CatalogFragment());
        }

        btnNavHome.setOnClickListener(v -> loadFragment(new CatalogFragment()));
        btnNavBorrowed.setOnClickListener(v -> loadFragment(new BorrowedFragment()));
        btnNavAbout.setOnClickListener(v -> loadFragment(new AboutFragment()));
        btnNavExit.setOnClickListener(v -> showExitConfirmationDialog());
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Exit Application")
                .setMessage("Are you sure you want to exit Digital Library?")
                .setPositiveButton("Exit", (dialog, which) -> finish())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }
}
