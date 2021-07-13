package com.example.celulareport.util;

import android.content.Context;
import android.widget.Toast;

import com.example.celulareport.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AlertDialogsUtil {

    public static MaterialAlertDialogBuilder DeleteDialog(Context context){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(
                context,
                R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

        return builder.setTitle(context.getString(R.string.delete_dialog_title))
                .setMessage(R.string.delete_dialog_desc);
    }
}
