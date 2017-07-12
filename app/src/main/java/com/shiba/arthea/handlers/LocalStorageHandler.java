package com.shiba.arthea.handlers;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.shiba.arthea.activities.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by balex on 12.07.2017.
 */

public class LocalStorageHandler {
    private static final LocalStorageHandler ourInstance = new LocalStorageHandler();

    public static LocalStorageHandler getInstance() {
        return ourInstance;
    }

    private DatabaseHandler databaseHandler;
    private final String STORE_FILENAME = "store.asf";

    ////
    private LocalStorageHandler() {
        databaseHandler = DatabaseHandler.getInstance();
    }

    ////
    public boolean isNotEmpty(Context context) {
        return context.getFileStreamPath(STORE_FILENAME).exists();
    }

    public void updateStorage(Context context) {

        if (databaseHandler.isOnline())
            try (FileOutputStream fOut = context.openFileOutput(STORE_FILENAME, Context.MODE_PRIVATE);
                 ObjectOutputStream oOut = new ObjectOutputStream(fOut)) {

                oOut.writeObject(databaseHandler.retrieveDatabaseContents());
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("tag", "Exception caught in LocalStorageHandler.updateStorage(Context)");
            }
    }

    public List<String> getSubjects(Context context) {
        try (FileInputStream fIn = context.openFileInput(STORE_FILENAME);
             ObjectInputStream oIn = new ObjectInputStream(fIn)) {

            return ((List[]) oIn.readObject())[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getAttributes(Context context) {
        try (FileInputStream fIn = context.openFileInput(STORE_FILENAME);
             ObjectInputStream oIn = new ObjectInputStream(fIn)) {

            return ((List[]) oIn.readObject())[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getPredicates(Context context) {
        try (FileInputStream fIn = context.openFileInput(STORE_FILENAME);
             ObjectInputStream oIn = new ObjectInputStream(fIn)) {

            return ((List[]) oIn.readObject())[2];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
