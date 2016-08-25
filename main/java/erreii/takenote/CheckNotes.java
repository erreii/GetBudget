package erreii.takenote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Asus on 8/18/2016.
 */

public class CheckNotes extends Activity {
    private ListView listNotes;
    private Button checkNotes_back_btn;
    private DBHelper db;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        img = (ImageView)findViewById(R.id.imageView4);

        db = new DBHelper(this);

        ArrayList listOfNotes = db.getAllNotes();

        if(listOfNotes.size() < 3 ){
            checkNotes_back_btn.setVisibility(View.INVISIBLE);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listOfNotes);

        listNotes = (ListView) findViewById(R.id.listNotes);
        listNotes.setAdapter(arrayAdapter);
        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                int id_To_Search = arg2 + 1;

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", id_To_Search);

                Intent intent = new Intent(getApplicationContext(), DisplayNotes.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
        checkNotes_back_btn = (Button) findViewById(R.id.checkNotes_back_btn);
        checkNotes_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });
    }
}
