package com.example.tooltip;

import android.app.Application;

import com.ramaraj.tooltip.ToolTipBuilder;
import com.ramaraj.tooltip.ToolTipComposer;
import com.ramaraj.tooltip.ToolTipInjector;

public class ToolTipApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ToolTipComposer tipComposer = new ToolTipComposer();
        tipComposer.addStaticTips("MainActivity", new String[]{"helloWorldLabel"}, new String[]{"Tips for hello world"});

        ToolTipInjector.init(this, tipComposer);
    }
}

