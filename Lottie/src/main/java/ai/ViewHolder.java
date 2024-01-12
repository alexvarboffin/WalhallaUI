package ai;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;

public class ViewHolder extends RecyclerView.ViewHolder {
    private final LottieAnimationView lottieView;
    private final TextView fileNameTextView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        lottieView = itemView.findViewById(R.id.lottieView);
        fileNameTextView = itemView.findViewById(R.id.fileNameTextView);
    }

    public void bind(LottieComposition lottieDrawable, String fileName) {
        //lottieView.setImageDrawable(lottieDrawable);
        if(lottieDrawable!=null){
            lottieView.setComposition(lottieDrawable);
            lottieView.playAnimation();
        }
        fileNameTextView.setText(fileName);
    }
}