package pl.remindapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import pl.remindapp.R;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.start_layout);
        super.onCreate(savedInstanceState);

        final ImageView imageView = findViewById(R.id.bgimage);
        imageView.animate().scaleX((float)1.2).scaleY((float)1.2).setDuration(10000);

        final Button createCvButton = findViewById(R.id.createCvButton);
        createCvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
