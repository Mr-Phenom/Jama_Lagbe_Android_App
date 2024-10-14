package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Fragment.AccountFragment;
import com.example.myapplication.Fragment.FavouritesFragment;
import com.example.myapplication.Fragment.HomeFragment;
import com.example.myapplication.Fragment.NotificationFragment;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityDashboardBinding;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding binding;
    String email;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ImageView imageViewSideBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        //email = getIntent().getStringExtra("email");

        imageViewSideBar = findViewById(R.id.imageViewDrawerMenu);

        navigationView = (NavigationView) findViewById(R.id.sideNavigationView);
        Menu menu = navigationView.getMenu();

        drawerLayout = findViewById(R.id.drawerLayout);
        imageViewSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });
        //status checkbox
        MenuItem statusAvailableItem = menu.findItem(R.id.status_option_1);
        CheckBox statusAvailableCheckBox = statusAvailableItem.getActionView().findViewById(R.id.filter_checkbox);


        MenuItem statusUnavailableItem = menu.findItem(R.id.status_option_2);
        CheckBox statusUnavailableCheckBox = statusUnavailableItem.getActionView().findViewById(R.id.filter_checkbox);


        statusAvailableCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Do something when Available is checked
            }
        });

        statusUnavailableCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Do something when Unavailable is checked
            }
        });

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
        itemClickedFromSideNavigation();
    }

    private void itemClickedFromSideNavigation()
    {
        navigationView.setNavigationItemSelectedListener(item -> {
            if(item.getItemId()==R.id.nav_sell_rent)
            {
                Intent intent = new Intent(DashboardActivity.this,SellRentActivity.class);
                intent.putExtra("type","sell/rent");
                startActivity(intent);
                return true;
            }
            else if(item.getItemId()==R.id.nav_donate)
            {
                Intent intent = new Intent(DashboardActivity.this,SellRentActivity.class);
                intent.putExtra("type","donate");
                startActivity(intent);
                return true;
            }
            return false;
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