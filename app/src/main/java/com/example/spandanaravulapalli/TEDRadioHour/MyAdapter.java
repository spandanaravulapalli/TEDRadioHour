package com.example.spandanaravulapalli.TEDRadioHour;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by spandanaravulapalli on 6/17/16.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<String> titles, descriptions, pubDates, imgURLs, durations, mp3urls;
    ArrayList<App> apps;
    Context context;
    int resource;
    static boolean flag;
    boolean change;
    static MediaPlayer mp = new MediaPlayer();
    boolean pausePlay = true;

    //constructor for adapter

    public MyAdapter(Activity activity,ArrayList<App> apps,boolean change) {

        this.context = activity;
        this.apps=apps;
        this.change=change;
    }

    public MyAdapter(Activity activity, int resource, ArrayList<App> apps, boolean change) {
        this.apps = apps;
        this.context=activity;
        this.change=change;

        Log.d("Apps", "constructor initialization: " + apps.size()+"");
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView appTitle, pubDate,appTitle_grid;
        public ImageView imageurl;
        public ImageButton playButton;
        public ImageView playButton2;

        public MyViewHolder(View view) {
            super(view);
            if (MainActivity.change) { //Grid View
                appTitle_grid=(TextView) view.findViewById(R.id.title_grid);
                imageurl = (ImageView) view.findViewById(R.id.image_grid);
                playButton2 = (ImageView) view.findViewById(R.id.image_play);
            }
            else { //List View
                appTitle = (TextView) view.findViewById(R.id.id_appName);
                pubDate = (TextView) view.findViewById(R.id.id_date);
                imageurl = (ImageView) view.findViewById(R.id.imageView);
                playButton = (ImageButton) view.findViewById(R.id.button_media);
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (!MainActivity.change) { //List view
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_item_layout, parent, false);
            Log.d("layout","inside row");
            return new MyViewHolder(itemView);
        }
        else{ //Grid View
            View itemView1 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.acitivity_grid, parent, false);
            Log.d("layout","inside grid");
            return new MyViewHolder(itemView1);
        }

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

            if(!MainActivity.change){
                holder.appTitle.setText(apps.get(position).getTitle());
                holder.pubDate.setText(apps.get(position).getPubDate());
                Log.d("layout","inside row view");
                Picasso.with(context).load(apps.get(position).getImgURL()).into(holder.imageurl);



                holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        // parent touch login

                        mp.stop();
                        MainActivity.imageView.setVisibility(ImageView.GONE);
                        MainActivity.pb.setVisibility(ProgressBar.GONE);

                        Intent intent = new Intent(context,PreviewActivity.class);
                        Log.d("intent" ,GetAppDataAsyncTask.apps.get(position).getTitle());
                        intent.putExtra(MainActivity.APP_KEY,GetAppDataAsyncTask.apps.get(position).getImgURL());
                        intent.putExtra(MainActivity.MEDIA_KEY,GetAppDataAsyncTask.apps.get(position).getMp3url());
                        intent.putExtra(MainActivity.DURATION_KEY,GetAppDataAsyncTask.apps.get(position).getDuration());
                        intent.putExtra(MainActivity.DESCRIPTION_KEY,GetAppDataAsyncTask.apps.get(position).getDescription());
                        intent.putExtra(MainActivity.PUBLICATIONDATE_KEY,GetAppDataAsyncTask.apps.get(position).getPubDate());

                        context.startActivity(intent);
                        return true;
                    }
                });
               holder.playButton.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        try {
                            mp.reset();
                            mp.setDataSource(apps.get(position).getMp3url());
                            mp.prepare();
                            mp.start();
                            MainActivity.pb.setVisibility(ProgressBar.VISIBLE);
                            MainActivity.imageView.setVisibility(ImageView.VISIBLE);
                            MainActivity.pb.setProgress(0);
                            Log.d("main", "play button clicked");
                            final int duration = Integer.parseInt(GetAppDataAsyncTask.apps.get(position).getDuration());
                            Log.d("duration",duration+"");
                            MainActivity.pb.setMax(duration*1000);
                            final long dur = duration;

                            new CountDownTimer(duration*1000, duration){

                                @Override
                                public void onTick(long millisUntilFinished) {
                                    if (mp.isPlaying()) {
                                        MainActivity.pb.setProgress(MainActivity.pb.getProgress() + duration);
                                    }
                                }

                                @Override
                                public void onFinish() {
                                    mp.stop();
                                    MainActivity.imageView.setVisibility(ImageView.GONE);
                                    MainActivity.pb.setVisibility(ProgressBar.GONE);
                                    return;

                                }

                            }.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return true;
                    }
                });

                MainActivity.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flag=!flag;
                        if (flag) {
                            MainActivity.imageView.setImageResource(R.drawable.ic_action_play);
                            mp.pause();
                        }
                        else{
                            MainActivity.imageView.setImageResource(R.drawable.ic_action_pause);
                            mp.start();
                        }
                    }
                });

            }
        else{
                Log.d("layout","inside grid view");
                holder.appTitle_grid.setText(apps.get(position).getTitle());
                Picasso.with(context).load(apps.get(position).getImgURL()).into(holder.imageurl);
                holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        // parent touch login

                        mp.stop();
                        MainActivity.imageView.setVisibility(ImageView.GONE);
                        MainActivity.pb.setVisibility(ProgressBar.GONE);

                        Intent intent = new Intent(context,PreviewActivity.class);
                        Log.d("intent" ,GetAppDataAsyncTask.apps.get(position).getTitle());
                        intent.putExtra(MainActivity.APP_KEY,GetAppDataAsyncTask.apps.get(position).getImgURL());
                        intent.putExtra(MainActivity.MEDIA_KEY,GetAppDataAsyncTask.apps.get(position).getMp3url());
                        intent.putExtra(MainActivity.DURATION_KEY,GetAppDataAsyncTask.apps.get(position).getDuration());
                        intent.putExtra(MainActivity.DESCRIPTION_KEY,GetAppDataAsyncTask.apps.get(position).getDescription());
                        intent.putExtra(MainActivity.PUBLICATIONDATE_KEY,GetAppDataAsyncTask.apps.get(position).getPubDate());

                        context.startActivity(intent);
                        return true;
                    }
                });
                holder.playButton2.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        try {
                            mp.reset();
                            mp.setDataSource(apps.get(position).getMp3url());
                            mp.prepare();
                            mp.start();
                            MainActivity.pb.setVisibility(ProgressBar.VISIBLE);
                            MainActivity.imageView.setVisibility(ImageView.VISIBLE);
                            MainActivity.pb.setProgress(0);
                            Log.d("main", "play button clicked");
                            final int duration = Integer.parseInt(GetAppDataAsyncTask.apps.get(position).getDuration());
                            MainActivity.pb.setMax(duration*100);
                            final long dur = duration;

                            new CountDownTimer(duration*100, duration){

                                @Override
                                public void onTick(long millisUntilFinished) {

                                    if (mp.isPlaying()) {
                                        MainActivity.pb.setProgress(MainActivity.pb.getProgress() + duration);

                                    }
                                }

                                @Override
                                public void onFinish() {
                                    mp.stop();
                                    MainActivity.imageView.setVisibility(ImageView.GONE);
                                    MainActivity.pb.setVisibility(ProgressBar.GONE);
                                    return;

                                }

                            }.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return true;
                    }
                });
                MainActivity.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flag=!flag;
                        if (flag) {
                            MainActivity.imageView.setImageResource(R.drawable.ic_action_play);
                            mp.pause();
                        }
                        else{
                            MainActivity.imageView.setImageResource(R.drawable.ic_action_pause);
                            mp.start();
                        }
                    }
                });

            }

        Log.d("position", "position in adapter is: "+position);

    }


    @Override
    public int getItemCount() {
        return apps.size();

    }
}


