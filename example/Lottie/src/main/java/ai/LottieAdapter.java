package ai;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LottieAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final List<FileItem> animationList;
    private final List<LottieComposition> lottieDrawables;

    private Context context;

    public LottieAdapter(List<FileItem> animationList, Context context) {
        this.animationList = animationList;
        this.context = context;
        lottieDrawables = new ArrayList<>();
        for (FileItem lottieAnimation : animationList) {
            try {
                InputStream stream = context.getAssets().open(lottieAnimation.getFileName());
//            LottieResult<LottieComposition> composition = LottieCompositionFactory.fromJsonInputStreamSync(stream, fileName);
//            holder.lottieView.setComposition(composition);

                LottieComposition composition = LottieComposition.Factory.fromInputStreamSync(stream, true);
//                LottieDrawable drawable = new LottieDrawable();
//                drawable.setComposition(composition);
                //holder.lottieView.setComposition(composition);
                lottieDrawables.add(composition);
            } catch (IOException e) {
                Log.d("@@@", "getLottieAnimations: "+e.getLocalizedMessage());
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lottie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FileItem animation = animationList.get(position);
        String fileName = animation.getFileName();
        holder.bind(lottieDrawables.get(position), fileName);
    }

    @Override
    public int getItemCount() {
        return animationList.size();
    }

}
