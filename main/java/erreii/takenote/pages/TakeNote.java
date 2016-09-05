package erreii.takenote.pages;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.Calendar;

import erreii.takenote.AddItem;
import erreii.takenote.DBHelper;
import erreii.takenote.DisplayNotes;
import erreii.takenote.MainActivity;
import erreii.takenote.R;
import erreii.takenote.listItems.DrawableProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class TakeNote extends Fragment {
    private Button addBtn;
    private ImageView main_app_img;
    private DBHelper db;
    private RelativeLayout main_listnotes_lay;

    private ShareActionProvider mShareActionProvider;

    private static final int HIGHLIGHT_COLOR = 0x99E9EFF2;

    // list of data items
    private ArrayList<ListData> mDataList = new ArrayList<ListData>();

    // declare the color generator and drawable builder
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;

    public static final String NOTE_SUBJECT = "subject";
    public static final String NOTE_NOTE = "note";
    public static final String NOTE_TIME = "creationtime";

    public TakeNote() {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_take_note, container, false);

        addBtn = (Button) rootView.findViewById(R.id.addNote);
        main_app_img = (ImageView) rootView.findViewById(R.id.main_app_image);
        main_listnotes_lay = (RelativeLayout) rootView.findViewById(R.id.main_listnotes_lay);

        Intent intent = getActivity().getIntent();
        int type = intent.getIntExtra(MainActivity.TYPE, DrawableProvider.SAMPLE_ROUND);

        switch (type) {
            case DrawableProvider.SAMPLE_ROUND:
                mDrawableBuilder = TextDrawable.builder()
                        .round();
                break;
        }

        db = new DBHelper(container.getContext());

        ArrayList listNotesSubject = new ArrayList();
        listNotesSubject = db.getAllNotes(NOTE_SUBJECT);

        ArrayList listNotesNote = new ArrayList();
        listNotesNote = db.getAllNotes(NOTE_NOTE);

        ArrayList listNotesTime = new ArrayList();
        ArrayList editNotesTime = new ArrayList();
        DateFormat df = new SimpleDateFormat("d MMM yyyy");
        String current_time = df.format(Calendar.getInstance().getTime());
        listNotesTime = db.getAllNotes(NOTE_TIME);

        for (int i = 0; i < listNotesTime.size(); i++) {
            String note_edit_time = listNotesTime.get(i).toString();
            if (note_edit_time.split(",")[0].equals(current_time)) {
                String edtTime = note_edit_time.split(",")[1];
                editNotesTime.add(edtTime);
            } else {
                editNotesTime.add(listNotesTime.get(i).toString().split(",")[0]);
            }
        }

        if (!listNotesSubject.isEmpty()) {
            main_listnotes_lay.setVisibility(View.VISIBLE);
            main_app_img.setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < listNotesSubject.size(); i++) {
            String note_subject = listNotesSubject.get(i).toString();
            String note_note = listNotesNote.get(i).toString();
            String note_time = editNotesTime.get(i).toString();
            ListData listOfobj = new ListData(note_subject, note_note, note_time);
            mDataList.add(listOfobj);
        }

        // init the list view and its adapter
        ListView listView = (ListView) rootView.findViewById(R.id.listNotes);
        listView.setAdapter(new SampleAdapter());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int id_To_Search = arg2;

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", id_To_Search);

                Intent intent = new Intent(getActivity(), DisplayNotes.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });

        /**
         * Bundle bundle=getArguments();

         if(bundle.getBoolean("fromNavMenu")){
         listNotes = db.getNotesWithCat(bundle.getString("category"));
         }
         else
         {
         listNotes = db.getAllNotes();
         }
         */

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNewPage = new Intent(getActivity(), AddItem.class);
                startActivity(addNewPage);
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    private class SampleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public ListData getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.list_item_layout, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ListData item = getItem(position);

            // provide support for selected state
            updateCheckedState(holder, item);
            //holder.imageView.setImageBitmap();
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // when the image is clicked, update the selected state
                    ListData data = getItem(position);
                    data.setChecked(!data.isChecked);
                    updateCheckedState(holder, data);
                }
            });
            holder.textView_subject.setText(item.note_subject);
            //selo1
            holder.textView_note.setText(item.note_note);
            holder.textView_time.setText(item.note_time);
            /*holder.note_favorite.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"Heloo",Toast.LENGTH_LONG).show();
                }
            });*/
            return convertView;
        }

        private void updateCheckedState(ViewHolder holder, ListData item) {
            if (item.isChecked) {
                holder.imageView.setImageDrawable(mDrawableBuilder.build(" ", 0xff556270));
                holder.view.setBackgroundColor(HIGHLIGHT_COLOR);
                holder.checkIcon.setVisibility(View.VISIBLE);
            } else {
                TextDrawable drawable = mDrawableBuilder.build(String.valueOf(item.note_subject.charAt(0)).toUpperCase(), mColorGenerator.getColor(item.note_subject));
                holder.imageView.setImageDrawable(drawable);
                holder.view.setBackgroundColor(Color.TRANSPARENT);
                holder.checkIcon.setVisibility(View.GONE);
            }
        }
    }

    private static class ViewHolder {

        private View view;

        private ImageView imageView;

        private TextView textView_subject, textView_note, textView_time;

        private ImageButton note_favorite;

        private ImageView checkIcon;

        private ViewHolder(View view) {
            this.view = view;
            imageView = (ImageView) view.findViewById(R.id.imageView);
            textView_subject = (TextView) view.findViewById(R.id.textView_subject);
            textView_note = (TextView) view.findViewById(R.id.textView_note);
            textView_time = (TextView) view.findViewById(R.id.textView_time);
            //note_favorite = (ImageButton) view.findViewById(R.id.note_favorite);
            checkIcon = (ImageView) view.findViewById(R.id.check_icon);
        }
    }

    private static class ListData {

        private String note_subject;
        private String note_note;
        private String note_time;

        private boolean isChecked;

        public ListData(String note_subject, String note_note, String note_time) {
            this.note_subject = note_subject;
            this.note_note = note_note;
            this.note_time = note_time;
        }

        public void setChecked(boolean isChecked) {
            this.isChecked = isChecked;
        }
    }
}
