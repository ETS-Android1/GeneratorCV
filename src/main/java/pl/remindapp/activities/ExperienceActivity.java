package pl.remindapp.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import pl.remindapp.R;
import pl.remindapp.adapters.LifeEventAdapter;
import pl.remindapp.cvObjects.LifeEvent;
import pl.remindapp.cvObjects.Person;

public class ExperienceActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Person user;
    private LifeEventAdapter lifeEventAdapter;
    private LinearLayout mainLayout;
    private Button addButton, nextButton;
    private ArrayList<LifeEvent> lifeEventArrayList;
    private TextView titleTextView;
    private ListView listView;
    private int whichDate; // 1 - begin, 2 - end
    private LocalDate beginDate, endDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_view_container_layout);

        beginDate = endDate = null;
        user = (Person) getIntent().getSerializableExtra("user_data");

        addButton = findViewById(R.id.addButton);
        nextButton = findViewById(R.id.nextButton);
        mainLayout = findViewById(R.id.abilitiesMainLayout);
        listView = findViewById(R.id.listView);

        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(R.string.experience);

        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = mainLayout.getRootView().getHeight() - mainLayout.getHeight();
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
                    float scale = getApplicationContext().getResources().getDisplayMetrics().density;
                    int pixels = (int) (10 * scale + 0.5f);
                    param.setMargins(0,0,0, pixels);
                    addButton.setLayoutParams(param);
                    nextButton.setLayoutParams(param);
                }
            }
        });

        lifeEventArrayList = new ArrayList<LifeEvent>();

        lifeEventAdapter = new LifeEventAdapter(this, R.layout.life_event_list_item, lifeEventArrayList);

        listView.setAdapter(lifeEventAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ExperienceActivity.this);
                builder.setTitle("Dodawanie doświadczenie zawodowego:");

                LinearLayout linearLayout = new LinearLayout(ExperienceActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                final EditText titleEditText = new EditText(ExperienceActivity.this);
                titleEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                titleEditText.setHint("Tytuł");

                final EditText descriptionEditText = new EditText(ExperienceActivity.this);
                descriptionEditText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                descriptionEditText.setHint("Opis");

                final Button beginDateButton = new Button(ExperienceActivity.this);
                beginDateButton.setText(R.string.beginDate);
                beginDateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        whichDate = 1;
                        DatePickerDialog datePickerDialog = new DatePickerDialog(ExperienceActivity.this, AlertDialog.THEME_HOLO_DARK, ExperienceActivity.this, 1998, 5, 3);
                        datePickerDialog.show();
                    }
                });

                final Button endDateButton = new Button(ExperienceActivity.this);
                endDateButton.setText(R.string.endDate);
                endDateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        whichDate = 2;
                        DatePickerDialog datePickerDialog = new DatePickerDialog(ExperienceActivity.this, AlertDialog.THEME_HOLO_DARK, ExperienceActivity.this, 1998, 5, 3);
                        datePickerDialog.show();
                    }
                });

                linearLayout.addView(titleEditText);
                linearLayout.addView(descriptionEditText);
                linearLayout.addView(beginDateButton);
                linearLayout.addView(endDateButton);

                builder.setView(linearLayout);
                builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addElementToListView(titleEditText.getText().toString(), descriptionEditText.getText().toString());
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
                List<LifeEvent> experience = new ArrayList<>();
                for(int i = 0; i < lifeEventAdapter.getCount(); i++){
                    experience.add(lifeEventAdapter.getItem(i));
                }
                user.setExperience(experience);
                Intent intent = new Intent(ExperienceActivity.this, CoursesActivity.class);
                intent.putExtra("user_data", user);
                startActivity(intent);
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(whichDate == 1){
            beginDate = LocalDate.of(year, month, dayOfMonth);
        }
        else if(whichDate == 2){
            endDate  = LocalDate.of(year, month, dayOfMonth);
        }
    }

    private void addElementToListView(String title, String description){
        lifeEventAdapter.add(new LifeEvent(beginDate, endDate, title, description));
        lifeEventAdapter.notifyDataSetChanged();
    }
}
