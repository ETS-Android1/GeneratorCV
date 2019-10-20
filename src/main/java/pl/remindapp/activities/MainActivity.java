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
    private final int ABILITIES_CODE = 123;
    private final String USER_DATA = "user_data";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == ABILITIES_CODE && resultCode == RESULT_OK && data != null && data.getSerializableExtra(USER_DATA) != null ){
            user = (Person)data.getSerializableExtra(USER_DATA);
            fillPersonalData();

        }
        else {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
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

            } else {
                Toast.makeText(this, R.string.noPhoto, Toast.LENGTH_LONG).show();
            }
        }
    }

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
        user.setChosenTemplate(0);
        user.setName("");
        user.setSurname("");
        user.setEmailAddress("");
        user.setDateOfBirth(null);
        user.setShortInfo("");
        user.setAddress(new Address());

        List<String> skills  = new ArrayList<>();
        user.setSkills(skills);

        List<String> hobby  = new ArrayList<>();
        user.setInterest(hobby);

        List<LifeEvent> education = new ArrayList<>();
        user.setEducation(education);

        List<LifeEvent> experience = new ArrayList<>();
        user.setExperience(experience);

        List<LifeEvent> courses = new ArrayList<>();
        user.setCourses(courses);

        fillPersonalData();

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
                checkEditTextIsEmpty(R.id.cityEditText);
                checkEditTextIsEmpty(R.id.houseNumberEditText);

                EditText phoneEditText = findViewById(R.id.phoneEditText);
                if(phoneEditText.getText().length() == 9)
                    phoneEditText.setBackground(getDrawable(R.drawable.edit_text_green));
                else{
                    phoneEditText.setBackground(getDrawable(R.drawable.edit_text_red));
                    isEverythingCorrect = false;
                }

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

                if(isEverythingCorrect){
                    user.setName(((EditText)findViewById(R.id.nameEditText)).getText().toString());
                    user.setSurname(((EditText)findViewById(R.id.surnameEditText)).getText().toString());
                    user.setPhoneNumber(Integer.valueOf(((EditText)findViewById(R.id.phoneEditText)).getText().toString()));
                    user.setEmailAddress(((EditText)findViewById(R.id.emailEditText)).getText().toString());
                    Integer flatNumber = -1;
                    if(!((EditText)findViewById(R.id.flatNumberEditText)).getText().toString().equals("")){
                        flatNumber = Integer.valueOf(((EditText)findViewById(R.id.flatNumberEditText)).getText().toString());
                    }

                    Address address = new Address(((EditText)findViewById(R.id.streetEditText)).getText().toString(),
                            Integer.valueOf(((EditText)findViewById(R.id.houseNumberEditText)).getText().toString()),
                            flatNumber,
                            null,
                            ((EditText)findViewById(R.id.postCodeEditText1)).getText().toString() + "-" +((EditText)findViewById(R.id.postCodeEditText2)).getText().toString(),
                            ((EditText)findViewById(R.id.cityEditText)).getText().toString());
                    user.setAddress(address);
                    user.setShortInfo(((EditText)findViewById(R.id.shortInfoEditText)).getText().toString());
                    user.setDateOfBirth(LocalDate.of(year, month+1, day));

                    user.setRotationAngle(rotation);
                    if(user.getImageFile() == null) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.man);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();
                        String imageString = Base64.encodeBytes(imageBytes);
                        user.setImageFile(imageString);
                    }

                    Intent intent = new Intent(MainActivity.this, AbilitiesActivity.class);
                    intent.putExtra(USER_DATA, user);
                    startActivityForResult(intent, ABILITIES_CODE);
                }
                else{
                    Toast.makeText(MainActivity.this, "Popraw dane!", Toast.LENGTH_SHORT).show();
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

    private void setTextInEditText(int id, @Nullable String text){
        EditText editText = findViewById(id);
        if(text != null)
            editText.setText(text);
        else
            editText.setText("");
    }

    private void fillPersonalData(){
        setTextInEditText(R.id.nameEditText, user.getName());
        setTextInEditText(R.id.surnameEditText, user.getSurname());
        if(user.getPhoneNumber() != null)
            setTextInEditText(R.id.phoneEditText,String.valueOf(user.getPhoneNumber()));
        else
            setTextInEditText(R.id.phoneEditText,null);
        setTextInEditText(R.id.streetEditText, user.getAddress().getStreet());
        setTextInEditText(R.id.cityEditText, user.getAddress().getCity());
        setTextInEditText(R.id.emailEditText, user.getEmailAddress());

        if(user.getAddress().getPostCode() != null) {
            setTextInEditText(R.id.postCodeEditText1, user.getAddress().getPostCode().substring(0, 2));
            setTextInEditText(R.id.postCodeEditText2, user.getAddress().getPostCode().substring(3, 6));
        }
        else{
            setTextInEditText(R.id.postCodeEditText1, null);
            setTextInEditText(R.id.postCodeEditText2, null);
        }

        if(user.getAddress().getFlatNumber() != null &&  user.getAddress().getFlatNumber() >= 0)
            setTextInEditText(R.id.flatNumberEditText, String.valueOf(user.getAddress().getFlatNumber()));
        else
            setTextInEditText(R.id.flatNumberEditText, null);

        if(user.getAddress().getHouseNumber() != null)
            setTextInEditText(R.id.houseNumberEditText, String.valueOf(user.getAddress().getHouseNumber()));
        else
            setTextInEditText(R.id.houseNumberEditText, null);

        setTextInEditText(R.id.shortInfoEditText, user.getShortInfo());
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
