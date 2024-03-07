package ai;

import android.graphics.drawable.PictureDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

public class SvgViewHolder extends RecyclerView.ViewHolder {
    private final ImageView lottieView;
    private final TextView fileNameTextView;

    public SvgViewHolder(@NonNull View itemView) {
        super(itemView);
        lottieView = itemView.findViewById(R.id.imageView);
        fileNameTextView = itemView.findViewById(R.id.fileNameTextView);
    }

    public void bind(PictureDrawable pictureDrawable, String fileName) {
        //lottieView.setImageDrawable(lottieDrawable);
        if (pictureDrawable != null) {
            // Set the drawable to the ImageView
            lottieView.setImageDrawable(pictureDrawable);
        }
    }
}