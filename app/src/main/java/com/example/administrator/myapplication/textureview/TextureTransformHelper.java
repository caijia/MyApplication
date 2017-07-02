package com.example.administrator.myapplication.textureview;

import android.graphics.Matrix;
import android.view.TextureView;

/**
 * Created by cai.jia on 2017/7/2.
 */

public class TextureTransformHelper {

    public static void wrapContent(TextureView textureView, int videoWidth, int videoHeight) {
        double aspectRatio = (double) videoHeight / videoWidth;
        int viewWidth = textureView.getWidth();
        int viewHeight = textureView.getHeight();

        int newWidth, newHeight;
        if (viewHeight > (int) (viewWidth * aspectRatio)) {
            newWidth = viewWidth;
            newHeight = (int) (viewWidth * aspectRatio);

        } else {
            newWidth = (int) (viewHeight / aspectRatio);
            newHeight = viewHeight;
        }
        int xOffset = (viewWidth - newWidth) / 2;
        int yOffset = (viewHeight - newHeight) / 2;

        Matrix transform = new Matrix();
        transform.setScale((float) newWidth / viewWidth, (float) newHeight / viewHeight);
        transform.postTranslate(xOffset, yOffset);
        textureView.setTransform(transform);
    }

    public static void centerCrop(TextureView textureView, int videoWidth, int videoHeight) {
        int viewWidth = textureView.getWidth();
        int viewHeight = textureView.getHeight();
        double aspectRatio = (double) videoHeight / videoWidth;

        int newWidth, newHeight;
        if (viewHeight > viewWidth * aspectRatio) {
            newHeight = viewHeight;
            newWidth = (int) (newHeight / aspectRatio);

        } else {
            newWidth = viewWidth;
            newHeight = (int) (newWidth * aspectRatio);
        }

        int xOffset = (viewWidth - newWidth) / 2;
        int yOffset = (viewHeight - newHeight) / 2;
        Matrix transform = new Matrix();
        transform.setScale((float) newWidth / viewWidth, (float) newHeight / viewHeight);
        transform.postTranslate(xOffset, yOffset);
        textureView.setTransform(transform);
    }
}
