
package kacaumap.kacau.com.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static final String DBName = "testDB.db";
    private DBOpenHelper openHelper;
    EditText category;
    EditText section;
    Button SubmitButton;
    Button loadButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        openHelper = new DBOpenHelper(getApplicationContext(),DBName,null,1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        category = (EditText)findViewById(R.id.category);
        section = (EditText)findViewById(R.id.section);

        SubmitButton = (Button)findViewById(R.id.submit);
        loadButton = (Button)findViewById(R.id.load);

        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = openHelper.getWritableDatabase();
                String sql = "INSERT INTO clothes_type VALUES ('" + category.getText().toString() +"', '"+ section.getText().toString()+"');";
                db.execSQL(sql);
                category.setText("");
                section.setText("");
            }
        });
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = openHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("select * from clothes_type",null);
                if(cursor.moveToNext()){
                    category.setText(cursor.getString(0).toString());
                    section.setText(cursor.getString(1).toString());
                }

            }
        });
    }
}
