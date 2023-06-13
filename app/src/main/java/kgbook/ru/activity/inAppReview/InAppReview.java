package kgbook.ru.activity.inAppReview;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import kgbook.ru.R;
import kgbook.ru.activity.BaseActivity;
import com.walhalla.ui.observer.RateAppModule;

public class InAppReview extends BaseActivity {

    private RateAppModule module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_main2);
        module = new RateAppModule(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Toast.makeText(this, "@@", Toast.LENGTH_SHORT).show();
            module.launchNow();
        });
        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, MainFragment8888.newInstance())
//                    .commitNow();
        }

    }
}
