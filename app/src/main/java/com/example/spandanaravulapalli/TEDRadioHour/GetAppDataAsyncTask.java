package com.example.spandanaravulapalli.TEDRadioHour;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Spandana on 13-06-2016.
 */
public class GetAppDataAsyncTask extends AsyncTask<String, Void, ArrayList<App>> {
    Activity activity;
    ListView listView;

    public static final String APP_KEY="appObj";
    ProgressDialog progressDialog;

    public GetAppDataAsyncTask(Activity activity) {
        this.activity = activity;
    }

    boolean image_type = false;

    static ArrayList<App> apps=new ArrayList<>();

    ArrayList value ;

    @Override
    protected ArrayList<App> doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int statusCode = connection.getResponseCode();
            if(statusCode==HttpURLConnection.HTTP_OK){
                Log.d("demo","connected successfully");
                InputStream inputStream = connection.getInputStream();


                return AppUtil.AppPullParser.parseApps(inputStream);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(activity, R.style.AppTheme);
        progressDialog.setCancelable(false);
        // progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading Episodes");
        progressDialog.setMax(100);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(final ArrayList<App> apps) {
        super.onPostExecute(apps);

        if (apps != null){

            //Recycler view
            this.apps=apps;
            MainActivity.recyclerView = (RecyclerView) activity.findViewById(R.id.my_recycler_view);
            if(MainActivity.change){ //Grid View
                Log.d("change",MainActivity.change+"");
                MainActivity.mAdapter = new MyAdapter(activity,R.layout.acitivity_grid,apps,MainActivity.change);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(activity,2);
                Log.d("layout", mLayoutManager+"");
                MainActivity.recyclerView.setLayoutManager(mLayoutManager);
                MainActivity.recyclerView.setItemAnimator(new DefaultItemAnimator());
                MainActivity.recyclerView.setAdapter(MainActivity.mAdapter);
                MainActivity.mAdapter.notifyDataSetChanged();
            }
            else { //List View
                Log.d("change",MainActivity.change+"");
                MainActivity.mAdapter = new MyAdapter(activity, R.layout.row_item_layout, apps, MainActivity.change);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
                Log.d("layout", mLayoutManager + "");
                MainActivity.recyclerView.setLayoutManager(mLayoutManager);
                MainActivity.recyclerView.setItemAnimator(new DefaultItemAnimator());
                MainActivity.recyclerView.setAdapter(MainActivity.mAdapter);
                MainActivity.mAdapter.notifyDataSetChanged();
                Log.d("notify", "data set changed");


            }

        }
        progressDialog.dismiss();

    }

}