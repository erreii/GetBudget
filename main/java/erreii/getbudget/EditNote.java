package erreii.getbudget;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Asus on 8/23/2016.
 */

public class EditNote extends Activity {

    private DBHelper db;
    private TextView editSubject_txt, noteEdit_txt;
    private Button confirmEdit;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_edit);

        db = new DBHelper(this);

        editSubject_txt = (TextView) findViewById(R.id.noteEditSubject);
        noteEdit_txt = (TextView) findViewById(R.id.noteEdit_txv);


        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int value = extras.getInt("id");

            if (value > 0) {
                Cursor rs = db.getData(value);
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
            });

        }
    }
}
