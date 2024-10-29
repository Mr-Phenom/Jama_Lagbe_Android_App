package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Fragment.AccountFragment;
import com.example.myapplication.Fragment.FavouritesFragment;
import com.example.myapplication.Fragment.HomeFragment;
import com.example.myapplication.Fragment.NotificationFragment;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityDashboardBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding binding;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ImageView imageViewSideBar;
    Menu menu;
    private List<String> selectedTypes = new ArrayList<>();
    private List<String> selectedStatus = new ArrayList<>();
    private List<String> selectedCondition = new ArrayList<>();
    private String maxPrice="";
    private String minPrice="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());


        imageViewSideBar = findViewById(R.id.imageViewDrawerMenu);

        navigationView = (NavigationView) findViewById(R.id.sideNavigationView);
        menu = navigationView.getMenu();

        drawerLayout = findViewById(R.id.drawerLayout);
        imageViewSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
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
                intent.putExtra("type","donation");
                startActivity(intent);
                return true;
            }


            return true;
        });



        CheckBox statusAvailable =(CheckBox) menu.findItem(R.id.status_option_1).getActionView().findViewById(R.id.filter_checkbox);
        CheckBox statusUnavailable = menu.findItem(R.id.status_option_2).getActionView().findViewById(R.id.filter_checkbox);

        if(statusAvailable!=null)
    {

        statusAvailable.setOnCheckedChangeListener((buttonView, isChecked) -> {


            if (isChecked) {
                selectedStatus.add("available");
            } else {
                selectedStatus.remove("available");
            }
            updateHomeFragment();

        });
    }


        statusUnavailable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedStatus.add("sold");
            } else {
                selectedStatus.remove("sold");
            }
            updateHomeFragment();
        });

        CheckBox sellCheckBox = menu.findItem(R.id.type_option_1).getActionView().findViewById(R.id.filter_checkbox);
        CheckBox rentCheckBox = menu.findItem(R.id.type_option_2).getActionView().findViewById(R.id.filter_checkbox);
        CheckBox donateCheckBox = menu.findItem(R.id.type_option_3).getActionView().findViewById(R.id.filter_checkbox);


        sellCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) selectedTypes.add("sale");
            else selectedTypes.remove("sale");
            updateHomeFragment();
        });

        rentCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) selectedTypes.add("rent");
            else selectedTypes.remove("rent");
            updateHomeFragment();
        });

        donateCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) selectedTypes.add("donation");
            else selectedTypes.remove("donation");
            updateHomeFragment();
        });

        CheckBox usedCheckBox = menu.findItem(R.id.Condition_used).getActionView().findViewById(R.id.filter_checkbox);
        CheckBox newCheckBox = menu.findItem(R.id.condition_new).getActionView().findViewById(R.id.filter_checkbox);

        usedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) selectedCondition.add("used");
            else selectedCondition.remove("used");
            updateHomeFragment();
        });
        newCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) selectedCondition.add("new");
            else selectedCondition.remove("new");
            updateHomeFragment();
        });


        TextView minPriceView = menu.findItem(R.id.nav_price_min).getActionView().findViewById(R.id.min_price_input);
        TextView maxPriceView = menu.findItem(R.id.nav_price_max).getActionView().findViewById(R.id.max_price_input);

        minPriceView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                minPrice = s.toString();
                updateHomeFragment();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        maxPriceView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                maxPrice = s.toString();
                updateHomeFragment();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });
    }


    private void updateHomeFragment() {
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.dashboardFrameLayout);
        if (homeFragment != null) {
            homeFragment.applyFilters(selectedTypes,selectedStatus,selectedCondition, minPrice, maxPrice);
        }

    }
    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.dashboardFrameLayout,fragment);
        fragmentTransaction.commit();
    }
}