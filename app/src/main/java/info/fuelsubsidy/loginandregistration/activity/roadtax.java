/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package info.fuelsubsidy.loginandregistration.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import info.fuelsubsidy.loginandregistration.R;
import info.fuelsubsidy.loginandregistration.helper.SQLiteHandler;
import info.fuelsubsidy.loginandregistration.helper.SessionManager;

public class roadtax extends Activity {
    private static final String TAG = roadtax.class.getSimpleName();
    private Button btnNext;
    private EditText inputroadSerialNumber;
    private EditText inputcarPlateNumber;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    String personalinfo;
    Bitmap bmpmykad;
    Bitmap bmployalty;

    private EditText vehicleNumber;
    private EditText vehicleOwner;
    private EditText vehicleOwnerMykad;
    private EditText vehicleRelationship;

    private EditText licenseNumber;
    private EditText licenseExpiry;
    private EditText licenseClass;

    public String camera;
    private ImageView roadTaxImage;
    private ImageView authorizeImage;
    private Bitmap  mphoto;

    String mykadnumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roadtax);

        btnNext = (Button) findViewById(R.id.btnNext);

       // inputroadSerialNumber = (EditText) findViewById(R.id.roadSerialNumber);
        vehicleNumber = (EditText) findViewById(R.id.vehicleNumber);

        roadTaxImage = (ImageView) findViewById(R.id.imageView3);
        authorizeImage = (ImageView) findViewById(R.id.imageView5);

        vehicleNumber = (EditText) findViewById(R.id.vehicleNumber);
        vehicleOwner = (EditText) findViewById(R.id.vehicleOwner);
        vehicleOwnerMykad = (EditText) findViewById(R.id.vehicleOwnerMykad);
        vehicleRelationship = (EditText) findViewById(R.id.vehicleRelationship);
        licenseNumber = (EditText) findViewById(R.id.licenseNumber);
        licenseExpiry = (EditText) findViewById(R.id.licenseExpiry);
        licenseClass = (EditText) findViewById(R.id.licenseClass);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        personalinfo = getIntent().getExtras().getString("personalinfo");

     /* Bundle extras = getIntent().getExtras();
        bmpmykad = (Bitmap) extras.getParcelable("Bitmap");
        bmployalty = (Bitmap) extras.getParcelable("Bitmap2");*/

        // Register Button Click event
        btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (!vehicleNumber.getText().toString().isEmpty() && !licenseNumber.getText().toString().isEmpty() ) {
                    Intent i = new Intent(getApplicationContext(),
                            preview.class);
                    i.putExtra("personalinfo", personalinfo +";"+vehicleNumber.getText().toString().trim()+";"+vehicleOwner.getText().toString().trim()
                            +";"+vehicleOwnerMykad.getText().toString().trim()+";"+vehicleRelationship.getText().toString().trim()+";"+licenseNumber.getText().toString().trim()+";"+licenseExpiry.getText().toString().trim()
                            +";"+licenseClass.getText().toString().trim()


                    );





                    /*Bundle extras = new Bundle();
                    extras.putParcelable("Bitmap", bmpmykad);
                    extras.putParcelable("Bitmap2", bmployalty);
                    i.putExtras(extras);*/
                    startActivity(i);
                    //finish();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });



       /* vehicleOwnerMykad.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus)
                {
                    ViewFile();
                    if (vehicleOwnerMykad.getText().toString().trim()==  mykadnumber.trim())
                    {
                        //button2 hide
                        Button button2 = (Button) findViewById(R.id.button2);
                        button2.setVisibility(View.GONE);
                        authorizeImage.setVisibility(View.GONE);
                    }
                    //authorizeImage

                }
                // TODO: the editText has just been left
            }
        });*/

        CheckBox repeatChkBx = ( CheckBox ) findViewById( R.id.checkBox2 );
        repeatChkBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    //button2 hide
                    Button button2 = (Button) findViewById(R.id.button2);
                    button2.setVisibility(View.GONE);
                    authorizeImage.setVisibility(View.GONE);

                    TextView textview19 = (TextView)findViewById(R.id.textView19);
                    textview19.setVisibility(View.GONE);
                    vehicleOwner.setVisibility(View.GONE);

                    TextView textview20 = (TextView)findViewById(R.id.textView20);
                    textview20.setVisibility(View.GONE);
                    vehicleOwnerMykad.setVisibility(View.GONE);

                    TextView textview22 = (TextView)findViewById(R.id.textView22);
                    textview22.setVisibility(View.GONE);
                    vehicleRelationship.setVisibility(View.GONE);

                }
                else
                {
                    Button button2 = (Button) findViewById(R.id.button2);
                    button2.setVisibility(View.VISIBLE);
                    //authorizeImage.setVisibility(View.VISIBLE);

                    TextView textview19 = (TextView)findViewById(R.id.textView19);
                    textview19.setVisibility(View.VISIBLE);
                    vehicleOwner.setVisibility(View.VISIBLE);

                    TextView textview20 = (TextView)findViewById(R.id.textView20);
                    textview20.setVisibility(View.VISIBLE);
                    vehicleOwnerMykad.setVisibility(View.VISIBLE);

                    TextView textview22 = (TextView)findViewById(R.id.textView22);
                    textview22.setVisibility(View.VISIBLE);
                    vehicleRelationship.setVisibility(View.VISIBLE);
                }

            }
        });


    }



    public void ViewFile()
    {


        try {
            String[] separated = (personalinfo).split("\\;");
            //personal info
            mykadnumber = separated[0];

        }
        catch (Exception ex)
        {
//            Toast.makeText(getApplicationContext(), "Error"+ex.toString(), Toast.LENGTH_LONG).show();
        }
    }


    public void imageClick(View view) { //photo
        camera = "1";
        startCamera();
    }

    public void imageClick2(View view) { //photo
        camera = "2";
        startCamera2();
    }


    private void startCamera()
    {
        File file;
        Uri fileUri;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File("/storage/emulated/0/test3.jpg");
        fileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        this.startActivityForResult(intent, 1888);
    }


    private void startCamera2()
    {
        File file;
        Uri fileUri;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File("/storage/emulated/0/test4.jpg");
        fileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        this.startActivityForResult(intent, 1889);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {

            case 1888:
                if (requestCode == 1888 && resultCode == RESULT_OK) {

                    if (mphoto != null) {
                        mphoto.recycle();
                    }

                    File imgFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test3.jpg");
                    Matrix matrix = new Matrix();
                    boolean swapWidthHeight = false;

                    try {

                        ExifInterface exif = new ExifInterface(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test3.jpg");
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

                        ImageView imageCapture = (ImageView) findViewById(R.id.imageView3);
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

                        roadTaxImage.setImageBitmap(actualimage);
                        roadTaxImage.setVisibility(View.VISIBLE);



                    }
                    break;
                }


            case 1889:
                if (requestCode == 1889 && resultCode == RESULT_OK) {

                    if (mphoto != null) {
                        mphoto.recycle();
                    }

                    File imgFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test4.jpg");
                    Matrix matrix = new Matrix();
                    boolean swapWidthHeight = false;

                    try {

                        ExifInterface exif = new ExifInterface(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test4.jpg");
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

                        ImageView imageCapture = (ImageView) findViewById(R.id.imageView3);
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

                        authorizeImage.setImageBitmap(actualimage);
                        authorizeImage.setVisibility(View.VISIBLE);



                    }
                    break;
                }

            default:
            {

            }
        }


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
