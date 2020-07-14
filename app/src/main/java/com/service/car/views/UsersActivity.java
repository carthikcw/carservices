package com.service.car.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.service.car.R;
import com.service.car.adapters.UsersAdapter;
import com.service.car.models.BaseResponse;
import com.service.car.models.User;
import com.service.car.models.UserRequest;
import com.service.car.services.WebServices;
import com.service.car.services.RetrofitInstance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = UsersActivity.class.getSimpleName();
    private TextInputEditText fromDt, toDt;
    private TextInputLayout fromtxtip, totxtip;
    private List<User> customersList;
    private RecyclerView recyclerViewUsers;
    private UsersAdapter usersAdapter;
    private TextView tv_noUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        //init
        fromDt = findViewById(R.id.txtip_Et_fromDt);
        toDt = findViewById(R.id.txtip_Et_toDt);
        fromtxtip = findViewById(R.id.from_txtip);
        totxtip = findViewById(R.id.to_txtip);
        tv_noUsers = findViewById(R.id.tv_nousers);

        //clicks for txt ips
        fromDt.setOnClickListener(this);
        toDt.setOnClickListener(this);

        //default intialization of list & adapter
        customersList = new ArrayList<>();
        usersAdapter = new UsersAdapter(customersList, this,tv_noUsers);

        //Load Default Current Date Data
        getUsers(0);

    }


    @SuppressLint("SimpleDateFormat")
    private void getUsers(int i) {
        WebServices api = RetrofitInstance.getRetrofit().create(WebServices.class);
        // Dates Format are yyyy-mm-dd
        Calendar calendar = Calendar.getInstance();
        String currentDate  = ""+calendar.getTime();

        //Default loading with current date
        UserRequest requestObj = new UserRequest();
        if (i == 0) {
            requestObj.setFromDate(getRequiredDateFormat(currentDate));
            requestObj.setToDate(getRequiredDateFormat(currentDate));
        }else if (i == 1){
            requestObj.setFromDate(getRequiredDateFormat(fromDt.getText().toString().trim()));
            requestObj.setFromDate(getRequiredDateFormat(toDt.getText().toString().trim()));
        }
        Call<BaseResponse> call = api.getRequests(requestObj);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (baseResponse != null && baseResponse.getStatus().equalsIgnoreCase("Success")) {
                    customersList.addAll(baseResponse.getRequests());
                    usersAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                usersAdapter.notifyDataSetChanged();
                tv_noUsers.setVisibility(View.VISIBLE);
                tv_noUsers.setText("No Requests "+t.getMessage());
                Log.e(TAG,"Error in API Call");
            }
        });
    }

    /***
     * Date Format (yyyy-MM-dd)
     * @param currentDate
     * @return
     */
    private String getRequiredDateFormat(String currentDate) {
        String dt = "";
        try {
            dt = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtip_Et_fromDt:
                toDt.setText("");
                openCalendarDialog(v.getContext(),1);
                break;

            case R.id.txtip_Et_toDt:
                openCalendarDialog(v.getContext(),2);
                if (!fromDt.getText().toString().isEmpty()
                && !toDt.getText().toString().isEmpty()){
                    getUsers(1);
                }
                break;
        }
    }

    private void openCalendarDialog(Context context, final int type) {
        final Calendar cldr = Calendar.getInstance();
        final int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        final long startTime = 0;
        long fromDtTimeInMillis = 0;
        // date picker dialog
        final DatePickerDialog datePicker = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        // Date Validations
                        String exactDate = "";
                        int dayofmnth  = dayOfMonth;
                        if (dayofmnth < 10){
                            exactDate = "0"+dayofmnth;
                        }else{
                            exactDate = ""+dayofmnth;
                        }
                        //Month Validations
                        String exactmonth = "";
                        int month  = (monthOfYear + 1);
                        if (month < 10){
                           exactmonth = "0"+month;
                        }else{
                            exactmonth = ""+month;
                        }

                        if (type == 1) {
                            fromDt.setText(year + "-" + exactmonth + "-" + exactDate);
                        } else if (type == 2) {
                            toDt.setText(year + "-" + exactmonth + "-" + exactDate);
                        }
                    }
                }, year, month, day);

        if (type == 2 && fromDt != null && !fromDt.getText().toString().isEmpty()) {
            String myDate = fromDt.getText().toString();
            fromDtTimeInMillis = 0;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(myDate);
                fromDtTimeInMillis = date.getTime();
            }catch (Exception e){
                e.printStackTrace();
            }
            if (fromDtTimeInMillis > 0) {
                datePicker.getDatePicker().setMinDate(fromDtTimeInMillis);
            }
        }else{
            fromDtTimeInMillis = 0;
            datePicker.getDatePicker().setMinDate(1594668661);
        }

        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePicker.show();
    }
}
