package pl.remindapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import pl.remindapp.PdfGenerator;
import pl.remindapp.R;
import pl.remindapp.cvObjects.Person;

public class ChooseCvTemplateActivity extends AppCompatActivity {

    private static final int STORAGE_CODE = 1000;
    private int chosen;
    private Person user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cv_templates_layout);
        user = (Person) getIntent().getSerializableExtra("user_data");
        chosen = 0;

        Button nextButton = findViewById(R.id.nextTemplateButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseCvTemplateActivity.this, SuccessfullActivity.class);
                intent.putExtra("user_data", user);
                startActivity(intent);
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
        Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show();
        ImageView imageView = findViewById(R.id.templateButton);
        imageView.setImageResource(R.drawable.ok);
        ImageView imageView1 = findViewById(R.id.template1Button);
        imageView1.setImageResource(R.drawable.no_ok);
        ImageView imageView2= findViewById(R.id.template2Button);
        imageView2.setImageResource(R.drawable.no_ok);
        chosen = 0;
    }

    public void change1(View v){
        Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show();
        ImageView imageView = findViewById(R.id.templateButton);
        imageView.setImageResource(R.drawable.no_ok);
        ImageView imageView1 = findViewById(R.id.template1Button);
        imageView1.setImageResource(R.drawable.ok);
        ImageView imageView2= findViewById(R.id.template2Button);
        imageView2.setImageResource(R.drawable.no_ok);
        chosen = 1;
    }

    public void change2(View v){
        Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show();
        ImageView imageView = findViewById(R.id.templateButton);
        imageView.setImageResource(R.drawable.no_ok);
        ImageView imageView1 = findViewById(R.id.template1Button);
        imageView1.setImageResource(R.drawable.no_ok);
        ImageView imageView2= findViewById(R.id.template2Button);
        imageView2.setImageResource(R.drawable.ok);
        chosen = 2;
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
        int numberOfPage;
        PdfGenerator pdfGenerator = new PdfGenerator(user, filePath, chosen, maxFontSize);
        numberOfPage = pdfGenerator.generateCv();

        while( numberOfPage > 1){
            System.out.println(maxFontSize);
            maxFontSize--;
            pdfGenerator.setFontSize(maxFontSize);
            numberOfPage = pdfGenerator.generateCv();
        }

        if(numberOfPage == 1)
            Toast.makeText(this, "File created in: " + filePath , Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Creating file failed", Toast.LENGTH_LONG).show();
    }
}
