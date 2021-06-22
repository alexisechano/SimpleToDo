package com.example.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {
    // init the elements
    EditText editTextItem;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // match to view
        editTextItem = findViewById(R.id.editTextItem);
        btnSave = findViewById(R.id.btnSave);

        // support action bar init
        getSupportActionBar().setTitle("Edit item");

        // populate existing list item
        editTextItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        // set listener for button and save the new text
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create intent with desired text
                Intent intent = new Intent();

                // pass the data
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, editTextItem.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                // set the result of the intent
                setResult(RESULT_OK, intent);

                // finish activity
                finish();
            }
        });
    }
}