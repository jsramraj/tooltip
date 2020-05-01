package com.example.tooltip;

import android.app.Application;

import com.ramaraj.tooltip.ToolTipInjector;

public class ToolTipApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ToolTipInjector.init(this);
    }
}

