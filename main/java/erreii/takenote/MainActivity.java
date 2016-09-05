package erreii.takenote;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import erreii.takenote.pages.AddCategory;
import erreii.takenote.pages.TakeNote;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    private DBHelper db;
    private ArrayList catList;
    public static final String TYPE = "TYPE";
    private static final int DYNAMIC_MENU_ITEM = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Open take note fragment in Main OnCreate
        FragmentManager fragmentMgr= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentMgr.beginTransaction();
        TakeNote takenote = new TakeNote();
        fragmentTransaction.add(R.id.flContent, takenote, "TakeNote");
        fragmentTransaction.commit();

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view

        db = new DBHelper(this);

        catList = db.getAllCategories();

        Menu menu = nvDrawer.getMenu();
        if(catList != null && !catList.isEmpty()){

            for(int i=0;i<catList.size();i++) {
                String menuString = catList.get(i).toString().substring(0,1).toUpperCase() + catList.get(i).toString().substring(1);
                MenuItem new_item = menu.add(0, DYNAMIC_MENU_ITEM, 0, menuString);
                new_item.setIcon(R.drawable.ic_action_next_item);
            }

        }
        MenuItem mi = menu.getItem(menu.size()-1);
        mi.setTitle(mi.getTitle());

        setupDrawerContent(nvDrawer);

    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        /*MenuItem searchItem = menu.findItem(R.id.menu_search);
        //MenuItem shareItem = menu.findItem(R.id.menu_search);
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
        });*/
        return super.onCreateOptionsMenu(menu);
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
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = TakeNote.class;
                mDrawer.closeDrawers();
                break;
            case R.id.nav_second_fragment:
                fragmentClass = TakeNote.class;
                final Dialog dialog = new Dialog(MainActivity.this);
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
            case DYNAMIC_MENU_ITEM:
                Toast.makeText(getApplicationContext(),"Toast message",Toast.LENGTH_SHORT).show();

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case R.id.nav_second_fragment:
                /*mDrawer.openDrawer(GravityCompat.START);*/

                return true;


        }



        /*return super.onOptionsItemSelected(item);*/
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
