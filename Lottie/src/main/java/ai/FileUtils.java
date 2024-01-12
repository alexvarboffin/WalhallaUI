package ai;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static void loadSvgFromAssets(Context context, String filename, ImageView imageView) {
        try {
            InputStream inputStream = context.getAssets().open(filename);
            SVG svg = SVG.getFromInputStream(inputStream);

            // Convert SVG to a drawable
            PictureDrawable pictureDrawable = new PictureDrawable(svg.renderToPicture());

            // Set the drawable to the ImageView
            imageView.setImageDrawable(pictureDrawable);
        } catch (IOException | SVGParseException e) {
            e.printStackTrace();
        }
    }

    public static void loadSvgFromUri(Context context, Uri uri, ImageView imageView) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            SVG svg = SVG.getFromInputStream(inputStream);

            // Convert SVG to a drawable
            PictureDrawable pictureDrawable = new PictureDrawable(svg.renderToPicture());

            // Set the drawable to the ImageView
            imageView.setImageDrawable(pictureDrawable);
        } catch (IOException | SVGParseException e) {
            e.printStackTrace();
        }
    }

    public static List<FileItem> getLottieAnimations(Context context, String folderName) {
        List<FileItem> lottieDrawables = new ArrayList<>();
        AssetManager assetManager = context.getAssets();

        String fileName = "";

        try {
            String[] lottieFiles = assetManager.list(folderName);
            if (lottieFiles != null) {
                for (String lottieFile : lottieFiles) {
                    try {
                        fileName = folderName + "/" + lottieFile;
                        InputStream inputStream = assetManager.open(fileName);
                        lottieDrawables.add(new FileItem(fileName));
                    } catch (IOException e) {
                        String[] lottieFiles0 = assetManager.list(fileName);
                        if (lottieFiles0 != null) {
                            for (String lottieFile0 : lottieFiles0) {
                                try {
                                    String s = fileName + "/" + lottieFile0;
                                    InputStream inputStream = assetManager.open(s);
                                    lottieDrawables.add(new FileItem(s));

                                } catch (IOException e0) {
                                    Log.d("@@", "getLottieAnimations: " + fileName);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            Log.d("@@", "getLottieAnimations: " + fileName);
        }
        return lottieDrawables;
    }
}
