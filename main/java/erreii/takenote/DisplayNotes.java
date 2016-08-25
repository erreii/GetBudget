package erreii.takenote;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Asus on 8/16/2016.
 */

public class DisplayNotes extends Activity {
    private TextView dispSubject,dispNote,category_disp;
    private DBHelper db;
    private ImageButton edit_disp_btn,delete_disp_btn;
    private String selected_id;
    final Context context = this;
    public static final String NOTES_TABLE_NAME = "notes";
    public static final String CATEGORIES_TABLE_NAME = "categories";


    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_displaynote);

        db = new DBHelper(this);

        dispSubject = (TextView) findViewById(R.id.displaySubject);
        dispNote = (TextView) findViewById(R.id.displayNote);
        edit_disp_btn = (ImageButton) findViewById(R.id.edit_disp_btn);
        delete_disp_btn = (ImageButton) findViewById(R.id.delete_disp_btn);
        category_disp = (TextView) findViewById(R.id.category_disp);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int value = extras.getInt("id");

            if(value>0){
                Cursor rs = db.getData(value,NOTES_TABLE_NAME);
                rs.moveToFirst();

                selected_id = rs.getString(rs.getColumnIndex(DBHelper.NOTES_COLUMN_ID));
                String subject = rs.getString(rs.getColumnIndex(DBHelper.NOTES_COLUMN_SUBJECT));
                String note = rs.getString(rs.getColumnIndex(DBHelper.NOTES_COLUMN_NOTE));
                String category = rs.getString(rs.getColumnIndex(DBHelper.NOTES_COLUMN_CATEGORY));

                dispSubject.setText((CharSequence)subject);
                dispSubject.setFocusable(false);
                dispSubject.setClickable(false);

                dispNote.setText((CharSequence)note);
                dispNote.setFocusable(false);
                dispNote.setClickable(false);

                category_disp.setText((CharSequence)category);
                category_disp.setFocusable(false);
                category_disp.setClickable(false);

            }
        }

        edit_disp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNewPage = new Intent(getApplicationContext(), EditNote.class);
                startActivity(addNewPage);
            }
        });

        delete_disp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Delete");


                alertDialogBuilder
                        .setMessage("Are you sure you want to delete selected note?")
                        .setCancelable(false)
                        .setPositiveButton("YES",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                db.deleteNote(Integer.valueOf(selected_id));
                            }
                        })
                        .setNegativeButton("NO",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                Button yes = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                yes.setTextColor(Color.RED);

                Button no = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                no.setTextColor(Color.GRAY);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

            default:
                return super.onOptionsItemSelected(item);

        }
    }


}
