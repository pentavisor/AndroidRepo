package com.example.myapplicationisbetter.ui.usercreatepage;

import android.widget.Switch;

import com.example.myapplicationisbetter.data.models.UserInetInform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;


public class RandomUserAPIParser {


    public static UserInetInform goParse(InputStream inputStream){

        String nameSegment = null;
        String pictureSegment = null;
        String sexSegment = null;
        Boolean sex = true;
        String mapSegment = null;

        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        try {
            for (; ; ) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
        }catch (Exception e){
        }


        try {
            JSONObject root = new JSONObject(out.toString());

            JSONArray array = root.getJSONArray("results");

            //String nameSegment =  root.getJSONObject("name").getString("first")
            //String pictureSegment =  array.getString(1);
            for (int i = 0; i < array.length(); i++)
            {
                nameSegment =  array.getJSONObject(i).getJSONObject("name").getString("first");
                pictureSegment = array.getJSONObject(i).getJSONObject("picture").getString("large");
                sexSegment = array.getJSONObject(i).getJSONObject("name").getString("title");
                mapSegment = array.getJSONObject(i).getJSONObject("location").getJSONObject("coordinates").getString("latitude") + ":"+array.getJSONObject(i).getJSONObject("location").getJSONObject("coordinates").getString("longitude");
            }
            switch (sexSegment){
                case("miss"):sex = false;break;
                case("ms"):sex = false;break;
                case("mrs"):sex = false;break;
                default:sex = true;
            }
            return new UserInetInform(nameSegment,pictureSegment,sex,mapSegment);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}


