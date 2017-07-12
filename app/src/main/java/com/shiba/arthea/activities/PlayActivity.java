package com.shiba.arthea.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shiba.arthea.R;
import com.shiba.arthea.handlers.LocalStorageHandler;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by balex on 09.07.2017.
 */
public class PlayActivity extends FragmentActivity {

    private List<String> subjects;
    private List<String> attributes;
    private List<String> predicates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        subjects = LocalStorageHandler.getInstance().getSubjects(this);
        attributes = LocalStorageHandler.getInstance().getAttributes(this);
        predicates = LocalStorageHandler.getInstance().getPredicates(this);

        final TextView subjectText = (TextView) findViewById(R.id.activity_play_subject_textview);
        final TextView attributeText = (TextView) findViewById(R.id.activity_play_attribute_textview);
        final TextView predicateText = (TextView) findViewById(R.id.activity_play_predicate_textview);

        final Thread subjectUpdater = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                subjectText.setText(subjects.get(ThreadLocalRandom.current().nextInt(0, subjects.size() - 1)));
                            }
                        });
                    }
                } catch (InterruptedException e) {e.printStackTrace();}
            }
        };

        final Thread attributeUpdater = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                attributeText.setText(attributes.get(ThreadLocalRandom.current().nextInt(0, attributes.size() - 1)));
                            }
                        });
                    }
                } catch (InterruptedException e) {e.printStackTrace();}
            }
        };

        final Thread predicateUpdater = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                predicateText.setText(predicates.get(ThreadLocalRandom.current().nextInt(0, predicates.size() - 1)));
                            }
                        });
                    }
                } catch (InterruptedException e) {e.printStackTrace();}
            }
        };

        subjectUpdater.start();
        attributeUpdater.start();
        predicateUpdater.start();

        subjectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subjectUpdater.interrupt();
            }
        });

        attributeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attributeUpdater.interrupt();
            }
        });

        predicateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                predicateUpdater.interrupt();
            }
        });
    }
}