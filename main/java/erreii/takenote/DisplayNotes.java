package erreii.takenote;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import erreii.takenote.pages.TakeNote;

/**
 * Created by Asus on 8/16/2016.
 */

public class DisplayNotes extends AppCompatActivity implements View.OnClickListener{
    private TextView dispSubject, dispNote, category_disp;
    private DBHelper db;
    private ImageButton edit_disp_btn, delete_disp_btn;
    private String selected_id;
    final Context context = this;

    private Uri fileUri;

    private String subject, note, category, disp_note_imgpath;
    public static final String NOTES_TABLE_NAME = "notes";
    public static final String CATEGORIES_TABLE_NAME = "categories";

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    private ImageView disp_note_img;

    //Maybe delete
    public static final String EXTRA_PARAM_ID = "place_id";
    public static final String NAV_BAR_VIEW_NAME = Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME;
    private ListView mList;
    private ImageView mImageView;
    private TextView mTitle;
    private LinearLayout mTitleHolder;
    private Palette mPalette;
    private ImageButton mAddButton;
    private Animatable mAnimatable;
    private LinearLayout mRevealView;
    private EditText mEditTextTodo;
    private boolean isEditTextVisible;
    private InputMethodManager mInputManager;
    private Place mPlace;
    private ArrayList<String> mTodoList;
    private ArrayAdapter mToDoAdapter;
    int defaultColorForRipple;


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_displaynote);

        db = new DBHelper(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("View Note");

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_disp);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        nvDrawer = (NavigationView) findViewById(R.id.nvView_disp);


        mPlace = PlaceData.placeList().get(getIntent().getIntExtra(EXTRA_PARAM_ID, 0));

        mList = (ListView) findViewById(R.id.list);
        mImageView = (ImageView) findViewById(R.id.placeImage);
        mTitle = (TextView) findViewById(R.id.textView);
        mTitleHolder = (LinearLayout) findViewById(R.id.placeNameHolder);
        mAddButton = (ImageButton) findViewById(R.id.btn_add);
        mRevealView = (LinearLayout) findViewById(R.id.llEditTextHolder);
        mEditTextTodo = (EditText) findViewById(R.id.etTodo);

        mAddButton.setImageResource(R.drawable.icn_morph_reverse);
        mAddButton.setOnClickListener(this);
        defaultColorForRipple = getResources().getColor(R.color.primary_dark);
        mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mRevealView.setVisibility(View.INVISIBLE);
        isEditTextVisible = false;

        mTodoList = new ArrayList<>();
        mToDoAdapter = new ArrayAdapter(this, R.layout.row_todo, mTodoList);
        mList.setAdapter(mToDoAdapter);







        /*dispSubject = (TextView) findViewById(R.id.displaySubject);
        dispNote = (TextView) findViewById(R.id.displayNote);
        category_disp = (TextView) findViewById(R.id.category_disp);
        disp_note_img = (ImageView) findViewById(R.id.disp_note_img);*/

        /*Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int value = extras.getInt("id");

            if (value > -1) {
                Cursor rs = db.getData((value), NOTES_TABLE_NAME);
                rs.moveToFirst();

                selected_id = rs.getString(rs.getColumnIndex(DBHelper.NOTES_COLUMN_ID));
                subject = rs.getString(rs.getColumnIndex(DBHelper.NOTES_COLUMN_SUBJECT));
                note = rs.getString(rs.getColumnIndex(DBHelper.NOTES_COLUMN_NOTE));
                category = rs.getString(rs.getColumnIndex(DBHelper.NOTES_COLUMN_CATEGORY));
                disp_note_imgpath = rs.getString(rs.getColumnIndex(DBHelper.NOTES_COLUMN_IMGPATH));

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 3;

                final Bitmap bitmap = BitmapFactory.decodeFile(disp_note_imgpath,
                        options);
                *//*disp_note_img.setImageBitmap(bitmap);

                dispSubject.setText((CharSequence) subject);
                dispSubject.setFocusable(false);
                dispSubject.setClickable(false);

                dispNote.setText((CharSequence) note);
                dispNote.setFocusable(false);
                dispNote.setClickable(false);

                category_disp.setText((CharSequence) category);
                category_disp.setFocusable(false);
                category_disp.setClickable(false);*//*

            }
        }*/

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int value = extras.getInt("id");

            if (value >= 0) {
                Cursor rs = db.getData((value), NOTES_TABLE_NAME);
                rs.moveToFirst();

                selected_id = rs.getString(rs.getColumnIndex(DBHelper.NOTES_COLUMN_ID));
                subject = rs.getString(rs.getColumnIndex(DBHelper.NOTES_COLUMN_SUBJECT));
                note = rs.getString(rs.getColumnIndex(DBHelper.NOTES_COLUMN_NOTE));
                category = rs.getString(rs.getColumnIndex(DBHelper.NOTES_COLUMN_CATEGORY));
                disp_note_imgpath = rs.getString(rs.getColumnIndex(DBHelper.NOTES_COLUMN_IMGPATH));
            }
        }
        loadPlace(subject);
        windowTransition();
        getPhoto();

        setupDrawerContent(nvDrawer);
    }



    private void loadPlace(String subject) {
        mTitle.setText(subject);
        mImageView.setImageResource(mPlace.getImageResourceId(this));
    }

    private void windowTransition() {
        getWindow().setEnterTransition(makeEnterTransition());
        getWindow().getEnterTransition().addListener(new TransitionAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                mAddButton.animate().alpha(1.0f);
                getWindow().getEnterTransition().removeListener(this);
            }
        });
    }

    public static Transition makeEnterTransition() {
        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        return fade;
    }

    private void addToDo(String todo) {
        mTodoList.add(todo);
    }

    private void getPhoto() {
        Bitmap photo = BitmapFactory.decodeResource(getResources(), mPlace.getImageResourceId(this));
        colorize(photo);
    }

    private void colorize(Bitmap photo) {
        mPalette = Palette.generate(photo);
        applyPalette();
    }

    private void applyPalette() {
        getWindow().setBackgroundDrawable(new ColorDrawable(mPalette.getDarkMutedColor(defaultColorForRipple)));
        mTitleHolder.setBackgroundColor(mPalette.getMutedColor(defaultColorForRipple));
        applyRippleColor(mPalette.getVibrantColor(defaultColorForRipple),
                mPalette.getDarkVibrantColor(defaultColorForRipple));
        mRevealView.setBackgroundColor(mPalette.getLightVibrantColor(defaultColorForRipple));
    }

    private void applyRippleColor(int bgColor, int tintColor) {
        colorRipple(mAddButton, bgColor, tintColor);
    }

    private void colorRipple(ImageButton id, int bgColor, int tintColor) {
        View buttonView = id;
        RippleDrawable ripple = (RippleDrawable) buttonView.getBackground();
        GradientDrawable rippleBackground = (GradientDrawable) ripple.getDrawable(0);
        rippleBackground.setColor(bgColor);
        ripple.setColor(ColorStateList.valueOf(tintColor));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                if (!isEditTextVisible) {
                    revealEditText(mRevealView);
                    mEditTextTodo.requestFocus();
                    mInputManager.showSoftInput(mEditTextTodo, InputMethodManager.SHOW_IMPLICIT);
                    mAddButton.setImageResource(R.drawable.icn_morp);
                    mAnimatable = (Animatable) (mAddButton).getDrawable();
                    mAnimatable.start();
                    applyRippleColor(getResources().getColor(R.color.colormenu), getResources().getColor(R.color.colormenu_txt));
                } else {
                    addToDo(mEditTextTodo.getText().toString());
                    mToDoAdapter.notifyDataSetChanged();
                    mInputManager.hideSoftInputFromWindow(mEditTextTodo.getWindowToken(), 0);
                    hideEditText(mRevealView);
                    mAddButton.setImageResource(R.drawable.icn_morph_reverse);
                    mAnimatable = (Animatable) (mAddButton).getDrawable();
                    mAnimatable.start();
                    applyRippleColor(mPalette.getVibrantColor(defaultColorForRipple),
                            mPalette.getDarkVibrantColor(defaultColorForRipple));
                }
        }
    }

    private void revealEditText(LinearLayout view) {
        int cx = view.getRight() - 30;
        int cy = view.getBottom() - 60;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        view.setVisibility(View.VISIBLE);
        isEditTextVisible = true;
        anim.start();
    }

    private void hideEditText(final LinearLayout view) {
        int cx = view.getRight() - 30;
        int cy = view.getBottom() - 60;
        int initialRadius = view.getWidth();
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });
        isEditTextVisible = false;
        anim.start();
    }

    @Override
    public void onBackPressed() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(100);
        mAddButton.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAddButton.setVisibility(View.GONE);
                finishAfterTransition();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = TakeNote.class;
                mDrawer.closeDrawers();
                break;
            case R.id.nav_second_fragment:
                fragmentClass = TakeNote.class;
                final Dialog dialog = new Dialog(DisplayNotes.this);
                dialog.setContentView(R.layout.addcategory_dialog);
                dialog.setTitle("Add Category");

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.seloo);
                text.setText("Android custom dialog example!");
                ImageView image = (ImageView) dialog.findViewById(R.id.addCategoryImg);
                image.setImageResource(R.drawable.ic_launcher);

                Button dialogButton = (Button) dialog.findViewById(R.id.addCategoryBtn);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                //mDrawer.closeDrawers();
                break;
            default:
                fragmentClass = TakeNote.class;
                /*Intent addNewPage = new Intent(MainActivity.this, AddItem.class);
                startActivity(addNewPage);*/
                mDrawer.closeDrawers();
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        //menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_display, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        /*return super.onOptionsItemSelected(item);*/
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.menuDisp_edit:
                Intent addNewPage = new Intent(getApplicationContext(), EditNote.class);
                startActivity(addNewPage);
                return true;
            case R.id.menuDisp_del:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Delete Note");


                alertDialogBuilder
                        .setMessage("Are you sure you want to delete selected note?")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                db.deleteNote(Integer.valueOf(selected_id));
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                Button yes = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                yes.setTextColor(Color.RED);

                Button no = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                no.setTextColor(Color.GRAY);
                return true;

            case R.id.menuDisp_share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareText = "Take Note" + "\n" + "_______________" + "\n" + "*Subject: " + subject + "\n" + "*Note: " + note;
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }


}
