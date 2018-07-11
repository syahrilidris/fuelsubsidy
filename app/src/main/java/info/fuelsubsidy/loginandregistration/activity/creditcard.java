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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;

import info.fuelsubsidy.loginandregistration.R;
import info.fuelsubsidy.loginandregistration.helper.SQLiteHandler;
import info.fuelsubsidy.loginandregistration.helper.SessionManager;

public class creditcard extends Activity {
    private static final String TAG = creditcard.class.getSimpleName();

    private Spinner spinnerLoyalty;
    private Button btnNext;
    private EditText inputcardName;
    private EditText inputcardNumber;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    String personalinfo;

    public String camera;
    private ImageView loyaltyImage;
    private Bitmap  mphoto;
    Bitmap bmpmykad;
    Bitmap bitmap;
    int No=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creditcard);

        btnNext = (Button) findViewById(R.id.btnNext);
        spinnerLoyalty = (Spinner) findViewById(R.id.spinnerLoyalty);
        inputcardName = (EditText) findViewById(R.id.cardName);
        inputcardNumber = (EditText) findViewById(R.id.cardNumber);

        loyaltyImage = (ImageView) findViewById(R.id.im1);


      //  Bundle extras = getIntent().getExtras();
       //  bmpmykad = (Bitmap) extras.getParcelable("Bitmap");

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        personalinfo = getIntent().getExtras().getString("personalinfo");



        // Register Button Click event
        btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String cardName = inputcardName.getText().toString().trim();
                String cardNumber = inputcardNumber.getText().toString().trim();

                if ( !cardNumber.isEmpty()) {
                    Intent i = new Intent(getApplicationContext(), loyalty.class);

                    Spinner spinnerLoyalty2 = (Spinner) findViewById(R.id.spinnerLoyalty2);
                    Spinner spinnerLoyalty3 = (Spinner) findViewById(R.id.spinnerLoyalty3);
                    EditText cardNumber2 = (EditText) findViewById(R.id.cardNumber2);
                    EditText cardNumber3 = (EditText) findViewById(R.id.cardNumber3);

                    i.putExtra("personalinfo", personalinfo +";"+spinnerLoyalty.getSelectedItem().toString()+","+
                            spinnerLoyalty2.getSelectedItem().toString()+","+spinnerLoyalty3.getSelectedItem().toString()
                            +";"+cardNumber+","+cardNumber2.getText().toString()+","+cardNumber3.getText().toString());


                  /*  Bundle extras = new Bundle();
                    extras.putParcelable("Bitmap", bmpmykad);
                    extras.putParcelable("Bitmap2", mphoto);
                    i.putExtras(extras);*/



                    startActivity(i);
                   // finish();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


    }

    public void imageClick(View view) { //photo

        camera = "1";
        startCamera();
    }


   /* public void undoaddCredit(View view) {
        No--;

        if (No==1)
        {
            LinearLayout CC1 = (LinearLayout) findViewById(R.id.CC1);
            CC1.setVisibility(View.VISIBLE);


        }

        if (No==2)
        {
            LinearLayout CC1 = (LinearLayout) findViewById(R.id.CC1);
            CC1.setVisibility(View.VISIBLE);

            LinearLayout CC2 = (LinearLayout) findViewById(R.id.CC2);
            CC2.setVisibility(View.VISIBLE);

        }
        if (No==3)
        {


            LinearLayout CC1 = (LinearLayout) findViewById(R.id.CC1);
            CC1.setVisibility(View.VISIBLE);

            LinearLayout CC2 = (LinearLayout) findViewById(R.id.CC2);
            CC2.setVisibility(View.VISIBLE);

            LinearLayout CC3 = (LinearLayout) findViewById(R.id.CC3);
            CC3.setVisibility(View.GONE);

        }


    }
*/

    public void addCredit(View view) {
        No++;

        if (No==1)
        {
            LinearLayout CC1 = (LinearLayout) findViewById(R.id.CC1);
            CC1.setVisibility(View.VISIBLE);
            loyaltyImage = (ImageView) findViewById(R.id.im1);

            Button btnScan1 = (Button) findViewById(R.id.btnScan1);
            btnScan1.setVisibility(View.VISIBLE);

        //    Button btnUndo= (Button) findViewById(R.id.btnUndo);
         //   btnUndo.setVisibility(View.GONE);

        }

        if (No==2)
        {
            Button btnScan1 = (Button) findViewById(R.id.btnScan1);
            btnScan1.setVisibility(View.GONE);
         //   Button btnUndo= (Button) findViewById(R.id.btnUndo);
         //   btnUndo.setVisibility(View.VISIBLE);




            loyaltyImage = (ImageView) findViewById(R.id.im2);

            LinearLayout CC1 = (LinearLayout) findViewById(R.id.CC1);
            CC1.setVisibility(View.VISIBLE);

            LinearLayout CC2 = (LinearLayout) findViewById(R.id.CC2);
            CC2.setVisibility(View.VISIBLE);
        }
        if (No==3)
        {

          //  Button btnUndo= (Button) findViewById(R.id.btnUndo);
         //   btnUndo.setVisibility(View.VISIBLE);

            Button btnScan1 = (Button) findViewById(R.id.btnScan1);
            btnScan1.setVisibility(View.GONE);
            Button btnScan2 = (Button) findViewById(R.id.btnScan2);
            btnScan2.setVisibility(View.GONE);

            Button btnadd = (Button) findViewById(R.id.btnAdd);
            btnadd.setVisibility(View.GONE);


            loyaltyImage = (ImageView) findViewById(R.id.im3);

            LinearLayout CC1 = (LinearLayout) findViewById(R.id.CC1);
            CC1.setVisibility(View.VISIBLE);

            LinearLayout CC2 = (LinearLayout) findViewById(R.id.CC2);
            CC2.setVisibility(View.VISIBLE);

            LinearLayout CC3 = (LinearLayout) findViewById(R.id.CC3);
            CC3.setVisibility(View.VISIBLE);

        }


     /*   File from = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"test1.jpg");
        File to = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"CCdet"+No+".jpg");
        from.renameTo(to);

        if (No==1)
        {

           LinearLayout CCdet1 = (LinearLayout) findViewById(R.id.CCdet1);
            CCdet1.setVisibility(View.VISIBLE);

            ImageView imageCC1 = (ImageView) findViewById(R.id.imageCC1);
            File imgFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/CCdet1.jpg");
            Bitmap bm1 = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()), 260, 116, true);
            imageCC1.setImageBitmap(bm1);
            imageCC1.setVisibility(View.VISIBLE);

            TextView tv1 =(TextView)findViewById(R.id.tv1);
            tv1.setText(inputcardName.getText().toString().trim());


            TextView dis1 =(TextView)findViewById(R.id.dis1);
            dis1.setText(inputcardNumber.getText().toString().trim());


        }

        if (No==2)
        {

        }
        if (No==3)
        {

        }*/

    }

    private void startCamera()
    {


        File file;
        Uri fileUri;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        file = new File("/storage/emulated/0/test1_"+No+".jpg");


        fileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        this.startActivityForResult(intent, 1888);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {

            case 1888:
                if (requestCode == 1888 && resultCode == RESULT_OK) {

                    if (mphoto != null) {
                        mphoto.recycle();
                    }

                    File imgFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test1_"+No+".jpg");
                    Matrix matrix = new Matrix();
                    boolean swapWidthHeight = false;

                    try {

                        ExifInterface exif = new ExifInterface(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test1_"+No+".jpg");
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

                        ImageView imageCapture = (ImageView) findViewById(R.id.im1);



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

                        loyaltyImage.setImageBitmap(actualimage);
                        loyaltyImage.setVisibility(View.VISIBLE);

                        if (No<=3) {
                            Button btnadd = (Button) findViewById(R.id.btnAdd);
                            btnadd.setVisibility(View.VISIBLE);
                        }

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
