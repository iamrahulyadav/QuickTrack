package app.quicktrack.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.quicktrack.R;
import app.quicktrack.adapters.ExpandableAdapter;
import app.quicktrack.models.ExpDrawerModel;
import app.quicktrack.utils.AppGlobal;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    Toolbar myToolbar;
    AppGlobal global= AppGlobal.getInstancess();
    private RelativeLayout main_content;
    ExpandableListView expandableListView;
    List<ExpDrawerModel> listparent;
    HashMap<ExpDrawerModel, List<String>> listchild;
    ExpandableAdapter expandableAdapter;
    int lastpostion=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolBar();
        preaparedate();
        global.context = getApplicationContext();
        expandableListView= (ExpandableListView) findViewById(R.id.expdrawerlist);
       expandableAdapter=new ExpandableAdapter(this,listparent,listchild);
        expandableListView.setAdapter(expandableAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if(lastpostion!=-1 && lastpostion!=groupPosition)
                {
                    expandableListView.collapseGroup(lastpostion);
                }
                lastpostion=groupPosition;
            }
        });
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(groupPosition==0)
                {
                 //   startActivity(new Intent(MainActivity.this,MapViewActivity.class));
                    Toast.makeText(getApplicationContext(),"You are at Home",Toast.LENGTH_SHORT).show();
                }
                else if(groupPosition==2){
                    startActivity(new Intent(MainActivity.this,MyProfileActivity.class));

                }
               /* else if(groupPosition==3){
                    startActivity(new Intent(MainActivity.this,ChangePasswordActivity.class));

                }*/
                else if(groupPosition==3){
                    startActivity(new Intent(MainActivity.this,ContactUsActivity.class));
                }
                else if(groupPosition==4){
                    TinyDB tinyDB = new TinyDB(getApplicationContext());
                    tinyDB.remove("login_Data");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();

                }
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if(childPosition==0)
                {
                    startActivity(new Intent(MainActivity.this,MapViewActivity.class));
                }
                else if(childPosition==1)
                {
                    startActivity(new Intent(MainActivity.this,ListViewActivity.class));
                }
                else if(childPosition==2)
                {
                    startActivity(new Intent(MainActivity.this,AlertListActivity.class));
                }
                else if(childPosition==3)
                {
                    startActivity(new Intent(MainActivity.this,NotificationActivity.class));
                }
                else if(childPosition==4)
                {
                    startActivity(new Intent(MainActivity.this,TrackHistoryActivity.class));
                }
                return false;
            }
        });
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        main_content = (RelativeLayout) findViewById(R.id.main_content) ;
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void preaparedate() {
        listparent=new ArrayList<ExpDrawerModel>();
        listchild=new HashMap<>();
        ExpDrawerModel item1=new ExpDrawerModel();
        item1.setIconName("Home");
        item1.setImageid(R.drawable.nav_home);
        listparent.add(item1);
        ExpDrawerModel item2=new ExpDrawerModel();
        item2.setIconName("System Management");
        item2.setImageid(R.drawable.nav_system_mgmt);
        listparent.add(item2);
        ExpDrawerModel item3=new ExpDrawerModel();
        item3.setIconName("My Profile");
        item3.setImageid(R.drawable.nav_update_profile);
        listparent.add(item3);
//        ExpDrawerModel item4=new ExpDrawerModel();
//        item4.setIconName("Change Password");
//        item4.setImageid(R.drawable.nav_change_pwd);
//        listparent.add(item4);
        ExpDrawerModel item5=new ExpDrawerModel();
        item5.setIconName("Contact");
        item5.setImageid(R.drawable.nav_contact);
        listparent.add(item5);
        ExpDrawerModel item6=new ExpDrawerModel();
        item6.setIconName("Logout");
        item6.setImageid(R.drawable.nav_logout);
        listparent.add(item6);

        List<String> heading1 = new ArrayList<String>();

        List<String> heading2 = new ArrayList<String>();
        heading2.add("Map View");
        heading2.add("List View");
        heading2.add("Alert List");
        heading2.add("Notification");
        heading2.add("Track History");

        listchild.put(listparent.get(0),heading1);
        listchild.put(listparent.get(1),heading2);
        listchild.put(listparent.get(2),heading1);
        listchild.put(listparent.get(3),heading1);
        listchild.put(listparent.get(4),heading1);
//        listchild.put(listparent.get(5),heading1);


    }

    public void gotoContactUs(View view) {
        startActivity(new Intent(MainActivity.this,ContactUsActivity.class));
    }

    public void gotoProfile(View view) {
        startActivity(new Intent(MainActivity.this,MyProfileActivity.class));
    }

    public void gotoTrackHistory(View view) {
        startActivity(new Intent(MainActivity.this,TrackHistoryActivity.class));
    }

    public void gotoNotification(View view) {
        startActivity(new Intent(MainActivity.this,NotificationActivity.class));
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    public void gotoAlertList(View view) {
        startActivity(new Intent(MainActivity.this,AlertListActivity.class));
    }

    public void gotoListView(View view) {
        startActivity(new Intent(MainActivity.this,ListViewActivity.class));
    }

    public void gotoMapView(View view) {
        startActivity(new Intent(MainActivity.this,MapViewActivity.class));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){

        }

        handleDrawer();
        return false;
    }


    private TextView tv_title ;
    private ImageView btnHome ;
    private ImageView btnOption ;
    private void setupToolBar() {
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_title = (TextView) myToolbar.findViewById(R.id.tv_title);
        btnHome = (ImageView) myToolbar.findViewById(R.id.btnHome);
        btnOption = (ImageView) myToolbar.findViewById(R.id.btnOption);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDrawer();
            }
        });
        btnOption.setVisibility(View.INVISIBLE);
        setSupportActionBar(myToolbar);
    }

    private void handleDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        }else{
            drawer.openDrawer(GravityCompat.START);
        }
    }



    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        }else{
            super.onBackPressed();
        }

    }
}
