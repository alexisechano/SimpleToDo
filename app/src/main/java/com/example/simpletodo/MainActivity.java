package com.example.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    // creates a new list of strings
    List<String> items;

    // add the elements
    Button btnAddItem;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    // called by android
    protected void onCreate(Bundle savedInstanceState) {
        // super class will be executed first
        super.onCreate(savedInstanceState);

        // inflating layout file
        setContentView(R.layout.activity_main);

        // find the elements from the view
        btnAddItem = findViewById(R.id.btnAddItem);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        // init the array
        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                // delete item from the model
                items.remove(position);
                // notify adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at position " + position);
                // create new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                // pass data being edited
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                // display activity
                startActivityForResult(i, EDIT_TEXT_CODE);


            }
        };

        // creates new items adapter
        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        // create new onclick for button to add items
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();

                // add item to list
                items.add(todoItem);

                // notify adapter
                itemsAdapter.notifyItemInserted(items.size() - 1);
                etItem.setText("");

                // let user know
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    // update list from edit and handle result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // retrieve updated text
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);

            // extract the orig pos from position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            // update model at right position
            items.set(position, itemText);

            // notify adapter
            itemsAdapter.notifyItemChanged(position);
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully!", Toast.LENGTH_SHORT).show();

        } else {
            Log.w("MainActivity", "Unknown Call to onActivityResult");
        }
    }

    // gets the file
    private File getFile(){
        return new File(getFilesDir(), "data.txt");
    }

    // read the lines of the file
    private void loadItems(){
        try{
            items = new ArrayList<>(org.apache.commons.io.FileUtils.readLines(getFile(), Charset.defaultCharset()));
        }catch (IOException e){
            Log.e("MainActivity", "Error reading items", e);
            items = items = new ArrayList<>();
        }
    }

    // write to the file
    private void saveItems(){
        try{
            org.apache.commons.io.FileUtils.writeLines(getFile(), items);
        }catch (IOException e){
            Log.e("MainActivity", "Error saving items", e);
        }
    }
}