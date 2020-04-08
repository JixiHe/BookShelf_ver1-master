package com.example.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Book> books = new ArrayList<>();
    private BookListFragment listFragment;
    private BookDetailsFragment detailFragment;
    private EditText Search;
    private Button search_buttom;
    private ViewPagerFragment viewpagerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Search = findViewById(R.id.search_content);
        search_buttom = findViewById(R.id.search);
        search_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = Search.getText().toString();
                searchBooks();
            }
        });
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        viewpagerFragment = ViewPagerFragment.newInstance();
        fragmentTransaction.replace(R.id.book_display, viewpagerFragment);
        fragmentTransaction.commit();
    }

    /*
    Generate an arbitrary list of "books" for testing
     */

    /*
    private ArrayList<HashMap<String, String>> getTestBooks() {
        ArrayList<HashMap<String, String>> books = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> book;
        for (int i = 0; i < 10; i++) {
            book = new HashMap<String, String>();
            book.put("title", "Book" + i);
            book.put("author", "Author" + i);
            books.add(book);
        }
        return books;
    };

     */
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            try{
                JSONArray bookArray = new JSONArray((String)msg.obj);

                if(viewpagerFragment!=null){
                    //Book book = new Book();
                    for(int i = 0 ; i < bookArray.length(); i++){
                        JSONObject jsonObject = bookArray.optJSONObject(i);
                        Book book = new Book();
                        book.id = jsonObject.optInt("book_id");
                        book.title = jsonObject.optString("title");
                        book.author= jsonObject.optString("author");
                        book.published = jsonObject.optInt("duration");
                        book.cover_url= jsonObject.optString("cover_url");
                        books.add(book);
                    }

                    viewpagerFragment.setBooks(books);
                }else if (listFragment==null){
                    listFragment.setBooks(bookArray);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    private void searchBooks() {
        new Thread(){
            public void run(){
                try{
                    String urlStr = "https://kamorris.com/lab/abp/booksearch.php?search=";
                    URL url = new URL(urlStr);
                    BufferedReader reader = new BufferedReader(new InputStreamReader (url.openStream()));
                    StringBuilder builder = new StringBuilder();
                    String tmpString;

                    while((tmpString = reader.readLine()) != null){
                        builder.append(tmpString);
                    }
                    Log.e("tag",builder.toString());
                    Message msg = Message.obtain();
                    msg.obj = builder.toString();
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
