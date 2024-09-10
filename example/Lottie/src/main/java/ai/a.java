package ai;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class a extends AppCompatActivity {
    private LottieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new LottieAdapter(FileUtils.getLottieAnimations(this, "lottie"), this);
        recyclerView.setAdapter(adapter);
    }


//    private List<LottieDrawable> getLottieAnimations(Context context) {
//        List<LottieDrawable> lottieDrawables = new ArrayList<>();
//        AssetManager assetManager = context.getAssets();
//        try {
//            String[] lottieFiles = assetManager.list("lottie");
//            if (lottieFiles != null) {
//                for (String lottieFile : lottieFiles) {
//                    InputStream inputStream = assetManager.open("lottie/" + lottieFile);
//                    LottieComposition composition = LottieComposition.Factory.fromInputStreamSync(inputStream, lottieFile);
//                    LottieDrawable drawable = new LottieDrawable();
//                    drawable.setComposition(composition);
//                    lottieDrawables.add(drawable);
//                    inputStream.close();
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return lottieDrawables;
//    }
}
