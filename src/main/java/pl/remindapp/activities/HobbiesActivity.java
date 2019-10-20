package pl.remindapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import pl.remindapp.R;
import pl.remindapp.adapters.AbilityAdapter;
import pl.remindapp.cvObjects.Person;
import pl.remindapp.models.AbilityModel;

public class HobbiesActivity extends AppCompatActivity {
    private ListView listView;
    private AbilityAdapter abilityAdapter;
    private LinearLayout mainLayout;
    private Button addButton, nextButton;
    private ArrayList<AbilityModel> hobbies;
    private TextView titleTextView;
    private Person user;
    private final int EDUCATION_CODE = 125;
    private final String USER_DATA = "user_data";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == EDUCATION_CODE && resultCode == RESULT_OK){
            user = (Person)data.getSerializableExtra(USER_DATA);

            hobbies = new ArrayList<AbilityModel>();
            for(String element : user.getSkills()){
                hobbies.add(new AbilityModel(element));
            }

            abilityAdapter = new AbilityAdapter(this, R.layout.ability_item, hobbies);
            listView.setAdapter(abilityAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        List<String> hobbies = new ArrayList<>();
        for(int i = 0; i < abilityAdapter.getCount(); i++){
            hobbies.add(abilityAdapter.getItem(i).toString());
        }
        user.setInterest(hobbies);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(USER_DATA, user);

        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_container_layout);

        user = (Person) getIntent().getSerializableExtra(USER_DATA);

        addButton = findViewById(R.id.addButton);
        nextButton = findViewById(R.id.nextButton);
        mainLayout = findViewById(R.id.abilitiesMainLayout);
        listView = findViewById(R.id.listView);

        titleTextView = findViewById(R.id.titleTextView);

        titleTextView.setText(R.string.hobbies);
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int heightDiff = mainLayout.getRootView().getHeight() - mainLayout.getHeight();
                if(heightDiff > 216){
                    nextButton.setVisibility(View.INVISIBLE);
                    addButton.setVisibility(View.INVISIBLE);
                }
                else{
                    nextButton.setVisibility(View.VISIBLE);
                    addButton.setVisibility(View.VISIBLE);
                }
            }
        });

        listView.setDivider(null);
        hobbies = new ArrayList<AbilityModel>();
        for(String element : user.getInterest()){
            hobbies.add(new AbilityModel(element));
        }

        abilityAdapter = new AbilityAdapter(this, R.layout.ability_item, hobbies);

        listView.setAdapter(abilityAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(HobbiesActivity.this);

                final View customLayout = getLayoutInflater().inflate(R.layout.add_ability_layout, null);

                final TextView title = customLayout.findViewById(R.id.addAbilityTitle);
                title.setText(R.string.hobby);

                final EditText input = customLayout.findViewById(R.id.addAbilityEditText);
                input.setHint(R.string.hobbyExample);
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

                builder.setView(customLayout);
                final Dialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.gradient_rect));


                final Button confirmButton = customLayout.findViewById(R.id.addAbilityConfirmButton);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addElementToListView(input.getText().toString());
                        dialog.dismiss();
                    }
                });

                final Button cancelButton = customLayout.findViewById(R.id.addAbilityCancelButton);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });


                dialog.show();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> hobbies = new ArrayList<>();
                for(int i = 0; i < abilityAdapter.getCount(); i++){
                    hobbies.add(abilityAdapter.getItem(i).toString());
                }
                user.setInterest(hobbies);
                Intent intent = new Intent(HobbiesActivity.this, EducationActivity.class);
                intent.putExtra(USER_DATA, user);
                startActivityForResult(intent, EDUCATION_CODE);
            }
        });
    }

    private void addElementToListView(String a){
        abilityAdapter.add(new AbilityModel(a));
        abilityAdapter.notifyDataSetChanged();
    }
}
