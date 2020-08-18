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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.content.ContentValues.TAG;

public class SignUp extends AppCompatActivity {
    EditText nameedt,dateofjoin,emailedt,passwordedt,marketesec,userType;
    Button donebtn;
    CircularImageView circularImageView;
    Bitmap photo;

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
        setContentView(R.layout.activity_sign_up);
        nameedt=(EditText) findViewById(R.id.nameedt);
        dateofjoin=(EditText) findViewById(R.id.dateofjoin);
        emailedt=(EditText) findViewById(R.id.Emailedt);
        passwordedt=(EditText) findViewById(R.id.passwordedt);
        marketesec=(EditText) findViewById(R.id.marketeSector);
        userType=(EditText) findViewById(R.id.userType);
        donebtn=(Button) findViewById(R.id.contibtn);
        circularImageView=(CircularImageView) findViewById(R.id.myimage);
       // loadCircularImage(circularImageView);
        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=nameedt.getText().toString();
                String datejoin=dateofjoin.getText().toString();
                String email=emailedt.getText().toString();
                String pasword=passwordedt.getText().toString();
                String uType=userType.getText().toString();
                String marketePlace=marketesec.getText().toString();
                Bitmap image=((BitmapDrawable)circularImageView.getDrawable()).getBitmap();
                String photo=BitmapToString(image);
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference databaseReference=firebaseDatabase.getReference();
                String id=databaseReference.push().getKey();
                UserData user=new UserData(id,name,datejoin,email,pasword,uType,marketePlace,photo);
                databaseReference.child(user.getmUserType()).child(id).setValue(user);

            }
        });
        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
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
                SignUp.this);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to upload your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (ContextCompat.checkSelfPermission(SignUp.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(
                                    SignUp.this,
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
                        if (ContextCompat.checkSelfPermission(SignUp.this,Manifest.permission.CAMERA)
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