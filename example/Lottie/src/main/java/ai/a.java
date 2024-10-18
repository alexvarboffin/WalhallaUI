package ai;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import ai.databinding.ActivityMainBinding;
import ai.fragment.LottieFragment;
import ai.fragment.SvgFragment;
import ai.fragment.XmlFragment;

public class a extends AppCompatActivity {


    private ActivityMainBinding binding;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LottieFragment()); // Add your first fragment
        fragments.add(new SvgFragment()); // Add your second fragment (replace with actual fragment)
        fragments.add(new XmlFragment());

        viewPagerAdapter = new ViewPagerAdapter(this, fragments);
        binding.viewPager.setAdapter(viewPagerAdapter);

        // TabLayout setup
        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> tab.setText("Tab " + (position + 1))
        ).attach();
    }

    public static class ViewPagerAdapter extends FragmentStateAdapter {

        private final List<Fragment> fragmentList;

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragmentList) {
            super(fragmentActivity);
            this.fragmentList = fragmentList;  // Initialize the list of fragments
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);  // Return the fragment based on position
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();  // Return the number of fragments
        }
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
