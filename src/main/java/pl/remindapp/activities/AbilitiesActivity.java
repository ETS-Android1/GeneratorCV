package pl.remindapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

public class AbilitiesActivity extends AppCompatActivity {
    private ListView abilitiesListView;
    private AbilityAdapter abilityAdapter;
    private LinearLayout mainLayout;
    private Button addButton, nextButton;
    private ArrayList<AbilityModel> abilities;
    private Person user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_container_layout);

        user = (Person) getIntent().getSerializableExtra("user_data");

        addButton = findViewById(R.id.addButton);
        nextButton = findViewById(R.id.nextButton);
        mainLayout = findViewById(R.id.abilitiesMainLayout);
        abilitiesListView = findViewById(R.id.listView);

        final TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(R.string.skills);

        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int heightDiff = mainLayout.getRootView().getHeight() - mainLayout.getHeight();
                if(heightDiff > 216){
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0, 0.0f
                    );
                    param.setMargins(0,0,0, 0);
                    addButton.setLayoutParams(param);
                    nextButton.setLayoutParams(param);
                }
                else{
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0, 0.6f
                    );
                    final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
                    final int pixels = (int) (10 * scale + 0.5f);
                    param.setMargins(0,0,0, pixels);
                    addButton.setLayoutParams(param);
                    nextButton.setLayoutParams(param);
                }
            }
        });

        abilitiesListView.setDivider(null);
        abilities = new ArrayList<AbilityModel>();
        for(String element : user.getSkills()){
            abilities.add(new AbilityModel(element));
        }
        abilityAdapter = new AbilityAdapter(this, R.layout.ability_item, abilities);

        abilitiesListView.setAdapter(abilityAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AbilitiesActivity.this);

                final View customLayout = getLayoutInflater().inflate(R.layout.add_ability_layout, null);

                final EditText input = customLayout.findViewById(R.id.addAbilityEditText);

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
                List<String> skills = new ArrayList<>();
                for(int i = 0; i < abilityAdapter.getCount(); i++){
                    skills.add(abilityAdapter.getItem(i).toString());
                }
                user.setSkills(skills);
                Intent intent = new Intent(AbilitiesActivity.this, HobbiesActivity.class);
                intent.putExtra("user_data", user);
                startActivity(intent);
            }
        });
    }

    private void addElementToListView(String a){
        abilityAdapter.add(new AbilityModel(a));
        abilityAdapter.notifyDataSetChanged();
    }
}
