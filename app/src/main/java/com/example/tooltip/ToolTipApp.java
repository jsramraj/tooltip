package com.example.tooltip;

import android.app.Application;
import android.util.Log;

import com.ramaraj.tooltip.ToolTipComposer;
import com.ramaraj.tooltip.ToolTipInjector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ToolTipApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ToolTipComposer tipComposer = new ToolTipComposer();

        try {
            JSONObject obj = new JSONObject(readJSONFromAsset());

            for (Iterator<String> it = obj.keys(); it.hasNext(); ) {
                String key = it.next();
                JSONArray data = obj.getJSONArray(key);

                ArrayList<String> identifiers = new ArrayList<>();
                ArrayList<String> tips = new ArrayList<>();

                for (int i = 0; i < data.length(); i++) {
                    JSONObject tipObject = data.getJSONObject(i);
                    identifiers.add(tipObject.getString("id"));
                    tips.add(tipObject.getString("tip"));
                }

                tipComposer.addStaticTips(key, identifiers.toArray(new String[0]), tips.toArray(new String[0]));
            }
            Log.d("TTA", String.valueOf(obj));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ToolTipInjector.init(this, tipComposer);
    }

    public String readJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("tooltip_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}

