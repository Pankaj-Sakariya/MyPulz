package com.example.mypulz.UICore.Detail;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mypulz.R;
import com.example.mypulz.UICore.Detail.dummy.DummyContent;
import com.example.mypulz.UICore.Security.LoginActivity;
import com.example.mypulz.UICore.Security.OtpActivity;

import org.json.JSONArray;
import org.json.JSONException;

import Common.CommonFunction;
import Common.Constant;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , DoctorListFragment.OnListFragmentInteractionListener, MyProfileFragment.OnFragmentInteractionListener,FindDoctorFragment.OnFragmentInteractionListener, ReviewDoctorFragment.OnFragmentInteractionListener , BookAppointmentFragment.OnFragmentInteractionListener, MyAppointmentFragment.OnListFragmentInteractionListener, FindDoctorGridFragment.OnListFragmentInteractionListener{
    private boolean viewIsAtHome;

    TextView txt_full_name,txt_email_id;
    Activity activity = null;

    JSONArray jsonArray_customer_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = this;


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_find_doctor);
        displayView(R.id.nav_find_doctor);

        initializeWidget(navigationView);
    }

    private void initializeWidget(NavigationView navigationView) {

        View hView =  navigationView.getHeaderView(0);
//        TextView nav_user = (TextView)hView.findViewById(R.id.nav_name);
//        nav_user.setText(user);
        txt_full_name = (TextView)hView.findViewById(R.id.txt_full_name);
        txt_email_id = (TextView)hView.findViewById(R.id.txt_email_id);

        setData();

    }

    private void setData() {

        String str_customer_detail =  new CommonFunction().getSharedPreference(Constant.TAG_jArray_customer_detail, getApplicationContext());
        System.out.println("!!!!pankaj_customer_detail"+str_customer_detail);
//        new CommonFunction().showAlertDialog(str_customer_detail,"Response",activity);
        String str_full_name = null;
        String str_email_id = null;


        try {
            jsonArray_customer_detail = new JSONArray(str_customer_detail);
            str_full_name = new CommonFunction().parseStringFromJsonArray(jsonArray_customer_detail,"full_name");
            str_email_id = new CommonFunction().parseStringFromJsonArray(jsonArray_customer_detail,"email_id");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        txt_full_name.setText(str_full_name);
        txt_email_id.setText(str_email_id);
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (!viewIsAtHome) { //if the current view is not the News fragment
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_find_doctor);
            displayView(R.id.nav_find_doctor); //display the News fragment
        } else {
            moveTaskToBack(true);  //If view is in News fragment, exit application
        }

//        int count = getFragmentManager().getBackStackEntryCount();
//
//        if (count == 0) {
//            super.onBackPressed();
//            //additional code
//        } else {
//            getFragmentManager().popBackStack();
//        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.toolbar_item, menu);
//        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
//        Button buttonSearch = (Button) myActionMenuItem.getActionView();
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        int id = item.getItemId();
        displayView(item.getItemId());

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//
//
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void displayView(int viewId) {

        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (viewId) {


            case R.id.nav_find_doctor:
                fragment = new FindDoctorGridFragment();
                title  = "Find Doctor";
                viewIsAtHome = true;

                break;

//            case R.id.nav_find_doctor:
//                fragment = new FindDoctorFragment();
//                title  = "Find Doctor";
//                viewIsAtHome = true;
//
//                break;
            case R.id.nav_my_appointment:
                fragment = new MyAppointmentFragment();
                title  = "My Appointment";
                viewIsAtHome = false;

                break;
            case R.id.nav_my_profile:
                fragment = new MyProfileFragment();
                title = "My Profile";
                viewIsAtHome = false;
                break;

//            case R.id.nav_review:
//                fragment = new ReviewDoctorFragment();
//                title = "Review";
//                viewIsAtHome = false;
//                break;


//            case R.id.nav_book_appointment:
//                fragment = new BookAppointmentFragment();
//                title = "Book Appointment";
//                viewIsAtHome = false;
//                break;

            case R.id.nav_logout:

                new CommonFunction().saveSharedPreference(Constant.TAG_login_verified, "0", activity);
                Intent i = new Intent(MainActivity.this, OtpActivity.class);
                startActivity(i);
                finish();
//                fragment = new DoctorListFragment();
//                title = "Logout";
                viewIsAtHome = false;
                break;


        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }




    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
