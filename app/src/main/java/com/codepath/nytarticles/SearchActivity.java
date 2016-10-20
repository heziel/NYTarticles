package com.codepath.nytarticles;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.nytarticles.Model.Article;
import com.codepath.nytarticles.adapter.ArticleArrayAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    private final static String NYT_URL = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    private final static String PRIVATE_API_KEY = "a72dbc47f1ba479b8f451e35a1e916f3";

    @BindView(R.id.tvSearch) TextView tvSearch;
    @BindView(R.id.searchButton) Button searchButton;
    @BindView(R.id.gvSearch) GridView gvSearch;

    ArrayList<Article> articles;
    ArticleArrayAdapter articleArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Show Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
               Log.d("SearchActivity","Inside onClick");
                articleSearch();
            }
        }
        );

    }

    /*
    *  Method to initialize Variables
    */
    public void init()
    {
        ButterKnife.bind(this);
        articles = new ArrayList<>();
        articleArrayAdapter = new ArticleArrayAdapter(this,articles);
        gvSearch.setAdapter(articleArrayAdapter);
    }

    /*
    * This Method is for Getting an Article from the NYT API
     */
    private void articleSearch() {

        // get the query text from the search text
        String query = tvSearch.getText().toString();

        // connect to the Articles in the NYT
        AsyncHttpClient client  = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        // put params into the request
        params.put(getString(R.string.api_key), PRIVATE_API_KEY);
        params.put(getString(R.string.page),0);
        params.put(getString(R.string.query), query);

        client.get(NYT_URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray jsonArrayResults = null;

                try{

                    // getting the results into Article Array
                    jsonArrayResults = response.getJSONObject("response").getJSONArray("docs");
                    // add all and notify the adapter about the changes
                    articleArrayAdapter.addAll(Article.fromJsonArray(jsonArrayResults));

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
