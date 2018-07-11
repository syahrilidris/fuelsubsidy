/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package info.fuelsubsidy.loginandregistration.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellego.livenesstest.FaceLivenessActivity;
import com.intellego.ocr.MyKadOcrActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.fuelsubsidy.loginandregistration.R;



public class mykad extends Activity {

    private static final String BASE_URL = " https://ekyc.inetgo.net/fr-web/api/";
    private static AsyncHttpClient client = new AsyncHttpClient();
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }
    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    private static final String TAG = mykad.class.getSimpleName();

    private static final int PERMISSION_REQUEST_CODE = 200;


    private static final String ServerAddress = "https://ekyc.inetgo.net/fr-web/api";

    //https://ekyc.inetgo.net/fr-web/api/compareFace

    //personal info
    private Spinner spinnerBank;
    // private Spinner spinnerGender;

    private String connectstate;
    String connection2;
    private EditText inputmykadNumber;
    private EditText inputfirstName;
    private EditText inputlastName;
    private EditText inputgender;
    private EditText inputaddress;
    private EditText inputphoneNumber;
    //bank info
    private EditText inputbankName;
    private EditText inputbankAccount;

    private ProgressDialog progress;

    public String camera;
    private ImageView mykadImage;
    private Bitmap bitmap;
    private Bitmap bitmap2;
    private Bitmap bitmapmykad;

    Bitmap liveFace;
    float f;
    private Bitmap liveFace2;

    private String PersonID;
    private ProgressDialog pDialog;
    private Button btnNext;
    private Button btnSelfie;

    public static final int OCR_REQUEST_CODE = 0x101;
    String repl;

    private final String boundary =  "*****";
    private final String crlf = "\r\n";
    private final String twoHyphens = "--";
    private DataOutputStream dataOutputStream;
    private HttpURLConnection connection;
    String response;

    Boolean facestatus = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mykad);


        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }


        btnSelfie= (Button) findViewById(R.id.btnSelfie);
        btnNext = (Button) findViewById(R.id.btnNext);

        spinnerBank = (Spinner) findViewById(R.id.spinnerBank);
        //spinnerGender = (Spinner) findViewById(R.id.spinnerGender);

        inputmykadNumber =  (EditText) findViewById(R.id.mykadNumber);
        inputfirstName = (EditText) findViewById(R.id.firstName);
        inputgender= (EditText) findViewById(R.id.gender);
        // inputgender= (EditText) findViewById(R.id.gender);
        inputaddress= (EditText) findViewById(R.id.address);
        inputphoneNumber= (EditText) findViewById(R.id.phoneNumber);

        inputbankName= (EditText) findViewById(R.id.bankName);
        inputbankAccount= (EditText) findViewById(R.id.bankAccount);

        mykadImage = (ImageView) findViewById(R.id.imageView);



        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        try {
            File imgFilemykad = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/mykad.jpg");
            File imgFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test1_0.jpg");
            File imgFile2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test2_0.jpg");
            File imgFile3 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test3.jpg");
            File imgFile4 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test4.jpg");
            File imgFile5 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/livephoto.jpg");



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


            if (imgFile5.exists()) {
                String deleteCmd = "rm -r " + imgFile5;
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

        // Register Button Click event
        btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String mykadNumber = inputmykadNumber.getText().toString().trim();
                String firstName = inputfirstName.getText().toString().trim();
                //String lastName = inputlastName.getText().toString().trim();

                String gender  = inputgender.getText().toString().trim();
                String address = inputaddress.getText().toString().trim();
                String phoneNumber = inputphoneNumber.getText().toString().trim();
                String bankName = inputbankName.getText().toString().trim();
                String bankAccount  = inputbankAccount.getText().toString().trim();

                try {

                }
                catch (Exception ex)
                {

                }

                if (!mykadNumber.isEmpty() &&!firstName.isEmpty() && !gender.isEmpty() && !address.isEmpty()&& !phoneNumber.isEmpty()&& !bankAccount.isEmpty()) {


                    Intent i = new Intent(getApplicationContext(), creditcard.class);
                    i.putExtra("personalinfo", mykadNumber+";"+firstName+";"+gender+";"+address+";"+phoneNumber+";"+spinnerBank.getSelectedItem().toString()+";"+bankAccount);

                    startActivity(i);
                    //finish();


                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });



    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void savemykad()
    {
        File file = new File("/storage/emulated/0/mykad.jpg");// the File to save , append increasing numeric counter to prevent files from getting overwritten.
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            //  Toast.makeText(getApplicationContext(), "Saved to your folder", Toast.LENGTH_SHORT ).show();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Missing Mykad Image", Toast.LENGTH_LONG).show();
        }
    }

    public void saveLivephoto()
    {
        File file = new File("/storage/emulated/0/livephoto.jpg");// the File to save , append increasing numeric counter to prevent files from getting overwritten.
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            liveFace.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            //  Toast.makeText(getApplicationContext(), "Saved to your folder", Toast.LENGTH_SHORT ).show();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "Missing Mykad Image", Toast.LENGTH_LONG)
                    .show();
        }
    }




    public void imageClick(View view) { //photo
        camera = "1";
        startCamera();
    }

    public void ScanMyCard(View view)
    {
        btnSelfie.setText("Verify Your Selfie");
        ImageView tick = (ImageView) findViewById(R.id.imtick);
        tick.setVisibility(View.GONE);
        try{
        File imgFile5 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/livephoto.jpg");

        if (imgFile5.exists()) {
            String deleteCmd = "rm -r " + imgFile5;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
            }
        }}
        catch (Exception ex)
        {

        }

      //  btnSelfie.setTextColor(Color.WHITE);
        Intent intent = new Intent(this, MyKadOcrActivity.class);
        // Use "X73KG-N3W8F-4WKXP-RJ76X-T8Y9B" for demo
        intent.putExtra("OCR_LICENSE_KEY", "X73KG-N3W8F-4WKXP-RJ76X-T8Y9B");
        startActivityForResult(intent,  OCR_REQUEST_CODE);
    }

    public void FaceLive(View view) {
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setText("");
            Intent intent = new Intent(this, FaceLivenessActivity.class);

            // Use "X73KG-N3W8F-4WKXP-RJ76X-T8Y9B" for demo
            intent.putExtra("LIVENESS_CHECK_LICENSE_KEY", "X73KG-N3W8F-4WKXP-RJ76X-T8Y9B");
            startActivityForResult(intent, 0);
      //  }

    }

    private void startCamera()
    {
        File file;
        Uri fileUri;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File("/storage/emulated/0/test.jpg");
        fileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        this.startActivityForResult(intent, 1888);
    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                liveFace = (Bitmap) data.getParcelableExtra("LIVENESS_IMAGE_DATA");
                liveFace2 = (Bitmap) data.getParcelableExtra("LIVENESS_IMAGE_DATA");
                //Toast.makeText(getApplicationContext(), "Live Face VERIFIED", Toast.LENGTH_SHORT ).show();
                saveLivephoto();

                btnSelfie.setText("Check with Mykad");

                sendPost();



            } else {
                // Handling errors. Eg: License key invalid/expired error.
                if (data != null  && !TextUtils.isEmpty(data.getStringExtra("LICENSE_KEY_ERROR"))) {

                    Toast.makeText(getApplicationContext(), "Invalid License"+data.getStringExtra("LICENSE_KEY_ERROR"), Toast.LENGTH_SHORT ).show();
                    //AlertError.showDialog(this, "Invalid License", data.getStringExtra("LICENSE_KEY_ERROR"));
                } else {
                    //  AlertError.showErrorMessage(resultCode, this);
                    Toast.makeText(getApplicationContext(), "Result: "+resultCode, Toast.LENGTH_SHORT ).show();

                }
            }
        }

        if (requestCode == OCR_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK && data != null) {
                // perform processing of the data here
                String icNumber = data.getStringExtra("IC_NUMBER").replace("-", "");
                // String icNumberBack = data.getStringExtra("IC_NUMBER_BACK").replace("-", "");
                String name = data.getStringExtra("NAME");
                String gender = data.getStringExtra("GENDER");
                String address = data.getStringExtra("ADDRESS");
                bitmap = (Bitmap) data.getParcelableExtra("MYKADIMAGE");
                bitmap2 = (Bitmap) data.getParcelableExtra("MYKADIMAGE");
                bitmapmykad = (Bitmap) data.getParcelableExtra("MYKADIMAGE");

                inputmykadNumber.setText(icNumber);
                inputfirstName.setText(name);
                inputgender.setText(gender);
                inputaddress.setText(address);
                mykadImage.setImageBitmap(bitmap);
                mykadImage.setVisibility(View.VISIBLE);

                savemykad();
                TextView textView2 = (TextView) findViewById(R.id.textView2);
                textView2.setText("");
                btnSelfie.setVisibility(View.VISIBLE);

            }
        }

