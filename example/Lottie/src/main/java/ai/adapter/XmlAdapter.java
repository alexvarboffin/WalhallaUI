package ai.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ai.FileItem;
import ai.R;
import ai.SvgViewHolder;

public class XmlAdapter extends RecyclerView.Adapter<SvgViewHolder> {

    private final List<FileItem> items;
    private final List<Drawable> drawables = new ArrayList<>();

    private Context context;


    public XmlAdapter(Context context, List<FileItem> items) {
        this.items = items;
        this.context = context;
        AssetManager am = context.getAssets();

        for (FileItem filename : items) {
            try {
                Log.d("@@@", "XmlAdapter: " + filename.getFileName());
                //Drawable drawable = Drawable.createFromStream(am.open(filename.getFileName()), null);
                Drawable drawable =  Drawable.createFromResourceStream(
                        context.getResources(),new TypedValue(),
                        context.getResources().getAssets().open(filename.getFileName()), null);
                if (drawable != null) {
                    drawables.add(drawable);
                }
            } catch (IOException e) {
                Log.d("@@@", "XmlAdapter: " + e.getLocalizedMessage());
            } catch (Exception e) {
                drawables.add(null);
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
        FileItem animation = items.get(position);
        String fileName = animation.getFileName();
        holder.bind(drawables.get(position), fileName);
    }

    @Override
    public int getItemCount() {
        return drawables.size();
    }

}
