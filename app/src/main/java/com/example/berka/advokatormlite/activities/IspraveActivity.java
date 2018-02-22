package com.example.berka.advokatormlite.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.adapter.FixedTabsPagerAdapter;
import com.example.berka.advokatormlite.fragments.FragmentSaProcentima;
import com.example.berka.advokatormlite.fragments.FragmentSaSpinerom;

/**
 * Created by berka on 19-Jan-18.
 */

public class IspraveActivity extends AppCompatActivity implements FragmentSaProcentima.OnFragmentInteractionListener, FragmentSaSpinerom.OnFragmentInteractionListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isprave);

        TabLayout tabLayout = findViewById(R.id.isprave_tablelayout);
        tabLayout.addTab(tabLayout.newTab().setText("Procenti"));
        tabLayout.addTab(tabLayout.newTab().setText("Tabele"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        final FixedTabsPagerAdapter adapter = new FixedTabsPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
