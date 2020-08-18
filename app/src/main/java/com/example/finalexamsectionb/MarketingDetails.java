package com.example.finalexamsectionb;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.content.ContentValues.TAG;

public class MarketingDetails extends AppCompatActivity {
EditText proType,proQuantity,proDiscount,proPrice,locationX,locationY;
CircularImageView circularImageView;
Button donebtn;
    private Bitmap photo;
    private static final int CAMERA_REQUEST = 1888;
    private static final int GALLERY_PICTURE = 1999;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int MY_GALLERY_PERMISSION_CODE=200;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketing_details);
        proType=(EditText) findViewById(R.id.ptype);
        proQuantity=(EditText) findViewById(R.id.pquantity);
        proDiscount=(EditText) findViewById(R.id.pdiscount);
        proPrice=(EditText) findViewById(R.id.pprice);
        locationX=(EditText) findViewById(R.id.pXcoordinate);
        locationY=(EditText) findViewById(R.id.pYcoordinate);
        circularImageView=(CircularImageView) findViewById(R.id.myimage1) ;
        donebtn=(Button) findViewById(R.id.ubtn);
        loadCircularImage(circularImageView);
        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
            }
        });

        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type=proType.getText().toString();
                String quantity=proQuantity.getText().toString();
                String discount=proDiscount.getText().toString();
                String price=proPrice.getText().toString();
                String X=locationX.getText().toString();
                String Y=locationY.getText().toString();
                Bitmap image=((BitmapDrawable)circularImageView.getDrawable()).getBitmap();
                String photo=BitmapToString(image);
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("product");
                String id=reference.push().getKey();
                Products product=new Products(id,type,price,quantity,discount,photo,X,Y);
              //  reference.child(id).setValue(product);
                Toast.makeText(getApplicationContext(),"Product Data Inserted Successsfully!",Toast.LENGTH_SHORT).show();
            }
        });
    }
    /** CircularImageView code we have used third party library for it**/
    public void loadCircularImage(CircularImageView circularImageView)
    {
        // Set Color
        circularImageView.setCircleColor(Color.WHITE);
// or with gradient
        circularImageView.setCircleColorStart(Color.BLACK);
        circularImageView.setCircleColorEnd(Color.RED);
        circularImageView.setCircleColorDirection(CircularImageView.GradientDirection.TOP_TO_BOTTOM);

// Set Border
        circularImageView.setBorderWidth(10f);
        circularImageView.setBorderColor(Color.BLACK);
// or with gradient
        circularImageView.setBorderColorStart(Color.BLACK);
        circularImageView.setBorderColorEnd(Color.RED);
        circularImageView.setBorderColorDirection(CircularImageView.GradientDirection.TOP_TO_BOTTOM);

// Add Shadow with default param
        circularImageView.setShadowEnable(true);
// or with custom param
        circularImageView.setShadowRadius(7f);
        circularImageView.setShadowColor(Color.RED);
        circularImageView.setShadowGravity(CircularImageView.ShadowGravity.CENTER);
    }
    /** Circular Image code ends here **/

    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                MarketingDetails.this);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to upload your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (ContextCompat.checkSelfPermission(MarketingDetails.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(
                                    MarketingDetails.this,
                                    PERMISSIONS_STORAGE,
                                    MY_GALLERY_PERMISSION_CODE
                            );
                        } else {

                            Intent intent = new Intent();
                            intent.setType("image/*");
                            // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent,"Select Picture"), GALLERY_PICTURE);


                        }

                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (ContextCompat.checkSelfPermission(MarketingDetails.this,Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA},
                                    MY_CAMERA_PERMISSION_CODE);
                        } else {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                            }
                        }

                    }
                });
        myAlertDialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        } else if(requestCode==MY_GALLERY_PERMISSION_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "gallery read permission granted", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setType("image/*");
                //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), GALLERY_PICTURE);
            } else {
                Toast.makeText(this, "gallery read permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");

            circularImageView.setImageBitmap(photo);
        } else if (requestCode == GALLERY_PICTURE && resultCode == Activity.RESULT_OK) {


            // The following code snipet is used when Intent for single image selection is set

            Uri imageUri = data.getData();
            Log.d(TAG,"file uri: "+imageUri.toString());
            try {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            circularImageView.setImageBitmap(photo);


        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.d(TAG, "PHOTO is null");
            finish();
        }
    }
    public static String BitmapToString(Bitmap photo)
    {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte [] b=stream.toByteArray();
        String temp= Base64.encodeToString(b,Base64.DEFAULT);
        return temp;
    }
}