/*
        switch(requestCode) {
            case 1888:
                if (requestCode == 1888 && resultCode == RESULT_OK) {
                    if (mphoto != null) {
                        mphoto.recycle();
                    }
                    File imgFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test.jpg");
                    Matrix matrix = new Matrix();
                    boolean swapWidthHeight = false;
                    try {
                        ExifInterface exif = new ExifInterface(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test.jpg");
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,1);
                        switch (orientation)
                        {
                            case 3: //180
                                matrix.postRotate(180);
                                break;
                            case 6://90
                                matrix.postRotate(90);
                                swapWidthHeight = true;
                                break;
                            case 8://270
                                matrix.postRotate(270);
                                swapWidthHeight = true;
                                break;
                        }
                    }
                    catch (Exception ex)
                    {
                    }
                    if(imgFile.exists()){
                        ImageView imageCapture = (ImageView) findViewById(R.id.imageView);
                        imageCapture.setVisibility(View.VISIBLE);
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        mphoto = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        Bitmap rotatedBitmap = Bitmap.createBitmap(mphoto,0,0,mphoto.getWidth(),mphoto.getHeight(),matrix,true);
                        int newWidth=0, newHeight=0;
                        if (mphoto.getWidth() >= mphoto.getHeight()) {
                            newHeight = 500;
                            newWidth = mphoto.getWidth() * newHeight / mphoto.getHeight();
                        } else {
                            newWidth = 500;
                            newHeight = mphoto.getHeight() * newWidth / mphoto.getWidth();
                        }
                        if (swapWidthHeight) {
                            int temp = newWidth;
                            newWidth = newHeight;
                            newHeight = temp;
                        }
                        Bitmap actualimage = Bitmap.createScaledBitmap(rotatedBitmap, 460, 345, false);
                        mphoto = Bitmap.createScaledBitmap(rotatedBitmap, 460, 345, false);
                       // actualimage = Bitmap.createScaledBitmap(rotatedBitmap, newWidth, newHeight, false);
                        mykadImage.setImageBitmap(actualimage);
                        mykadImage.setVisibility(View.VISIBLE);

                    }
                    break;
                }
            default:
            {
            }
        }*/
    }


    public class SearchingTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            isConnectedToInternet();
            //progress = ProgressDialog.show(MainActivity.this, "Waiting", "Sending", true);
        }

        @Override
        protected String doInBackground(String... params)
        {
            return compare();
        }

        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(mykad.this, result, Toast.LENGTH_LONG).show();

            if (result != null) {
                System.out.println("repl : " + repl);
                if (result.isEmpty())
                {
                    Toast.makeText(mykad.this, "Face 1 : Registration failed", Toast.LENGTH_LONG).show();
                }

                else
                {
                    System.out.println("repl : " + repl);
                    Float test = Float.parseFloat(repl);
                    //Float test = Float.parseFloat(repl);
                    if(test > 0.80)
                    {
                        Toast.makeText(mykad.this, "Face Matches", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(mykad.this, "Face Does Not Match", Toast.LENGTH_LONG).show();
                    }
                }
            }

            else
            {
                if(connectstate.contains("false"))
                {
                    //progress.dismiss();
                    Toast.makeText(mykad.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
                else {
                    //progress.dismiss();
                    Toast.makeText(mykad.this, "Request Failure", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------Checking Internet Connection---------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public void sendPost() {
        //progress = ProgressDialog.show(mykad.this, "Waiting", "Checking Face", true);
        final File facePhoto = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/mykad.jpg");
        final File facePhoto2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/livephoto.jpg");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://ekyc.inetgo.net/fr-web/api/compareFace");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);

                    dataOutputStream = new DataOutputStream(connection.getOutputStream());
                    addMultipart("facePhoto", facePhoto);
                    addMultipart("facePhoto2", facePhoto2);

                    finishRequest();

                    //Toast.makeText(getApplicationContext(),"Status :"+facestatus.toString() , Toast.LENGTH_LONG).show();

                   //TextView textView2 = (TextView) findViewById(R.id.textView2);
                    //textView2.setText(facestatus.toString());

                    if (facestatus.toString().contains("true"))
                    {
                        //Toast.makeText(getApplicationContext(),"Match with Mykad" , Toast.LENGTH_LONG).show();
                        btnSelfie.setText("Verify Your Selfie");
                        TextView textView2 = (TextView) findViewById(R.id.textView2);
                        textView2.setText("Match :"+ response.substring(response.indexOf(":") + 1, response.indexOf("}")));
                        textView2.setTextColor(Color.GREEN);
                        // btnSelfie.setTextColor(Color.WHITE);
                    }
                    else
                    {
                        //Toast.makeText(getApplicationContext(),"Does Not Match with Mykad" , Toast.LENGTH_LONG).show();
                        btnSelfie.setText("Verify Your Selfie");
                        //ImageView tick = (ImageView) findViewById(R.id.imtick);
                        //tick.setVisibility(View.GONE);
                        TextView textView2 = (TextView) findViewById(R.id.textView2);
                        textView2.setTextColor(Color.RED);

                        textView2.setText("Does Not Match :" + response.substring(response.indexOf(":") + 1, response.indexOf("}")));

                        // btnSelfie.setTextColor(Color.WHITE);
                    }






                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void addMultipart(String fieldName, File uploadFile) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + crlf);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                fieldName + "\"; filename=\"" + uploadFile.getName() +"\"" + crlf);
        dataOutputStream.writeBytes("Content-Type: image/jpeg" + crlf);
        dataOutputStream.writeBytes(crlf);

        InputStream fileInputStream = new FileInputStream(uploadFile);
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        bytesAvailable = fileInputStream.available();
        bufferSize = Math.min(bytesAvailable, maxBufferSize);
        buffer = new byte[bufferSize];

        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        while(bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(crlf);
    }

    public String finishRequest() throws IOException {
        response = "";

        dataOutputStream.writeBytes(this.crlf);
        dataOutputStream.writeBytes(this.twoHyphens + this.boundary +
                this.twoHyphens + this.crlf);

        dataOutputStream.flush();
        dataOutputStream.close();

        int status = connection.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            InputStream responseStream = new BufferedInputStream(connection.getInputStream());
            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            response = stringBuilder.toString();
            System.out.println("RESPONSE: "+response);
            f = Float.parseFloat(response.substring(response.indexOf(":") + 1, response.indexOf("}")));

            //Toast.makeText(getApplicationContext(),"Match Score:"+ response.substring(response.indexOf(":") + 1, response.indexOf("}")) , Toast.LENGTH_LONG).show();
            if (f>=80)
            {
               facestatus=true;
              //  TextView textView2 = (TextView) findViewById(R.id.textView2);
               // textView2.setText("Face Match With MyKad:"+ response.substring(response.indexOf(":") + 1, response.indexOf("}")));
              //  textView2.setTextColor(Color.GREEN);

                // btnSelfie.setTextColor(Color.WHITE);
            }
            else
            {
               facestatus=false;
                //TextView textView2 = (TextView) findViewById(R.id.textView2);
                //textView2.setTextColor(Color.RED);
                //textView2.setText("Face Does Not Match With MyKad:" + response.substring(response.indexOf(":") + 1, response.indexOf("}")));

            }


            connection.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }
        return response;
    }





    protected String compare() {
        Map<String, String> params = new HashMap<String, String>();
        // params.put("app-id", "test");
        // params.put("app-key", "test");
        String test = toBase64Image(liveFace2);
        String test2 = toBase64Image(bitmapmykad);
        params.put("facePhoto",  Environment.getExternalStorageDirectory().getAbsolutePath()+"/mykad.jpg");
        params.put("facePhoto2", Environment.getExternalStorageDirectory().getAbsolutePath()+"/livephoto.jpg");

        try {
            byte[] resp = doPost("/compareFace", params);
            System.out.println("Response :" + new String(resp, "UTF-8"));

            Pattern p = Pattern.compile("[0-9]*\\.[0-9]*");
            Matcher m = p.matcher(new String(resp,"UTF-8"));

            if (m.find()) {

                repl = m.group(0).toString();
                System.out.println("Result : " + repl);
            }

            Toast.makeText(getApplicationContext(), "Similarity: "+repl, Toast.LENGTH_LONG).show();
            return repl;

        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
            return null;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    protected String toBase64Image(Bitmap image) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] byteArray = bos.toByteArray();
        return "data:image/jpeg;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public void isConnectedToInternet(){
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        connectstate = String.valueOf(isConnected);
        if(connectstate.contains("false"))
        {
            //Toast.makeText(getApplicationContext(), "No Connection", Toast.LENGTH_LONG).show();
            connection2 = "No Connection";
        }
        else
        {
            //Toast.makeText(getApplicationContext(), "Good Connection", Toast.LENGTH_LONG).show();
            connection2 = "Good Connection";
        }
    }

    static public String getServerUrlBase() {
        //intellego URL
        return "https://ekyc.inetgo.net/fr-web/api";
    }



    protected byte[] doPost(String path, Map<String, String> parameters) throws Exception {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        String serverBase = getServerUrlBase();
        if (serverBase == null) {
            throw new Exception("Server's URL base is not set!");
        }

        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(serverBase + path);
            urlConnection = (HttpURLConnection) url.openConnection();
            if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
                urlConnection.setRequestProperty("Connection", "close");
            }

            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "image/jpeg");
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            String postData = createQueryString(parameters);
            urlConnection.setFixedLengthStreamingMode(postData.getBytes().length);
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(postData);
            out.close();
            int statusCode = urlConnection.getResponseCode();

            if (statusCode != HttpURLConnection.HTTP_OK) {
                if (statusCode == 500) {
                    try {
                        byte[] data = getResponseData(urlConnection);
                        ObjectMapper objectMapper = new ObjectMapper();
                        ServerException exp = objectMapper.readValue(data, ServerException.class);
                        Log.w(TAG, "Fail to search: " + exp.getMessage());
                    } catch (IOException e) {
                        Log.w(TAG, "Fail to search: " + statusCode);
                    }
                }
                throw new Exception("Access failure");
            }
            return getResponseData(urlConnection);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Bad URL [" + serverBase + path + "]: " + e.getMessage(), e);
            throw new Exception("Bad URL: " + serverBase + path);
        } catch (IOException e) {
            Log.e(TAG, "Access remote service error: " + e.getMessage(), e);
            throw new Exception("Access remote service error: " + e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    public static class ServerException extends Throwable {
        private static final long serialVersionUID = 4017805196493169996L;

        private int code;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    private byte[] getResponseData(HttpURLConnection conn) throws IOException {
        InputStream inputStream = new BufferedInputStream(conn.getInputStream());
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[16384];
        int n;
        while ((n = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, n);
        }
        buffer.flush();
        return buffer.toByteArray();
    }


    private static final char PARAMETER_DELIMITER = '&';
    private static final char PARAMETER_EQUALS_CHAR = '=';

    private String createQueryString(Map<String, String> parameters) {
        StringBuilder queryString = new StringBuilder();
        if (parameters != null) {
            boolean first = true;
            for (String name : parameters.keySet()) {
                if (!first) {
                    queryString.append(PARAMETER_DELIMITER);
                }
                try {
                    queryString.append(name).append(PARAMETER_EQUALS_CHAR).append(URLEncoder.encode(parameters.get(name), "UTF-8"));
                    if (first) {
                        first = false;
                    }
                } catch (UnsupportedEncodingException e) {
                    Log.w(TAG, "Encode http request param [" + name + "] error: " + e.getMessage());
                }
            }
        }
        return queryString.toString();
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
