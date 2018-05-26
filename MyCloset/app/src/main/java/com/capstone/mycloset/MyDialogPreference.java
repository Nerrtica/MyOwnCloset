package com.capstone.mycloset;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.preference.DialogPreference;
import android.util.AttributeSet;

import java.io.File;
import java.util.ArrayList;

public class MyDialogPreference extends DialogPreference {
    public MyDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE) {
            DBController controller ;
            controller = new DBController(getContext());
            ArrayList<Closet> closetArrayList = controller.FindCloset();
            for(Closet closet : closetArrayList) {
                deleteImgFile(closet);
            }
            controller.deleteAllCloset();
            controller.deleteAllCoordi();
            ClosetActivity.refreshCloset = true;
        }
        else if(which == DialogInterface.BUTTON_NEGATIVE) {
            dialog.cancel();
        }
    }

    private boolean deleteImgFile(Closet closet) {
        String folder = Environment.getExternalStorageDirectory() + "/MyCloset/";
        String fileName = closet.getImagePath();
        int idx = fileName.indexOf("MyCloset/");
        fileName = fileName.substring(idx + 9);
        String thumFileName = "thum_" + fileName;
        File deleteFile = new File(folder, fileName);
        boolean delete = deleteFile.delete();
        deleteFile = new File(folder, thumFileName);
        delete = deleteFile.delete();

        return delete;
    }
}
