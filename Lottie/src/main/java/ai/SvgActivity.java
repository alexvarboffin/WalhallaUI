package ai;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SvgActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SvgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie);

        recyclerView = findViewById(R.id.recyclerView);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new SvgAdapter(FileUtils.getLottieAnimations(this, "svg"), this);
        recyclerView.setAdapter(adapter);
    }
}
