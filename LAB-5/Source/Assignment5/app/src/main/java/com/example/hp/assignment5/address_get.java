package com.example.hp.assignment5;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by hp on 2/22/2017.
 */

public class address_get extends IntentService {
    private String errorMessage;
    protected ResultReceiver mReceiver;
    protected Location mLocation;
    private String TAG = "DEB";

    public address_get() {
        super("address_get");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        mLocation = intent.getParcelableExtra(resources.LOCATION_DATA_EXTRA);
        mReceiver = intent.getParcelableExtra(resources.RECEIVER);



        Geocoder mGeocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses = null;

        try {
            addresses = mGeocoder.getFromLocation(
                    mLocation.getLatitude(),
                    mLocation.getLongitude(),
                    //Barasat
                    //   22.724484,
                    //   88.484284,
                    // In this sample, get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = "service_not_available";
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = "invalid_lat_long_used";
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + mLocation.getLatitude() +
                    ", Longitude = " +
                    mLocation.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "no_address_found";
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(resources.FAILURE_RESULT, errorMessage,null);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG, "address_found");
            Log.d(TAG, "Address Line = " + TextUtils.join(System.getProperty("line.separator"), addressFragments));

            deliverResultToReceiver(resources.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments), address);
        }
    }

    private void deliverResultToReceiver(int resultCode, String message, Address adrs) {
        Bundle bundle = new Bundle();
        bundle.putString(resources.RESULT_DATA_KEY, message);
        bundle.putParcelable("ADDRESS_DATA",adrs);
        mReceiver.send(resultCode, bundle);
    }
}
