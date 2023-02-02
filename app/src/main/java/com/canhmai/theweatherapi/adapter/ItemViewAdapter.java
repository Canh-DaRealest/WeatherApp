package com.canhmai.theweatherapi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.canhmai.theweatherapi.R;

import com.canhmai.theweatherapi.api.response.forecast.ForecastListElemnt;
import com.google.android.material.card.MaterialCardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ItemViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int type = 1;
    public static final int CURRENT = 1;
    public static final int FORECAST = 2;
    private List<ForecastListElemnt> forecastDayElementList;
    private Context context;
    MutableLiveData<ForecastListElemnt> forecastListElemntMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<ForecastListElemnt> getForecastListElemntMutableLiveData() {
        return forecastListElemntMutableLiveData;
    }

    public ItemViewAdapter(Context context, int type, List<ForecastListElemnt> forecastListElemnts) {
        this.context = context;
        this.forecastDayElementList = forecastListElemnts;
        this.type = type;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (type == CURRENT) {
            return new CurrentViewHolder(LayoutInflater.from(context).inflate(R.layout.item_weather_detail, parent, false));

        } else {
            return new ForecastViewHolder(LayoutInflater.from(context).inflate(R.layout.item_weather_detail_land, parent, false));

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (type == CURRENT) {

            CurrentViewHolder currentViewHolder = (CurrentViewHolder) holder;

            if (forecastDayElementList != null) {
                Date date = null;
                ForecastListElemnt forecastDayElement = forecastDayElementList.get(position);

                Glide.with(context).load("https://openweathermap.org/img/wn/" +
                        forecastDayElement.getWeather().get(0).getIcon() + "@2x.png").into(currentViewHolder.ivWeatherImage);
                String time = forecastDayElement.getDtTxt();

                try {
                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);

                    String newstring = new SimpleDateFormat("HH:mm\ndd-MM-yyyy").format(date);
                    currentViewHolder.tvTime.setText(newstring);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                currentViewHolder.tvTemp.setText(forecastDayElement.getMain().getTemp() + "°C");
                currentViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });


            }

        } else {
            ForecastViewHolder forecastViewHolder = (ForecastViewHolder) holder;

            if (forecastDayElementList != null) {
                Date date = null;
                ForecastListElemnt forecastDayElement = forecastDayElementList.get(position);

                Glide.with(context).load("https://openweathermap.org/img/wn/" +
                        forecastDayElement.getWeather().get(0).getIcon() + "@2x.png").into(forecastViewHolder.ivWeatherImage);
                String time = forecastDayElement.getDtTxt();

                try {
                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);

                    String newstring = new SimpleDateFormat("HH:mm\ndd-MM-yyyy").format(date);
                    forecastViewHolder.tvTime.setText(newstring);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                forecastViewHolder.tvTemp.setText(forecastDayElement.getMain().getTemp() + "°C");
                forecastViewHolder.tvHumidity.setText(forecastDayElement.getMain().getHumidity() + "%");
                forecastViewHolder.tvWind.setText(forecastDayElement.getWind().getSpeed() + "km/h");
                forecastViewHolder.tvCloud.setText(forecastDayElement.getClouds().getAll() + "%");
                forecastViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickItemView(forecastDayElement);
                    }
                });


            }
        }
    }

    private void clickItemView(ForecastListElemnt forecastDayElement) {
        forecastListElemntMutableLiveData.postValue(forecastDayElement);
        notifyItemRangeChanged(0, forecastDayElementList.size());
    }


    @Override
    public int getItemCount() {
        if (forecastDayElementList != null) {
            return forecastDayElementList.size();
        }
        return 0;

    }


    public class ForecastViewHolder extends RecyclerView.ViewHolder {
        LinearLayout cardView;
        TextView tvTime;
        ImageView ivWeatherImage;

        TextView tvHumidity;
        TextView tvWind;
        TextView tvCloud;
        TextView tvTemp;


        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cv_item_view);
            tvTemp = itemView.findViewById(R.id.tv_item_temp);
            tvTime = itemView.findViewById(R.id.tv_item_time);
            tvHumidity = itemView.findViewById(R.id.tv_item_humidity);
            tvWind = itemView.findViewById(R.id.tv_item_wind);
            tvCloud = itemView.findViewById(R.id.tv_item_cloud);
            ivWeatherImage = itemView.findViewById(R.id.iv_item_image);
        }
    }

    public class CurrentViewHolder extends RecyclerView.ViewHolder {
        LinearLayout cardView;
        TextView tvTime;
        ImageView ivWeatherImage;
        TextView tvTemp;


        public CurrentViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cv_item_view);
            tvTemp = itemView.findViewById(R.id.tv_item_temp);
            tvTime = itemView.findViewById(R.id.tv_item_time);
            ivWeatherImage = itemView.findViewById(R.id.iv_item_image);
        }
    }
}
