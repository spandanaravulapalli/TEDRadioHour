package com.example.spandanaravulapalli.TEDRadioHour;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import java.util.ArrayList;

/**
 * Created by spandanaravulapalli on 6/13/16.
 */

public class MainActivity extends AppCompatActivity{

    public static final String APP_KEY="appObj";
    public static final String MEDIA_KEY="mediaObj";
    public static final String DURATION_KEY="durationObj";
    public static final String DESCRIPTION_KEY="descObj";
    public static final String PUBLICATIONDATE_KEY="pubObj";
    public static boolean change=false;
    public static String image=null;

    public static ProgressBar pb;
    public static ImageView imageView;

    //recycler view
    static RecyclerView recyclerView;
    static MyAdapter mAdapter;
    ArrayList<App> apps =  new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView= (ImageView) findViewById(R.id.imageView2);
        imageView.setImageResource(R.drawable.ic_action_pause);
        imageView.setVisibility(ImageView.GONE);

        pb = new ProgressBar(MainActivity.this,null,android.R.attr.progressBarStyleHorizontal);
        pb.setMax(100);
        pb = (ProgressBar) findViewById(R.id.progressBar2);
        pb.setVisibility(ProgressBar.GONE);

        //Recycler View
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        Log.d("recycler", recyclerView +"");
        mAdapter = new MyAdapter(this,apps,change);
        recyclerView.setHasFixedSize(false);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        Log.d("layout", mLayoutManager+"");
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        //Add ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ted);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

        if(isConnected()){
            new GetAppDataAsyncTask(MainActivity.this).execute("http://www.npr.org/rss/podcast.php?id=510298");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.action_refresh:
                MainActivity.imageView.setVisibility(ImageView.GONE);
                MainActivity.pb.setVisibility(ProgressBar.GONE);
                MyAdapter.mp.stop();
                pb.setProgress(0);
                change=!change;
                    Log.d("spandana","refresh checked"+change);
                if(!change){ // List View
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                    Log.d("layout", mLayoutManager+"");
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);

                }else{  //Grid View
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,2);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);

                }

                break;
        }
        return true;

    }


    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }

        return false;
    }


}
