/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package info.fuelsubsidy.loginandregistration.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import info.fuelsubsidy.loginandregistration.R;

public class preview extends Activity {
    private static final String TAG = preview.class.getSimpleName();

    String insertSubsidy = "http://103.21.34.216:81/simcarddemo/insertSubsidy.php";
    public static String SuppdocURL = "http://103.21.34.216:81/subsidysupp/suppdoc.php";

    private Button btnRegister;
    private Button btnProceed;
    private Button btnFinish;

    private Button btnLinkToLogin;
    private ProgressDialog pDialog;
    private TextView personalInfo1;
    private TextView personalInfo2;

    private TextView disMykad;
    private TextView disName;
    private TextView disGender;
    private TextView disAddress;
    private TextView disPhone;
    private TextView disBankName;
    private TextView disAccount;
    private TextView disCCName;
    private TextView disCCNumber;

    private TextView disLoyaltyName;
    private TextView disLoyaltyNumber;
    private TextView disVehicleNumber;
    private TextView disVehicleOwner;
    private TextView disOwnerMykad;
    private TextView disRelationshipOwner;
    private TextView disLicenseNo;
    private TextView disLicenseExp;
    private TextView disLicenseClass;



    String personalinfo;

    private ImageView imMykad;
    private ImageView imCredit;
    private ImageView imLoyalty;
    private ImageView imRoadTax;
    private ImageView imAuthorize;

    //personal info
    String mykadNumber;
    String firstName;
    String lastName;

    String address;
    String gender;
    String phoneNumber;

    //bank info
    String bankName;
    String bankAccount;

    //credit card info
    String ccName;
    String ccNumber;

    //loyalty info
    String cardName;
    String cardNumber;

    //roadtax info
    String vehicleNumber;
    String vehicleOwner;
    String vehicleOwnerMykad;
    String vehicleRelationship;
    String licenseNumber;
    String licenseExpiry;
    String licenseClass;

    int No;
    String ba1;
    String[] testArray = {"/mykad.jpg"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnProceed = (Button) findViewById(R.id.btnProceed);
        btnFinish = (Button) findViewById(R.id.btnFinish);

        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        personalInfo1 = (TextView) findViewById (R.id.personalInfo1);
        personalInfo2 = (TextView) findViewById (R.id.personalInfo2);

        imMykad = (ImageView) findViewById(R.id.imMykad);
        imLoyalty = (ImageView) findViewById(R.id.imLoyalty);
        imCredit = (ImageView) findViewById(R.id.imCredit);
        imRoadTax = (ImageView) findViewById(R.id.imRoadTax);
        imAuthorize  = (ImageView) findViewById(R.id.imAuthorize);

        disMykad = (TextView) findViewById(R.id.disMykad);
        disName= (TextView) findViewById(R.id.disName);
        disGender= (TextView) findViewById(R.id.disGender);
        disAddress= (TextView) findViewById(R.id.disAddress);
        disPhone= (TextView) findViewById(R.id.disPhone);
        disBankName= (TextView) findViewById(R.id.disBankName);
        disAccount= (TextView) findViewById(R.id.disAccount);
        disCCName= (TextView) findViewById(R.id.disCCName);
        disCCNumber= (TextView) findViewById(R.id.disCCNumber);

        disLoyaltyName= (TextView) findViewById(R.id.disLoyaltyName);
        disLoyaltyNumber= (TextView) findViewById(R.id.disLoyaltyNumber);
        disVehicleNumber= (TextView) findViewById(R.id.disVehicleNumber);
        disVehicleOwner= (TextView) findViewById(R.id.disVehicleOwner);
        disOwnerMykad= (TextView) findViewById(R.id.disOwnerMykad);
        disRelationshipOwner= (TextView) findViewById(R.id.disRelationshipOwner);
        disLicenseNo= (TextView) findViewById(R.id.disLicenseNo);
        disLicenseExp= (TextView) findViewById(R.id.disLicenseExp);
        disLicenseClass= (TextView) findViewById(R.id.disLicenseClass);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        personalinfo = getIntent().getExtras().getString("personalinfo");

   /*   Bundle extras = getIntent().getExtras();
        Bitmap bmpmykad = (Bitmap) extras.getParcelable("Bitmap");
        Bitmap bmployalty = (Bitmap) extras.getParcelable("Bitmap2");
        imMykad.setImageBitmap(bmpmykad);
        imLoyalty.setImageBitmap(bmployalty);*/
        try {
            getImage();
        }
        catch (Exception ex){}

        //personalinfo.replaceAll(";","\n");
        //personalInfo2.setText(personalinfo);
        // Register Button Click event

        ViewFile();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

             LinearLayout mainpreview = (LinearLayout) findViewById(R.id.mainpreview);
                LinearLayout disclaimer = (LinearLayout) findViewById(R.id.disclaimer);

                mainpreview.setVisibility(View.GONE);
                disclaimer.setVisibility(View.VISIBLE);

                //registerUser();

            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);

