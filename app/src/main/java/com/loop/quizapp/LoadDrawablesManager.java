package com.loop.quizapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.WindowManager;

import java.io.IOException;

public class LoadDrawablesManager {

    Context myContext;
    Point screenSize;

    public LoadDrawablesManager(Context context) {
        myContext = context;
        screenSize = getScreenSize();
    }

    public Drawable getDrawable(String fileName, float maxDrawableWidthFactor,
                                float maxDrawableHeightFactor) {
        Drawable drawable = null;
        int maxImageWidth = (int)(screenSize.x * maxDrawableWidthFactor);
        int maxImageHeight = (int)(screenSize.y * maxDrawableHeightFactor);
        try {
            drawable = resize(
                    Drawable.createFromStream(myContext.getAssets().open(fileName), null),
                    maxImageWidth, maxImageHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drawable;
    }

    private Drawable resize(Drawable image, int maxImageWidth, int maxImageHeight) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        int imageWidth = b.getWidth();
        int imageHeight = b.getHeight();
        if (imageWidth < maxImageWidth && imageHeight < maxImageHeight) {
            return image;
        }
        float aspectRatio = (float)imageWidth/(float)imageHeight;
        int resizedImageWidth = imageWidth;
        int resizedImageHeight = imageHeight;
        //
        if (imageWidth > maxImageWidth) {
            resizedImageWidth = maxImageWidth;
            resizedImageHeight = (int)(resizedImageWidth/aspectRatio);
        } else if (imageHeight > maxImageHeight) {
            resizedImageHeight = maxImageHeight;
            resizedImageWidth = (int)(resizedImageHeight * aspectRatio);
        }
        Bitmap bitmapResized = Bitmap.createScaledBitmap(
                b, resizedImageWidth, resizedImageHeight, true);
        return new BitmapDrawable(myContext.getResources(), bitmapResized);
    }

    private Point getScreenSize() {
        WindowManager windowManager = (WindowManager) myContext.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return size;
        } else {
            return null;
        }
    }

}
