package com.example.library_managment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library_managment.BookDetailsActivity;
import com.example.library_managment.R;
import com.example.library_managment.adapter.BookAdapter;
import com.example.library_managment.model.Book;
import com.example.library_managment.repository.BookRepository;

import java.util.List;

public class CatalogFragment extends Fragment implements BookRepository.OnDataChangeListener {

    private EditText etSearch;
    private Spinner spCategory;
    private RecyclerView rvCatalog;
    private TextView tvEmptyCatalog;

    private BookAdapter adapter;
    private BookRepository repository;
    private final String[] categories = {"All Categories", "Programming", "Fiction", "Science", "Business"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_catalog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etSearch = view.findViewById(R.id.etSearch);
        spCategory = view.findViewById(R.id.spCategory);
        rvCatalog = view.findViewById(R.id.rvCatalog);
        tvEmptyCatalog = view.findViewById(R.id.tvEmptyCatalog);

        repository = BookRepository.getInstance();

        // Horizontal book cards in a clean list
        rvCatalog.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new BookAdapter(book -> {
            Intent intent = new Intent(requireContext(), BookDetailsActivity.class);
            intent.putExtra(BookDetailsActivity.EXTRA_BOOK_ID, book.getId());
            startActivity(intent);
        });
        rvCatalog.setAdapter(adapter);

        // Spinner Adapter
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(spinnerAdapter);

        // Search TextWatcher
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Category Spinner Listener
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        applyFilter();
    }

    private void applyFilter() {
        String searchQuery = etSearch.getText().toString();
        String selectedCategory = (spCategory.getSelectedItem() != null) ? spCategory.getSelectedItem().toString() : "All Categories";

        List<Book> filtered = repository.filterBooks(searchQuery, selectedCategory);
        adapter.setBooks(filtered);

        if (filtered.isEmpty()) {
            tvEmptyCatalog.setVisibility(View.VISIBLE);
            rvCatalog.setVisibility(View.GONE);
        } else {
            tvEmptyCatalog.setVisibility(View.GONE);
            rvCatalog.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        repository.addListener(this);
        applyFilter();
    }

    @Override
    public void onPause() {
        super.onPause();
        repository.removeListener(this);
    }

    @Override
    public void onDataChanged() {
        if (isAdded()) {
            applyFilter();
        }
    }
}
