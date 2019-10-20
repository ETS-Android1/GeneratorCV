package pl.remindapp.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;

import java.util.Collections;

import pl.remindapp.GenerateCVTask;
import pl.remindapp.PdfGenerator;
import pl.remindapp.R;
import pl.remindapp.cvObjects.Person;

public class ChooseCvTemplateActivity extends AppCompatActivity {

    private static final int STORAGE_CODE = 1000;
    private Person user;
    private final String USER_DATA = "user_data";
    private boolean letBack;

    @Override
    public void onBackPressed() {
        if(letBack) {
            System.out.println(user.getChosenTemplate());
            Intent resultIntent = new Intent();
            resultIntent.putExtra(USER_DATA, user);

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }

    @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
        letBack = true;
            super.onCreate(savedInstanceState);
            setContentView(R.layout.cv_templates_layout);
            user = (Person) getIntent().getSerializableExtra(USER_DATA);

        System.out.println(user.getChosenTemplate());
            if(user.getChosenTemplate() == 0){
                change(null);
            }

            if(user.getChosenTemplate() == 1){
                change1(null);
            }

            if(user.getChosenTemplate() == 2){
                change2(null);
            }


            Button nextButton = findViewById(R.id.nextTemplateButton);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                            String []permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permissions, STORAGE_CODE);
                        }
                        else
                            savePDF();
                    }
                    else
                        savePDF();
                }
            });
        }

    public void change(View v){
        ImageView imageView = findViewById(R.id.templateButton);
        imageView.setImageResource(R.drawable.ok);
//        ImageView imageView1 = findViewById(R.id.template1Button);
//        imageView1.setImageResource(R.drawable.no_ok);
//        ImageView imageView2= findViewById(R.id.template2Button);
//        imageView2.setImageResource(R.drawable.no_ok);
        user.setChosenTemplate(0);
    }

    public void change1(View v){
        ImageView imageView = findViewById(R.id.templateButton);
        imageView.setImageResource(R.drawable.no_ok);
//        ImageView imageView1 = findViewById(R.id.template1Button);
//        imageView1.setImageResource(R.drawable.ok);
//        ImageView imageView2= findViewById(R.id.template2Button);
//        imageView2.setImageResource(R.drawable.no_ok);
        user.setChosenTemplate(1);
    }

    public void change2(View v){
        ImageView imageView = findViewById(R.id.templateButton);
        imageView.setImageResource(R.drawable.no_ok);
//        ImageView imageView1 = findViewById(R.id.template1Button);
//        imageView1.setImageResource(R.drawable.no_ok);
//        ImageView imageView2= findViewById(R.id.template2Button);
//        imageView2.setImageResource(R.drawable.ok);
        user.setChosenTemplate(2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case STORAGE_CODE:{
                if(grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    savePDF();
                else
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void savePDF() {
        String filePath = Environment.getExternalStorageDirectory() + "/Download/" + user.getName() + user.getSurname() +"CV.pdf";
        int maxFontSize = 30;
        Collections.sort(user.getExperience());
        Collections.sort(user.getEducation());
        Collections.sort(user.getCourses());

        new GenerateCVTask(this).execute(new PdfGenerator(user, filePath, user.getChosenTemplate(), maxFontSize));

    }

    public void setAnimate() {
        letBack = false;
        setContentView(R.layout.loading_window);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        Sprite fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
    }
}
