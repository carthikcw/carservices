package com.service.car.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.service.car.R;
import com.service.car.adapters.UsersAdapter;
import com.service.car.models.BaseResponse;
import com.service.car.models.User;
import com.service.car.models.UserRequest;
import com.service.car.services.EndPoints;
import com.service.car.services.RetrofitInstance;
import com.service.car.utils.ClickListener;
import com.service.car.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersActivity extends AppCompatActivity implements View.OnClickListener, ClickListener {

    private static final String TAG = UsersActivity.class.getSimpleName();
    private static final int REQUEST_CALL_PERMISSION = 1009;
    private EditText fromDt, toDt;
    private TextInputLayout fromtxtip, totxtip;
    private List<User> customersList;
    private RecyclerView recyclerViewUsers;
    private UsersAdapter usersAdapter;
    private TextView tv_noUsers;
    private ProgressBar progressBar;
    private AppCompatSpinner usersTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        //init
        usersTypeSpinner = findViewById(R.id.spinner_usertype);
        progressBar = findViewById(R.id.progress_bar);
        fromDt = findViewById(R.id.txtip_Et_fromDt);
        toDt = findViewById(R.id.txtip_Et_toDt);
        fromtxtip = findViewById(R.id.from_txtip);
        totxtip = findViewById(R.id.to_txtip);
        tv_noUsers = findViewById(R.id.tv_nousers);
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.top_ll).setOnClickListener(this);

        //clicks for txt ips
        fromDt.setOnClickListener(this);
        toDt.setOnClickListener(this);

        //default intialization of list & adapter
        customersList = new ArrayList<>();
        usersAdapter = new UsersAdapter(customersList, this, tv_noUsers,this);
        recyclerViewUsers.setAdapter(usersAdapter);

        //Load Default Current Date Data
        getUsers(0);
    }


    @SuppressLint("SimpleDateFormat")
    private void getUsers(int i) {
        EndPoints api = RetrofitInstance.getRetrofit().create(EndPoints.class);
        // Dates Format are yyyy-mm-dd
        Calendar calendar = Calendar.getInstance();
        String currentDate = "" + calendar.getTime();

        //Default loading with current date
        UserRequest requestObj = new UserRequest();
        if (i == 0) {
            requestObj.setFromDate(getCurrentDateFormat());
            requestObj.setToDate(getCurrentDateFormat());
        } else if (i == 1) {
            requestObj.setFromDate(fromDt.getText().toString().trim());
            requestObj.setToDate(toDt.getText().toString().trim());
        }
        Call<BaseResponse> call = api.getRequests(requestObj);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (baseResponse != null && baseResponse.getStatus() != null && baseResponse.getStatus().equals(Constants.SUCCESS)) {
                    customersList.clear(); // clearing cache
                    customersList.addAll(baseResponse.getRequests());
                    usersAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                } else {
                    customersList.clear(); // clearing cache
                    usersAdapter.notifyDataSetChanged();
                    tv_noUsers.setVisibility(View.VISIBLE);
                    tv_noUsers.setText(R.string.no_requests);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                usersAdapter.notifyDataSetChanged();
                tv_noUsers.setVisibility(View.VISIBLE);
                tv_noUsers.setText(R.string.no_requests + t.getMessage());
                Log.e(TAG, "Error in API Call");
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    /***
     * Current Date Format (yyyy-MM-dd)
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    private String getCurrentDateFormat() {
        String dt = "";
        try {
            dt = new SimpleDateFormat(Constants.ONLY_DATE_FORMAT).format(Calendar.getInstance().getTime());
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
                openCalendarDialog(v.getContext(), 1);
                break;

            case R.id.txtip_Et_toDt:
                if (!fromDt.getText().toString().isEmpty()) {
                    openCalendarDialog(v.getContext(), 2);
                }
                break;

            case R.id.top_ll:
                getUsers(usersTypeSpinner.getSelectedItemPosition());
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
                        int dayofmnth = dayOfMonth;
                        if (dayofmnth < 10) {
                            exactDate = "0" + dayofmnth;
                        } else {
                            exactDate = "" + dayofmnth;
                        }
                        //Month Validations
                        String exactmonth = "";
                        int month = (monthOfYear + 1);
                        if (month < 10) {
                            exactmonth = "0" + month;
                        } else {
                            exactmonth = "" + month;
                        }

                        if (type == 1) {
                            fromDt.setText(year + "-" + exactmonth + "-" + exactDate);
                        } else if (type == 2) {
                            toDt.setText(year + "-" + exactmonth + "-" + exactDate);
                            if (!fromDt.getText().toString().isEmpty()
                                    && !toDt.getText().toString().isEmpty()) {
                                // FIXME : REQUESTING WEB SERVICE HERE
                                getUsers(1); // requesting webservice here
                                progressBar.setVisibility(View.VISIBLE);
                            }
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (fromDtTimeInMillis > 0) {
                datePicker.getDatePicker().setMinDate(fromDtTimeInMillis);
            }

        } else {
            fromDtTimeInMillis = 0;
            datePicker.getDatePicker().setMinDate(1594668661);
        }

        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePicker.show();

//        if (type == 2 && !fromDt.getText().toString().isEmpty()
//                && !toDt.getText().toString().isEmpty()) {
//            // FIXME : REQUESTING WEB SERVICE HERE
//            getUsers(1); // requesting webservice here
//        }
    }


    private void callUser(Context context, String phoneno) {
        if (ActivityCompat.checkSelfPermission(UsersActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UsersActivity.this, new String[]{
                            Manifest.permission.CALL_PHONE},
                    REQUEST_CALL_PERMISSION);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneno));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(callIntent);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (resultCode == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Please try to call customer now", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemClick(Context context, int pos, String data) {
        callUser(getApplicationContext(),data);
    }
}
