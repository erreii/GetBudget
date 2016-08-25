package erreii.takenote;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button addBtn, checkBtn;
    private ImageView main_app_img;
    private DBHelper db;
    private ListView listNotes;
    private RelativeLayout main_listnotes_lay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = (Button) findViewById(R.id.addNote);
        main_app_img = (ImageView) findViewById(R.id.main_app_image);
        main_listnotes_lay = (RelativeLayout) findViewById(R.id.main_listnotes_lay);
        db = new DBHelper(this);

        ArrayList listOfNotes = db.getAllNotes();
        if (!listOfNotes.isEmpty()) {
            main_listnotes_lay.setVisibility(View.VISIBLE);
            main_app_img.setVisibility(View.INVISIBLE);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listOfNotes);

        listNotes = (ListView) findViewById(R.id.listNotes);
        listNotes.setAdapter(arrayAdapter);
        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int id_To_Search = arg2 + 1;

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", id_To_Search);

                Intent intent = new Intent(getApplicationContext(), DisplayNotes.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNewPage = new Intent(MainActivity.this, AddItem.class);
                startActivity(addNewPage);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        MenuItem shareItem = menu.findItem(R.id.menu_share);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.Delete:
                Toast.makeText(getApplicationContext(), "Delete", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
