package com.example.tooltip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Switch otherDetailsSwitch = findViewById(R.id.otherDetailsSwitch);
        final TextView otherDetailsTextView = findViewById(R.id.otherDetailsTextView);
        otherDetailsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                otherDetailsTextView.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
    }
}
