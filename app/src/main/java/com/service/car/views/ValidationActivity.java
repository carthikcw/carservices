package com.service.car.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.service.car.R;
import com.service.car.models.BaseResponse;
import com.service.car.models.Customer;
import com.service.car.models.FcmUser;
import com.service.car.models.OtpMessageResponse;
import com.service.car.services.EndPoints;
import com.service.car.services.RetrofitInstance;
import com.service.car.utils.Constants;
import com.service.car.utils.LocalStorage;
import com.service.car.utils.OtpReceivedListener;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ValidationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ValidationActivity.class.getSimpleName();
    private TextView tv_terms;
    private CheckBox cb_terms;
    private TextInputEditText etUserLogin;
    private TextInputLayout txtip;

    private TextInputLayout txtipuserOtp;
    private TextInputEditText edtOtp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        etUserLogin = findViewById(R.id.et_login);
        txtip = findViewById(R.id.txtipuserlogin);
        cb_terms = findViewById(R.id.cb_terms);
        tv_terms = findViewById(R.id.tv_terms);
        txtipuserOtp = findViewById(R.id.txtipuserOtp);
        edtOtp = findViewById(R.id.et_Otp);
        tv_terms.setPaintFlags(tv_terms.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        findViewById(R.id.card_go).setOnClickListener(this);
        findViewById(R.id.tv_terms).setOnClickListener(this);
        findViewById(R.id.ll_termsnconditions).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_go:
                if (getAppUserType().contains("admin") && isValidNumber(etUserLogin.getText().toString())) {
                    startActivity(new Intent(ValidationActivity.this, UsersActivity.class));
                    finish();
                } else if (getAppUserType().contains("provider") && isValidNumber(etUserLogin.getText().toString())) {
                    startActivity(new Intent(ValidationActivity.this, RequestActivity.class));
                    finish();
                } else if (getAppUserType().contains("customer")) {
                    if (cb_terms.isChecked()) {
                        if (isValidNumber(etUserLogin.getText().toString())) {

                            if (edtOtp.getText() != null && !TextUtils.isEmpty(edtOtp.getText().toString())) {
                                String sessionId = LocalStorage.getLocallyStoredValue(this, Constants.OTP_SESSION_ID);
                                validateOtpService(this, sessionId, edtOtp.getText().toString());
                            } else {
                                requestOtpService(getApplicationContext(), etUserLogin.getText().toString());
                            }
                        }else{
                            Toast.makeText(this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();

                        }

                    } else {
                        Toast.makeText(this, "Please read terms and check the above box to continue", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.ll_termsnconditions:
                if (!cb_terms.isChecked()) {
                    cb_terms.setChecked(true);
                } else {
                    cb_terms.setChecked(false);
                }
                break;
            case R.id.tv_terms:
                startActivity(new Intent(ValidationActivity.this, WebViewActivity.class));
                break;
            case R.id.et_login:
                txtip.setHint("Enter Your Mobile Number");
                break;

        }
    }


    private boolean isValidNumber(String mobileNumber) {
        if (!TextUtils.isEmpty(mobileNumber) && mobileNumber.length() == 10) {
            return true;
        } else {
            txtip.setHint("Invalid Number");
        }
        return false;
    }

    private String getAppUserType() {
        return getApplicationContext().getPackageName();
    }


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(getApplicationContext(), RequestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getApplicationContext(), channelId)
                        .setSmallIcon(R.drawable.carwashoriginal)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    /**
     * OTP PROVIDER CALL HERE
     *
     * @param context
     * @param userMobileNumber
     */
    private void requestOtpService(final Context context, String userMobileNumber) {
        EndPoints webService = RetrofitInstance.getRetrofit(3).create(EndPoints.class);
        Call<OtpMessageResponse> call = webService.sendOTP(Constants.OTP_API_KEY, userMobileNumber);
        ;
        call.enqueue(new Callback<OtpMessageResponse>() {
            @Override
            public void onResponse(Call<OtpMessageResponse> call, Response<OtpMessageResponse> response) {
                OtpMessageResponse otpResponse = response.body();
                Log.v(TAG, "otp response "+ otpResponse.toString());
                if (otpResponse != null && otpResponse.getStatus() != null && otpResponse.getDetails() != null) {
                    String sessionId = otpResponse.getDetails();
                    LocalStorage.saveToLocalStorage(context, Constants.OTP_SESSION_ID, sessionId);
                    showOtpDialog(context, sessionId);
                } else {
                    Toast.makeText(ValidationActivity.this, "Error in reading OTP Detail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OtpMessageResponse> call, Throwable t) {
                Toast.makeText(ValidationActivity.this, "Error in reading OTP, Please try again with valid mobile number", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showOtpDialog(final Context context, final String sessionId) {
//        final Dialog dialog = new Dialog(context);
//        dialog.setContentView(R.layout.layout_validate_otp);
//        Window window = dialog.getWindow();
//        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        final TextInputLayout txtip_otp = dialog.findViewById(R.id.txtip_otp);
//        final EditText et_otp = dialog.findViewById(R.id.et_otp);
//
//        LinearLayout send_otp = dialog.findViewById(R.id.id_otp_ll);
//        send_otp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (et_otp.getText() != null && !TextUtils.isEmpty(et_otp.getText().toString())) {
//                    txtip_otp.setErrorEnabled(false);
//                    validateOtpService(context, sessionId, et_otp.getText().toString(), dialog);
//                }
//            }
//        });
//
//        dialog.show();
    }

    private void validateOtpService(Context context, String sessionId, String userEnteredOtp) {
        EndPoints webService = RetrofitInstance.getRetrofit(3).create(EndPoints.class);
        Call<OtpMessageResponse> call = webService.verifyOTP(Constants.OTP_API_KEY, sessionId, userEnteredOtp);
        call.enqueue(new Callback<OtpMessageResponse>() {
            @Override
            public void onResponse(Call<OtpMessageResponse> call, Response<OtpMessageResponse> response) {
                OtpMessageResponse otpvalidatingResponnse = response.body();
                Log.v(TAG,"success in otp response "+otpvalidatingResponnse.toString());
                if (otpvalidatingResponnse != null && otpvalidatingResponnse.getStatus().equals("Success")) {
                    Log.v(TAG,"success in otp validation");
                    startActivity(new Intent(ValidationActivity.this, RequestActivity.class));
                    finish();
                } else {
                    Log.d(TAG, " error in validating otp response " + response.body().getDetails() + "|||" + response.body().getStatus());
                }
            }

            @Override
            public void onFailure(Call<OtpMessageResponse> call, Throwable t) {
//                if (dialog != null && dialog.isShowing()) dialog.dismiss();
                Log.d(TAG, " error in validating otp response " + t.getMessage());

            }
        });
    }


}


