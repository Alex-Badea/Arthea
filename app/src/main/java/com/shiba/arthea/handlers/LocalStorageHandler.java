package com.shiba.arthea.handlers;

import android.content.Context;
import android.util.Log;

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
    private final String TRAITS_STORE_FILENAME = "traits.asf";
    private final String NAME_STORE_FILENAME = "name.asf";

    ////
    private LocalStorageHandler() {
        databaseHandler = DatabaseHandler.getInstance();
    }

    ////
    public boolean traitsStoreIsNotEmpty(Context context) {
        return context.getFileStreamPath(TRAITS_STORE_FILENAME).exists();
    }

    public void updateTraitsStorage(Context context) {
        if (databaseHandler.isOnline(context))
            try (FileOutputStream fOut = context.openFileOutput(TRAITS_STORE_FILENAME, Context.MODE_PRIVATE);
                 ObjectOutputStream oOut = new ObjectOutputStream(fOut)) {

                oOut.writeObject(databaseHandler.retrieveDatabaseContents(context));
            } catch (Exception e) {
                Log.d("tag", "Exception caught in LocalStorageHandler.updateTraitsStorage(Context)");
                throw new RuntimeException(e);
            }
    }

    public List<String> getSubjects(Context context) {
        try (FileInputStream fIn = context.openFileInput(TRAITS_STORE_FILENAME);
             ObjectInputStream oIn = new ObjectInputStream(fIn)) {

            return ((List[]) oIn.readObject())[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getAttributes(Context context) {
        try (FileInputStream fIn = context.openFileInput(TRAITS_STORE_FILENAME);
             ObjectInputStream oIn = new ObjectInputStream(fIn)) {

            return ((List[]) oIn.readObject())[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getPredicates(Context context) {
        try (FileInputStream fIn = context.openFileInput(TRAITS_STORE_FILENAME);
             ObjectInputStream oIn = new ObjectInputStream(fIn)) {

            return ((List[]) oIn.readObject())[2];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateNameStorage(Context context, String name){
        try (FileOutputStream fOut = context.openFileOutput(NAME_STORE_FILENAME, Context.MODE_PRIVATE);
             ObjectOutputStream oOut = new ObjectOutputStream(fOut)) {

            oOut.writeObject(name);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("tag", "Exception caught in LocalStorageHandler.updateTraitsStorage(Context)");
        }
    }

    public String getName(Context context) {
        try (FileInputStream fIn = context.openFileInput(NAME_STORE_FILENAME);
             ObjectInputStream oIn = new ObjectInputStream(fIn)) {
            return oIn.readObject().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "[unset]";
        }
    }
}
