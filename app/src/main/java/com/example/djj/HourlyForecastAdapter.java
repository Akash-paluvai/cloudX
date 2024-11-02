package com.example.djj;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder> {
    private List<HourlyForecast> hourlyForecasts;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("ha", Locale.getDefault());

    public HourlyForecastAdapter(List<HourlyForecast> hourlyForecasts) {
        this.hourlyForecasts = hourlyForecasts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hourly_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HourlyForecast forecast = hourlyForecasts.get(position);

        String time = position == 0 ? "Now" : timeFormat.format(forecast.getTime() * 1000); // Ensure time is in milliseconds
        holder.timeText.setText(time);

        holder.tempText.setText(String.format("%.0f°", forecast.getTemperature()));

        holder.weatherIcon.setImageResource(getWeatherIcon(forecast.getCondition()));

        if (forecast.getPrecipitationChance() > 0) {
            holder.precipText.setVisibility(View.VISIBLE);
            holder.precipText.setText(String.format("%d%%", (int) (forecast.getPrecipitationChance() * 100)));
        } else {
            holder.precipText.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return hourlyForecasts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeText;
        ImageView weatherIcon;
        TextView tempText;
        TextView precipText;

        ViewHolder(View view) {
            super(view);
            timeText = view.findViewById(R.id.timeText);
            weatherIcon = view.findViewById(R.id.weatherIcon);
            tempText = view.findViewById(R.id.tempText);
            precipText = view.findViewById(R.id.precipText);
        }
    }

    private int getWeatherIcon(String condition) {
        switch (condition.toLowerCase()) {
            case "clear":
                return R.drawable.ic_clear;
            case "rain":
                return R.drawable.ic_rain;
            case "snow":
                return R.drawable.ic_snow;
            case "partly cloudy":
                return R.drawable.ic_partly_cloudy;
            default:
                return R.drawable.ic_cloudy;
        }
    }
}
