package pl.remindapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
        abilityAdapter = new AbilityAdapter(this, R.layout.ability_item, abilities);

        abilitiesListView.setAdapter(abilityAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AbilitiesActivity.this);
                builder.setTitle(R.string.skill);

                final EditText input = new EditText(AbilitiesActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addElementToListView(input.getText().toString());
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
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
