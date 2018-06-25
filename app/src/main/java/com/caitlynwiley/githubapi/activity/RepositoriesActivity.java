package com.caitlynwiley.githubapi.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.caitlynwiley.githubapi.R;
import com.caitlynwiley.githubapi.ReposAdapter;
import com.caitlynwiley.githubapi.model.GitHubRepo;
import com.caitlynwiley.githubapi.rest.APIClient;
import com.caitlynwiley.githubapi.rest.GitHubRepoEndPoint;
import com.caitlynwiley.githubapi.rest.GitHubUserEndPoints;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepositoriesActivity extends AppCompatActivity {

    String receivedUserName;
    TextView usernameTextView;
    RecyclerView mRecyclerView;
    List<GitHubRepo> myDataSource = new ArrayList<>();
    RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositories);

        Bundle extras = getIntent().getExtras();
        receivedUserName = extras.getString("username");

        usernameTextView = findViewById(R.id.usernameRepoScreen);
        usernameTextView.setText("User: " + receivedUserName);

        mRecyclerView = findViewById(R.id.repos_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new ReposAdapter(myDataSource, R.layout.repo_list_item, getApplicationContext());
        mRecyclerView.setAdapter(myAdapter);

        loadRepositories();
    }

    public void loadRepositories() {
        GitHubRepoEndPoint apiService = APIClient.getClient().create(GitHubRepoEndPoint.class);

        Call<List<GitHubRepo>> call = apiService.getRepo(receivedUserName);
        call.enqueue(new Callback<List<GitHubRepo>>() {
            @Override
            public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
                myDataSource.clear();
                // Fills in info for each repo
                myDataSource.addAll(response.body());
                // Tell adapter that something changed
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
                Log.e("Repos", t.toString());
            }
        });
    }
}
