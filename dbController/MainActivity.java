package com.example.caucse.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private static final String DBName = "testDB.db";
    private DBOpenHelper openHelper;
    EditText category;
    EditText section;
    EditText name;
    Button SubmitButton;
    Button loadButton;
    ImageView imageView;
    DBcontroller controller ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        controller = new DBcontroller(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        category = (EditText)findViewById(R.id.category);
        section = (EditText)findViewById(R.id.section);
        SubmitButton = (Button)findViewById(R.id.submit);
        loadButton = (Button)findViewById(R.id.load);
        name = (EditText)findViewById(R.id.name);


        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.InsertCoordi(name.getText().toString(),category.getText().toString(),section.getText().toString());
                name.setText("");
                category.setText("");
                section.setText("");
            }
        });
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Coordi name = controller.FindCoordi()[0];
                category.setText(name.getTop());
                section.setText(name.getBottom());
            }
        });
    }
}
