package com.si.styletimer.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.si.styletimer.R;
import com.si.styletimer.databinding.ActivityMyBookingBinding;
import com.si.styletimer.fragment.CompletedFragment;
import com.si.styletimer.fragment.ConfirmedFragment;

import java.util.ArrayList;
import java.util.List;


public class MyBookingActivity extends AppCompatActivity {
    private static final String TAG = "MyBookingActivity";
    private Context context;
    ActivityMyBookingBinding binding;
    int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_booking);

        context = this;
        initView();

    }

    private void initView() {

        setupViewPager(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

//onClick----

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTaskRoot()) {
                    Intent intent = new Intent(context, DashboardActivity.class);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    finish();
                }

              //  Bungee.slideRight(context);
            }
        });

        binding.ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,SettingActivity.class));
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {

        try{
            Bundle b =new Bundle();

            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new ConfirmedFragment(), context.getResources().getString(R.string.upcoming_booking));
            adapter.addFragment(new CompletedFragment(), context.getResources().getString(R.string.recent_booking));
            //adapter.addFragment(new CancelledFragment(), "CANCELLED");
            viewPager.setAdapter(adapter);
            if (getIntent() != null && getIntent().getStringExtra("selected") != null) {
                viewPager.setCurrentItem(Integer.valueOf(getIntent().getStringExtra("selected")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        initView();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // Bungee.slideRight(context);
    }
}
