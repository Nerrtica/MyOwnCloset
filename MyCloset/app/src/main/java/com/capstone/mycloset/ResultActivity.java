package com.capstone.mycloset;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONObject;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        int typeCode = 0;
        int colorCode = 0;
        int patternCode = 0;
        boolean isLong = false;

        String[] type = getResources().getStringArray(R.array.closet_type);
        String[] color = getResources().getStringArray(R.array.closet_color);
        String[] pattern = getResources().getStringArray(R.array.closet_pattern);

        JSONObject json = null;
        try {
            json = new JSONObject(getIntent().getStringExtra("Result"));
            typeCode = json.getInt("category");
            colorCode = json.getInt("color");
            patternCode = json.getInt("pattern");
            isLong = json.getBoolean("is_long");
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuilder result = new StringBuilder();
        result.append("TYPE : ");
        result.append(type[typeCode]);
        result.append("\nCOLOR : ");
        result.append(color[colorCode]);
        result.append("\nPATTERN : ");
        result.append(pattern[patternCode]);
        result.append("\nIsLONG : ");
        result.append(isLong);

        TextView textView = (TextView)findViewById(R.id.result_text);
    }
}
