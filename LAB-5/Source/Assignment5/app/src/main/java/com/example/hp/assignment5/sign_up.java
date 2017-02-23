package com.example.hp.assignment5;

import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class sign_up extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private boolean mAddressRequested = true;
    private GoogleApiClient mGoogleApiClient;
    private String TAG = "DEB";
    private TextView mTextView;
    protected Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mTextView = (TextView) findViewById(R.id.input_address);
        mResultReceiver = new AddressResultReceiver(null);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        //button_photo = (Button) findViewById(R.id.main_btn_photo);
        public void onClick(View view) {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();}



        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        //make sure super is called at the end
        super.onStop();
    }

    @Override


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.activity_main) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void startIntentService() {
        Intent intent = new Intent(this, address_get.class);
        intent.putExtra(resources.RECEIVER, mResultReceiver);
        intent.putExtra(resources.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            // Determine whether a Geocoder is available.
            String mLatitude = String.valueOf(mLastLocation.getLatitude());
            String mLongitude = String.valueOf(mLastLocation.getLongitude());
            if (!Geocoder.isPresent()) {
                Toast.makeText(this, "no_geocoder_available", Toast.LENGTH_LONG).show();
                return;
            }

            if (mAddressRequested) {
                //    startIntentService();
            }
        }
    }
    @Override
    public void onConnectionSuspended ( int i){

    }

    @Override
    public void onConnectionFailed (ConnectionResult connectionResult){

    }

    class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            String mAddressOutput = resultData.getString(resources.RESULT_DATA_KEY);
            Toast.makeText(getBaseContext(), "Return code = " + resultCode + " & Address = " + mAddressOutput, Toast.LENGTH_LONG).show();
            address = resultData.getParcelable("ADDRESS_DATA");
            if (address != null) {
                String mData = address.getLocality()+","+address.getAdminArea()+","+address.getCountryName()+","+address.getPostalCode();
                runOnUiThread(new DebRunnable(mData));

            }
        }
    }

   

    public void getAdrs(View v) {

        if (mGoogleApiClient.isConnected() && mLastLocation != null) {
            startIntentService();
        }
        updateUI();
    }

    public void updateUI() {

        if(address != null ) {
            mTextView.setText(address.getCountryName());
        }

    }

    class DebRunnable implements Runnable {

        String mData;

        public DebRunnable (String mData) {
            this.mData = mData;
        }
        @Override
        public void run() {
            mTextView.setText(mData);
            showData(mData);

        }
    }

    public void onClickMapLocation(View v) {
        //This code redirects to the photo activity.
        Intent redirect = new Intent(sign_up.this, home.class);
        startActivity(redirect);
    }
    final int REQUEST_CAMERA =1;
    final int SELECT_FILE=2;

    public void selectImage(View view) {

        final CharSequence[] options = {  "Select from Library", "Camera", "Cancel" };
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(sign_up.this);
        alertBuilder.setTitle("Select Photo");
        alertBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int option) {
                if (options[option].equals("Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (options[option].equals("Select from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (options[option].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        alertBuilder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        ImageView imageView = (ImageView) findViewById(R.id.imageButton);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(bitmap);
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = intent.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                imageView.setImageBitmap(bm);
            }
        }
    }

}
