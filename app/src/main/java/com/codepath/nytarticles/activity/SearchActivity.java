package com.codepath.nytarticles.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.codepath.nytarticles.R;
import com.codepath.nytarticles.adapter.EndlessRecyclerViewScrollListener;
import com.codepath.nytarticles.fragment.FilterDialogFragment;
import com.codepath.nytarticles.model.Article;
import com.codepath.nytarticles.adapter.ArticleArrayAdapter;
import com.codepath.nytarticles.model.Filter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static android.R.attr.filter;

public class SearchActivity extends AppCompatActivity implements FilterDialogFragment.FilterEditedListener {

    private final static String NYT_URL = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    private final static String PRIVATE_API_KEY = "a72dbc47f1ba479b8f451e35a1e916f3";

    private Filter mainFilter;

    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private String apiQuery;

    private AsyncHttpClient client;
    private RequestParams params;

    @BindView(R.id.rvSearch) RecyclerView rvSearch;
    @BindView(R.id.toolbar) Toolbar toolbar;

    ArrayList<Article> articles;
    ArticleArrayAdapter articleArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();
    }

    /*
    *  Method to initialize Variables
    */
    public void init()
    {
        ButterKnife.bind(this);

        // Display the Toolbar
        setSupportActionBar(toolbar);

        // connect to the Articles in the NYT
        client  = new AsyncHttpClient();
        params = new RequestParams();

        apiQuery = "New York";
        // set the Filters
        mainFilter = new Filter("","newest", null );
        articleSearch(apiQuery,0);

        // create new articles list
        articles = new ArrayList<>();
        articleArrayAdapter = new ArticleArrayAdapter(this,articles);

        recycleViewSettings();
    }

    /*
    /* This Method Handle the Recycle View Setting ( Data <-> Adapter <-> Recycle View )
    */
    private void recycleViewSettings() {

        // settings for gaggered Grid Layout
        rvSearch.setHasFixedSize(true);
        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);

        // Gap Strategy
        gaggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        // Attach the adapter to the recyclerview to populate items
        rvSearch.setAdapter(articleArrayAdapter);

        // Set layout manager to position the items
        rvSearch.setLayoutManager(gaggeredGridLayoutManager);

        // endless scrolling
        rvSearch.addOnScrollListener(new EndlessRecyclerViewScrollListener(gaggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(page);
            }
        });
    }

    private void customLoadMoreDataFromApi(int page) {

        int offset = page % 100;
        articleSearch(getApiQuery(),offset);

        Toast.makeText(this, "LOADING...", Toast.LENGTH_SHORT);
    }

    /*
    * This Method is for Getting an Article from the NYT API
     */
    private void articleSearch(String query, int offset) {

        // put params into the request
        params.put(getString(R.string.api_key), PRIVATE_API_KEY);
        params.put(getString(R.string.page),offset);
        params.put(getString(R.string.query), query);

        if ( ! mainFilter.getSortOrder().isEmpty()){
            params.put("sort", mainFilter.getSortOrder());
         }
        if ( !mainFilter.getBeginDate().isEmpty()) {
            params.put("begin_date", mainFilter.getBeginDate());
        }
        String newsDesk = getNewsDeskString();
        if ( newsDesk != "" )
           params.put(getString(R.string.news_desk_values),newsDesk);

        client.get(NYT_URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                JSONArray jsonArrayResults = null;

                try{
                    // getting the results into Article Array
                    jsonArrayResults = response.getJSONObject("response").getJSONArray("docs");

                    // add all and notify the adapter about the changes
                    articles.addAll(Article.fromJsonArray(jsonArrayResults));
                    articleArrayAdapter.notifyDataSetChanged();

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("debug","not working");
                if (!isNetworkAvailable()) {
                    Toast.makeText(getApplicationContext(), "Opps looks like " +
                                    "network connectivity problem",
                            Toast.LENGTH_LONG).show();
                }

                if (!isOnline()) {
                    Toast.makeText(getBaseContext(), "Your device is not online, " +
                                    "check wifi and try again!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String getNewsDeskString() {
        List<String> newsList = mainFilter.getNewsDesks();
        String newsDesk = "";
        if ( newsList != null ){
            for ( String news : newsList){
                newsDesk = newsDesk.concat("&").concat(news);
            }
        }

        if  (newsDesk == "")
            return newsDesk;

        return newsDesk.substring(1,newsDesk.length());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Search Button Query
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // clear the previous search
                articles.clear();
                // notify the adapter
                articleArrayAdapter.notifyDataSetChanged();

                // set the new query
                setApiQuery(query);
                articleSearch(query,0);
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
           showEditDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    // Get Query
    public String getApiQuery() {
        return apiQuery;
    }

    // Set Query
    public void setApiQuery(String apiQuery) {
        this.apiQuery = apiQuery;
    }

    /*
    * Method to execute the Filter Dialog Fragment
    */
    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterDialogFragment filterDialogFragment = FilterDialogFragment.newInstance("Search Filter");

        filterDialogFragment.show(fm, "fragment_sort_dialog");
    }

    /*
    *  Method for getting response from the FilterDialof
    */
    @Override
    public void onFinishEditFilter(Filter filter) {
        mainFilter = filter;
        Toast.makeText(this,filter.getBeginDate(), Toast.LENGTH_SHORT).show();

        articles.clear();
        articleSearch(apiQuery,0);
    }


    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (InterruptedException | IOException e) { e.printStackTrace(); }
        return false;
    }
}
