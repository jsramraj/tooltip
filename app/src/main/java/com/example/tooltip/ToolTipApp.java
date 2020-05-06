package com.example.tooltip;

import android.app.Application;

import com.navram.tooltip.ToolTipComposer;
import com.navram.tooltip.ToolTipConfig;
import com.navram.tooltip.ToolTipManager;

public class ToolTipApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ToolTipComposer.Builder tipComposerBuilder = new ToolTipComposer.Builder();
        tipComposerBuilder.addStaticTip(this, "tooltip_data.json");

        // customize the tooltips globally.
        // this will override the default appearance of the tip
        ToolTipConfig globalConfig = new ToolTipConfig();
        globalConfig.setTipTitleStyleResId(R.style.tipTitleTextStyleGlobal);
        globalConfig.setTipMessageStyleResId(R.style.tipMessageStyleGlobal);
        globalConfig.setNextButtonStyleResId(R.style.tipNextButtonStyle);
        tipComposerBuilder.setGlobalConfig(globalConfig);

        ToolTipManager.init(this, tipComposerBuilder.build());
    }
}

