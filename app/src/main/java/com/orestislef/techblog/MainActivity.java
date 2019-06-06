package com.orestislef.techblog;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

//*****************************************************************************
//  1.      na valw na pernei perisetera otan ftanei katw to recyclerViewer
//          video: https://www.youtube.com/watch?v=PamhELVWYY0
//          Android Studio Tutorial - Recycler View Dynamic Load Data edmt dev
//  2.      na valw darkMode se olo to UI
//          video: https://www.youtube.com/watch?v=-qsHE3TpJqw
//          Implement Night Mode in your App | Custom Styles in Android Studio
//  3.      na valw to etimo pou exw to per_page(query) na epilegei o xristis
//          posa post thelei na ton emvanizonte
//          mporw na kanw copy/paste to retrofit alla na parw to getPostById();
//          kai to fori na 3ekinaei apo to 10+
//          to media tha mini to idio
//  4.      na valw setting menu pou na vgazei posa post na fenonte kai sta
//          settings na exei kai to dark mode
//*****************************************************************************


    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle("Home");
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                toolbar.setSubtitle("Home");
                break;
            case R.id.nav_smartphones:
                Bundle smartphoneIdBundle= new Bundle();
                smartphoneIdBundle.putInt("category",8);
                PostListFragment smartphoneListFragment = new PostListFragment();
                smartphoneListFragment.setArguments(smartphoneIdBundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, smartphoneListFragment).commit();
                toolbar.setSubtitle("Smartphones");
                break;
            case R.id.nav_gear:
                Bundle gearIdBundle= new Bundle();
                gearIdBundle.putInt("category",24);
                PostListFragment gearListFragment = new PostListFragment();
                gearListFragment.setArguments(gearIdBundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, gearListFragment).commit();
                toolbar.setSubtitle("Gear");
                break;
            case R.id.nav_reviews:
                Bundle reviewsIdBundle= new Bundle();
                reviewsIdBundle.putInt("category",58);
                PostListFragment reviewsListFragment = new PostListFragment();
                reviewsListFragment.setArguments(reviewsIdBundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, reviewsListFragment).commit();
                toolbar.setSubtitle("Reviews");
                break;
            case R.id.nav_workshop:
                Bundle workshopIdBundle= new Bundle();
                workshopIdBundle.putInt("category",1033);
                PostListFragment workshopListFragment = new PostListFragment();
                workshopListFragment.setArguments(workshopIdBundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, workshopListFragment).commit();
                toolbar.setSubtitle("Workshop");
                break;
            case R.id.nav_internet:
                Bundle internetIdBundle= new Bundle();
                internetIdBundle.putInt("category",49);
                PostListFragment internetListFragment = new PostListFragment();
                internetListFragment.setArguments(internetIdBundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, internetListFragment).commit();
                toolbar.setSubtitle("Internet");
                break;
            case R.id.nav_cars:
                Bundle carsIdBundle= new Bundle();
                carsIdBundle.putInt("category",9131);
                PostListFragment carsListFragment = new PostListFragment();
                carsListFragment.setArguments(carsIdBundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, carsListFragment).commit();
                toolbar.setSubtitle("Cars");
                break;
            case R.id.nav_homecinema:
                Bundle homecinemaIdBundle= new Bundle();
                homecinemaIdBundle.putInt("category",57);
                PostListFragment homecinemaListFragment = new PostListFragment();
                homecinemaListFragment.setArguments(homecinemaIdBundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homecinemaListFragment).commit();
                toolbar.setSubtitle("Homecinema");
                break;
            case R.id.nav_software:
                Bundle softwareIdBundle= new Bundle();
                softwareIdBundle.putInt("category",584);
                PostListFragment softwareListFragment = new PostListFragment();
                softwareListFragment.setArguments(softwareIdBundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, softwareListFragment).commit();
                toolbar.setSubtitle("Software");
                break;
            case R.id.nav_computers:
                Bundle computersIdBundle= new Bundle();
                computersIdBundle.putInt("category",29);
                PostListFragment computersListFragment = new PostListFragment();
                computersListFragment.setArguments(computersIdBundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, computersListFragment).commit();
                toolbar.setSubtitle("Computers");
                break;
            case R.id.nav_business:
                Bundle businessIdBundle= new Bundle();
                businessIdBundle.putInt("category",24);
                PostListFragment businessListFragment = new PostListFragment();
                businessListFragment.setArguments(businessIdBundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, businessListFragment).commit();
                toolbar.setSubtitle("Business");
                break;
        }
        setSupportActionBar(toolbar);
        drawer.closeDrawer(GravityCompat.START);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();

    }
}
