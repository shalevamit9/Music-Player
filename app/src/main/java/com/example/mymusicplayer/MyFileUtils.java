package com.example.mymusicplayer;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MyFileUtils {

    /**
     *
     * @param context context to open file output stream
     * @param fileKey the key of the file to save into
     * @param o the object to save into a file
     */
    public static void saveObjectToFile(Context context, String fileKey, Object o) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileKey, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(o);

            oos.close();
        } catch (IOException e) {
            e.printStackTrace();

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    /**
     *
     * @param context context to open file input stream
     * @param fileKey the key of the file to load from
     * @return Returns the object loaded from the file
     */
    public static Object loadObjectFromFile(Context context, String fileKey) {
        FileInputStream fis = null;
        Object o = null;
        try {
            fis = context.openFileInput(fileKey);
            ObjectInputStream ois = new ObjectInputStream(fis);

            o = ois.readObject();

            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }

        return o;
    }

    /**
     * @param dir      you can get from many places like Environment.getExternalStorageDirectory() or mContext.getFilesDir() depending on where you want to save the image.
     * @param fileName The file name.
     * @param bm       The Bitmap you want to save.
     */
    public static void saveBitmapToFile(File dir, String fileName, Bitmap bm) {
        File imageFile = new File(dir, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            bm.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.close();
        } catch (IOException e) {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
