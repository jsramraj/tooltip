package com.example.tooltip;

import android.app.Application;

import com.ramaraj.tooltip.ToolTipComposer;
import com.ramaraj.tooltip.ToolTipConfig;
import com.ramaraj.tooltip.ToolTipManager;

public class ToolTipApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ToolTipComposer.Builder tipComposerBuilder = new ToolTipComposer.Builder();
        tipComposerBuilder.addStaticTip(this, "tooltip_data.json");

        // customize the tooltips globally.
        // this will override the default appearance of the tip
        ToolTipConfig globalConfig = new ToolTipConfig();
        globalConfig.setTipMessageStyleResId(R.style.tipTextStyleGlobal);
        globalConfig.setTipTitleStyleResId(R.style.tipTitleTextStyleGlobal);
        tipComposerBuilder.setGlobalConfig(globalConfig);

        ToolTipManager.init(this, tipComposerBuilder.build());
    }
}

