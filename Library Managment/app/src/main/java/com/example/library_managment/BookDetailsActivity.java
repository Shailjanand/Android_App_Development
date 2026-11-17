package com.example.library_managment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.library_managment.model.Book;
import com.example.library_managment.repository.BookRepository;
import com.google.android.material.appbar.MaterialToolbar;

public class BookDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_BOOK_ID = "extra_book_id";
    private static final String TAG = "BookDetailsActivity";

    private MaterialToolbar toolbarDetails;
    private TextView tvDetailTitle, tvDetailAuthor, tvDetailCategory, tvDetailId, tvDetailStatus, tvDetailDescription;
    private Button btnBorrow, btnReturnDetail;

    private BookRepository repository;
    private Book currentBook;
    private String bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_book_details);

        repository = BookRepository.getInstance();

        toolbarDetails = findViewById(R.id.toolbarDetails);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailAuthor = findViewById(R.id.tvDetailAuthor);
        tvDetailCategory = findViewById(R.id.tvDetailCategory);
        tvDetailId = findViewById(R.id.tvDetailId);
        tvDetailStatus = findViewById(R.id.tvDetailStatus);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        btnBorrow = findViewById(R.id.btnBorrow);
        btnReturnDetail = findViewById(R.id.btnReturnDetail);

        toolbarDetails.setNavigationOnClickListener(v -> finish());

        if (getIntent() != null && getIntent().hasExtra(EXTRA_BOOK_ID)) {
            bookId = getIntent().getStringExtra(EXTRA_BOOK_ID);
        }

        loadBookData();

        btnBorrow.setOnClickListener(v -> showBorrowConfirmationDialog());
        btnReturnDetail.setOnClickListener(v -> showReturnConfirmationDialog());
    }

    private void loadBookData() {
        currentBook = repository.getBookById(bookId);
        if (currentBook == null) {
            Toast.makeText(this, "Book not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvDetailTitle.setText(currentBook.getTitle());
        tvDetailAuthor.setText("by " + currentBook.getAuthor());
        tvDetailCategory.setText(currentBook.getCategory());
        tvDetailId.setText("ID: " + currentBook.getId());
        tvDetailDescription.setText(currentBook.getDescription());

        updateAvailabilityUI();
    }

    private void updateAvailabilityUI() {
        if (currentBook.isAvailable()) {
            tvDetailStatus.setText("Available");
            tvDetailStatus.setTextColor(ContextCompat.getColor(this, R.color.green_available));
            tvDetailStatus.setBackgroundResource(R.drawable.bg_badge_available);

            btnBorrow.setVisibility(View.VISIBLE);
            btnReturnDetail.setVisibility(View.GONE);
        } else {
            tvDetailStatus.setText("Borrowed");
            tvDetailStatus.setTextColor(ContextCompat.getColor(this, R.color.red_borrowed));
            tvDetailStatus.setBackgroundResource(R.drawable.bg_badge_borrowed);

            btnBorrow.setVisibility(View.GONE);
            btnReturnDetail.setVisibility(View.VISIBLE);
        }
    }

    private void showBorrowConfirmationDialog() {
        if (currentBook == null || !currentBook.isAvailable()) {
            Toast.makeText(this, "Book is not available for borrowing.", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Borrowing")
                .setMessage("Do you want to borrow " + currentBook.getTitle() + "?")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    boolean success = repository.borrowBook(currentBook.getId());
                    if (success) {
                        Toast.makeText(BookDetailsActivity.this, "Successfully borrowed " + currentBook.getTitle(), Toast.LENGTH_SHORT).show();
                        loadBookData();
                    } else {
                        Toast.makeText(BookDetailsActivity.this, "Failed to borrow book.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    Toast.makeText(BookDetailsActivity.this, "Borrow operation canceled.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .show();
    }

    private void showReturnConfirmationDialog() {
        if (currentBook == null || currentBook.isAvailable()) {
            Toast.makeText(this, "Book is not currently borrowed.", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Return")
                .setMessage("Do you want to return " + currentBook.getTitle() + "?")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    boolean success = repository.returnBook(currentBook.getId());
                    if (success) {
                        Toast.makeText(BookDetailsActivity.this, "Successfully returned " + currentBook.getTitle(), Toast.LENGTH_SHORT).show();
                        loadBookData();
                    } else {
                        Toast.makeText(BookDetailsActivity.this, "Failed to return book.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    Toast.makeText(BookDetailsActivity.this, "Return operation canceled.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
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
        loadBookData();
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
