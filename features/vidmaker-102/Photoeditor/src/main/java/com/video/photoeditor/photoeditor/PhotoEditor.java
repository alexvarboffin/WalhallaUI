package com.video.photoeditor.photoeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.video.photoeditor.view.RoundFrameLayout;
import com.video.photoeditor.view.StrokeTextView;
import com.video.maker.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PhotoEditor implements BrushViewChangeListener {
    private static final String TAG = "PhotoEditor";
    private final List<View> addedViews;
    private final View alignView;
    private final BrushDrawingView brushDrawingView;
    private final Context context;
    private final View deleteView;
    private RoundFrameLayout frBorder;
    Handler handler;
    private final ImageView imageView;
    private final boolean isTextPinchZoomable;
    private final Typeface mDefaultEmojiTypeface;
    private final Typeface mDefaultTextTypeface;
    private final LayoutInflater mLayoutInflater;

    public OnPhotoEditorListener mOnPhotoEditorListener;

    public PointF midPoint;

    public PhotoEditorView parentView;
    private final List<View> redoViews;
    private final PointF startPoint;
    private final View zoomView;



    public interface OnSaveListener {
        void onFailure(Exception exc);

        void onSuccess(String str);
    }

    private PhotoEditor(Builder builder) {
        this.handler = new Handler();
        this.midPoint = new PointF();
        this.startPoint = new PointF();
        this.context = builder.context;
        this.parentView = builder.parentView;
        this.imageView = builder.imageView;
        this.deleteView = builder.deleteView;
        this.alignView = builder.alignView;
        this.zoomView = builder.zoomView;
        this.brushDrawingView = builder.brushDrawingView;
        this.isTextPinchZoomable = builder.isTextPinchZoomable;
        this.mDefaultTextTypeface = builder.textTypeface;
        this.mDefaultEmojiTypeface = builder.emojiTypeface;
        this.mLayoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.brushDrawingView.setBrushViewChangeListener(this);
        this.addedViews = new ArrayList();
        this.redoViews = new ArrayList();
    }

    public void addImage(Bitmap bitmap) {
        View layout = getLayout(ViewType.IMAGE);
        final ImageView imageView2 =  layout.findViewById(R.id.imgPhotoEditorImage);
        FrameLayout frameLayout =  layout.findViewById(R.id.frmBorder);
        View findViewById = layout.findViewById(R.id.imgPhotoEditorClose);
        View findViewById2 = layout.findViewById(R.id.imgPhotoEditorAlign);
        View findViewById3 = layout.findViewById(R.id.imgPhotoEditorZoom);
        imageView2.setImageBitmap(bitmap);
        final View view = findViewById;
        final View view2 = findViewById2;
        final View view3 = findViewById3;
        final FrameLayout frameLayout2 = frameLayout;
        Runnable r0 = new Runnable() {
            public void run() {
                view.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                frameLayout2.setBackgroundResource(0);
            }
        };
        findViewById.setVisibility(View.VISIBLE);
        findViewById2.setVisibility(View.VISIBLE);
        findViewById3.setVisibility(View.VISIBLE);
        frameLayout.setBackgroundResource(R.drawable.rounded_border_tv);
        this.handler.postDelayed(r0, 2500);
        MultiTouchListener multiTouchListener = getMultiTouchListener();
        final View view4 = findViewById;
//        final Runnable r6 = r0;
        final View view5 = layout;
        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            public void onDoubleClick() {
            }

            public void onLongClick() {
            }

            public void onClick() {
                view4.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                frameLayout2.setBackgroundResource(R.drawable.rounded_border_tv);
                PhotoEditor.this.handler.removeCallbacks(r0);
                PhotoEditor.this.handler.postDelayed(r0, 2500);
            }

            public void onSingleTap() {
                view4.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                frameLayout2.setBackgroundResource(R.drawable.rounded_border_tv);
                PhotoEditor.this.handler.removeCallbacks(r0);
                PhotoEditor.this.handler.postDelayed(r0, 2500);
                if (PhotoEditor.this.mOnPhotoEditorListener != null) {
                    PhotoEditor.this.mOnPhotoEditorListener.onClickGetImageViewListener(imageView2, view5);
                }
            }
        });
        layout.setOnTouchListener(multiTouchListener);
        addViewToParent(layout, ViewType.IMAGE);
    }

    public void addImage2(Bitmap bitmap) {
        View layout = getLayout(ViewType.IMAGE);
        final ImageView imageView2 =  layout.findViewById(R.id.imgPhotoEditorImage);
        FrameLayout frameLayout =  layout.findViewById(R.id.frmBorder);
        View findViewById = layout.findViewById(R.id.imgPhotoEditorClose);
        View findViewById2 = layout.findViewById(R.id.imgPhotoEditorAlign);
        View findViewById3 = layout.findViewById(R.id.imgPhotoEditorZoom);
        imageView2.setImageBitmap(bitmap);
        final View view = findViewById;
        final View view2 = findViewById2;
        final View view3 = findViewById3;
        final FrameLayout frameLayout2 = frameLayout;
        Runnable r0 = new Runnable() {
            public void run() {
                view.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                frameLayout2.setBackgroundResource(0);
            }
        };
        findViewById.setVisibility(View.VISIBLE);
        findViewById2.setVisibility(View.VISIBLE);
        findViewById3.setVisibility(View.VISIBLE);
        frameLayout.setBackgroundResource(R.drawable.rounded_border_tv);
        this.handler.removeCallbacks(r0);
        this.handler.postDelayed(r0, 2500);
        MultiTouchListener multiTouchListener = getMultiTouchListener();
        final View view4 = findViewById;

        final FrameLayout frameLayout3 = frameLayout;
        final View view5 = layout;
        final Bitmap bitmap2 = bitmap;
        MultiTouchListener.OnGestureControl r02 = new MultiTouchListener.OnGestureControl() {
            public void onDoubleClick() {
            }

            public void onLongClick() {
            }

            public void onClick() {
                view4.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                frameLayout3.setBackgroundResource(R.drawable.rounded_border_tv);
                PhotoEditor.this.handler.removeCallbacks(r0);
                PhotoEditor.this.handler.postDelayed(r0, 2500);
            }

            public void onSingleTap() {
                view4.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                frameLayout3.setBackgroundResource(R.drawable.rounded_border_tv);
                PhotoEditor.this.handler.removeCallbacks(r0);
                PhotoEditor.this.handler.postDelayed(r0, 2500);
                if (PhotoEditor.this.mOnPhotoEditorListener != null) {
                    PhotoEditor.this.mOnPhotoEditorListener.onClickGetGraphicViewListener(imageView2, view5, view4);
                    PhotoEditor.this.mOnPhotoEditorListener.onClickGetBitmapOverlay(bitmap2);
                }
            }
        };
        multiTouchListener.setOnGestureControl(r02);
        //TODO not tlu'


        layout.setOnTouchListener(multiTouchListener);
        addViewToParent(layout, ViewType.IMAGE);
    }

    public void addText(String str, int i) {
        addText( null, str, i);
    }

    public void addText(Typeface typeface, String str, int i) {
        Typeface typeface2 = typeface;
        this.brushDrawingView.setBrushDrawingMode(false);
        View layout = getLayout(ViewType.TEXT);
        StrokeTextView strokeTextView =  layout.findViewById(R.id.tvPhotoEditorText);
        View findViewById = layout.findViewById(R.id.imgPhotoEditorClose);
        View findViewById2 = layout.findViewById(R.id.imgPhotoEditorAlign);
        View findViewById3 = layout.findViewById(R.id.imgPhotoEditorZoom);
        RoundFrameLayout roundFrameLayout =  layout.findViewById(R.id.frmBorder_highlight);
        FrameLayout frameLayout =  layout.findViewById(R.id.frmBorder);
        strokeTextView.setText(str);
        strokeTextView.setTextColor(i);
        final View view = findViewById;
        final View view2 = findViewById2;
        final View view3 = findViewById3;
        final FrameLayout frameLayout2 = frameLayout;
        Runnable r0 = new Runnable() {
            public void run() {
                view.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                frameLayout2.setBackgroundResource(0);
            }
        };
        Runnable r16 = r0;

        findViewById.setVisibility(View.VISIBLE);
        findViewById2.setVisibility(View.VISIBLE);
        findViewById3.setVisibility(View.VISIBLE);
        frameLayout.setBackgroundResource(R.drawable.rounded_border_tv);
        Runnable r7 = r16;
        this.handler.removeCallbacks(r7);
        this.handler.postDelayed(r7, 2500);
        if (typeface2 != null) {
            strokeTextView.setTypeface(typeface2);
        }
        final View view4 = findViewById;
        final View view5 = findViewById2;
        final View view6 = findViewById3;
        Runnable r13 = r0;
        final FrameLayout frameLayout3 = frameLayout;
        MultiTouchListener multiTouchListener = getMultiTouchListener();
        final Runnable r6 = r7;
        final StrokeTextView strokeTextView2 = strokeTextView;
        final View view7 = layout;
        final RoundFrameLayout roundFrameLayout2 = roundFrameLayout;
        MultiTouchListener.OnGestureControl r02 = new MultiTouchListener.OnGestureControl() {
            public void onLongClick() {
            }

            public void onClick() {
                view4.setVisibility(View.VISIBLE);
                view5.setVisibility(View.VISIBLE);
                view6.setVisibility(View.VISIBLE);
                frameLayout3.setBackgroundResource(R.drawable.rounded_border_tv);
                PhotoEditor.this.handler.removeCallbacks(r6);
                PhotoEditor.this.handler.postDelayed(r6, 2500);
            }

            public void onDoubleClick() {
                String charSequence = strokeTextView2.getText().toString();
                int currentTextColor = strokeTextView2.getCurrentTextColor();
                if (PhotoEditor.this.mOnPhotoEditorListener != null) {
                    PhotoEditor.this.mOnPhotoEditorListener.onEditTextChangeListener(view7, charSequence, currentTextColor);
                }
            }

            public void onSingleTap() {
                view4.setVisibility(View.VISIBLE);
                view5.setVisibility(View.VISIBLE);
                view6.setVisibility(View.VISIBLE);
                frameLayout3.setBackgroundResource(R.drawable.rounded_border_tv);
                PhotoEditor.this.handler.removeCallbacks(r6);
                PhotoEditor.this.handler.postDelayed(r6, 2500);
                if (PhotoEditor.this.mOnPhotoEditorListener != null) {
                    PhotoEditor.this.mOnPhotoEditorListener.onClickGetEditTextChangeListener(strokeTextView2, roundFrameLayout2);
                }
            }
        };
        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            public void onLongClick() {
            }

            public void onClick() {
                view4.setVisibility(View.VISIBLE);
                view5.setVisibility(View.VISIBLE);
                view6.setVisibility(View.VISIBLE);
                frameLayout3.setBackgroundResource(R.drawable.rounded_border_tv);
                PhotoEditor.this.handler.removeCallbacks(r6);
                PhotoEditor.this.handler.postDelayed(r6, 2500);
            }

            public void onDoubleClick() {
                String charSequence = strokeTextView2.getText().toString();
                int currentTextColor = strokeTextView2.getCurrentTextColor();
                if (PhotoEditor.this.mOnPhotoEditorListener != null) {
                    PhotoEditor.this.mOnPhotoEditorListener.onEditTextChangeListener(view7, charSequence, currentTextColor);
                }
            }

            public void onSingleTap() {
                view4.setVisibility(View.VISIBLE);
                view5.setVisibility(View.VISIBLE);
                view6.setVisibility(View.VISIBLE);
                frameLayout3.setBackgroundResource(R.drawable.rounded_border_tv);
                PhotoEditor.this.handler.removeCallbacks(r6);
                PhotoEditor.this.handler.postDelayed(r6, 2500);
                if (PhotoEditor.this.mOnPhotoEditorListener != null) {
                    PhotoEditor.this.mOnPhotoEditorListener.onClickGetEditTextChangeListener(strokeTextView2, roundFrameLayout2);
                }
            }
        });
        layout.setOnTouchListener(multiTouchListener);
        addViewToParent(layout, ViewType.TEXT);
        this.mOnPhotoEditorListener.onAdded(strokeTextView, roundFrameLayout);
        String charSequence = strokeTextView.getText().toString();
        int currentTextColor = strokeTextView.getCurrentTextColor();
        OnPhotoEditorListener onPhotoEditorListener = this.mOnPhotoEditorListener;
        if (onPhotoEditorListener != null) {
            onPhotoEditorListener.onEditTextChangeListener(layout, charSequence, currentTextColor);
        }
    }

    public void editText(View view, String str, int i) {
        editText(view, null, str, i);
    }

    public void editText(View view, Typeface typeface, String str, int i) {
        TextView textView =  view.findViewById(R.id.tvPhotoEditorText);
        if (textView != null && this.addedViews.contains(view) && !TextUtils.isEmpty(str)) {
            textView.setText(str);
            if (typeface != null) {
                textView.setTypeface(typeface);
            }
            textView.setTextColor(i);
            this.parentView.updateViewLayout(view, view.getLayoutParams());
            int indexOf = this.addedViews.indexOf(view);
            if (indexOf > -1) {
                this.addedViews.set(indexOf, view);
            }
        }
    }

    public void addEmoji(String str) {
        addEmoji( null, str);
    }

    public void addEmoji(Typeface typeface, String str) {
        this.brushDrawingView.setBrushDrawingMode(false);
        View layout = getLayout(ViewType.EMOJI);
        TextView textView =  layout.findViewById(R.id.tvPhotoEditorText);
        FrameLayout frameLayout =  layout.findViewById(R.id.frmBorder);
        View findViewById = layout.findViewById(R.id.imgPhotoEditorClose);
        View findViewById2 = layout.findViewById(R.id.imgPhotoEditorAlign);
        View findViewById3 = layout.findViewById(R.id.imgPhotoEditorZoom);
        if (typeface != null) {
            textView.setTypeface(typeface);
        }
        final View view = findViewById;
        final View view2 = findViewById2;
        final View view3 = findViewById3;
        final FrameLayout frameLayout2 = frameLayout;
        Runnable r4 = new Runnable() {
            public void run() {
                view.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                frameLayout2.setBackgroundResource(0);
            }
        };
        findViewById.setVisibility(View.VISIBLE);
        findViewById2.setVisibility(View.VISIBLE);
        findViewById3.setVisibility(View.VISIBLE);
        frameLayout.setBackgroundResource(R.drawable.rounded_border_tv);
        this.handler.postDelayed(r4, 2500);
        textView.setTextSize(56.0f);
        textView.setText(str);
        MultiTouchListener multiTouchListener = getMultiTouchListener();
        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            public void onDoubleClick() {
            }

            public void onLongClick() {
            }

            public void onClick() {
                view.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                frameLayout2.setBackgroundResource(R.drawable.rounded_border_tv);
                PhotoEditor.this.handler.postDelayed(r4, 2500);
            }

            public void onSingleTap() {
                view.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                frameLayout2.setBackgroundResource(R.drawable.rounded_border_tv);
                PhotoEditor.this.handler.postDelayed(r4, 2500);
            }
        });
        layout.setOnTouchListener(multiTouchListener);
        addViewToParent(layout, ViewType.EMOJI);
    }

    private void addViewToParent(View view, ViewType viewType) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(13, -1);
        this.parentView.addView(view, layoutParams);
        this.addedViews.add(view);
        OnPhotoEditorListener onPhotoEditorListener = this.mOnPhotoEditorListener;
        if (onPhotoEditorListener != null) {
            onPhotoEditorListener.onAddViewListener(viewType, this.addedViews.size());
        }
    }

    private MultiTouchListener getMultiTouchListener() {
        return new MultiTouchListener(this.context, this.parentView, this.imageView, this.isTextPinchZoomable, this.mOnPhotoEditorListener);
    }



    private View getLayout(final ViewType viewType) {
         View view = null;
        if (viewType == ViewType.TEXT) {
            view = this.mLayoutInflater.inflate(R.layout.view_photo_editor_text,  null);
            TextView textView =  view.findViewById(R.id.tvPhotoEditorText);
            if (!(textView == null || this.mDefaultTextTypeface == null)) {
                textView.setGravity(17);
                if (this.mDefaultEmojiTypeface != null) {
                    textView.setTypeface(this.mDefaultTextTypeface);
                }
            }
        } else if (viewType == ViewType.IMAGE) {
            view = this.mLayoutInflater.inflate(R.layout.view_photo_editor_image,  null);
        } else if (viewType == ViewType.EMOJI) {
            View inflate = this.mLayoutInflater.inflate(R.layout.view_photo_editor_emoji,  null);
            TextView textView2 =  inflate.findViewById(R.id.tvPhotoEditorText);
            if (textView2 != null) {
                Typeface typeface = this.mDefaultEmojiTypeface;
                if (typeface != null) {
                    textView2.setTypeface(typeface);
                }
                textView2.setGravity(17);
                textView2.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            view = inflate;
        }
        if (view != null) {
            view.setTag(viewType);
            final FrameLayout frameLayout =  view.findViewById(R.id.frmBorder);
            final View findViewById = view.findViewById(R.id.imgPhotoEditorClose);
            if (findViewById != null) {
                View finalView = view;
                findViewById.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        PhotoEditor.this.viewUndo(finalView, viewType);
                    }
                });
            }
            final View findViewById2 = view.findViewById(R.id.imgPhotoEditorAlign);
            if (findViewById2 != null) {
                View finalView1 = view;
                findViewById2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ((View) finalView1.getParent()).setRotation(0.0f);
                    }
                });
            }
            View findViewById3 = view.findViewById(R.id.imgPhotoEditorZoom);
            if (findViewById3 != null) {
                final View view2 = findViewById3;
                findViewById3.setOnTouchListener(new View.OnTouchListener() {
                    float startRotate = 0.0f;
                    float startScale = 1.0f;

                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        Log.d("XXXXXXXX", "setOnTouchListener " + motionEvent.getRawX() + " " + motionEvent.getRawY());
                        int action = motionEvent.getAction();
                        if (action == 0) {
                            this.startScale = ((View) view.getParent()).getScaleX();
                            this.startRotate = ((View) view.getParent()).getRotation();
                            PhotoEditor.this.calculateMidPoint(view, motionEvent);
                            Log.d("XXXXXXXX", "ACTION_DOWN " + this.startScale + " " + this.startRotate + " mid " + PhotoEditor.this.midPoint.x + " " + PhotoEditor.this.midPoint.y);
                            return true;
                        } else if (action != 1 && action != 2) {
                            return false;
                        } else {
                            PhotoEditor.this.zoomAndRotateSticker((View) view.getParent(), motionEvent, frameLayout, findViewById, findViewById2, view2, this.startScale, this.startRotate);
                            return false;
                        }
                    }
                });
            }
        }
        return view;
    }


    public PointF calculateMidPoint(View view, MotionEvent motionEvent) {
        View view2 = (View) view.getParent();
        this.startPoint.set(motionEvent.getRawX(), motionEvent.getRawY());
        this.midPoint.set(view2.getX() + ((float) (view2.getWidth() / 2)), view2.getY() + ((float) (view2.getHeight() / 2)));
        return this.midPoint;
    }

    public void zoomAndRotateSticker(View view, MotionEvent motionEvent, FrameLayout frameLayout, View view2, View view3, View view4, float f, float f2) {
        if (view != null) {
            float calculateDistance = (calculateDistance(this.midPoint.x, this.midPoint.y, motionEvent.getRawX(), motionEvent.getRawY()) / calculateDistance(this.startPoint.x, this.startPoint.y, this.midPoint.x, this.midPoint.y)) * f;
            view.setPivotX((float) (view.getWidth() / 2));
            view.setPivotY((float) (view.getHeight() / 2));
            view.setScaleX(calculateDistance);
            view.setScaleY(calculateDistance);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
            int dimension = (int) (this.context.getResources().getDimension(R.dimen.frame_margin) / calculateDistance);
            layoutParams.setMargins(dimension, dimension, dimension, dimension);
            frameLayout.setLayoutParams(layoutParams);
            view2.setPivotX(0.0f);
            view2.setPivotY(0.0f);
            view3.setPivotX(0.0f);
            view3.setPivotY((float) view3.getHeight());
            view4.setPivotX((float) view4.getWidth());
            view4.setPivotY((float) view4.getHeight());
            float f3 = 1.0f / calculateDistance;
            view2.setScaleX(f3);
            view2.setScaleY(f3);
            view3.setScaleX(f3);
            view3.setScaleY(f3);
            view4.setScaleX(f3);
            view4.setScaleY(f3);
            view3.requestLayout();
            view3.invalidate();
            float degrees = f2 + ((float) Math.toDegrees(Math.atan2(motionEvent.getRawY() - this.midPoint.y, motionEvent.getRawX() - this.midPoint.x) - Math.atan2(this.startPoint.y - this.midPoint.y, this.startPoint.x - this.midPoint.x)));
            view.setRotation(degrees);
            view.requestLayout();
            view3.requestLayout();
            Log.d("XXXXXXXX", "ACTION_MOVE  " + calculateDistance + " " + degrees + " " + motionEvent.getRawX() + " " + motionEvent.getRawY());
        }
    }


    public float calculateDistance(float f, float f2, float f3, float f4) {
        double d =  (f - f3);
        double d2 =  (f2 - f4);
        return (float) Math.sqrt((d * d) + (d2 * d2));
    }

    public void setBrushDrawingMode(boolean z) {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.setBrushDrawingMode(z);
        }
    }

    public Boolean getBrushDrawableMode() {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        return Boolean.valueOf(brushDrawingView2 != null && brushDrawingView2.getBrushDrawingMode());
    }

    public void setBrushSize(float f) {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.setBrushSize(f);
        }
    }

    public void setOpacity(int i) {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.setOpacity((int) ((((double) i) / 100.0d) * 255.0d));
        }
    }

    public void setBrushColor(int i) {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.setBrushColor(i);
        }
    }

    public void setBrushEraserSize(float f) {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.setBrushEraserSize(f);
        }
    }


    public void setBrushEraserColor(int i) {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.setBrushEraserColor(i);
        }
    }

    public float getEraserSize() {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            return brushDrawingView2.getEraserSize();
        }
        return 0.0f;
    }

    public float getBrushSize() {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            return brushDrawingView2.getBrushSize();
        }
        return 0.0f;
    }

    public int getBrushColor() {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            return brushDrawingView2.getBrushColor();
        }
        return 0;
    }

    public void brushEraser() {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.brushEraser();
        }
    }


    public void viewUndo(View view, ViewType viewType) {
        if (this.addedViews.size() > 0 && this.addedViews.contains(view)) {
            this.parentView.removeView(view);
            this.addedViews.remove(view);
            this.redoViews.add(view);
            OnPhotoEditorListener onPhotoEditorListener = this.mOnPhotoEditorListener;
            if (onPhotoEditorListener != null) {
                onPhotoEditorListener.onRemoveViewListener(this.addedViews.size());
                this.mOnPhotoEditorListener.onRemoveViewListener(viewType, this.addedViews.size());
            }
        }
    }

    public boolean undo() {
        if (this.addedViews.size() > 0) {
            List<View> list = this.addedViews;
            View view = list.get(list.size() - 1);
            if (view instanceof BrushDrawingView) {
                BrushDrawingView brushDrawingView2 = this.brushDrawingView;
                return brushDrawingView2 != null && brushDrawingView2.undo();
            }
            List<View> list2 = this.addedViews;
            list2.remove(list2.size() - 1);
            this.parentView.removeView(view);
            this.redoViews.add(view);
            OnPhotoEditorListener onPhotoEditorListener = this.mOnPhotoEditorListener;
            if (onPhotoEditorListener != null) {
                onPhotoEditorListener.onRemoveViewListener(this.addedViews.size());
                Object tag = view.getTag();
                if (tag != null && (tag instanceof ViewType)) {
                    this.mOnPhotoEditorListener.onRemoveViewListener((ViewType) tag, this.addedViews.size());
                }
            }
        }
        return this.addedViews.size() != 0;
    }

    public boolean redo() {
        if (this.redoViews.size() > 0) {
            List<View> list = this.redoViews;
            View view = list.get(list.size() - 1);
            if (view instanceof BrushDrawingView) {
                BrushDrawingView brushDrawingView2 = this.brushDrawingView;
                return brushDrawingView2 != null && brushDrawingView2.redo();
            }
            List<View> list2 = this.redoViews;
            list2.remove(list2.size() - 1);
            this.parentView.addView(view);
            this.addedViews.add(view);
            Object tag = view.getTag();
            OnPhotoEditorListener onPhotoEditorListener = this.mOnPhotoEditorListener;
            if (!(onPhotoEditorListener == null || tag == null || !(tag instanceof ViewType))) {
                onPhotoEditorListener.onAddViewListener((ViewType) tag, this.addedViews.size());
            }
        }
        return this.redoViews.size() != 0;
    }

    private void clearBrushAllViews() {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.clearAll();
        }
    }

    public void clearAllViews() {
        for (int i = 0; i < this.addedViews.size(); i++) {
            this.parentView.removeView(this.addedViews.get(i));
        }
        if (this.addedViews.contains(this.brushDrawingView)) {
            this.parentView.addView(this.brushDrawingView);
        }
        this.addedViews.clear();
        this.redoViews.clear();
        clearBrushAllViews();
    }

    public void clearHelperBox() {
        for (int i = 0; i < this.parentView.getChildCount(); i++) {
            View childAt = this.parentView.getChildAt(i);
            FrameLayout frameLayout = childAt.findViewById(R.id.frmBorder);
            if (frameLayout != null) {
                frameLayout.setBackgroundResource(0);
                View findViewById = childAt.findViewById(R.id.imgPhotoEditorClose);
                if (findViewById != null) {
                    findViewById.setVisibility(View.GONE);
                }
                View findViewById2 = childAt.findViewById(R.id.imgPhotoEditorAlign);
                if (findViewById2 != null) {
                    findViewById2.setVisibility(View.GONE);
                }
                View findViewById3 = childAt.findViewById(R.id.imgPhotoEditorZoom);
                if (findViewById3 != null) {
                    findViewById3.setVisibility(View.GONE);
                }
            }
        }
    }

    public void setFilterEffect(CustomEffect customEffect) {
        this.parentView.setFilterEffect(customEffect);
    }

    public void setFilterEffect(PhotoFilter photoFilter) {
        this.parentView.setFilterEffect(photoFilter);
    }

    @Deprecated
    public void saveImage(String str, OnSaveListener onSaveListener) {
        saveAsFile(str, onSaveListener);
    }

    public void saveAsFile(String str, OnSaveListener onSaveListener) {
        saveAsFile(str, new SaveSettings.Builder().build(), onSaveListener);
    }

    public void saveAsFile(final String str, final SaveSettings saveSettings, final OnSaveListener onSaveListener) {
        Log.d(TAG, "Image Path: " + str);
        this.parentView.saveFilter(new OnSaveBitmap() {
            public void onBitmapReady(Bitmap bitmap) {
                new AsyncTask<String, String, Exception>() {

                    public void onPreExecute() {
                        super.onPreExecute();
                        PhotoEditor.this.clearHelperBox();
                        PhotoEditor.this.parentView.setDrawingCacheEnabled(false);
                    }


                    public Exception doInBackground(String... strArr) {
                        Bitmap bitmap;
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(new File(str), false);
                            if (PhotoEditor.this.parentView != null) {
                                PhotoEditor.this.parentView.setDrawingCacheEnabled(true);
                                if (saveSettings.isTransparencyEnabled()) {
                                    bitmap = BitmapUtil.removeTransparency(PhotoEditor.this.parentView.getDrawingCache());
                                } else {
                                    bitmap = PhotoEditor.this.parentView.getDrawingCache();
                                }
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                            }
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            Log.d(PhotoEditor.TAG, "Filed Saved Successfully");
                            return null;
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(PhotoEditor.TAG, "Failed to save File");
                            return e;
                        }
                    }


                    public void onPostExecute(Exception exc) {
                        super.onPostExecute(exc);
                        if (exc == null) {
                            if (saveSettings.isClearViewsEnabled()) {
                                PhotoEditor.this.clearAllViews();
                            }
                            onSaveListener.onSuccess(str);
                            return;
                        }
                        onSaveListener.onFailure(exc);
                    }
                }.execute();
            }

            public void onFailure(Exception exc) {
                onSaveListener.onFailure(exc);
            }
        });
    }

    public void saveAsBitmap(OnSaveBitmap onSaveBitmap) {
        saveAsBitmap(new SaveSettings.Builder().build(), onSaveBitmap);
    }

    public void saveAsBitmap(final SaveSettings saveSettings, final OnSaveBitmap onSaveBitmap) {
        this.parentView.saveFilter(new OnSaveBitmap() {
            public void onBitmapReady(Bitmap bitmap) {
                new AsyncTask<String, String, Bitmap>() {

                    public void onPreExecute() {
                        super.onPreExecute();
                        PhotoEditor.this.clearHelperBox();
                        PhotoEditor.this.parentView.setDrawingCacheEnabled(false);
                    }


                    public Bitmap doInBackground(String... strArr) {
                        if (PhotoEditor.this.parentView == null) {
                            return null;
                        }
                        PhotoEditor.this.parentView.setDrawingCacheEnabled(true);
                        if (saveSettings.isTransparencyEnabled()) {
                            return BitmapUtil.removeTransparency(PhotoEditor.this.parentView.getDrawingCache());
                        }
                        return PhotoEditor.this.parentView.getDrawingCache();
                    }


                    public void onPostExecute(Bitmap bitmap) {
                        super.onPostExecute(bitmap);
                        if (bitmap != null) {
                            if (saveSettings.isClearViewsEnabled()) {
                                PhotoEditor.this.clearAllViews();
                            }
                            onSaveBitmap.onBitmapReady(bitmap);
                            return;
                        }
                        onSaveBitmap.onFailure(new Exception("Failed to load the bitmap"));
                    }
                }.execute();
            }

            public void onFailure(Exception exc) {
                onSaveBitmap.onFailure(exc);
            }
        });
    }



    public void setOnPhotoEditorListener(OnPhotoEditorListener onPhotoEditorListener) {
        this.mOnPhotoEditorListener = onPhotoEditorListener;
    }

    public boolean isCacheEmpty() {
        return this.addedViews.size() == 0 && this.redoViews.size() == 0;
    }

    public void onViewAdd(BrushDrawingView brushDrawingView2) {
        if (this.redoViews.size() > 0) {
            List<View> list = this.redoViews;
            list.remove(list.size() - 1);
        }
        this.addedViews.add(brushDrawingView2);
        OnPhotoEditorListener onPhotoEditorListener = this.mOnPhotoEditorListener;
        if (onPhotoEditorListener != null) {
            onPhotoEditorListener.onAddViewListener(ViewType.BRUSH_DRAWING, this.addedViews.size());
        }
    }

    public void onViewRemoved(BrushDrawingView brushDrawingView2) {
        if (this.addedViews.size() > 0) {
            List<View> list = this.addedViews;
            View remove = list.remove(list.size() - 1);
            if (!(remove instanceof BrushDrawingView)) {
                this.parentView.removeView(remove);
            }
            this.redoViews.add(remove);
        }
        OnPhotoEditorListener onPhotoEditorListener = this.mOnPhotoEditorListener;
        if (onPhotoEditorListener != null) {
            onPhotoEditorListener.onRemoveViewListener(this.addedViews.size());
            this.mOnPhotoEditorListener.onRemoveViewListener(ViewType.BRUSH_DRAWING, this.addedViews.size());
        }
    }

    public void onStartDrawing() {
        OnPhotoEditorListener onPhotoEditorListener = this.mOnPhotoEditorListener;
        if (onPhotoEditorListener != null) {
            onPhotoEditorListener.onStartViewChangeListener(ViewType.BRUSH_DRAWING);
        }
    }

    public void onStopDrawing() {
        OnPhotoEditorListener onPhotoEditorListener = this.mOnPhotoEditorListener;
        if (onPhotoEditorListener != null) {
            onPhotoEditorListener.onStopViewChangeListener(ViewType.BRUSH_DRAWING);
        }
    }

    public static class Builder {

        public View alignView;

        public BrushDrawingView brushDrawingView;

        public Context context;

        public View deleteView;

        public Typeface emojiTypeface;

        public ImageView imageView;

        public boolean isTextPinchZoomable = true;

        public PhotoEditorView parentView;

        public Typeface textTypeface;

        public View zoomView;

        public Builder(Context context2, PhotoEditorView photoEditorView) {
            this.context = context2;
            this.parentView = photoEditorView;
            this.imageView = photoEditorView.getSource();
            this.brushDrawingView = photoEditorView.getBrushDrawingView();
        }


        public Builder setDeleteView(View view) {
            this.deleteView = view;
            return this;
        }

        public Builder setDefaultTextTypeface(Typeface typeface) {
            this.textTypeface = typeface;
            return this;
        }

        public Builder setDefaultEmojiTypeface(Typeface typeface) {
            this.emojiTypeface = typeface;
            return this;
        }

        public Builder setPinchTextScalable(boolean z) {
            this.isTextPinchZoomable = z;
            return this;
        }

        public PhotoEditor build() {
            return new PhotoEditor(this);
        }
    }
}
