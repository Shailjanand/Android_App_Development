package com.example.library_managment.repository;

import com.example.library_managment.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private static BookRepository instance;
    private final List<Book> books;
    private final List<OnDataChangeListener> listeners = new ArrayList<>();

    public interface OnDataChangeListener {
        void onDataChanged();
    }

    private BookRepository() {
        books = new ArrayList<>();
        initSampleData();
    }

    public static synchronized BookRepository getInstance() {
        if (instance == null) {
            instance = new BookRepository();
        }
        return instance;
    }

    private void initSampleData() {
        books.add(new Book("B101", "Learn Java", "James Gosling", "Programming", "A complete guide to Java programming fundamentals and object-oriented concepts.", true));
        books.add(new Book("B102", "Python Basics", "Guido van Rossum", "Programming", "Get started with Python syntax, data structures, and simple scripting.", true));
        books.add(new Book("B103", "The Alchemist", "Paulo Coelho", "Fiction", "An inspiring fable about following your dreams and listening to your heart.", true));
        books.add(new Book("B104", "A Brief History of Time", "Stephen Hawking", "Science", "Explore black holes, modern cosmology, and the origins of the universe.", true));
        books.add(new Book("B105", "Think and Grow Rich", "Napoleon Hill", "Business", "Timeless financial wisdom and personal achievement principles.", true));
        books.add(new Book("B106", "C++ Fundamentals", "Bjarne Stroustrup", "Programming", "Master memory management, pointers, and object-oriented design in C++.", true));
        books.add(new Book("B107", "The Hobbit", "J.R.R. Tolkien", "Fiction", "An epic fantasy adventure story following Bilbo Baggins.", true));
        books.add(new Book("B108", "Cosmos", "Carl Sagan", "Science", "A fascinating exploration of the universe, science, and human history.", true));
        books.add(new Book("B109", "Atomic Habits", "James Clear", "Business", "An easy and proven way to build good habits and break bad ones.", true));
        books.add(new Book("B110", "Clean Code", "Robert C. Martin", "Programming", "A handbook of agile software craftsmanship for writing maintainable code.", true));
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    public Book getBookById(String id) {
        if (id == null) return null;
        for (Book book : books) {
            if (book.getId().equalsIgnoreCase(id)) {
                return book;
            }
        }
        return null;
    }

    public List<Book> getBorrowedBooks() {
        List<Book> borrowedList = new ArrayList<>();
        for (Book book : books) {
            if (!book.isAvailable()) {
                borrowedList.add(book);
            }
        }
        return borrowedList;
    }

    public List<Book> filterBooks(String query, String category) {
        List<Book> filteredList = new ArrayList<>();
        String searchQuery = query != null ? query.trim().toLowerCase() : "";
        boolean isAllCategory = category == null || category.equalsIgnoreCase("All Categories") || category.equalsIgnoreCase("All");

        for (Book book : books) {
            boolean matchesTitle = searchQuery.isEmpty() || book.getTitle().toLowerCase().contains(searchQuery);
            boolean matchesCategory = isAllCategory || book.getCategory().equalsIgnoreCase(category);

            if (matchesTitle && matchesCategory) {
                filteredList.add(book);
            }
        }
        return filteredList;
    }

    public boolean borrowBook(String id) {
        Book book = getBookById(id);
        if (book != null && book.isAvailable()) {
            book.setAvailable(false);
            notifyDataChanged();
            return true;
        }
        return false;
    }

    public boolean returnBook(String id) {
        Book book = getBookById(id);
        if (book != null && !book.isAvailable()) {
            book.setAvailable(true);
            notifyDataChanged();
            return true;
        }
        return false;
    }

    public void addListener(OnDataChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(OnDataChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifyDataChanged() {
        for (OnDataChangeListener listener : new ArrayList<>(listeners)) {
            listener.onDataChanged();
        }
    }
}
