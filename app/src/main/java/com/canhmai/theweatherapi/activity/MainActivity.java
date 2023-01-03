package com.canhmai.theweatherapi.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canhmai.theweatherapi.R;
import com.canhmai.theweatherapi.adapter.ItemViewAdapter;
import com.canhmai.theweatherapi.api.response.aircondition.AirconditionResponse;
import com.canhmai.theweatherapi.api.response.curentweather.CurrenWeatherResponse;
import com.canhmai.theweatherapi.api.response.forecast.ForecastListElemnt;
import com.canhmai.theweatherapi.api.response.forecast.ForecastResponse;
import com.canhmai.theweatherapi.api.response.getlocation.LocationResponse;
import com.canhmai.theweatherapi.broadcast.BroadCast;
import com.canhmai.theweatherapi.callback.OnBroadcastCallback;
import com.canhmai.theweatherapi.callback.OnChangeFragmentCallback;

import com.canhmai.theweatherapi.callback.OnResponseCallback;
import com.canhmai.theweatherapi.databinding.ActivityMainBinding;
import com.canhmai.theweatherapi.viewmodel.MainViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnResponseCallback, OnBroadcastCallback {
    private com.canhmai.theweatherapi.databinding.ActivityMainBinding binding;
    private MainViewModel viewModel;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetDialog bottomSheetDialog2;
    private TextView dialogTv;
    private Object data;
    private OnChangeFragmentCallback onChangeFragmentCallback;
    private FusedLocationProviderClient fusedLocationClient;
    private Dialog netWorkDialog;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.setOnResponseCallback(this);
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        initViews();
        checkPermission();

    }

    private void initViews() {
        bottomSheetDialog = new BottomSheetDialog(MainActivity.this);

        initProgressDialog();
        initNetworkDialog();
        initBroadcast();
        binding.includeFragment.trLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchBottomSheet();
            }
        });
    }

    private void updateView() {

    }

    private void checkPermission() {
        int fineLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        int coardLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);

        if (fineLocationPermission != PackageManager.PERMISSION_GRANTED
                && coardLocationPermission != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) && !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                showMessengerOkCancel("Chúng tôi cần dữ liệu vị trí của bạn để có thể cập nhật dữ liệu thời tiết một cách chính xác nhất, vui lòng cấp quyền truy cập vào vị trí cho ứng dụng ",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

                            }
                        }
                );
                return;
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            return;
        }

        getUserLocation();

    }

    private void showMessengerOkCancel(String s, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(this).setMessage(s).setPositiveButton("Đồng ý", onClickListener).setCancelable(false).create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                getUserLocation();
            } else {
                Toast.makeText(MainActivity.this, "Bạn đã từ chối cập quyề truy cập vị trí", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkCompletion() {

        if (viewModel.checkStateComplete()) {
            if (!viewModel.isCalled) {
                viewModel.isCalled = true;
            }
            dismissProgressDialog();
        }
    }

    private void updateCurrentWeather() {

        CurrenWeatherResponse currenWeatherResponse;

        try {
            currenWeatherResponse = viewModel.getCurrenWeatherResponse();


            Date date = Calendar.getInstance().getTime();
            String dateString = new SimpleDateFormat("HH:mm dd-MM-yyyy").format(date);
            binding.includeFragment.tvLastUpdate.setText(String.format("Cập nhật lần cuối lúc: %s", dateString));
            String dateNewString = new SimpleDateFormat("dd-MM-yyyy").format(date);
            binding.includeFragment.tvCurentTime.setText(dateNewString);

            // condition
            binding.includeFragment.tvCondition.setText(currenWeatherResponse.getWeather().get(0).getDescription());
            //cityname
            binding.includeFragment.tvLocation.setText(currenWeatherResponse.getName());
            //degree
            binding.includeFragment.tvDegree.setText(String.format("%s°C", currenWeatherResponse.getMain().getTemp()));


            Glide.with(MainActivity.this).load("https://openweathermap.org/img/wn/" + currenWeatherResponse.getWeather().get(0).getIcon() + "@2x.png").into(binding.includeFragment.ivWeatherImage);


            binding.includeFragment.tvCondition.setText(String.format("Trạng thái: %s", currenWeatherResponse.getWeather().get(0).getMain()));
            binding.includeFragment.tvPressure.setText(String.format("Áp suất: %dhPa", currenWeatherResponse.getMain().getPressure()));


            binding.includeFragment.tvMaxMinTemp.setText(String.format("Nhiệt độ cao/thấp: %s/%s", currenWeatherResponse.getMain().getTempMax(), currenWeatherResponse.getMain().getTempMin()));
            binding.includeFragment.tvFeellike.setText(String.format("Cảm giác như: %s°C", currenWeatherResponse.getMain().getFeelsLike()));

            binding.includeFragment.tvWind.setText(String.format("gió: %skm/h", currenWeatherResponse.getWind().getSpeed()));
            binding.includeFragment.tvCloud.setText(String.format("Mây: %d%%", currenWeatherResponse.getClouds().getAll()));


            long sunRiseHour = Long.valueOf(currenWeatherResponse.getSys().getSunrise()) * 1000;// its need to be in milisecond
            Date sunRiseDate = new java.util.Date(sunRiseHour);
            String sunRiseString = new SimpleDateFormat("hh:mm", Locale.CHINA).format(sunRiseDate);

            long sunSetHour = Long.valueOf(currenWeatherResponse.getSys().getSunset()) * 1000;// its need to be in milisecond
            Date sunSetDate = new java.util.Date(sunSetHour);
            String sunSetString = new SimpleDateFormat("hh:mm", Locale.CHINA).format(sunSetDate);


            binding.includeFragment.tvSunrise.setText(String.format("Bình minh: %s", sunRiseString));
            binding.includeFragment.tvSunset.setText(String.format("Hoàng hôn: %s", sunSetString));

            viewModel.setCurrent(true);
            checkCompletion();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onApiSuccess(String key, Object data, String msg) {

        if (key.equals(MainViewModel.GET_WEATHER_FORECAST)) {

            ForecastResponse forecastResponse = (ForecastResponse) data;
            updateForecast(forecastResponse);

        } else if (key.equals(MainViewModel.GET_CURENT_WEATHER)) {
            CurrenWeatherResponse response = (CurrenWeatherResponse) data;
            if (response != null) {

                viewModel.setCurrenWeatherResponse(response);
                updateCurrentWeather();

            }
        } else if (key.equals(MainViewModel.GET_AIR_CONDTION)) {
            AirconditionResponse response = (AirconditionResponse) data;
            if (response != null) {

                updateAirrCondition(response);

            }
        } else if (key.equals(MainViewModel.GET_LAT_LON)) {
            List<LocationResponse> response = (List<LocationResponse>) data;
            if (response != null) {

                updateLocation(response);

            }
        }
    }

    private void updateLocation(List<LocationResponse> response) {
        viewModel.setLat(response.get(0).getLat());
        viewModel.setLon(response.get(0).getLon());
        checkNetWorkAvailable();
    }

    private void updateAirrCondition(AirconditionResponse response) {
        Log.e(TAG, "updateAirrCondition: oke");
        binding.includeFragment.tvCo.setText(response.getList().get(0).getComponents().getCo() + "μg/m3");
        binding.includeFragment.tvNo.setText(response.getList().get(0).getComponents().getNo() + "μg/m3");
        binding.includeFragment.tvNo2.setText(response.getList().get(0).getComponents().getNo2() + "μg/m3");
        binding.includeFragment.tvO3.setText(response.getList().get(0).getComponents().getO3() + "μg/m3");
        binding.includeFragment.tvSo2.setText(response.getList().get(0).getComponents().getSo2() + "μg/m3");
        binding.includeFragment.tvPm25.setText(response.getList().get(0).getComponents().getPm25() + "μg/m3");
        binding.includeFragment.tvPm10.setText(response.getList().get(0).getComponents().getPm10() + "μg/m3");
        binding.includeFragment.tvNh3.setText(response.getList().get(0).getComponents().getNh3() + "μg/m3");


        int index = response.getList().get(0).getMain().getAqi();

        switch (index) {
            case 1:
                binding.includeFragment.tvAircondition.setText("Tốt");
                break;

            case 2:
                binding.includeFragment.tvAircondition.setText("Khá");
                break;
            case 3:
                binding.includeFragment.tvAircondition.setText("Trung bình");
                break;
            case 4:
                binding.includeFragment.tvAircondition.setText("Kém");
                break;
            case 5:
                binding.includeFragment.tvAircondition.setText("Rất kém");
                break;

        }

        viewModel.setAir(true);
        checkCompletion();
    }

    private void updateForecast(ForecastResponse forecastResponse) {
        try {
            List<ForecastListElemnt> forecastListElemntsToday = new ArrayList<>();
            List<ForecastListElemnt> forecastListElemntsDay1 = new ArrayList<>();


            forecastListElemntsToday.add(forecastResponse.getList().get(0));


            String timeAtFirst = forecastResponse.getList().get(0).getDtTxt();
            Date dateAtFirs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeAtFirst);
            String stringDateAtFirs = new SimpleDateFormat("dd-MM-yyyy").format(dateAtFirs);

            for (int i = 1; i < forecastResponse.getList().size(); i++) {

                String time = forecastResponse.getList().get(i).getDtTxt();
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
                String newstring = new SimpleDateFormat("dd-MM-yyyy").format(date);

                if (newstring.equals(stringDateAtFirs)) {
                    forecastListElemntsToday.add(forecastResponse.getList().get(i));
                } else {
                    forecastListElemntsDay1.add(forecastResponse.getList().get(i));
                }
            }

            ItemViewAdapter currentAdapter = new ItemViewAdapter(MainActivity.this, ItemViewAdapter.CURRENT, forecastListElemntsToday);
            binding.includeFragment.rvTodayWeather.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
            binding.includeFragment.rvTodayWeather.setAdapter(currentAdapter);


            ItemViewAdapter itemForecastDayAdapter = new ItemViewAdapter(MainActivity.this, ItemViewAdapter.FORECAST, forecastListElemntsDay1);
            binding.includeFragment.rvForecastWeather.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
            binding.includeFragment.rvForecastWeather.setAdapter(itemForecastDayAdapter);
            itemForecastDayAdapter.getForecastListElemntMutableLiveData().observe(this, new Observer<ForecastListElemnt>() {
                @Override
                public void onChanged(ForecastListElemnt forecastListElemnt) {
                    showForecastBottomSheeet(forecastListElemnt)
                    ;
                }
            });
            viewModel.setForecast(true);
            checkCompletion();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void showForecastBottomSheeet(ForecastListElemnt forecastListElemnt) {
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_forecast_detail, null);

        ImageView ivClose = view.findViewById(R.id.iv_close);
        ImageView ivIcon = view.findViewById(R.id.iv_icon);

        TextView tv_time = view.findViewById(R.id.tv_time);
        TextView tv_description = view.findViewById(R.id.tv_description);
        TextView tv_temp = view.findViewById(R.id.tv_temp);
        TextView tv_maxtemp = view.findViewById(R.id.tv_maxtemp);
        TextView tv_mintemp = view.findViewById(R.id.tv_min_temp);
        TextView tv_feels_like = view.findViewById(R.id.tv_feels_like);
        TextView tv_pressure = view.findViewById(R.id.tv_pressure);
        TextView tv_humidity = view.findViewById(R.id.tv_humidity);
        TextView tv_cloud = view.findViewById(R.id.tv_cloud);
        TextView tv_wind_speed = view.findViewById(R.id.tv_wind_speed);
        TextView tv_wind_gust = view.findViewById(R.id.tv_wind_gust);
        TextView tv_visibility = view.findViewById(R.id.tv_visibility);


        Date date = null;


        Glide.with(MainActivity.this).load("https://openweathermap.org/img/wn/" +
                forecastListElemnt.getWeather().get(0).getIcon() + "@2x.png").into(ivIcon);
        String time = forecastListElemnt.getDtTxt();

        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);

            String newstring = new SimpleDateFormat("HH:mm\ndd-MM-yyyy").format(date);
            tv_time.setText(newstring);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        tv_temp.setText(forecastListElemnt.getMain().getTemp() + " °C");
        tv_humidity.setText(forecastListElemnt.getMain().getHumidity() + " %");
        tv_wind_speed.setText(forecastListElemnt.getWind().getSpeed() + " km/h");
        tv_cloud.setText(forecastListElemnt.getClouds().getAll() + " %");

        tv_description.setText(forecastListElemnt.getWeather().get(0).getDescription());
        tv_maxtemp.setText(forecastListElemnt.getMain().getTempMax() + " °C");
        tv_mintemp.setText(forecastListElemnt.getMain().getTempMin() + " °C");
        tv_feels_like.setText(forecastListElemnt.getMain().getFeelsLike() + " °C");
        tv_pressure.setText(forecastListElemnt.getMain().getPressure() + " hPa");
        tv_wind_gust.setText(forecastListElemnt.getWind().getGust() + " km/h");
        tv_visibility.setText(forecastListElemnt.getVisibility() + " km/h");


        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

    }

    @Override
    public void onApiFailure(String key, Object data, String msg) {
        dismissProgressDialog();
        if (key.equals(MainViewModel.GET_WEATHER_FORECAST)) {

            Log.e(TAG, "onApiFailure: call api fali" + msg);
        } else if (key.equals(MainViewModel.GET_LAT_LON)) {
            if (msg.equals("404")) {
                Toast.makeText(MainActivity.this, "Tên thành phố đúng, vui lòng thử lại", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void showSearchBottomSheet() {

        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.fragment_search, null);

        EditText editText = view.findViewById(R.id.edt_cityName);

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    String text = editText.getText().toString().trim();

                    if (viewModel.isNetworkState()) {
                        if (!text.isEmpty()) {
                            showProgressDialog();
                            bottomSheetDialog.dismiss();
                            viewModel.getLocationAPI(text);
                        } else {
                            bottomSheetDialog.dismiss();
                        }

                    } else {
                        bottomSheetDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Không có kết nối mạng", Toast.LENGTH_SHORT).show();

                    }

                    return true;
                }

                return false;
            }
        });

    }


    public void setData(Object data) {
        this.data = data;
    }


    private void initProgressDialog() {
        progressDialog = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View viewV = LayoutInflater.from(MainActivity.this).inflate(R.layout.fragment_splash, null, false);
        progressDialog.setContentView(viewV);
        progressDialog.create();
    }

    private void initNetworkDialog() {
        netWorkDialog = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View view2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.progressdialog, null, false);
        netWorkDialog.setContentView(view2);
        netWorkDialog.create();
    }


    private void initBroadcast() {
        BroadCast broadCast = new BroadCast(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        MainActivity.this.registerReceiver(broadCast, intentFilter);
    }


    public void returnNetworkState(boolean state) {

        if (!state) {

            if (!viewModel.isCalled) {
                if (!netWorkDialog.isShowing()) {
                    showNetWorkDialog();
                }

            }

            viewModel.setNetworkState(false);

        } else {
            if (netWorkDialog.isShowing()) {
                netWorkDialog.dismiss();
            }
            if (!viewModel.isCalled) {
                showProgressDialog();
                getApiData();
            }
            viewModel.setNetworkState(true);
        }


    }

    private void getUserLocation() {

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    viewModel.setLat(location.getLatitude());
                    viewModel.setLon(location.getLongitude());

                    checkNetWorkAvailable();

                }
            }
        });
    }

    private void showProgressDialog() {

        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();

        }
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void dismissNetworkDialog() {
        if (netWorkDialog != null && netWorkDialog.isShowing()) {
            netWorkDialog.dismiss();
        }
    }

    protected void checkNetWorkAvailable() {
        if (viewModel.isNetworkState()) {
            showProgressDialog();
            getApiData();
        }
    }

    protected void getApiData() {
        viewModel.getWeatherAPI();
        viewModel.getAirContidionAPI();
        viewModel.getForecastAPI();
    }

    protected void showNetWorkDialog() {
        if (netWorkDialog != null && !netWorkDialog.isShowing()) {
            netWorkDialog.show();
        }

    }


}