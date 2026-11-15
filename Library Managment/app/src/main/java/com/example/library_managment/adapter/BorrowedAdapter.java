package com.example.library_managment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library_managment.R;
import com.example.library_managment.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BorrowedAdapter extends RecyclerView.Adapter<BorrowedAdapter.BorrowedViewHolder> {

    private List<Book> books = new ArrayList<>();
    private final OnReturnClickListener listener;

    public interface OnReturnClickListener {
        void onReturnClick(Book book);
    }

    public BorrowedAdapter(OnReturnClickListener listener) {
        this.listener = listener;
    }

    public void setBooks(List<Book> newBooks) {
        this.books = newBooks != null ? newBooks : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BorrowedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_borrowed_book, parent, false);
        return new BorrowedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BorrowedViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book, listener);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class BorrowedViewHolder extends RecyclerView.ViewHolder {
        TextView tvBorrowedTitle, tvBorrowedAuthor, tvBorrowedCategory;
        Button btnReturn;

        public BorrowedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBorrowedTitle = itemView.findViewById(R.id.tvBorrowedTitle);
            tvBorrowedAuthor = itemView.findViewById(R.id.tvBorrowedAuthor);
            tvBorrowedCategory = itemView.findViewById(R.id.tvBorrowedCategory);
            btnReturn = itemView.findViewById(R.id.btnReturn);
        }

        public void bind(Book book, OnReturnClickListener listener) {
            tvBorrowedTitle.setText(book.getTitle());
            tvBorrowedAuthor.setText("Author: " + book.getAuthor());
            tvBorrowedCategory.setText("Category: " + book.getCategory());

            btnReturn.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onReturnClick(book);
                }
            });
        }
    }
}
