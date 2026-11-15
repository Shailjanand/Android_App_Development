package com.example.library_managment.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library_managment.R;
import com.example.library_managment.adapter.BorrowedAdapter;
import com.example.library_managment.model.Book;
import com.example.library_managment.repository.BookRepository;

import java.util.List;

public class BorrowedFragment extends Fragment implements BookRepository.OnDataChangeListener {

    private RecyclerView rvBorrowed;
    private TextView tvEmptyBorrowed;
    private BorrowedAdapter adapter;
    private BookRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_borrowed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvBorrowed = view.findViewById(R.id.rvBorrowed);
        tvEmptyBorrowed = view.findViewById(R.id.tvEmptyBorrowed);
        repository = BookRepository.getInstance();

        rvBorrowed.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new BorrowedAdapter(this::showReturnConfirmationDialog);
        rvBorrowed.setAdapter(adapter);

        loadBorrowedBooks();
    }

    private void loadBorrowedBooks() {
        List<Book> borrowedBooks = repository.getBorrowedBooks();
        adapter.setBooks(borrowedBooks);

        if (borrowedBooks.isEmpty()) {
            tvEmptyBorrowed.setVisibility(View.VISIBLE);
            rvBorrowed.setVisibility(View.GONE);
        } else {
            tvEmptyBorrowed.setVisibility(View.GONE);
            rvBorrowed.setVisibility(View.VISIBLE);
        }
    }

    private void showReturnConfirmationDialog(Book book) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm Return")
                .setMessage("Do you want to return " + book.getTitle() + "?")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    boolean success = repository.returnBook(book.getId());
                    if (success) {
                        Toast.makeText(requireContext(), "Successfully returned " + book.getTitle(), Toast.LENGTH_SHORT).show();
                        loadBorrowedBooks();
                    } else {
                        Toast.makeText(requireContext(), "Failed to return book.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    Toast.makeText(requireContext(), "Return operation canceled.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        repository.addListener(this);
        loadBorrowedBooks();
    }

    @Override
    public void onPause() {
        super.onPause();
        repository.removeListener(this);
    }

    @Override
    public void onDataChanged() {
        if (isAdded()) {
            loadBorrowedBooks();
        }
    }
}
