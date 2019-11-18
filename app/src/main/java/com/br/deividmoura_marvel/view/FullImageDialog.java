package com.br.deividmoura_marvel.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.br.deividmoura_marvel.R;
import com.squareup.picasso.Picasso;

public class FullImageDialog {

    public static void showImage(Context context, String strImagePath) {
        AlertDialog.Builder imageDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.full_image_dialog, null);
        ImageView image = (ImageView) layout.findViewById(R.id.fullImage);

        Picasso.get()
                .load(strImagePath)
                .into(image);

        imageDialog.setView(layout);

        final AlertDialog alert = imageDialog.create();
        alert.getWindow().getAttributes();
        alert.show();
    }
}
