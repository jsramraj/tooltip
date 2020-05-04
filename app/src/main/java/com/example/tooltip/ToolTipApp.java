package com.example.tooltip;

import android.app.Application;

import com.ramaraj.tooltip.ToolTipComposer;
import com.ramaraj.tooltip.ToolTipConfig;
import com.ramaraj.tooltip.ToolTipManager;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

public class ToolTipApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ToolTipComposer.Builder tipComposerBuilder = new ToolTipComposer.Builder();
        try {
            tipComposerBuilder.addStaticTip(readJSONFromAsset());
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

