package erreii.takenote;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Asus on 8/23/2016.
 */

public class EditNote extends AppCompatActivity {

    private DBHelper db;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_edit);

        db = new DBHelper(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Edit Note");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        /*editSubject_txt = (TextView) findViewById(R.id.noteEditSubject);
        noteEdit_txt = (TextView) findViewById(R.id.noteEdit_txv);


        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int value = extras.getInt("id");

            if (value > 0) {
                Cursor rs = db.getData(value,NOTES_TABLE_NAME);
                rs.moveToFirst();

                String subject = rs.getString(rs.getColumnIndex(DBHelper.NOTES_COLUMN_SUBJECT));
                String note = rs.getString(rs.getColumnIndex(DBHelper.NOTES_COLUMN_NOTE));


                if (!rs.isClosed())
                {
                    rs.close();
                }

                editSubject_txt.setText(subject);
                editSubject_txt.setFocusable(false);
                editSubject_txt.setClickable(false);

                noteEdit_txt.setText(note);
                noteEdit_txt.setFocusable(false);
                noteEdit_txt.setClickable(false);
            }
            confirmEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),editSubject_txt.getText(),Toast.LENGTH_SHORT).show();
                }
            });*/


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        /*switch (item.getItemId()) {
            case R.id.menuAdd_addImg:
                selectImage();
                break;
            default:
                break;

        }*/
        return super.onOptionsItemSelected(item);

    }
}
