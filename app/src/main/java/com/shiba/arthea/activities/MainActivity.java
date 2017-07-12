package com.shiba.arthea.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.shiba.arthea.R;
import com.shiba.arthea.handlers.LocalStorageHandler;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button playButton = (Button) findViewById(R.id.activity_main_play_button);
        Button settingsButton = (Button) findViewById(R.id.activity_main_settings_button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Unimplemented", Toast.LENGTH_SHORT).show();
            }
        });

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            //RUNS ON SEPARATE THREAD!!! NO VIEW INTERACTION ALLOWED
            protected Boolean doInBackground(Void... params) {
                if(!LocalStorageHandler.getInstance().isNotEmpty(MainActivity.this)) {
                    Log.d("tag", "LocalStorage is empty, retrieving from database...");
                    LocalStorageHandler.getInstance().updateStorage(MainActivity.this);
                }else {
                    Log.d("tag", "LocalStorage is present, update *must* be done explicitly.");
                }
                return LocalStorageHandler.getInstance().isNotEmpty(MainActivity.this);
            }

            @Override
            //RUNS ON MAIN THREAD!!! NO NETWORKING ALLOWED
            protected void onPostExecute(Boolean result) {
                findViewById(R.id.activity_main_play_button).setEnabled(result);
                ((Button) findViewById(R.id.activity_main_play_button)).setText(result ? "Play!" : "Error!");
            }
        }.execute();
    }
}
