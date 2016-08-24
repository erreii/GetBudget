package erreii.getbudget;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Asus on 8/16/2016.
 */
public class AddItem extends AppCompatActivity {

    Button add;
    TextView notes, date;
    TextView subject;
    int id_To_Update = 0;
    private DBHelper db;
    private LinearLayout add_info_lay;
    private Switch add_info_switch;
    private Spinner add_categories;
    private ArrayAdapter<String> dataForCategories;

    private String[] iller = {"Default", "Music"};

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        db = new DBHelper(this);

        add_info_lay = (LinearLayout) findViewById(R.id.add_info_lay);
        add_categories = (Spinner) findViewById(R.id.add_categories);
        subject = (EditText) findViewById(R.id.addNewSubject);
        notes = (EditText) findViewById(R.id.noteUser);
        date = (TextView) findViewById(R.id.currentDate);
        add = (Button) findViewById(R.id.confirmNew);
        add_info_switch = (Switch) findViewById(R.id.add_info_switch);

        dataForCategories = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, iller);
        dataForCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_categories.setAdapter(dataForCategories);

        if(!add_info_switch.isChecked()){
            add_info_lay.setVisibility(View.INVISIBLE);
        }
        add_info_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked) {
                    add_info_lay.setVisibility(View.VISIBLE);
                } else {
                    add_info_lay.setVisibility(View.INVISIBLE);
                }
            }
        });

        add_categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (parent.getSelectedItem().toString().equals(iller[0])) {
                    //Toast.makeText(getApplicationContext(), "Default", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getApplicationContext(), "Music", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        DateFormat df = new SimpleDateFormat("d MMM yyyy, HH:mm");
        String current_time = df.format(Calendar.getInstance().getTime());

        date.setText(current_time);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subject.getText().toString().trim().length() != 0 &&
                        notes.getText().toString().trim().length() != 0) {

                    if (db.insertNote(subject.getText().toString(), notes.getText().toString(),date.getText().toString(),category.getText.toString())) {
                        Toast.makeText(getApplicationContext(), "Successfully saved!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please save it later...", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter all the parts.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

