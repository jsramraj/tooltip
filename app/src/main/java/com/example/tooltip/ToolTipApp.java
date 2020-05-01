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
        String[] identifiers = new String[]{"helloWorldLabel", String.valueOf(R.id.empName), "designation"};
        String[] tips = new String[]{"Tips for hello world", "Name of the logged in employee", "Role of the employee in the company"};
        tipComposer.addStaticTips("MainActivity", identifiers, tips);

        ToolTipInjector.init(this, tipComposer);
    }
}

