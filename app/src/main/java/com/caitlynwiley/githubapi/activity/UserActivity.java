package com.caitlynwiley.githubapi.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.caitlynwiley.githubapi.R;
import com.caitlynwiley.githubapi.model.GitHubUser;
import com.caitlynwiley.githubapi.rest.APIClient;
import com.caitlynwiley.githubapi.rest.GitHubUserEndPoints;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    ImageView avatarImg;
    TextView usernameTextView;
    TextView followersTextView;
    TextView followingTextView;
    TextView logIn;
    TextView email;
    TextView numRepos;
    TextView htmlUrl;
    Button repos;

    Bundle extras;
    String loginName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        avatarImg = findViewById(R.id.avatar);
        usernameTextView = findViewById(R.id.username);
        followersTextView = findViewById(R.id.followers);
        followingTextView = findViewById(R.id.following);
        logIn = findViewById(R.id.logIn);
        email = findViewById(R.id.email);
        numRepos = findViewById(R.id.numRepos);
        htmlUrl = findViewById(R.id.htmlUrl);
        repos = findViewById(R.id.btn_repos);

        extras = getIntent().getExtras();
        loginName = extras.getString("STRING_I_NEED");

        Log.i("Username", loginName);
        loadData();
    }

    private void loadData() {
        final GitHubUserEndPoints apiService = APIClient.getClient().create(GitHubUserEndPoints.class);

        Call<GitHubUser> call = apiService.getUser(loginName);
        call.enqueue(new Callback<GitHubUser>() {
            @Override
            public void onResponse(Call<GitHubUser> call, Response<GitHubUser> response) {
                if (response.body().getName() == null) {
                    usernameTextView.setText("Username: - No username provided -");
                } else {
                    usernameTextView.setText("Username: " + response.body().getName());
                }

                followersTextView.setText("Followers: " + response.body().getFollowers());
                followingTextView.setText("Following: " + response.body().getFollowers());
                numRepos.setText("Public Repositories: " + response.body().getNumRepos());
                logIn.setText("Login: " + response.body().getLogin());
                htmlUrl.setText("Link to Profile: " + response.body().getHtmlUrl());

                if (response.body().getEmail() == null) {
                    email.setText("Email: - No email provided -");
                } else {
                    email.setText("Email: " + response.body().getEmail());
                }

                ImageDownloader downloader = new ImageDownloader();
                try {
                    avatarImg.setImageBitmap(downloader.execute(response.body().getAvatar()).get());
                    avatarImg.getLayoutParams().height=220;
                    avatarImg.getLayoutParams().width=220;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GitHubUser> call, Throwable t) {

            }
        });
    }

    public void loadOwnRepos(View view) {
        Intent intent = new Intent(UserActivity.this, RepositoriesActivity.class);
        intent.putExtra("username", loginName);
        startActivity(intent);
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                Bitmap bm = BitmapFactory.decodeStream(in);
                return bm;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
