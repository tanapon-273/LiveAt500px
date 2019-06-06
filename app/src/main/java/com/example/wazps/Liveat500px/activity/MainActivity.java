package com.example.wazps.liveat500px.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.wazps.liveat500px.R;
import com.example.wazps.liveat500px.dao.PhotoItemDao;
import com.example.wazps.liveat500px.fragment.MainFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.FragmentListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionbarDrawerToggle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initInstances();

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, MainFragment.newInstance())
                    .commit();
        }
    }

    private void initInstances() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        actionbarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,
                drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer);
        drawerLayout.addDrawerListener(actionbarDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionbarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionbarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionbarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPhotoItemClicked(PhotoItemDao dao) {
        Intent intent = new Intent(MainActivity.this,MoreInfoActivity.class);
        intent.putExtra("dao",dao);
        startActivity(intent);

    }
}
