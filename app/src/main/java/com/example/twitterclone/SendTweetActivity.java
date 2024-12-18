package com.example.twitterclone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;

public class SendTweetActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtSendTweets;
    private Button btnOtherTweets;
    private GridView gridViewUserTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        edtSendTweets = findViewById(R.id.edtSendTweets);

        btnOtherTweets = findViewById(R.id.btnOtherTweets);
        gridViewUserTweets = findViewById(R.id.gridViewUserTweets);

        btnOtherTweets.setOnClickListener(SendTweetActivity.this);

    }

    public void sendTweets(View view) {

        ParseObject parseObject = new ParseObject("MyTweet");
        parseObject.put("tweet", edtSendTweets.getText().toString());
        parseObject.put("user", ParseUser.getCurrentUser().getUsername());

        final ProgressDialog progressDialog = new ProgressDialog(SendTweetActivity.this);
        progressDialog.setMessage("Sending a tweet...");
        progressDialog.show();

        parseObject.saveInBackground(e -> {

            if (e == null)
                FancyToast.makeText(SendTweetActivity.this, ParseUser.getCurrentUser().getUsername() +
                        "'s tweet " + "(" + edtSendTweets.getText().toString() + ") is saved!!!",
                        FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
            else
                FancyToast.makeText(SendTweetActivity.this, e.getMessage() ,
                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();

            progressDialog.dismiss();
        });
    }

    @Override
    public void onClick(View v) {

        final ArrayList<HashMap<String, String>> tweetList = new ArrayList<>();
        final SimpleAdapter adapter = new SimpleAdapter(SendTweetActivity.this, tweetList,
                android.R.layout.simple_list_item_2, new String[]{"tweetUsername", "tweetValue"},
                new int[]{android.R.id.text1, android.R.id.text2});

        try {

                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
                parseQuery.whereContainedIn("user", ParseUser.getCurrentUser().getList("fanOf"));
                parseQuery.findInBackground((objects, e) -> {

                    if (!objects.isEmpty() && e == null) {
                        for (ParseObject tweetObject : objects) {
                            HashMap<String, String> userTweet = new HashMap<>();
                            userTweet.put("tweetUsername", tweetObject.getString("user"));
                            userTweet.put("tweetValue", tweetObject.getString("tweet"));
                            tweetList.add(userTweet);

                        }
                        gridViewUserTweets.setAdapter(adapter);
                    }
                });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}