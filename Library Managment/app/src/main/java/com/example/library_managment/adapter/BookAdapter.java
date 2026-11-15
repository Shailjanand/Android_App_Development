package com.example.library_managment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library_managment.R;
import com.example.library_managment.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private static final int VIEW_TYPE_LARGE = 0;
    private static final int VIEW_TYPE_COMPACT = 1;

    private List<Book> books = new ArrayList<>();
    private final OnBookClickListener listener;

    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    public BookAdapter(OnBookClickListener listener) {
        this.listener = listener;
    }

    public void setBooks(List<Book> newBooks) {
        this.books = newBooks != null ? newBooks : new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        // Asymmetric masonry layout pattern: every 1st card in a group of 3 is large, others are compact
        return (position % 3 == 0) ? VIEW_TYPE_LARGE : VIEW_TYPE_COMPACT;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_LARGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_card_large, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_card, parent, false);
        }
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book, listener);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookTitle, tvBookCategory, tvBookDesc, tvStatusBadge;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvBookCategory = itemView.findViewById(R.id.tvBookCategory);
            tvBookDesc = itemView.findViewById(R.id.tvBookDesc);
            tvStatusBadge = itemView.findViewById(R.id.tvStatusBadge);
        }

        public void bind(Book book, OnBookClickListener listener) {
            tvBookTitle.setText(book.getTitle());
            tvBookCategory.setText(book.getCategory());
            tvBookDesc.setText(book.getDescription());

            if (book.isAvailable()) {
                tvStatusBadge.setText("Available");
                tvStatusBadge.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green_available));
                tvStatusBadge.setBackgroundResource(R.drawable.bg_badge_available);
            } else {
                tvStatusBadge.setText("Borrowed");
                tvStatusBadge.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red_borrowed));
                tvStatusBadge.setBackgroundResource(R.drawable.bg_badge_borrowed);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookClick(book);
                }
            });
        }
    }
}
