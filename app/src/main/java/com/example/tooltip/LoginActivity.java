package com.example.tooltip;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.navram.tooltip.ToolTip;
import com.navram.tooltip.ToolTipConfig;
import com.navram.tooltip.ToolTipListener;
import com.navram.tooltip.ToolTipManager;

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
            // to show home individual tips can be customized
            ToolTipConfig config = new ToolTipConfig();
            config.setTipMessageStyleResId(R.style.emailTipTextStyle);
            return config;
        }
        return null;
    }

    public void resetAllTips(View view) {
        ToolTipManager.DataWrapper.resetAllAcknowledgements();
    }

    public void showTips(View view) {
        ToolTipManager.ActivityWrapper.relaunchHelpForActivity(this);
    }
}
