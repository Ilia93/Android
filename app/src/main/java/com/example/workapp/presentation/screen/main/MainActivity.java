package com.example.workapp.presentation.screen.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.workapp.R;
import com.example.workapp.data.network.model.user.UserActionResult;
import com.example.workapp.data.network.model.user.UserCloudSource;
import com.example.workapp.data.network.model.user.UserModel;
import com.example.workapp.presentation.screen.archive.ArchiveFragment;
import com.example.workapp.presentation.screen.comment.CommentFragment;
import com.example.workapp.presentation.screen.timer.timer.TimerFragment;
import com.example.workapp.presentation.screen.user.UserAccountActivity;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.example.workapp.presentation.screen.user.UserAccountActivity.USER_STORAGE_PHOTO_PATH;
import static com.example.workapp.presentation.screen.user.UserEditActivity.USER_ID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageButton addUser;
    Toolbar toolbar;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Fragment mainFragment = new MainFragment();
    UserModel userModel = new UserModel();
    SharedPreferences sharedPreferences;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setScreenElements();
        initializePreferences();
        if (savedInstanceState == null) {
            replaceFragment(mainFragment);
        }
        setFragmentBackStackListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserCloudSource userCloudSource = new UserCloudSource();
        userCloudSource.getUser(userModel.getUserName(), new UserActionResult() {
            @Override
            public void onSuccess(List<UserModel> users) {
                getUserForNavigationView(users);
                getUserPhotoForNavigationView();
            }

            @Override
            public void onFailure(String message) {
                showToastMessage(message);
            }
        });
    }

    private void setFragmentBackStackListener() {
        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        Fragment fragment = getSupportFragmentManager().
                                findFragmentById(R.id.navigation_content_frame);
                        if (fragment instanceof ArchiveFragment) {
                            navigationView.setCheckedItem(R.id.nav_archive_fragment);
                        } else if (fragment instanceof CommentFragment) {
                            navigationView.setCheckedItem(R.id.nav_comments_fragment);
                        } else if (fragment instanceof MainFragment) {
                            navigationView.setCheckedItem(R.id.nav_home_fragment);
                        } else if (fragment instanceof TimerFragment) {
                            navigationView.setCheckedItem(R.id.nav_timer_fragment);
                        }
                    }
                });
    }

    private void initializePreferences() {
        sharedPreferences = getSharedPreferences(USER_ID, MODE_PRIVATE);
    }

    @SuppressLint("SetTextI18n")
    private void getUserForNavigationView(@NotNull List<UserModel> users) {
        StringBuilder stringBuilder = new StringBuilder();
        if (users.size() == 0) {
            stringBuilder
                    .append("Undefined");
            ((TextView) findViewById(R.id.main_header_label)).setText(stringBuilder.toString());
        } else if (users.size() > 0) {
            if (sharedPreferences.contains(USER_ID)) {
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getUserId().equals(sharedPreferences.getString(USER_ID, ""))) {
                        ((TextView) findViewById(R.id.main_header_label)).setText
                                (users.get(i).getUserName() + " " + users.get(i).getUserSecondName());
                    }
                }
            }
        }
    }

    private void getUserPhotoForNavigationView() {
        ImageView userRoundImage = findViewById(R.id.main_header_image);
        try {
            if (sharedPreferences.contains(USER_STORAGE_PHOTO_PATH)) {
                Drawable drawable = Drawable.createFromPath
                        (sharedPreferences.getString(USER_STORAGE_PHOTO_PATH, ""));
                userRoundImage.setImageDrawable(drawable);
                userRoundImage.setRotation(90);
            } else {
                userRoundImage.setImageResource(R.drawable.blank_profile_picture_80_80);
            }
        } catch (NullPointerException exception) {
            showToastMessage("Empty  photo");
        }
    }

    private void setClickListener() {
        addUser = findViewById(R.id.add_header_user);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userIntent = new Intent(getApplicationContext(), UserAccountActivity.class);
                startActivity(userIntent);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    private void setScreenElements() {
        toolbar = findViewById(R.id.main_screen_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle
                (this, drawerLayout,
                        toolbar, R.string.drawer_open,
                        R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void replaceFragment(Fragment fragmentName) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction()
                .add(R.id.navigation_content_frame, fragmentName);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setCheckable(true);
        int id = item.getItemId();
        if (id == R.id.nav_archive_fragment) {
            Fragment archiveFragment = new ArchiveFragment();
            replaceFragment(archiveFragment);
        } else if (id == R.id.nav_home_fragment) {
            Fragment mainFragment = new MainFragment();
            replaceFragment(mainFragment);
        } else if (id == R.id.nav_timer_fragment) {
            Fragment timerFragment = new TimerFragment();
            replaceFragment(timerFragment);
        } else if (id == R.id.nav_comments_fragment) {
            Fragment commentFragment = new CommentFragment();
            replaceFragment(commentFragment);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setClickListener();
        getMenuInflater().inflate(R.menu.drawer_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(false);
        menu.getItem(3).setVisible(false);
        return true;
    }

    public void showToastMessage(String text) {
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }
}