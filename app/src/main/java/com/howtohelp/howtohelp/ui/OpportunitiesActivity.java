package com.howtohelp.howtohelp.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import com.howtohelp.howtohelp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/** The type Opportunities activity. */
public class OpportunitiesActivity extends AppCompatActivity {

  /** The My permissions request location. */
  private final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

  /** The View model. */
  private OpportunitiesViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Get a new or existing ViewModel from the ViewModelProvider.
    viewModel = ViewModelProviders.of(this).get(OpportunitiesViewModel.class);

    setContentView(R.layout.activity_opportunities);
    // Check location permission
    if (hasRequisitePermissions()) getLastKnownAddress();
    else locationPermissionInformationDialog();

    Intent intent = getIntent();

    if (intentIsSharedText(intent)) {
      displayOpportunities(intent);
    } else {
      // Display user guide
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.container, UserGuideFragment.newInstance())
          .commitNow();
    }
  }

  /**
   * Checks if the activity was launched by being sent a text
   *
   * @param intent the intent used to launch the activity
   * @return true if the parameter intent's action is send, false otherwise
   */
  private boolean intentIsSharedText(Intent intent) {
    return Intent.ACTION_SEND.equals(intent.getAction());
  }

  @Override
  protected void onNewIntent(Intent intent) {
    // Get location if location access is granted, otherwise ask for location acecess
    if (hasRequisitePermissions()) {
      if (viewModel.getUserLocation() == null) getLastKnownAddress();
    } else locationPermissionInformationDialog();

    super.onNewIntent(intent);
    if (intentIsSharedText(intent)) {
      // Clear any existing data in the view model before querying new opportunities
      // Required as user can send a new URL when the activity is still open
      viewModel.emptyData();
      displayOpportunities(intent);
    }
  }

  /**
   * Checks if device is connected to the internet. Requires ACCESS_NETWORK_STATE permission
   *
   * @return true if device has an active internet connection, false otherwise
   */
  private boolean isConnectedToInternet() {
    boolean connected = false;
    ConnectivityManager connectivityManager =
        (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivityManager != null) {
      NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
      if (networkInfo != null) {
        if (networkInfo.isConnected()) connected = true;
      }
    }
    return connected;
  }

  /**
   * If the device has an internet connection, inflate OpportunitiesCollectionFragment and classify
   * the text packaged with the intent. If the device has no internet connection, display an error
   * dialog forcing the user to close the application or open the user guide
   *
   * @param intent the intent used to launch the activity
   */
  private void displayOpportunities(Intent intent) {

    if (isConnectedToInternet()) {
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.container, OpportunitiesCollectionFragment.newInstance())
          .commitNow();
      String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
      AsyncTask.execute(
          () -> {
            viewModel.setCategory(sharedText);
          });
    } else {
      closeAppDialog(
          getResources().getString(R.string.no_internet_title),
          getResources().getString(R.string.no_internet_body));
    }
  }

  /**
   * Called when a button in fragment_opportunities_card layout file is pressed. Opens the
   * opportunity URL in the device's default browser
   *
   * @param view view from fragment_opportunities_card. Contains the opportunity URL as a tag
   */
  public void goToUrl(View view) {
    String urlAsString = view.getTag().toString();
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(urlAsString));
    startActivity(intent);
  }

  /**
   * Checks if application has permission to access location data
   *
   * @return true application has permission to access location data, false otherwise
   */
  // GET LOCATION PERMISSION
  private boolean hasRequisitePermissions() {
    return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED;
  }

  /** Request permission to access location data using Android's default dialog box. */
  private void requestPermissions() {
    ActivityCompat.requestPermissions(
        this,
        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
        MY_PERMISSIONS_REQUEST_LOCATION);
    ActivityCompat.requestPermissions(
        this,
        new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
        MY_PERMISSIONS_REQUEST_LOCATION);
  }

  /**
   * If user has granted location permissions, get the user's location. Otherwise display error
   * message
   */
  @Override
  public void onRequestPermissionsResult(
      int requestCode, String[] permissions, int[] grantResults) {

    switch (requestCode) {
      case MY_PERMISSIONS_REQUEST_LOCATION:
        {
          // If request is cancelled, the result arrays are empty.
          if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) getLastKnownAddress();
            else {
              closeAppDialog(
                  getResources().getString(R.string.permission_denied),
                  getResources().getString(R.string.location_not_granted_body));
            }
          }
          return;
        }
    }
  }

  /**
   * Gets last known address of the user and save it to the ViewModel as a UserLocation. Display
   * error message if device is outside the UK or Ireland
   *
   */
  public void getLastKnownAddress() throws SecurityException {
    FusedLocationProviderClient fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(this);

    fusedLocationClient
        .getLastLocation()
        .addOnSuccessListener(
            this,
            location -> {
              // Gets last known location. Will be null if location fails
              if (location != null) {
                // Logic to handle location object
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Geocoder geocoder;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                  List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                  String country = addresses.get(0).getCountryName();
                  if (!(country.equals("Ireland") || country.equals("United Kingdom") || country.equals("Canada") || country.equals("United States"))) {
                    closeAppDialog(
                        getResources().getString(R.string.closing_application_title),
                        getResources().getString(R.string.outside_market_body));
                  }
                  AsyncTask.execute(
                      () -> {
                        viewModel.setUserLocation(
                            addresses.get(0).getCountryName(),
                            addresses.get(0).getAdminArea(),
                            addresses.get(0).getPostalCode(),
                            addresses.get(0).getLatitude(),
                            addresses.get(0).getLongitude());
                      });
                } catch (IOException e) {
                  e.printStackTrace();
                }

              } else {
                closeAppDialog(
                    getResources().getString(R.string.no_location_title),
                    getResources().getString(R.string.no_location_body));
              }
            });
  }

  /**
   * Explains why location data is needed and how it will be used. Must be displayed before asking
   * for location data in order to be GDPR compliant
   *
   * @return the alert dialog
   */
  private AlertDialog locationPermissionInformationDialog() {
    return new AlertDialog.Builder(this)
        .setTitle(getResources().getString(R.string.ask_location_title))
        .setMessage(getResources().getString(R.string.ask_location_body))
        .setCancelable(false)
        .setPositiveButton(
            getResources().getString(R.string.ask_location_button),
            (dialog, which) -> requestPermissions())
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show();
  }

  /**
   * Dialing giving users the options to go to the user guide or close the application
   *
   * @param title text to be displayed in the dialog's title
   * @param body text to be displayed in the dialog's body
   * @return the alert dialog
   */
  private AlertDialog closeAppDialog(String title, String body) {
    return new AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(body)
        .setCancelable(false)
        .setNegativeButton(
            "View Guide",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                viewModel.emptyData();
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, UserGuideFragment.newInstance())
                    .commitNow();
              }
            })
        .setPositiveButton(
            "Close HowtoHelp",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
               finishAndRemoveTask();
              }
            })
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show();
  }
}
