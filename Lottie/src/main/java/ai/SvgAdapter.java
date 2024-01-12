package ai;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SvgAdapter extends RecyclerView.Adapter<SvgViewHolder> {

    private final List<FileItem> animationList;
    private final List<PictureDrawable> lottieDrawables;

    private Context context;

    public SvgAdapter(List<FileItem> animationList, Context context) {
        this.animationList = animationList;
        this.context = context;
        lottieDrawables = new ArrayList<PictureDrawable>();
        for (FileItem filename : animationList) {
            try {
                InputStream inputStream = context.getAssets().open(filename.getFileName());
                SVG svg = SVG.getFromInputStream(inputStream);

                // Convert SVG to a drawable
                PictureDrawable pictureDrawable = new PictureDrawable(svg.renderToPicture());
                lottieDrawables.add(pictureDrawable);
            } catch (IOException | SVGParseException e) {
                Log.d("@@@", "getLottieAnimations: "+e.getLocalizedMessage());
            }
        }
    }

    @NonNull
    @Override
    public SvgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_svg, parent, false);
        return new SvgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SvgViewHolder holder, int position) {
        FileItem animation = animationList.get(position);
        String fileName = animation.getFileName();
        holder.bind(lottieDrawables.get(position), fileName);
    }

    @Override
    public int getItemCount() {
        return animationList.size();
    }

}
