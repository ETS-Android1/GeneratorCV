package pl.remindapp.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import pl.remindapp.R;
import pl.remindapp.models.AbilityModel;

public class AbilityAdapter extends ArrayAdapter<AbilityModel> {
    private  List<AbilityModel> objects;

    public AbilityAdapter(@NonNull Context context, int resource, @NonNull List<AbilityModel> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ability_item, parent, false);
        }

        final EditText editText = convertView.findViewById(R.id.listItemEditText);
        editText.setText(objects.get(position).getAbility());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(position < objects.size())
                    objects.get(position).setAbility(s.toString());
            }
        });

        final Button deleteButon = convertView.findViewById(R.id.listItemDeleteButton);
        deleteButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objects.remove(position);
                AbilityAdapter.this.notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public List<AbilityModel> getObjects() {
        return objects;
    }
}
