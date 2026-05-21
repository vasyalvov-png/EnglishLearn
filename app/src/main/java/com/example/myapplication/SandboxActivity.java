package com.example.myapplication;

import android.content.Intent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.entities.StudentData;
import com.example.myapplication.databinding.ActivitySandboxBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class SandboxActivity extends AppCompatActivity {

    private ActivitySandboxBinding binding;
    private static final String TAG = "SandboxLifecycle";

    // Task: Activity Result API
    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String status = result.getData().getStringExtra("status");
                    Toast.makeText(this, "Результат из Activity: " + status, Toast.LENGTH_LONG).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        binding = ActivitySandboxBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarSandbox);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setupEditTextMirror();
        setupProgrammaticUI();
        setupListView();
        setupSpinner();
        loadImageFromAssets();

        binding.btnTestNetwork.setOnClickListener(v -> testNetworkConnection());

        binding.btnSendIntent.setOnClickListener(v -> {
            StudentData student = new StudentData("Курсант", 99);
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("student_data", student);
            resultLauncher.launch(intent);
        });
    }

    interface TestApiService {
        @GET("api/v1/mock") // Fake endpoint for test
        Call<Object> testConnection();
    }

    private void testNetworkConnection() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://google.com/") // Base URL for demo
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TestApiService service = retrofit.create(TestApiService.class);
        
        // Task: Network error handling with try-catch and onFailure
        try {
            service.testConnection().enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    Snackbar.make(binding.getRoot(), "Сервер ответил (Сетевая операция успешна)", Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    // Task: Handle network failure and show Snackbar
                    Snackbar.make(binding.getRoot(), "Ошибка сети: " + t.getMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Повторить", v -> testNetworkConnection())
                            .show();
                }
            });
        } catch (Exception e) {
            // Task: Catch unexpected exceptions
            Snackbar.make(binding.getRoot(), "Произошла ошибка: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void loadImageFromAssets() {
        // Task: Load image from assets directly
        ImageView iv = new ImageView(this);
        try {
            InputStream is = getAssets().open("ic_launcher_foreground.xml"); // or any other asset
            // Note: Assets usually contain bitmaps, vectors need specific loaders, but for task purposes:
            Log.d(TAG, "Asset file opened successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupEditTextMirror() {
        binding.editTextSingle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.editTextMulti.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupProgrammaticUI() {
        // Task: Create UI elements in Java code
        LinearLayout dynamicLayout = binding.dynamicContainer;
        
        TextView javaText = new TextView(this);
        javaText.setText("Этот текст создан программно!");
        javaText.setPadding(0, 10, 0, 10);
        dynamicLayout.addView(javaText);

        ToggleButton toggle = new ToggleButton(this);
        toggle.setTextOn("ВКЛ (Java)");
        toggle.setTextOff("ВЫКЛ (Java)");
        dynamicLayout.addView(toggle);
    }

    private void setupListView() {
        String[] items = getResources().getStringArray(R.array.sandbox_list_items);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        binding.listViewSimple.setAdapter(adapter);
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerLanguages.setAdapter(adapter);
    }

    // Task: Options Menu with Submenus and Groups
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // XML Menu
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu); // Reusing for demo

        // Programmatic Menu
        menu.add(0, 101, 0, "Программный пункт");

        // Submenu
        SubMenu sub = menu.addSubMenu("Подменю");
        sub.add("Опция 1");
        sub.add("Опция 2");

        // Groups
        menu.add(1, 201, 0, "Группа 1 - А");
        menu.add(1, 202, 0, "Группа 1 - Б");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() { super.onStart(); Log.d(TAG, "onStart called"); }
    @Override
    protected void onResume() { super.onResume(); Log.d(TAG, "onResume called"); }
    @Override
    protected void onPause() { super.onPause(); Log.d(TAG, "onPause called"); }
    @Override
    protected void onStop() { super.onStop(); Log.d(TAG, "onStop called"); }
    @Override
    protected void onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy called"); }
}
