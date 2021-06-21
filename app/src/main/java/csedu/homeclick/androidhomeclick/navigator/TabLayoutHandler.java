package csedu.homeclick.androidhomeclick.navigator;

import android.content.Context;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class TabLayoutHandler {
    private static TabLayoutHandler tabLayoutHandler;
    private static TabLayout tabLayout;
    private static ViewPager2 viewPager;

    private TabLayoutHandler(TabLayout tabL, ViewPager2 viewPager2) {
        tabLayout = tabL;
        viewPager = viewPager2;
    }

    public static void setInstance(TabLayout tabL, ViewPager2 viewPager2) {
        tabLayoutHandler = new TabLayoutHandler(tabL, viewPager2);
    }

    public static void handle(Context context) {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }
}
