package com.example.spandanaravulapalli.TEDRadioHour;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Spandana on 13-06-2016.
 */
public class AppUtil {
    static public class AppPullParser {
        static ArrayList<App> parseApps(InputStream inputStream) throws XmlPullParserException, IOException {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(inputStream, "UTF-8");
            ArrayList<App> appArrayList = new ArrayList<>();
            App app = null;
            String nameSpace=xpp.getNamespace("itunes");

            boolean insideItem=false;
            boolean insideImage=false;
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT){
                if (eventType == XmlPullParser.START_TAG){

                    //app icon image

                    if(xpp.getName().equalsIgnoreCase("channel")){
                        Log.d("urlbyapp","inside     channel");
                        insideImage=true;
                        if (xpp.getName().equalsIgnoreCase("image") && insideImage){
                            Log.d("urlbyapp","inside     image");
                            if (xpp.getName().equalsIgnoreCase("url")){
                                Log.d("urlbyapp","inside     url");
                             //   App main = new App();
                                String s=xpp.nextText();
                                MainActivity.image=xpp.nextText();
                                Log.d("mainImage",s);
                               // main.setMainImage(s);
                            }

                        }

                    }

                    //check for entry tag item
                    else if (xpp.getName().equalsIgnoreCase("item")){
                        insideItem = true;
                        app = new App();
                    }

                    //app title
                    else if (xpp.getName().equalsIgnoreCase("title")){
                        if (insideItem){
                            String s=xpp.nextText();
                            Log.d("title",s);
                            app.setTitle(s);
                        }
                    }

                    //description
                    else if (xpp.getName().equalsIgnoreCase("description")){
                        if (insideItem){
                            String s=xpp.nextText();
                            Log.d("description",s);
                            app.setDescription(s);
                        }
                    }

                    //publishing date
                    else if (xpp.getName().equalsIgnoreCase("pubDate")){
                        if (insideItem){
                            String s=xpp.nextText();
                            Log.d("pubdate",s);
                            app.setPubDate(s);
                        }
                    }

                    //image url
                    else if (xpp.getName().equalsIgnoreCase("image")){
                        if(insideItem){
                            String s1 = xpp.getAttributeValue(nameSpace,"href");
                            Log.d("imageurl", s1);
                            app.setImgURL(s1);
                        }
                    }

                    //mp3 duration
                    else if (xpp.getName().equalsIgnoreCase("duration")){
                        if(insideItem){
                            String s=xpp.nextText();
                            Log.d("duration",s);
                            app.setDuration(s);
                        }
                    }

                    //mp3 url
                    else if(xpp.getName().equalsIgnoreCase("enclosure")){
                        if(insideItem){
                            String s1 = xpp.getAttributeValue(null,"url");
                            Log.d("mp3url", s1);
                            app.setMp3url(s1);
                        }

                    }

                }

                else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item"))
                {
                    insideItem = false;
                    appArrayList.add(app);
                    app=null;
                }
                eventType = xpp.next();
            }
                return appArrayList;
            }

        }




}