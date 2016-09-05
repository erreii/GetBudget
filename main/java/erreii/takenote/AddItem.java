package erreii.takenote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import erreii.takenote.Model.Note;
import erreii.takenote.pages.TakeNote;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

/**
 * Created by Asus on 8/16/2016.
 */
public class AddItem extends AppCompatActivity {

    private final static String allCategory = "All Notes";
    private static final String IMAGE_DIRECTORY_NAME = "TakeNote";

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private TextView notes, date;
    private TextView subject;
    private DBHelper db;
    private LinearLayout add_info_lay;
    private Switch add_info_switch;
    private Spinner add_categories;
    private ArrayAdapter<String> dataForCategories;

    private String category;
    private Toolbar toolbar;
    private String userChoosenTask;
    private ImageView addImage_img;
    private LinearLayout preview_add_img;
    private ImageButton del_preview_img;
    private String imagePathtoDB;
    private Uri fileUri;
    private Note note;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        setTitle("Add Note");


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        db = new DBHelper(this);
        note = new Note();

        add_info_lay = (LinearLayout) findViewById(R.id.add_info_lay);
        add_categories = (Spinner) findViewById(R.id.add_categories);
        subject = (EditText) findViewById(R.id.addNewSubject);
        notes = (EditText) findViewById(R.id.noteUser);
        date = (TextView) findViewById(R.id.currentDate);
        add_info_switch = (Switch) findViewById(R.id.add_info_switch);
        addImage_img = (ImageView) findViewById(R.id.addImage_img);
        preview_add_img = (LinearLayout) findViewById(R.id.preview_add_img);
        del_preview_img = (ImageButton) findViewById(R.id.del_preview_img);

        preview_add_img.setVisibility(View.GONE);

        DateFormat df = new SimpleDateFormat("d MMM yyyy, HH:mm");
        String current_time = df.format(Calendar.getInstance().getTime());

        date.setText(current_time);
        ArrayList listOfCategory = db.getAllCategories();
        ArrayList listOfCat = new ArrayList();
        if (listOfCategory == null || listOfCategory.isEmpty()) {
            listOfCategory.add(allCategory);
            dataForCategories = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listOfCategory);
        } else {
            listOfCat.add(allCategory);
            for (int i = 0; i < db.getAllCategories().size(); i++) {
                listOfCat.add(db.getAllCategories().get(i));
            }
            dataForCategories = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listOfCat);
        }

        dataForCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_categories.setAdapter(dataForCategories);

        add_info_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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

                if (!add_info_switch.isChecked()) {
                    note.setCategory(allCategory);
                    add_info_lay.setVisibility(View.INVISIBLE);
                }
                category = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        del_preview_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note.setImgPath("");
                preview_add_img.setVisibility(View.GONE);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "TakeNote_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
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
        switch (item.getItemId()) {
            case R.id.menuAdd_addImg:
                selectImage();
                break;
            case R.id.menuAdd_save:
                note.setSubject(subject.getText().toString());
                note.setNote(notes.getText().toString());
                note.setCreateTime(date.getText().toString());
                note.setCategory(add_categories.getSelectedItem().toString());
                if (addImage_img != null)
                    note.setImgPath(imagePathtoDB);
                else
                    note.setImgPath("");

                if (note.getSubject().toString().trim().length() != 0 &&
                        note.getNote().toString().trim().length() != 0) {

                    if (db.insertNote(note.getSubject(), note.getNote(), note.getCreateTime(), note.getCategory(), note.getImgPath())) {
                        Intent startMain = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(startMain);
                        Toast.makeText(getApplicationContext(), "Successfully saved!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please save it later...", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter all the parts.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Gallery"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddItem.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(AddItem.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask = "Choose from Gallery";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    note.setImgPath("");
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
                preview_add_img.setVisibility(View.VISIBLE);
            } else if (requestCode == REQUEST_CAMERA) {
                try {
                    onCaptureImageResult(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                preview_add_img.setVisibility(View.VISIBLE);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onCaptureImageResult(Intent data) throws IOException {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                options);
        imagePathtoDB = fileUri.getPath();
        addImage_img.setImageBitmap(bitmap);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        addImage_img.setImageBitmap(bm);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        fileUri = savedInstanceState.getParcelable("file_uri");
    }

}

