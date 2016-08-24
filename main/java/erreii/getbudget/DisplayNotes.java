package erreii.getbudget;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Asus on 8/16/2016.
 */

public class DisplayNotes extends Activity {
    private TextView dispSubject;
    private TextView dispNote;
    private DBHelper db;
    private ImageButton edit_disp_btn;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_displaynote);

        db = new DBHelper(this);

        dispSubject = (TextView) findViewById(R.id.displaySubject);
        dispNote = (TextView) findViewById(R.id.displayNote);
        edit_disp_btn = (ImageButton) findViewById(R.id.edit_disp_btn);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int value = extras.getInt("id");

            if(value>0){
                Cursor rs = db.getData(value);
                rs.moveToFirst();

                String subject = rs.getString(rs.getColumnIndex(DBHelper.NOTES_COLUMN_SUBJECT));
                String note = rs.getString(rs.getColumnIndex(DBHelper.NOTES_COLUMN_NOTE));

                if (!rs.isClosed())
                {
                    rs.close();
                }

                dispSubject.setText((CharSequence)subject);
                dispSubject.setFocusable(false);
                dispSubject.setClickable(false);

                dispNote.setText((CharSequence)note);
                dispNote.setFocusable(false);
                dispNote.setClickable(false);

            }
        }

        edit_disp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNewPage = new Intent(getApplicationContext(), EditNote.class);
                startActivity(addNewPage);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Bundle extras = getIntent().getExtras();

        if(extras !=null)
        {
            int Value = extras.getInt("id");
            if(Value>0){
                getMenuInflater().inflate(R.menu.menu, menu);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch(item.getItemId())
        {
            case R.id.Delete:
                Toast.makeText(getApplicationContext(),"Delete",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.Search:
                Toast.makeText(getApplicationContext(),"Search",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


}
