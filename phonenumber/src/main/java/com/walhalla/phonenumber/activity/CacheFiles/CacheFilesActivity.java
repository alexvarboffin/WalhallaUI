package com.walhalla.phonenumber.activity.CacheFiles;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.walhalla.phonenumber.R;

public class CacheFilesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_files);

        // Получаем RecyclerView из макета
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // Создаем экземпляр адаптера
        CacheFilesAdapter adapter = new CacheFilesAdapter(this);

        // Устанавливаем адаптер для RecyclerView
        recyclerView.setAdapter(adapter);

        // Устанавливаем менеджер компоновки (LayoutManager) для RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
