package com.example.bookshelf;import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class BookDetailsFragment extends Fragment {


    private TextView display;
    private TextView display1;

    public static BookDetailsFragment newInstance(Book book) {
        BookDetailsFragment bookDetailsFragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("book",book);
        bookDetailsFragment.setArguments(args);
        return bookDetailsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);
        display = view.findViewById(R.id.detail);
        display1 = view.findViewById(R.id.detail1);
        if (getArguments()!=null) {
            Book book = (Book) getArguments().getSerializable("book");
            if (book != null) {
                displayBook(book);
                displayBook1(book);
            }
        }
        return view;
    }


    public void displayBook(Book book) {
        display.setText(book.title);
    }

    public void displayBook1(Book book) {
        display.setText(book.author);
    }


}