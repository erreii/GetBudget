package erreii.takenote;

import android.app.Dialog;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;

import java.util.ArrayList;
import java.util.Calendar;

import erreii.takenote.Model.Category;

import static android.view.View.inflate;

/**
 * Created by Asus on 9/8/2016.
 */

public class EditCategory extends AppCompatActivity {

    private Toolbar toolbar;
    private String current_time;
    private EditText editCat_dia_edit;
    private DBHelper db;

    private ArrayList catList;
    private ArrayList catIdList;
    private DynamicListView cateListView;
    private ArrayList<Category> mDataList = new ArrayList<Category>();
    private Category listOfobj = null;
    private MyAdapter myAdapter;
    private Category item;

    public static final String CATEGORIES_ID = "cat_id";
    public static final String CATEGORIES_NAME = "categories_name";
    public static final String CATEGORIES_CREATION_TIME = "categry_createtime";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcategory);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DBHelper(this);
        setTitle("Edit Category");

        DateFormat df = new SimpleDateFormat("d MMM yyyy, HH:mm");
        current_time = df.format(Calendar.getInstance().getTime());

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        cateListView = (DynamicListView) findViewById(R.id.dynlistEdtCat);

        myAdapter = new MyAdapter();
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(myAdapter);
        animationAdapter.setAbsListView(cateListView);
        cateListView.setAdapter(animationAdapter);

        catList = db.getAllCategories(CATEGORIES_NAME);
        catIdList = db.getAllCategories(CATEGORIES_ID);

        for (int i = 0; i < catList.size(); i++) {
            listOfobj = new Category(Integer.valueOf(catIdList.get(i).toString()), catList.get(i).toString(),Integer.valueOf(catIdList.get(i).toString()));
            mDataList.add(listOfobj);
        }

        cateListView.enableDragAndDrop();
        cateListView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(final AdapterView<?> parent, final View view,
                                                   final int position, final long id) {
                        cateListView.startDragging(position);
                        //cateListView.setBackgroundColor(Color.GRAY);
                        return true;
                    }
                }
        );

        cateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                item = (Category) cateListView.getItemAtPosition(arg2);

                final Dialog dia = new Dialog(EditCategory.this);
                dia.setContentView(R.layout.addcategory_dialog);
                TextView diaHeader = (TextView) dia.findViewById(R.id.addCat_dialog_txt);
                diaHeader.setText("Edit Category");

                TextView text = (TextView) dia.findViewById(R.id.addCat_dia_txtInfo);
                text.setText("Rename Category");
                editCat_dia_edit = (EditText) dia.findViewById(R.id.addCat_dia_edit);
                editCat_dia_edit.setText(item.getCatName());

                Button addCat_dia_ok = (Button) dia.findViewById(R.id.addCatDiaEditBtn);
                addCat_dia_ok.setTextColor(Color.RED);

                addCat_dia_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editCat_dia_edit.getText().toString().trim().length() > 0) {
                            if (db.updateCategory(item.getCatId(), editCat_dia_edit.getText().toString())) {
                                dia.dismiss();
                                mDataList.get(item.getCatId() - 1).setCatName(editCat_dia_edit.getText().toString());
                                myAdapter.setData(mDataList);
                                myAdapter.notifyDataSetChanged();
                            } else {
                                dia.dismiss();
                                Toast.makeText(EditCategory.this, "Could not saved the category", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            dia.dismiss();
                            Toast.makeText(EditCategory.this, "Same category is not saved", Toast.LENGTH_LONG).show();
                        }

                    }
                });

                dia.getWindow().setLayout((int) (EditCategory.this.getWindow().peekDecorView().getWidth() * 0.8), 800);
                dia.show();
            }
        });



        cateListView.enableSwipeToDismiss(
                new OnDismissCallback() {
                    @Override
                    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            myAdapter.remove(position);
                        }

                        mDataList.remove(reverseSortedPositions);
                        myAdapter.setData(mDataList);
                        myAdapter.notifyDataSetChanged();

                        /*for (int pso :reverseSortedPositions)
                        {
                            db.deleteCategory(mDataList.get(pso).getCatId());
                        }*/

                    }
                }
        );



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editcat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        switch (item.getItemId()) {
            case R.id.menuCat_add:
                final Dialog dialog = new Dialog(EditCategory.this);
                dialog.setContentView(R.layout.addcategory_dialog);

                TextView text = (TextView) dialog.findViewById(R.id.addCat_dia_txtInfo);
                text.setText("New category in which name you want");
                editCat_dia_edit = (EditText) dialog.findViewById(R.id.addCat_dia_edit);

                Button addCat_dia_ok = (Button) dialog.findViewById(R.id.addCatDiaEditBtn);
                addCat_dia_ok.setTextColor(Color.RED);

                addCat_dia_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editCat_dia_edit.getText().toString().trim().length() > 0) {
                            if (db.insertCategory(editCat_dia_edit.getText().toString(), current_time,1)) {
                                mDataList.add(new Category(mDataList.size() + 1, editCat_dia_edit.getText().toString(), mDataList.size() + 1));
                                myAdapter.setData(mDataList);
                                myAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(EditCategory.this, "Could not saved the category", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            dialog.dismiss();
                            Toast.makeText(EditCategory.this, "Blank category is not saved", Toast.LENGTH_LONG).show();
                        }

                    }
                });

                dialog.getWindow().setLayout((int) (EditCategory.this.getWindow().peekDecorView().getWidth() * 0.8), 800);
                dialog.show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyAdapter extends BaseAdapter implements UndoAdapter {

        @Override
        public boolean hasStableIds() {
            return true;
        }

        public void setData(ArrayList<Category> list) {
            mDataList = list;
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Category getItem(int position) {
            return mDataList.get(position);
        }

        public Category remove(int position) {
            return mDataList.remove(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final EditCategory.ViewHolder holder;
            if (convertView == null) {
                convertView = inflate(getApplicationContext(), R.layout.cat_list_layout, null);
                holder = new EditCategory.ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (EditCategory.ViewHolder) convertView.getTag();
            }

            Category item = getItem(position);

            holder.catname.setText(item.getCatName());
            return convertView;
        }

        @NonNull
        @Override
        public View getUndoView(int position, @NonNull final View view, @NonNull ViewGroup parent) {

            /*View undoView = ((SwipeUndoView) view).getUndoView();*/

            //Seloooooo
            View undoView = View.inflate(getApplicationContext(), R.layout.list_item_layout, null);

            if (undoView == null) {
                throw new IllegalStateException("undoView == null");
            }
            return undoView;
        }

        @NonNull
        @Override
        public View getUndoClickView(@NonNull View view) {
            return null;
        }
    }

    private static class ViewHolder {

        private View view;

        private TextView catname;

        private ViewHolder(View view) {
            this.view = view;
            catname = (TextView) view.findViewById(R.id.catListItem);
        }
    }

}
