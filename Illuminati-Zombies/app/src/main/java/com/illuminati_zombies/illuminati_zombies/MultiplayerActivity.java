package com.illuminati_zombies.illuminati_zombies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by antoine on 28/02/17.
 */

public class MultiplayerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_multiplayer);

        String[] menu = {
                "Split Screen",
                "Bluetooth"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, menu);
        ListView list = (ListView) findViewById(R.id.listView2);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent newActivity;
                Bundle b = new Bundle();
                switch(position){
                    case 0:  newActivity = new Intent(MultiplayerActivity.this, SplitscreenActivity.class);
                        startActivity(newActivity);
                        break;
                    case 1: newActivity = new Intent(MultiplayerActivity.this, MultiplayerActivity.class);
                        startActivity(newActivity);
                        break;
                }
            }
        });

        Button button = (Button) findViewById(R.id.backButton1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(MultiplayerActivity.this, MenuActivity.class);
                finish();
                startActivity(newActivity);
            }
        });

    }

}
