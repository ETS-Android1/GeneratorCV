package pl.remindapp.activities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.itextpdf.text.pdf.codec.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import pl.remindapp.R;
import pl.remindapp.cvObjects.Address;
import pl.remindapp.cvObjects.LifeEvent;
import pl.remindapp.cvObjects.Person;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    public static final int PICK_IMAGE = 1;
    public Intent intent;
    private EditText dateText;
    private int day, month, year;
    private boolean isEverythingCorrect;
    private Person user;
    private int rotation;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            try {
                ImageView imageView = findViewById(R.id.imageView);
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                String imageString = Base64.encodeBytes(imageBytes);
                user.setImageFile(imageString);

                imageView.setImageBitmap(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.somethingWentWrong, Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, R.string.noPhoto,Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        day = 5;
        month = 2;
        year = 1998;
        dateText = findViewById(R.id.birthDate);
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        user = new Person();
        user.setName("Aleksander");
        user.setSurname("Obiekttestowy");
        user.setPhoneNumber(123456789);
        user.setEmailAddress("martinNowak@gmail.com");
        user.setDateOfBirth(LocalDate.now());
        user.setShortInfo("SLorem ipsum dolor sit amet, consectetur adipiscing elit. Proin sit amet " +
                "ipsum tempus lacus laoreet facilisis. Suspendisse tincidunt massa accumsan pellentesque" +
                " tincidunt. Ut lorem turpis, pellentesque sed quam eget, suscipit scelerisque augue. Nulla " +
                "placerat vestibulum urna, venenatis bibendum quam cursus id. Vestibulum lobortis, tellus et" +
                " gravida tincidunt, erat lectus pharetra risus, id cursus urna nunc nec est. Curabitur aliquet," +
                " purus a elementum auctor, felis quam commodo sapien, ac placerat tortor diam euismod lorem. Vestibulum" +
                " faucibus dui sed mauris posuere egestas. Proin luctus at arcu id dignissim. Suspendisse " +
                "ac augue malesuada, dictum metus eu, bibendum justo. Morbi id metus finibus odio tincidunt " +
                "blandit. Suspendisse quis lacinia sem. Vivamus laoreet tristique tristique. Sed fermentum" +
                " lacus nulla, ac pulvinar ipsum convallis eu. Donec vitae porttitor velit. Vivamus tempus" +
                " congue mollis. Quisque ac condimentum est, et euismod sapien. ");
        user.setAddress(new Address("Orzeszkowa",2,3, null, "87-100","Toruń"));

        List<String> skills  = new ArrayList<>();
        skills.add("skill1");
        skills.add("skill2");
        skills.add("skill1");
        skills.add("skill2");
        skills.add("skill1");
        skills.add("skill2");
        user.setSkills(skills);

        List<String> hobby  = new ArrayList<>();
        hobby.add("hobby1");
        hobby.add("hobby2");
        hobby.add("hobby1");
        hobby.add("hobby2");
        hobby.add("hobby1");
        hobby.add("hobby2");
        user.setInterest(hobby);

        List<LifeEvent> education = new ArrayList<>();
        education.add(new LifeEvent( LocalDate.now(), LocalDate.now() ,"Npwy kurs", "SLorem ipsum dolor sit amet, consectetur adipiscing elit. Proin sit amet " +
                "ipsum tempus lacus laoreet facilisis. Suspendisse tincidunt massa accumsan pellentesque" +
                " tincidunt. Ut lorem turpis, pellentesque sed quam eget, suscipit scelerisque augue. Nulla " +
                "placerat vestibulum urna, venenatis bibendum quam cursus id. Vestibulum lobortis, tellus et"));
        education.add(new LifeEvent( LocalDate.now(), LocalDate.now() ,"Npwy kurs", "SLorem ipsum dolor sit amet, consectetur adipiscing elit. Proin sit amet " +
                "ipsum tempus lacus laoreet facilisis. Suspendisse tincidunt massa accumsan pellentesque" +
                " tincidunt. Ut lorem turpis, pellentesque sed quam eget, suscipit scelerisque augue. Nulla " +
                "placerat vestibulum urna, venenatis bibendum quam cursus id. Vestibulum lobortis, tellus et"));
        education.add(new LifeEvent( LocalDate.now(), LocalDate.now() ,"Npwy kurs", "SLorem ipsum dolor sit amet, consectetur adipiscing elit. Proin sit amet " +
                "ipsum tempus lacus laoreet facilisis. Suspendisse tincidunt massa accumsan pellentesque" +
                " tincidunt. Ut lorem turpis, pellentesque sed quam eget, suscipit scelerisque augue. Nulla " +
                "placerat vestibulum urna, venenatis bibendum quam cursus id. Vestibulum lobortis, tellus et"));
        user.setEducation(education);

        List<LifeEvent> experience = new ArrayList<>();
        experience.add(new LifeEvent( LocalDate.now(), LocalDate.now() ,"Npwy kurs", "SLorem ipsum dolor sit amet, consectetur adipiscing elit. Proin sit amet " +
                "ipsum tempus lacus laoreet facilisis. Suspendisse tincidunt massa accumsan pellentesque" +
                " tincidunt. Ut lorem turpis, pellentesque sed quam eget, suscipit scelerisque augue. Nulla " +
                "placerat vestibulum urna, venenatis bibendum quam cursus id. Vestibulum lobortis, tellus et"));
        experience.add(new LifeEvent( LocalDate.now(), LocalDate.now() ,"Npwy kurs", "SLorem ipsum dolor sit amet, consectetur adipiscing elit. Proin sit amet " +
                "ipsum tempus lacus laoreet facilisis. Suspendisse tincidunt massa accumsan pellentesque" +
                " tincidunt. Ut lorem turpis, pellentesque sed quam eget, suscipit scelerisque augue. Nulla " +
                "placerat vestibulum urna, venenatis bibendum quam cursus id. Vestibulum lobortis, tellus et"));
        experience.add(new LifeEvent( LocalDate.now(), LocalDate.now() ,"Npwy kurs", "SLorem ipsum dolor sit amet, consectetur adipiscing elit. Proin sit amet " +
                "ipsum tempus lacus laoreet facilisis. Suspendisse tincidunt massa accumsan pellentesque" +
                " tincidunt. Ut lorem turpis, pellentesque sed quam eget, suscipit scelerisque augue. Nulla " +
                "placerat vestibulum urna, venenatis bibendum quam cursus id. Vestibulum lobortis, tellus et"));
        user.setExperience(experience);

        List<LifeEvent> courses = new ArrayList<>();
        courses.add(new LifeEvent( LocalDate.now(), LocalDate.now() ,"Npwy kurs", "SLorem ipsum dolor sit amet, consectetur adipiscing elit. Proin sit amet " +
                "ipsum tempus lacus laoreet facilisis. Suspendisse tincidunt massa accumsan pellentesque" +
                " tincidunt. Ut lorem turpis, pellentesque sed quam eget, suscipit scelerisque augue. Nulla " +
                "placerat vestibulum urna, venenatis bibendum quam cursus id. Vestibulum lobortis, tellus et"));
        courses.add(new LifeEvent( LocalDate.now(), LocalDate.now() ,"Npwy kurs", "SLorem ipsum dolor sit amet, consectetur adipiscing elit. Proin sit amet " +
                "ipsum tempus lacus laoreet facilisis. Suspendisse tincidunt massa accumsan pellentesque" +
                " tincidunt. Ut lorem turpis, pellentesque sed quam eget, suscipit scelerisque augue. Nulla " +
                "placerat vestibulum urna, venenatis bibendum quam cursus id. Vestibulum lobortis, tellus et"));
        courses.add(new LifeEvent( LocalDate.now(), LocalDate.now() ,"Npwy kurs", "SLorem ipsum dolor sit amet, consectetur adipiscing elit. Proin sit amet " +
                "ipsum tempus lacus laoreet facilisis. Suspendisse tincidunt massa accumsan pellentesque" +
                " tincidunt. Ut lorem turpis, pellentesque sed quam eget, suscipit scelerisque augue. Nulla " +
                "placerat vestibulum urna, venenatis bibendum quam cursus id. Vestibulum lobortis, tellus et"));
        courses.add(new LifeEvent( LocalDate.now(), LocalDate.now() ,"Npwy kurs", ""));
        user.setCourses(courses);

        Button rotateButton = findViewById(R.id.rotateButton);
        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = findViewById(R.id.imageView);
                rotation += 90;
                imageView.setRotation(rotation);
                if(rotation >= 360)
                    rotation %= 360;
            }
        });

        Button chosePhotoButton = findViewById(R.id.chosePhotoButton);
        chosePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        ImageButton dateButton = findViewById(R.id.dateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        Button nextButton = findViewById(R.id.nextButtonMain);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                isEverythingCorrect = true;
                checkEditTextIsEmpty(R.id.nameEditText);
                checkEditTextIsEmpty(R.id.surnameEditText);
                checkEditTextIsEmpty(R.id.emailEditText);
                checkEditTextIsEmpty(R.id.streetEditText);
                checkEditTextIsEmpty(R.id.phoneEditText);
                checkEditTextIsEmpty(R.id.cityEditText);
                checkEditTextIsEmpty(R.id.houseNumberEditText);

                EditText shortInfoEditText = findViewById(R.id.shortInfoEditText);
                shortInfoEditText.setBackground(getDrawable(R.drawable.edit_text_green));

                EditText flatEditText = findViewById(R.id.flatNumberEditText);
                flatEditText.setBackground(getDrawable(R.drawable.edit_text_green));

                EditText postCode1 = findViewById(R.id.postCodeEditText1);
                if(postCode1.getText().toString().length() == 2){
                    postCode1.setBackground(getDrawable(R.drawable.edit_text_green));
                }else {
                    postCode1.setBackground(getDrawable(R.drawable.edit_text_red));
                    isEverythingCorrect = false;
                }

                EditText postCode2 = findViewById(R.id.postCodeEditText2);
                if(postCode2.getText().toString().length() == 3){
                    postCode2.setBackground(getDrawable(R.drawable.edit_text_green));
                }else {
                    postCode2.setBackground(getDrawable(R.drawable.edit_text_red));
                    isEverythingCorrect = false;
                }

                if(true/*isEverythingCorrect*/){
                    /*
                    user.setName(((EditText)findViewById(R.id.nameEditText)).getText().toString());
                    user.setSurname(((EditText)findViewById(R.id.surnameEditText)).getText().toString());
                    user.setPhoneNumber(Integer.valueOf(((EditText)findViewById(R.id.phoneEditText)).getText().toString()));
                    user.setEmailAddress(((EditText)findViewById(R.id.emailEditText)).getText().toString());
                    Address address = new Address(((EditText)findViewById(R.id.streetEditText)).getText().toString(),
                            Integer.valueOf(((EditText)findViewById(R.id.flatNumberEditText)).getText().toString()),
                            Integer.valueOf(((EditText)findViewById(R.id.houseNumberEditText)).getText().toString()),
                            null,
                            ((EditText)findViewById(R.id.postCodeEditText1)).getText().toString() + "-" +((EditText)findViewById(R.id.postCodeEditText2)).getText().toString(),
                            ((EditText)findViewById(R.id.cityEditText)).getText().toString());
                    user.setAddress(address);
                    user.setShortInfo(((EditText)findViewById(R.id.shortInfoEditText)).getText().toString());
                    user.setDateOfBirth(LocalDate.of(year, month+1, day));
                    */
                    user.setRotationAngle(rotation);
                    if(user.getImageFile() == null) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.man);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();
                        String imageString = Base64.encodeBytes(imageBytes);//Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        user.setImageFile(imageString);
                    }

                    Intent intent = new Intent(MainActivity.this, AbilitiesActivity.class);
                    intent.putExtra("user_data", user);
                    startActivity(intent);
                }
            }
        });
    }

    private void checkEditTextIsEmpty(int id){
        EditText editText = findViewById(id);
        if(!editText.getText().toString().equals("")){
            editText.setBackground(getDrawable(R.drawable.edit_text_green));
        }else {
            editText.setBackground(getDrawable(R.drawable.edit_text_red));
            isEverythingCorrect = false;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String day, monthText;

        this.day = dayOfMonth;
        this.month = month;
        this.year = year;
        month +=1;

        if(dayOfMonth <= 9 && dayOfMonth >= 0)
            day = "0" + dayOfMonth;
        else
            day = String.valueOf(dayOfMonth);

        if(month <= 9 && month >= 0)
            monthText = "0" + (month);
        else
            monthText = String.valueOf(month);

        dateText.setText(day + "/" + monthText + "/" + year);
    }

    private void showDateDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_DARK, this, this.year, this.month, this.day);
        datePickerDialog.show();
    }
}
