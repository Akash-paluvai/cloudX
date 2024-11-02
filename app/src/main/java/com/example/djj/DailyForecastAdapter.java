package com.example.djj;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.djj.R;
import com.example.djj.DailyForecast;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastAdapter.ViewHolder> {
    private List<DailyForecast> dailyForecasts;
    private SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault());

    public DailyForecastAdapter(List<DailyForecast> dailyForecasts) {
        this.dailyForecasts = dailyForecasts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DailyForecast forecast = dailyForecasts.get(position);

        String day = position == 0 ? "Today" : dayFormat.format(forecast.getDate());
        holder.dayText.setText(day);

        holder.weatherIcon.setImageResource(getWeatherIcon(forecast.getCondition()));

        if (forecast.getPrecipitationChance() > 0) {
            holder.precipText.setVisibility(View.VISIBLE);
            holder.precipText.setText(String.format("%d%%",
                    (int)(forecast.getPrecipitationChance() * 100)));
        } else {
            holder.precipText.setVisibility(View.GONE);
        }

        holder.minTempText.setText(String.format("%.0f°", forecast.getMinTemp()));
        holder.maxTempText.setText(String.format("%.0f°", forecast.getMaxTemp()));
        holder.tempRangeView.setProgress((float) forecast.getMinTemp(), (float) forecast.getMaxTemp());
    }

    @Override
    public int getItemCount() {
        return dailyForecasts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayText;
        ImageView weatherIcon;
        TextView precipText;
        TextView minTempText;
        TextView maxTempText;
        TemperatureRangeView tempRangeView;

        ViewHolder(View view) {
            super(view);
            dayText = view.findViewById(R.id.dayText);
            weatherIcon = view.findViewById(R.id.weatherIcon);
            precipText = view.findViewById(R.id.precipText);
            minTempText = view.findViewById(R.id.minTempText);
            maxTempText = view.findViewById(R.id.maxTempText);
            tempRangeView = view.findViewById(R.id.tempRangeView);
        }
    }

    private int getWeatherIcon(String condition) {
        switch (condition.toLowerCase()) {
            case "sunny":
                return R.drawable.ic_sunny;
            case "cloudy":
                return R.drawable.ic_cloudy;
            case "partly cloudy":
                return R.drawable.ic_partly_cloudy;
            case "light rain":
                return R.drawable.ic_rain;
            case "clear":
                return R.drawable.ic_clear;
            default:
                return R.drawable.ic_default;
        }
    }
}
