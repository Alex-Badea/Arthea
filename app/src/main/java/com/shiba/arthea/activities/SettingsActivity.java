package com.shiba.arthea.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shiba.arthea.R;
import com.shiba.arthea.handlers.LocalStorageHandler;

public class SettingsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Button updateTraits = (Button) findViewById(R.id.activity_settings_updatetraits_button);
        Button changeName = (Button) findViewById(R.id.activity_settings_changename_button);

        updateTraits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute(){
                        updateTraits.setText("Wait...");
                        updateTraits.setEnabled(false);
                    }

                    @Override
                    //RUNS ON SEPARATE THREAD!!! NO VIEW INTERACTION ALLOWED
                    protected Void doInBackground(Void... params) {
                        LocalStorageHandler.getInstance().updateTraitsStorage(SettingsActivity.this);
                        return null;
                    }

                    @Override
                    //RUNS ON MAIN THREAD!!! NO NETWORKING ALLOWED
                    protected void onPostExecute(Void result) {
                        updateTraits.setText("Fetch traits from DB");
                        updateTraits.setEnabled(true);
                    }
                }.execute();
            }
        });

        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
                final EditText edittext = new EditText(SettingsActivity.this);
                alert.setTitle("Enter your name:");
                alert.setView(edittext);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String name = edittext.getText().toString();
                        LocalStorageHandler.getInstance().updateNameStorage(SettingsActivity.this, name);
                    }
                });
                alert.show();
            }
        });
    }
}