                if (checkBox.isChecked()) {
                    onAttachDocSend();

                    registerUser();
                }
                else
                {
                    Toast.makeText(preview.this, "Please agree to proceed.", Toast.LENGTH_LONG).show();
                }

            }
        });


        btnFinish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                try {
                    File imgFilemykad = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/mykad.jpg");
                    File imgFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test1_0.jpg");
                    File imgFile2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test2_0.jpg");
                    File imgFile3 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test3.jpg");
                    File imgFile4 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test4.jpg");

                    if (imgFilemykad.exists()) {
                        String deleteCmd = "rm -r " + imgFilemykad;
                        Runtime runtime = Runtime.getRuntime();
                        try {
                            runtime.exec(deleteCmd);
                        } catch (IOException e) {
                        }
                    }

                    if (imgFile.exists()) {
                        String deleteCmd = "rm -r " + imgFile;
                        Runtime runtime = Runtime.getRuntime();
                        try {
                            runtime.exec(deleteCmd);
                        } catch (IOException e) {
                        }
                    }

                    if (imgFile2.exists()) {
                        String deleteCmd = "rm -r " + imgFile2;
                        Runtime runtime = Runtime.getRuntime();
                        try {
                            runtime.exec(deleteCmd);
                        } catch (IOException e) {
                        }
                    }

                    if (imgFile3.exists()) {
                        String deleteCmd = "rm -r " + imgFile3;
                        Runtime runtime = Runtime.getRuntime();
                        try {
                            runtime.exec(deleteCmd);
                        } catch (IOException e) {
                        }
                    }

                    if (imgFile4.exists()) {
                        String deleteCmd = "rm -r " + imgFile4;
                        Runtime runtime = Runtime.getRuntime();
                        try {
                            runtime.exec(deleteCmd);
                        } catch (IOException e) {
                        }
                    }
                }
                catch (Exception ex)
                {

                }


                finishAffinity();

            System.exit(0);

            }
        });
    }

    public void getImage()
    {
        try {
            File imgFilemykad = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/mykad.jpg");
            Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFilemykad.getAbsolutePath()), 200, 200, true);
            imMykad.setImageBitmap(bm);
            imMykad.setVisibility(View.VISIBLE);

            /*String newItem = "/mykad.jpg";
            int currentSize = testArray.length;
            int newSize = currentSize + 1;
            String[] tempArray = new String[ newSize ];
            for (int i=0; i < currentSize; i++)
            {
                tempArray[i] = testArray [i];
            }
            tempArray[newSize- 1] = newItem;
            testArray = tempArray;
            for (String element:testArray ) {
                System.out.println( element );
            }*/
        }
        catch(Exception ex)
        {

        }

        try {
        File imgFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test1_0.jpg");
        Bitmap bm1 = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()), 360, 216, true);
        imCredit.setImageBitmap(bm1);
        imCredit.setVisibility(View.VISIBLE);

            String newItem = "/test1_0.jpg";
            int currentSize = testArray.length;
            int newSize = currentSize + 1;
            String[] tempArray = new String[ newSize ];
            for (int i=0; i < currentSize; i++)
            {
                tempArray[i] = testArray [i];
            }
            tempArray[newSize- 1] = newItem;
            testArray = tempArray;
            for (String element:testArray ) {
                System.out.println( element );
            }

        }
        catch(Exception ex)
        {

        }

        try {
            File imgFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test1_2.jpg");
            Bitmap bm1 = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()), 360, 216, true);
            ImageView imCredit2 = (ImageView) findViewById(R.id.imCredit2);
            imCredit2.setImageBitmap(bm1);
            imCredit2.setVisibility(View.VISIBLE);

            String newItem = "/test1_2.jpg";
            int currentSize = testArray.length;
            int newSize = currentSize + 1;
            String[] tempArray = new String[ newSize ];
            for (int i=0; i < currentSize; i++)
            {
                tempArray[i] = testArray [i];
            }
            tempArray[newSize- 1] = newItem;
            testArray = tempArray;
            for (String element:testArray ) {
                System.out.println( element );
            }
        }
        catch(Exception ex)
        {

        }



        try {
            File imgFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test1_3.jpg");
            Bitmap bm1 = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()), 360, 216, true);
            ImageView imCredit3 = (ImageView) findViewById(R.id.imCredit3);
            imCredit3.setImageBitmap(bm1);
            imCredit3.setVisibility(View.VISIBLE);

            String newItem = "/test1_3.jpg";
            int currentSize = testArray.length;
            int newSize = currentSize + 1;
            String[] tempArray = new String[ newSize ];
            for (int i=0; i < currentSize; i++)
            {
                tempArray[i] = testArray [i];
            }
            tempArray[newSize- 1] = newItem;
            testArray = tempArray;
            for (String element:testArray ) {
                System.out.println( element );
            }
        }
        catch(Exception ex)
        {

        }



            try {
        File imgFile2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test2_0.jpg");
        Bitmap bm2 = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFile2.getAbsolutePath()), 360, 216, true);
        imLoyalty.setImageBitmap(bm2);
        imLoyalty.setVisibility(View.VISIBLE);

                String newItem = "/test2_0.jpg";
                int currentSize = testArray.length;
                int newSize = currentSize + 1;
                String[] tempArray = new String[ newSize ];
                for (int i=0; i < currentSize; i++)
                {
                    tempArray[i] = testArray [i];
                }
                tempArray[newSize- 1] = newItem;
                testArray = tempArray;
                for (String element:testArray ) {
                    System.out.println( element );
                }

            }
            catch(Exception ex)
            {

            }

        try {
            File imgFile2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test2_2.jpg");
            Bitmap bm2 = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFile2.getAbsolutePath()), 360, 216, true);
            ImageView imLoyalty2 = (ImageView) findViewById(R.id.imLoyalty2);
            imLoyalty2.setImageBitmap(bm2);
            imLoyalty2.setVisibility(View.VISIBLE);

            String newItem = "/test2_2.jpg";
            int currentSize = testArray.length;
            int newSize = currentSize + 1;
            String[] tempArray = new String[ newSize ];
            for (int i=0; i < currentSize; i++)
            {
                tempArray[i] = testArray [i];
            }
            tempArray[newSize- 1] = newItem;
            testArray = tempArray;
            for (String element:testArray ) {
                System.out.println( element );
            }

        }
        catch(Exception ex)
        {

        }


        try {
            File imgFile2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test2_3.jpg");
            Bitmap bm2 = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFile2.getAbsolutePath()), 360, 216, true);
            ImageView imLoyalty3 = (ImageView) findViewById(R.id.imLoyalty3);
            imLoyalty3.setImageBitmap(bm2);
            imLoyalty3.setVisibility(View.VISIBLE);

            String newItem = "/test2_3.jpg";
            int currentSize = testArray.length;
            int newSize = currentSize + 1;
            String[] tempArray = new String[ newSize ];
            for (int i=0; i < currentSize; i++)
            {
                tempArray[i] = testArray [i];
            }
            tempArray[newSize- 1] = newItem;
            testArray = tempArray;
            for (String element:testArray ) {
                System.out.println( element );
            }

        }
        catch(Exception ex)
        {

        }





                try {
        File imgFile3 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test3.jpg");
        Bitmap bm3 = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFile3.getAbsolutePath()), 360, 216, true);
        imRoadTax.setImageBitmap(bm3);
        imRoadTax.setVisibility(View.VISIBLE);

                    String newItem = "/test3.jpg";
                    int currentSize = testArray.length;
                    int newSize = currentSize + 1;
                    String[] tempArray = new String[ newSize ];
                    for (int i=0; i < currentSize; i++)
                    {
                        tempArray[i] = testArray [i];
                    }
                    tempArray[newSize- 1] = newItem;
                    testArray = tempArray;
                    for (String element:testArray ) {
                        System.out.println( element );
                    }

                }
                catch(Exception ex)
                {

                }


                    try {
        File imgFile4 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test4.jpg");
        Bitmap bm4 = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFile4.getAbsolutePath()), 360, 216, true);
        imAuthorize.setImageBitmap(bm4);
        imAuthorize.setVisibility(View.VISIBLE);

                        String newItem = "/test4.jpg";
                        int currentSize = testArray.length;
                        int newSize = currentSize + 1;
                        String[] tempArray = new String[ newSize ];
                        for (int i=0; i < currentSize; i++)
                        {
                            tempArray[i] = testArray [i];
                        }
                        tempArray[newSize- 1] = newItem;
                        testArray = tempArray;
                        for (String element:testArray ) {
                            System.out.println( element );
                        }
                    }
                    catch(Exception ex)
                    {

                    }


    }

    public void registerUser()
    {
        LinearLayout disclaimer = (LinearLayout) findViewById(R.id.disclaimer);
        disclaimer.setVisibility(View.GONE);

        LinearLayout thankyou = (LinearLayout) findViewById(R.id.thankyou);
        thankyou.setVisibility(View.VISIBLE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, insertSubsidy,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w(TAG, "response: " + response);

                        if(response.toString().contains("Success")) {
                            //progress.dismiss();
                            Toast.makeText(preview.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                        }
                        if(response.toString().equals("1062"))
                        {
                           // progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Duplicate ID", Toast.LENGTH_LONG).show();
                        }
                        if(response.toString().equals("1292"))
                        {
                            //progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Insert Error", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error instanceof TimeoutError)
                        {
                            //DeleteFP();
                            Toast.makeText(getApplicationContext(), "Timeout error, please try again.", Toast.LENGTH_LONG).show();
                            // error.printStackTrace();
                        }
                        if (error instanceof NetworkError) {
                           // progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show();
                            //Log.d(TAG, "Cannot connect to Internet...Please check your connection!");
                        }
                        //System.out.println(error);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){

                //Converting Bitmap to String
                //String image = getStringImage(mphoto);

                Map<String,String> params = new HashMap<String, String>();

                params.put("mykadNumber", mykadNumber);
                params.put("firstName", firstName);

                params.put("address", address);
                params.put("gender", gender);
                params.put("phoneNumber", phoneNumber);
                params.put("bankName", bankName);
                params.put("bankAccount", bankAccount);

                params.put("cardName", cardName);
                params.put("cardNumber", cardNumber);

              //  params.put("serialNumber", serialNumber);
              //  params.put("carPlateNumber", carPlateNumber);

                params.put("vehicleNumber", vehicleNumber);
                params.put("vehicleOwner", vehicleOwner);
                params.put("vehicleOwnerMykad", vehicleOwnerMykad);
                params.put("vehicleRelationship", vehicleRelationship);
                params.put("licenseNumber", licenseNumber);
                params.put("licenseExpiry", licenseExpiry);
                params.put("licenseClass", licenseClass);
                return params;
            }
        };

        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(7000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void ViewFile()
    {
        System.out.println(personalinfo);

        try {
                String[] separated = (personalinfo).split("\\;");

            //personal info
             mykadNumber = separated[0];
             firstName = separated[1];
             gender = separated[2];
             address  = separated[3];
             phoneNumber = separated[4];

            //bank info
             bankName = separated[5];
             bankAccount  = separated[6];

            //credit info
            ccName = separated[7];
            ccNumber = separated[8];

            //loyalty info
             cardName = separated[9];
             cardNumber = separated[10];

            //roadtax info
            vehicleNumber = separated[11];
             vehicleOwner = separated[12];
             vehicleOwnerMykad = separated[13];
             vehicleRelationship = separated[14];
             licenseNumber = separated[15];
             licenseExpiry = separated[16];
             licenseClass = separated[17];

            disMykad.setText(mykadNumber);
                    disName.setText(firstName);
            disGender.setText(gender);
                    disAddress.setText(address);
            disPhone.setText(phoneNumber);
                    disBankName.setText(bankName);
            disAccount.setText(bankAccount);
            disCCName.setText(ccName);
            disCCNumber.setText(ccNumber);
                    disLoyaltyName.setText(cardName);
            disLoyaltyNumber.setText(cardNumber);
                    disVehicleNumber.setText(vehicleNumber);
            disVehicleOwner.setText(vehicleOwner);
                    disOwnerMykad.setText(vehicleOwnerMykad);
            disRelationshipOwner.setText(vehicleRelationship);
                    disLicenseNo.setText(licenseNumber);
            disLicenseExp.setText(licenseExpiry);
                    disLicenseClass.setText(licenseClass);

            personalInfo1.setText("MyKad Number:\n" +
                                "First Name:\n" +
                                "Gender:\n" +
                                "Address:\n" +

                                "Phone Number:\n\n" +
                                "BANK INFO:\n" +
                                "Bank Name:\n" +
                                "Bank Account:\n\n" +

                            "CREDIT CARD:\n" +
                            "CREDIT CARD Name:\n" +
                            "CREDIT CARD Number:\n\n" +

                            "LOYALTY CARD:\n" +
                            "Loyalty Name:\n" +
                            "Loyalty Number:\n\n" +
                            "VEHICLE INFO:\n" +
                            "Vehicle No:\n" +
                    "Vehicle Owner:\n" +
                    "Owner Mykad:\n" +
                    "Relationship:\n" +
                    "License Number:\n" +
                    "License Expiry:\n" +
                    "License Class:" );

            personalInfo2.setText(mykadNumber + "\n" +  firstName + "\n" + gender + "\n" +
                    address + "\n" +
                    phoneNumber + "\n\n\n" +
                    bankName + "\n" +
                    bankAccount + "\n\n\n" +
                    ccName + "\n" +
                    ccNumber + "\n\n\n" +
                    cardName + "\n" +
                    cardNumber + "\n\n\n" +
                    vehicleNumber+"\n" +
                    vehicleOwner+"\n" +
                    vehicleOwnerMykad+"\n" +
                    vehicleRelationship+"\n" +
                    licenseNumber+"\n" +
                    licenseExpiry+"\n" +
                    licenseClass

            );

        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Error"+ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void onAttachDocSend()
    {
        //  progress = ProgressDialog.show(Display.this, "Waiting", "Sending", true);
        try {
            No = 0;

           // String[] test = {"/mykad.jpg","/test1_0.jpg","/test2_0.jpg","/test3.jpg","/test4.jpg"};
            Toast.makeText(getApplicationContext(), "Array length:" +testArray.length, Toast.LENGTH_LONG).show();
            for (int i = 0; i <= testArray.length; i++) {
                //Toast.makeText(getApplicationContext(), "Error" +separated[i], Toast.LENGTH_LONG).show();
                upload(Environment.getExternalStorageDirectory().getAbsolutePath()+testArray[i]);
                No++;
            }

        }
        catch (Exception ex)
        {
            //Toast.makeText(getApplicationContext(), "Error" + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void upload(String path) {
       // No++;
        System.out.println("Path : " + path);
        Bitmap bm = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, bao);
        byte[] ba = bao.toByteArray();
        ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
        // Upload image to server
        sendAttachDoc();
    }

    public void sendAttachDoc() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SuppdocURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("Image uploaded")) {
                       //  Toast.makeText(getApplicationContext(), "Successfully uploaded "+No+" attach document", Toast.LENGTH_LONG).show();
                        }
                       //progress.dismiss();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError) {
                            System.out.println("Error : " + error.getMessage());
                            // Log.d(TAG, error.getMessage());
                        }
                        if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("base64", ba1);
                params.put("dir", mykadNumber);
                params.put("ImageName", mykadNumber+"/" +mykadNumber+"_"+ No +".jpg");

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
