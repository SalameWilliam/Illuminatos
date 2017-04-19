package com.illuminati_zombies.illuminati_zombies;

import android.app.Activity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.*;


/**
 * Created by antoine on 23/02/17.
 */

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] menu = {
                "Single Player",
                "Muliplayer",
                "Create Level",
                "ScoreboardActivity",
                "testGL"
        };

        setContentView(R.layout.activity_menu);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, menu);
        ListView list = (ListView) findViewById(R.id.listView1);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent newActivity;
                Bundle b = new Bundle();
                switch(position){
                    case 0: newActivity = new Intent(MenuActivity.this, GameActivity.class);
                        startActivity(newActivity);
                        break;
                    case 1: newActivity = new Intent(MenuActivity.this, MultiplayerActivity.class);
                        startActivity(newActivity);
                        break;
                    case 2: newActivity = new Intent(MenuActivity.this, LevelCreationActivity.class);
                        startActivity(newActivity);
                        break;
                    case 3: newActivity = new Intent(MenuActivity.this, ScoreboardActivity.class);
                        startActivity(newActivity);
                        break;
                    /*case 4: newActivity = new Intent(MenuActivity.this, OpenGLActivity.class);
                        startActivity(newActivity);
                        break;*/
                }
            }
        });

        Button button = (Button) findViewById(R.id.exitButton1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });


    }

}
