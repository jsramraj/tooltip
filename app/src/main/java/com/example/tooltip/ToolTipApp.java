package com.example.tooltip;

import android.app.Application;

import com.ramaraj.tooltip.ToolTipBuilder;
import com.ramaraj.tooltip.ToolTipInjector;

public class ToolTipApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ToolTipBuilder builder = new ToolTipBuilder();
        builder.addStaticTips("MainActivity", new int[]{R.id.helloWorldLabel}, new String[]{"Tips for hello world"});

        ToolTipInjector.init(this, builder);
    }
}

