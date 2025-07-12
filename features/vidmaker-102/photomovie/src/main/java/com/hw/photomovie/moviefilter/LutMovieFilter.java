package com.hw.photomovie.moviefilter;

import android.graphics.Bitmap;

import com.hw.photomovie.util.AppResources;

import static com.hw.photomovie.util.AppResources.loadShaderFromAssets;


public class LutMovieFilter extends TwoTextureMovieFilter {

    public LutMovieFilter(Bitmap lutBitmap){
        super(loadShaderFromAssets("shader/two_vertex.glsl"),loadShaderFromAssets("shader/lut.glsl"));
        setBitmap(lutBitmap);
    }

    public LutMovieFilter(LutType type){
        super(loadShaderFromAssets("shader/two_vertex.glsl"),loadShaderFromAssets("shader/lut.glsl"));
        setBitmap(typeToBitmap(type));
    }



    public enum LutType{
           A,B,C,D,E

    }
    public static Bitmap typeToBitmap(LutType type){
        switch (type){
            case A:
                return AppResources.loadBitmapFromAssets("lut/lut_1.jpg");
            case B:
                return AppResources.loadBitmapFromAssets("lut/lut_2.jpg");
            case C:
                return AppResources.loadBitmapFromAssets("lut/lut_3.jpg");
            case D:
                return AppResources.loadBitmapFromAssets("lut/lut_4.jpg");
            case E:
                return AppResources.loadBitmapFromAssets("lut/lut_5.jpg");
        }
        return null;
    }

}
