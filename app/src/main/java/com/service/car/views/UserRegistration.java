package com.service.car.views;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.service.car.R;
import com.service.car.db.DatabaseConnectivity;

public class UserRegistration extends AppCompatActivity implements View.OnClickListener{
    DatabaseConnectivity database;
    protected  EditText Register_FirstName;
    protected  EditText Register_LastName;

    protected  EditText Register_Email;
    protected  EditText Register_passWord;
    protected  EditText mobileNumber;
    public String UserFullName;
    public String Reigister_mobile_value;
    public Button registration;
    private   RadioButton gender_male;
    private   RadioButton gender_female;
    private TextInputLayout txtip_firstname;
    private TextInputLayout txtip_lastname;
    private TextInputLayout txtip_mobile;
    private TextInputLayout txtip_email;
    private TextInputLayout txtip_password;
    private RelativeLayout rootlayout;

    String usertable = "user";
    Context userRegistrationContext = this;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_new_registration);
            Register_FirstName = findViewById(R.id.register_firstname);
            Register_LastName = findViewById(R.id.register_lastname);
            Register_passWord = findViewById(R.id.register_password);
            Register_Email = findViewById(R.id.register_emailid);
            mobileNumber = findViewById(R.id.register_mobile);
            rootlayout = findViewById(R.id.rootlayout);
            txtip_firstname = findViewById(R.id.txtip_firstname);
            txtip_lastname = findViewById(R.id.txtip_lastname);
            txtip_mobile = findViewById(R.id.txtip_mobile);
            txtip_email = findViewById(R.id.txtip_email);
            txtip_password = findViewById(R.id.txtip_password);
            rootlayout =findViewById(R.id.rootlayout);

            findViewById(R.id.user_register_button).setOnClickListener(this);
        }


    @Override
    public void onClick(View v) {
        database = new DatabaseConnectivity(this);

            switch (v.getId()){

                case R.id.user_register_button:
                    String userfirstname = Register_FirstName.getText().toString();
                    String userlastname = Register_LastName.getText().toString();
                    String UserFullName = userfirstname+" "+userlastname;
                    String passwordValue = Register_passWord.getText().toString();
                    String emailValue = Register_Email.getText().toString();
                    String mobile_value = mobileNumber.getText().toString();

                    if(userfirstname.isEmpty()){
                        txtip_firstname.setError("This field is mandatory");
                        txtip_email.setErrorEnabled(false);
                        txtip_lastname.setErrorEnabled(false);
                        txtip_password.setErrorEnabled(false);
                        txtip_mobile.setErrorEnabled(false);
                    }
                    else if(userlastname.isEmpty()){
                        txtip_lastname.setError("This field is mandatory");
                        txtip_firstname.setErrorEnabled(false);
                        txtip_email.setErrorEnabled(false);
                        txtip_password.setErrorEnabled(false);
                        txtip_mobile.setErrorEnabled(false);
                    }
                   else if(passwordValue.isEmpty()){
                        txtip_password.setError("This field is mandatory");
                        txtip_lastname.setErrorEnabled(false);
                        txtip_firstname.setErrorEnabled(false);
                        txtip_mobile.setErrorEnabled(false);
                        txtip_email.setErrorEnabled(false);
                    }
                    else if(mobile_value.isEmpty()){
                        txtip_mobile.setError("This field is mandatory");
                        txtip_lastname.setErrorEnabled(false);
                        txtip_firstname.setErrorEnabled(false);
                        txtip_password.setErrorEnabled(false);
                        txtip_email.setErrorEnabled(false);

                    }
                   else if(!isValidEmailAddress(emailValue)||emailValue.isEmpty()){
                        txtip_email.setError("This field is mandatory");
                        txtip_password.setErrorEnabled(false);
                        txtip_lastname.setErrorEnabled(false);
                        txtip_firstname.setErrorEnabled(false);
                        txtip_mobile.setErrorEnabled(false);
                    }
//                   else if(userfirstname.isEmpty()&&userlastname.isEmpty()&&passwordValue.isEmpty()&&mobile_value.isEmpty()&&emailValue.isEmpty()){
//                        txtip_firstname.setError("This field is mandatory");
//                        txtip_lastname.setError("This field is mandatory");
//                        txtip_password.setError("This field is mandatory");
//                        txtip_mobile.setError("This field is mandatory");
//                        txtip_email.setError("This field is mandatory");
//                    }
                    else {
                        txtip_email.setErrorEnabled(false);
                        txtip_password.setErrorEnabled(false);
                        txtip_lastname.setErrorEnabled(false);
                        txtip_firstname.setErrorEnabled(false);
                        txtip_mobile.setErrorEnabled(false);
                        txtip_email.clearFocus();
                        txtip_password.clearFocus();
                        txtip_mobile.clearFocus();
                        txtip_lastname.clearFocus();
                        txtip_firstname.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(rootlayout.getWindowToken(), 0);


                        ContentValues contentValues = new ContentValues();
                        contentValues.put("userFirstName",userfirstname);
                        contentValues.put("userLastName", userlastname);
                        contentValues.put("email", emailValue);
                        contentValues.put("password", passwordValue);
                        contentValues.put("MobileNumber", mobile_value);

                        boolean insertdescision = database.insertvalues(contentValues,usertable,userfirstname,userlastname,emailValue,userRegistrationContext);

                        if(insertdescision){
                            Toast.makeText(UserRegistration.this, "User registered!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UserRegistration.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(UserRegistration.this, "User registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                  break;
            }
        }

   public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();

    }



}
