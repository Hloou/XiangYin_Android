package net.xy360.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

/**
 * Created by jolin on 2016/3/12.
 */
public class RealPathFromURI {
    public static String getRealPathFromURI(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(uri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getFileType(String realPath) {
        int i = realPath.lastIndexOf(".");
        if (i >= 0)
            return realPath.substring(i + 1).toLowerCase();
        else
            return "";
    }

    public static String getFileName(String realPath) {
        int i = realPath.lastIndexOf("/");
        if (i >= 0)
            return realPath.substring(i + 1);
        else
            return realPath;
    }
}
