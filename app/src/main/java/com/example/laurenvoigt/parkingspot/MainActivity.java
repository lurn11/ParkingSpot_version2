package com.example.laurenvoigt.parkingspot;

//import android.app.Fragment;
import android.content.Intent;
import android.net.Network;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Context;
import android.widget.RelativeLayout;


import static com.example.laurenvoigt.parkingspot.R.id.action_context_bar;
import static com.example.laurenvoigt.parkingspot.R.id.nv;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout dl;
    private DrawerLayout nv;
    private ActionBarDrawerToggle mToggle;
    //Changes here
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //changes here
        //mContext = this;
        setContentView(R.layout.activity_main);

        //put this here?

        //new stuff here
        /*
        Fragment fragment = new HomePage();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.findspot, fragment).commit();
        */

        //Added Stuff Here
        //https://stackoverflow.com/questions/5658675/replacing-a-fragment-with-another-fragment-inside-activity-group

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.flcontent, new HomePage());
        tx.commit();

        dl = (DrawerLayout) findViewById(R.id.dl);
        mToggle = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);
        dl.addDrawerListener(mToggle);
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nv);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(nvDrawer);

        /*
        //final Button button = findViewById(R.id.button1);
        Button b =(Button) findViewById(R.id.dl);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(view.getContext(), findspot.class);
                view.getContext().startActivity(intent);
            }
        });
        */

        //New stuff here
        //homepage as id?
        getSupportFragmentManager().beginTransaction().replace(R.id.flcontent, new HomePage()).addToBackStack(null).commit();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void selectItemDrawer(MenuItem menuItem){
        Fragment myFragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()){
            case R.id.accountinformation:
                fragmentClass = AccountInformation.class;
                break;
            case R.id.settings:
                fragmentClass = Settings.class;
                break;
            case R.id.homepage:
                fragmentClass = HomePage.class;
            default:
                fragmentClass = HomePage.class;

        }
        try{
            myFragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flcontent, myFragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        dl.closeDrawers();
    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectItemDrawer(item);
                return true;
            }
        });
    }

}

