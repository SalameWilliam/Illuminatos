package com.illuminati_zombies.illuminati_zombies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by antoine on 27/02/17.
 */

public class ScoreboardActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scoreboard);


        Button button = (Button) findViewById(R.id.backButton3);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(ScoreboardActivity.this, MenuActivity.class);
                finish();
                startActivity(newActivity);
            }
        });

    }
}