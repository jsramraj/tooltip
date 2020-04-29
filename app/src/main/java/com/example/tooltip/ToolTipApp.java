package com.example.tooltip;

import android.app.Application;

public class ToolTipApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ToolTipInjector.init(this);
    }
}

