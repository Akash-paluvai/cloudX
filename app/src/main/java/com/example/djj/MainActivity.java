package com.example.djj;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private RecyclerView hourlyRecyclerView;
    private RecyclerView dailyRecyclerView;
    private WeatherViewModel weatherViewModel;
    private EditText locationEditText;
    private Button searchButton;
    private View backgroundView;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupLocation();
        setupSearch();
        setupRecyclerViews();
        setupViewModel();
    }

    private void initializeViews() {
        hourlyRecyclerView = findViewById(R.id.hourlyForecastRecyclerView);
        dailyRecyclerView = findViewById(R.id.dailyForecastRecyclerView);
        locationEditText = findViewById(R.id.locationEditText);
        searchButton = findViewById(R.id.searchButton);
        backgroundView = findViewById(R.id.backgroundView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
    }

    private void setupLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (checkLocationPermission()) {
            getCurrentLocation();
        } else {
            requestLocationPermission();
        }
    }

    private void getCurrentLocation() {
        if (checkLocationPermission()) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        weatherViewModel.fetchWeatherForLocation(
                                location.getLatitude(),
                                location.getLongitude()
                        );
                    }
                }
            });
        }
    }

    private void setupSearch() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        locationEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
    }

    private void performSearch() {
        String query = locationEditText.getText().toString().trim();
        if (!query.isEmpty()) {
            showLoading(true);
            weatherViewModel.fetchWeatherForCity(query);
            locationEditText.clearFocus();
            hideKeyboard();
        } else {
            Toast.makeText(MainActivity.this, "Please enter a location", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRecyclerViews() {
        hourlyRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        dailyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupViewModel() {
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        weatherViewModel.getCurrentWeather().observe(this, weather -> {
            if (weather != null) {
                updateUI(weather);
            }
        });

        weatherViewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", error);
                showLoading(false);
            }
        });

        weatherViewModel.getIsLoading().observe(this, this::showLoading);
    }

    private void showLoading(boolean show) {
        loadingProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        searchButton.setEnabled(!show);
        locationEditText.setEnabled(!show);
    }

    private void updateUI(WeatherData weather) {
        TextView tempView = findViewById(R.id.temperatureText);
        tempView.setText(String.format("%.0f°", weather.getTemperature()));

        TextView locationView = findViewById(R.id.locationText);
        locationView.setText(weather.getLocation());

        TextView conditionView = findViewById(R.id.conditionText);
        conditionView.setText(weather.getCondition());

        HourlyForecastAdapter hourlyAdapter = new HourlyForecastAdapter(weather.getHourlyForecast());
        hourlyRecyclerView.setAdapter(hourlyAdapter);

        DailyForecastAdapter dailyAdapter = new DailyForecastAdapter(weather.getDailyForecast());
        dailyRecyclerView.setAdapter(dailyAdapter);

        updateBackground(weather);
        showLoading(false);
    }

    private void updateBackground(WeatherData weather) {
        int backgroundRes = getBackgroundResource(weather);
        backgroundView.setBackgroundResource(backgroundRes);
    }

    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    private int getBackgroundResource(WeatherData weather) {
        String condition = weather.getCondition().toLowerCase();
        boolean isNight = !weather.isDaytime();

        if (isNight) {
            return R.drawable.night_background;
        } else if (condition.contains("rain") || condition.contains("rainy")||condition.contains("thunderstrom")||condition.contains("lightining"))
        {
            return R.drawable.rainy_background;
        } else if (condition.contains("cloud"))
        {
            return R.drawable.cloudy_background;
        }
        else if(condition.contains(("mist")) || condition.contains("snow"))
        {
            return  R.drawable.snowy_background ;
        }
        else {
            return R.drawable.sunny_background;
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(locationEditText.getWindowToken(), 0);
        }
    }
}
