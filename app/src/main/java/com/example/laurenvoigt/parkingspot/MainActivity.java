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

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;


import static com.example.laurenvoigt.parkingspot.R.id.action_context_bar;
import static com.example.laurenvoigt.parkingspot.R.id.nv;

public class MainActivity extends AppCompatActivity {
    TextView userName;
    TextView userId;
    LoginButton login_button;
    CallbackManager callbackManager;
    ProfilePictureView profilePictureView;
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
        //make fragment default page when open application


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

        //New stuff here
        //super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplication());
        //setContentView(R.layout.activity_main);
        initializeControls();
        loginWithFB();

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

    //NEW STUFF HERE
    private void initializeControls(){
        callbackManager = CallbackManager.Factory.create();
        userName = (TextView)findViewById(R.id.userName);
        userId = (TextView)findViewById(R.id.userId);
        profilePictureView = (ProfilePictureView)findViewById(R.id.picture);
        login_button = (LoginButton)findViewById(R.id.login_button);
    }

    private void loginWithFB(){
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("Main", response.toString());
                                setProfileToView(object);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name");
                request.setParameters(parameters);
                request.executeAsync();
            }
            @Override
            public void onCancel() {
                userName.setText("Login Cancelled.");
            }

            @Override
            public void onError(FacebookException error) {
                userName.setText("Login Error: " + error.getMessage());
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setProfileToView(JSONObject jsonObject) {
        try {

            userName.setText("Name:" + jsonObject.getString("name"));
            userId.setText("UserId: " + jsonObject.getString("id"));
            profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
            profilePictureView.setProfileId(jsonObject.getString("id"));

            //infoLayout.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}


