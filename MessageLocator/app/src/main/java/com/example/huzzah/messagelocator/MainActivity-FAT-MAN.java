package com.example.huzzah.messagelocator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    //      Alustetut muuttujat:
    //      -Locationmanager kaivelee sijaintia
    //      -Latitude ja Longitude String -muuttujat ovat tavallaan tarpeettomia, mutta käytetään debugaukseen toistaiseksi
    //      -LatitudeBest ja LongitudeBNest double-muuttujat ovat varsinaiset sijaintimuuttujat pituus/leveyspiireille
    //      -getmessage -String on WebViewiin kaivettavan sivuston osoite, tässä tilanteessa lähiverkossa sijaitseva palvelin
    LocationManager locationManager;
    public double latitudeBest;
    public double longitudeBest;
    public String latitude;
    public String longitude;
    public EditText textField;
    public WebView vw;
    public String getmessage = "http://192.168.1.103/messageget.php"; //vaihda tämä jos haluat vaihtaa serveriä
    private String username = "";
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Perus alustukset kaikille muuttujille
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        vw = (WebView) findViewById(R.id.webView);

        //GiveUserName -aktiviteetistä lähetetty muuttuja username, käytetään BackgroundTaskissa ja Toastissa.
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }
        //Tervehtii käyttäjää
        Toast.makeText(this, "Welcome " + username, Toast.LENGTH_SHORT).show();
        textField = (EditText) findViewById(R.id.editText);

        //Allaoleva kaivaa viestit webViewiin ja automaattisesti rullaa sen alas uusimpaan viestiin.
        try {

            vw.loadUrl(getmessage);
            Thread.sleep(500);
            vw.scrollTo(0,10000000);
        }
        catch (Exception e){
            e.getLocalizedMessage();
        };
        vw.loadUrl(getmessage);
        textView = (TextView)findViewById(R.id.textView);
    }

    //Boolean -muuttuja joka kertoo onko sijainti sallittu vai ei
    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }
    //Herjaa jos sijainti ei ole sallittu ja linkkaa asetuksiin tarpeen vaatiessa
    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    //Tarkistetaan onko sijainti käytössä
    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    //Kytkee pois/päälle sijainnin viestistä
    //TODO: Erota viestit joissa ei ole sijaintia ollenkaan
    public void toggleNetworkUpdates(View view) {
        if(!checkLocation())
            return;
        Button button = (Button) view;
        if(button.getText().equals(getResources().getString(R.string.pause))) {
            locationManager.removeUpdates(locationListenerBest);
            button.setText(R.string.resume);
        }
        else {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 60 * 1000, 10, locationListenerBest);
            Toast.makeText(this, "Getting location...", Toast.LENGTH_LONG).show();
            button.setText(R.string.pause);
        }
    }
    //Kaivaa sijainnin. Jos tämä on päällä ja sijainti muuttuu, sijainti päivittyy automaattisesti
    //TODO: Anna mahdollisuus vaihdella verkkosijainnin ja GPS-sijainnin välillä, tällä hetkellä pelkkä verkkosijainti käytössä.
    private final LocationListener locationListenerBest = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeBest = location.getLongitude();
            latitudeBest = location.getLatitude();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("ON: " + latitudeBest +", " +longitudeBest);
                    Toast.makeText(MainActivity.this, "Location updated", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    //Viestinlähetysmetodi, ottaa tekstin textFieldistä, käyttäjän nimen ja koordinaatit ja lähettää ne BackgroundTaskiin parametreina.
    //Päivittää webViewn ja tyhjää textFieldin lopuksi.
    //TODO: tee joku funktio mikä automaattisesti rullaa alas webViewn sen päivittymisen jälkeen
    public void sendMessage(View view) {
            String text = textField.getText().toString();
            BackgroundTask backgroundtask = new BackgroundTask(this);
            latitude = String.valueOf(latitudeBest);
            longitude = String.valueOf(longitudeBest);
            backgroundtask.execute(text, username, latitude, longitude);
            //androidin sleeppi tähän väliin, varmistaa että ehtii lataamaan ajoissa sivun uudelleen webViewiin.
            try{
                vw.loadUrl(getmessage);
                Thread.sleep(1000);

            }catch (Exception e)
            {
                e.getLocalizedMessage();
            }

            textField.setText("");

    };
}

