package pl.remindapp.adapters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.List;

import pl.remindapp.R;
import pl.remindapp.activities.EducationActivity;
import pl.remindapp.cvObjects.LifeEvent;

public class LifeEventAdapter extends ArrayAdapter<LifeEvent> {
    private List<LifeEvent> lifeEvents;
    public LifeEventAdapter(@NonNull Context context, int resource, @NonNull List<LifeEvent> objects) {
        super(context, resource, objects);
        lifeEvents = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final LifeEvent lifeEvent = lifeEvents.get(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.life_event_list_item, parent, false);
        }

        final EditText titleEditText = convertView.findViewById(R.id.lifeEventTitleListItem);
        titleEditText.setText(lifeEvent.getTitle());
        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(position < lifeEvents.size())
                    lifeEvents.get(position).setTitle(s.toString());
            }
        });

        final EditText descriptionEditText = convertView.findViewById(R.id.descriptionLifeEventEditText);
        descriptionEditText.setText(lifeEvent.getDescription());
        descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(position < lifeEvents.size())
                    lifeEvents.get(position).setDescription(s.toString());
            }
        });

        final EditText beginDateEditText = convertView.findViewById(R.id.beginDate);
        if(lifeEvent.getBegin() != null)
            beginDateEditText.setText(lifeEvent.getBegin().toString());
        else
            beginDateEditText.setText("-");

        final EditText endDateEditText = convertView.findViewById(R.id.endDate);
        if (lifeEvent.getEnd() != null)
            endDateEditText.setText(lifeEvent.getEnd().toString());
        else
            endDateEditText.setText("-");

        final Button deleteButton = convertView.findViewById(R.id.listItemDeleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lifeEvents.remove(position);
                LifeEventAdapter.this.notifyDataSetChanged();
            }
        });

        final ImageButton editBeginDateButton = convertView.findViewById(R.id.beginDateButtonListItem);
        editBeginDateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog;
                if(lifeEvents.get(position).getBegin() == null)
                    datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_DARK);
                else {
                    datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_DARK,
                            null, lifeEvents.get(position).getBegin().getYear(),
                            lifeEvents.get(position).getBegin().getMonthValue() - 1, lifeEvents.get(position).getBegin().getDayOfMonth());
                }
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        lifeEvents.get(position).setBegin(LocalDate.of(year, month+1, dayOfMonth));
                        LifeEventAdapter.this.notifyDataSetChanged();
                    }
                });
                datePickerDialog.show();
            }
        });

        final ImageButton editEndDateButton = convertView.findViewById(R.id.endDateButtonListItem);
        editEndDateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog;
                if(lifeEvents.get(position).getEnd() == null)
                    datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_DARK);
                else {
                    datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_DARK,
                            null, lifeEvents.get(position).getEnd().getYear(),
                            lifeEvents.get(position).getEnd().getMonthValue() - 1, lifeEvents.get(position).getEnd().getDayOfMonth());
                }
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        lifeEvents.get(position).setEnd(LocalDate.of(year, month+1, dayOfMonth));
                        LifeEventAdapter.this.notifyDataSetChanged();
                    }
                });
                datePickerDialog.show();
            }
        });

        return convertView;
    }
}
