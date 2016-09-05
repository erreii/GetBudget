package erreii.takenote.pages;


import android.app.Activity;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

import erreii.takenote.DBHelper;
import erreii.takenote.MainActivity;
import erreii.takenote.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCategory extends Fragment {
    private Activity titleChange;
    private Button category_save_btn;
    private String fragment_name = "";
    private DBHelper db;
    private String current_time;
    private EditText addNewCategory;

    public AddCategory() {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        titleChange.setTitle("Add Category");

        DateFormat df = new SimpleDateFormat("d MMM yyyy, HH:mm");
        current_time = df.format(Calendar.getInstance().getTime());

        db = new DBHelper(getActivity());

        View rootView = inflater.inflate(R.layout.fragment_add_category, container, false);

        addNewCategory = (EditText) rootView.findViewById(R.id.addNewCategory);
        category_save_btn = (Button) rootView.findViewById(R.id.categoryNew);

        category_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.insertCategory(addNewCategory.getText().toString(), current_time);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        titleChange = (MainActivity) activity;
    }

}
