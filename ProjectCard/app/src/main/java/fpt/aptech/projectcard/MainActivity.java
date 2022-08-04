package fpt.aptech.projectcard;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import fpt.aptech.projectcard.callApiService.ApiService;
import fpt.aptech.projectcard.databinding.ActivityMainBinding;
import fpt.aptech.projectcard.domain.User;
import fpt.aptech.projectcard.retrofit.RetrofitService;
import fpt.aptech.projectcard.session.SessionManager;
import fpt.aptech.projectcard.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private TextView txtUsername;
    private ImageView imgAvatarNavHeader;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        View nav_header_view = navigationView.getHeaderView(0);
        txtUsername = nav_header_view.findViewById(R.id.textViewUsername);
        imgAvatarNavHeader = nav_header_view.findViewById(R.id.imgAvatarNavHeader);
        String username = SessionManager.getSaveUsername();
        String avatarUrl = SessionManager.getSaveLinkImage();
        txtUsername.setText(username + "'s Smart Card");

        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(avatarUrl).getContent());
            imgAvatarNavHeader.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isLogined(SessionManager.getSaveToken());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    //============================NAVIGATION MENU EVENT CLICK===================================================
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        onStart();
        switch (item.getItemId()) {

            case R.id.nav_logout: {
                txtUsername.setText(R.string.nav_header_subtitle);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
            }
        }
        NavigationUI.onNavDestinationSelected(item, navController);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //==============================END NAVIGATION MENU ================================================

    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing App")
                    .setMessage("Are you sure you want to close this app?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                            System.exit(0);
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    //check login only for one device
    public void isLogined(String saveToken) {
        ApiService apiService = RetrofitService.getInstance().create(ApiService.class);
        try {
            User user = apiService.getProfile(SessionManager.getSaveUsername()).execute().body();
            if (!user.getToken().equals(saveToken)){
                Toast.makeText(this, "The your account was signed at another place", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}