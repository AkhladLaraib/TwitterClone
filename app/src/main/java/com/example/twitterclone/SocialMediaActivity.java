package com.example.twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;

public class SocialMediaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private final ArrayList<String> tUsers = new ArrayList<>();
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        FancyToast.makeText(SocialMediaActivity.this, "Welcome " +
                        ParseUser.getCurrentUser().getUsername(),
                    FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();


        ListView listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter(SocialMediaActivity.this,
                android.R.layout.simple_list_item_checked, tUsers);

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(SocialMediaActivity.this);

        try {

            ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
            query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

            query.findInBackground((objects, e) -> {

                if (e == null) {

                    if (objects.isEmpty()) {
                        FancyToast.makeText(SocialMediaActivity.this, "No other users found.",
                                FancyToast.LENGTH_SHORT, FancyToast.CONFUSING, true).show();
                    } else {
                        tUsers.clear();
                        for (ParseUser twitterUser : objects) {
                            String username = twitterUser.getString("username");
                            if (username != null && !username.trim().isEmpty())
                                tUsers.add(username);

                            else FancyToast.makeText(SocialMediaActivity.this, "User has null or empty username",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();

                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            listView.setVisibility(View.VISIBLE);
                        }

                    }
                } else {
                    // Handle error
                    System.out.println("Error fetching users: " + e.getMessage());
                    FancyToast.makeText(SocialMediaActivity.this, "Error fetching users.",
                            FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                }

            });

        } catch (Exception e) {

            Log.e("Exception", "Error: " + e.getMessage());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logoutUserItem) {
            ParseUser.getCurrentUser().logOutInBackground(e -> {

                Intent intent = new Intent(SocialMediaActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            });
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CheckedTextView checkedTextView = (CheckedTextView) view;
        if (checkedTextView.isChecked()){
            FancyToast.makeText(SocialMediaActivity.this, tUsers.get(position) + " is now followed!!!",
                    FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
        } else {

            FancyToast.makeText(SocialMediaActivity.this, tUsers.get(position) + " is no longer followed!!!",
                    FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
        }
    }
}

