package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.myapplication.Fragment.AccountFragment;
import com.example.myapplication.Fragment.FavouritesFragment;
import com.example.myapplication.Fragment.HomeFragment;
import com.example.myapplication.Fragment.NotificationFragment;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding binding;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        email = getIntent().getStringExtra("email");

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int menuId = item.getItemId();
            if(menuId==R.id.bottom_nav_home)
            {
                replaceFragment(new HomeFragment());
            }
            else if(menuId==R.id.bottom_nav_favourites)
            {
                replaceFragment(new FavouritesFragment());
            }
            else if(menuId==R.id.bottom_nav_notifications)
            {
                replaceFragment(new NotificationFragment());
            }
            else if(menuId==R.id.bottom_nav_profile)
            {
                replaceFragment(new AccountFragment());
            }


            return  true;
        });
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.dashboardFrameLayout,fragment);
        fragmentTransaction.commit();
    }
}