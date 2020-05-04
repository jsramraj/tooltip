package com.example.tooltip;

import android.app.Application;
import android.util.Log;

import com.ramaraj.tooltip.ToolTipComposer;
import com.ramaraj.tooltip.ToolTipConfig;
import com.ramaraj.tooltip.ToolTipManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class ToolTipApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ToolTipComposer.Builder tipComposerBuilder = new ToolTipComposer.Builder();

        try {
            JSONObject obj = new JSONObject(readJSONFromAsset());

            for (Iterator<String> it = obj.keys(); it.hasNext(); ) {
                String activityName = it.next();
                JSONArray data = obj.getJSONArray(activityName);

                for (int i = 0; i < data.length(); i++) {
                    JSONObject tipObject = data.getJSONObject(i);
                    tipComposerBuilder.addStaticTip(activityName,
                            tipObject.getString("id"),
                            tipObject.has("title") ? tipObject.getString("title") : "",
                            tipObject.getString("message"));
                }
            }
            Log.d("TTA", String.valueOf(obj));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // customize the tooltips globally.
        // this will override the default appearance of the tip
        ToolTipConfig globalConfig = new ToolTipConfig();
        globalConfig.setTipMessageStyleResId(R.style.tipTextStyleGlobal);
        globalConfig.setTipTitleStyleResId(R.style.tipTitleTextStyleGlobal);
        tipComposerBuilder.setGlobalConfig(globalConfig);

        ToolTipManager.init(this, tipComposerBuilder.build());
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

