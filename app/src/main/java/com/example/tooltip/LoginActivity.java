package com.example.tooltip;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ramaraj.tooltip.ToolTip;
import com.ramaraj.tooltip.ToolTipConfig;
import com.ramaraj.tooltip.ToolTipListener;
import com.ramaraj.tooltip.ToolTipManager;

public class LoginActivity extends AppCompatActivity implements ToolTipListener.ToolTipOnShowListener, ToolTipListener.ToolTipConfigChange {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public void onTipShown(ToolTip tip) {
        Log.d("TTA", String.format("Tip shown for Id:%s with text:%s for activity:%s", tip.getResourceId(), tip.getTipText(), tip.getActivityName()));
    }

    @Override
    public ToolTipConfig configForTip(ToolTip tip) {
        if (tip.getResourceId() == R.id.etEmail) {
            ToolTipConfig config = new ToolTipConfig();
            config.setTipTextStyleResId(R.style.emailTipTextStyle);
            return config;
        }
        return null;
    }

    public void resetAllTips(View button) {
        ToolTipManager.DataWrapper.resetAllAcknowledgements();
    }

    public void showTips(View button) {
        ToolTipManager.ActivityWrapper.relaunchHelpForActivity(this);
    }
}
