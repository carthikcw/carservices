package com.service.car.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnectivity extends SQLiteOpenHelper {
    public static final String name = "database";
    private static final String TAG = DatabaseConnectivity.class.getSimpleName();
    static int version = 4;

    public static final String firstName = "userFirstName";
    public static final String lastName = "userLastName";
    public static final String pwd = "password";
    public static final String email = "email";
    public static final String env = "Enivironment";
    public static final String cost = "Price";
    public static final String manufacturer = "Producers";
    public static final String foodproduct = "Product";
    public static final String vegan = "Vegan";
    public static final String vegetarian = "Vegetarian";
    public static final String gluten = "Glutenfree";
    public static final String lakto = "Laktofree";
    public static final String fair = "FairSocial";
    public static final String productstable = "shopProducts";
    public static final String usertable = "user";
    public static final String phone = "MobileNumber";
    // @tables user and milk are to table created to store the content values in the SQLdatabase

    static final String crateTableUser = "CREATE TABLE " + usertable + " ( id INTEGER PRIMARY KEY AUTOINCREMENT, " + firstName + " TEXT, " + lastName + " TEXT, " + email + " TEXT, " + pwd + " TEXT, " + phone + " INTEGER)";

    static final String crateproductstable = "CREATE TABLE " + productstable + " ( id INTEGER PRIMARY KEY AUTOINCREMENT, " + foodproduct + " TEXT, " + manufacturer + " TEXT, " + vegan + " TEXT, " + vegetarian + " TEXT, " + gluten + " TEXT, " + lakto + " TEXT, " + env + " INTEGER, " + fair + " INTEGER, " + cost + " INTEGER," + email +" TEXT, FOREIGN KEY(email) REFERENCES usertable(email)) ";


    public DatabaseConnectivity(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(crateproductstable);
        db.execSQL(crateTableUser);

        //  insertProducts method was called only once to insert the static data into the sqlite database
        try {
            insertProducts();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + usertable);
        db.execSQL("DROP TABLE IF EXISTS " + productstable);

        onCreate(db);

    }


    //@methods insertUser inserts the data into the user database
    public boolean insertvalues(ContentValues contentValues, String usertable, String usernamefirstname, String userlastname, String emailvalue, Context userRegistrationContext) {
        String recordValidation = "Select * from '" + usertable + "' where userFirstName = '" + usernamefirstname + "' AND userLastName = '" + userlastname + "' AND email = '" + emailvalue + "'";
        SQLiteDatabase dbreader = this.getReadableDatabase();
        SQLiteDatabase dbwriter = this.getWritableDatabase();

        Cursor cursor = dbreader.rawQuery(recordValidation, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            long result = dbwriter.insert(usertable, "", contentValues);
            if (result == -1) {
                return false;
            }
            return true;
        } else
            return false;
    }

    public boolean isLoginValid(String emailID, String password) {
        String sql = "Select count(*) from user where email='" + emailID + "' and password='" + password + "'";
        SQLiteStatement statement = getReadableDatabase().compileStatement(sql);
        long l = statement.simpleQueryForLong();
        statement.close();

        if (l != 0) {
            return true;

        } else {
            return false;
        }


    }


    /***
     * To Check User entered Email in our DB or not
     * @param email
     * @return
     */
    public boolean checkEmailExists(String email) {
        String query = "Select * from " + usertable + " where email like '" + email + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,
                null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    /***
     * Update User Password
     * @param updatedPassword
     * @param email
     * @return
     */
    public boolean update(String updatedPassword, String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + usertable + " SET password = " + "'" + updatedPassword + "' " + "WHERE email = " + "'" + email + "'");
        return true;
    }

    final String fileLocation = "/home/saikrishna/Futury/PrototypeFinalData.csv";

    //@insertProducts method is called only once to insert the static data of the products into sqlite database.
    public void insertProducts() throws IOException, Exception {

        try {
            //CSVReader csvParser = new CSVReader(new FileReader(fileLocation));

            //list of products with all the details
            List<String[]> listofproducts = new ArrayList<>();


            SQLiteDatabase dbproductswriter = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();

            for (int i = 1; i < listofproducts.size(); i++) {
                String[] temp = listofproducts.get(i);
                contentValues.put("Product", temp[0]);
                contentValues.put("Producers", temp[1]);
                contentValues.put("Vegan", temp[2]);
                contentValues.put("Vegetarian", temp[3]);
                contentValues.put("Glutenfree", temp[4]);
                contentValues.put("Laktofree", temp[5]);
                contentValues.put("Enivironment", temp[6]);
                contentValues.put("FairSocial", temp[7]);
                contentValues.put("Price", temp[8]);

            }

            long result = dbproductswriter.insert(productstable, null, contentValues);
            Log.v(TAG, "-----> result in products db " + result);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "-----> error result in products db " + e.getMessage());
        }

    }


    public Cursor queryDbForProducts(String productSearchQuery) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(productSearchQuery, null);

        return cursor;

    }


}


