package com.example.lenovo.gm_mrks_sv_rr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener,GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    public String locaciones="";
    public LatLng arr[];
    boolean existe=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String ool=onOpenLoad();
        if(ool!=null){
            if(ool!="") {
                String coordenadas[] = ool.split("!");

                arr = new LatLng[coordenadas.length];

                for (int i = 0; i < coordenadas.length; i++) {
                    String artemp[] = coordenadas[i].split("/");
                    double lat = Double.parseDouble(artemp[0]);
                    double lon = Double.parseDouble(artemp[1]);
                    LatLng temp = new LatLng(lat, lon);
                    arr[i] = temp;
                }
                existe = true;
            }
        }else{
            existe=false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        onCloseSave();
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(21.501062,-104.9119242), 2));
        mMap.setOnMarkerClickListener(this);
        if(existe){
            for(int i=0;i<arr.length;i++){
                mMap.addMarker(new MarkerOptions().position(arr[i]).title(arr[i].toString()).icon(BitmapDescriptorFactory.fromResource(R.drawable.grande)).zIndex(1.0f));
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).title(latLng.toString()).icon(BitmapDescriptorFactory.fromResource(R.drawable.pn)).zIndex(1.0f));
        locaciones=locaciones+latLng.latitude+"/"+latLng.longitude+"!";
    }


    public void onCloseSave() {
        String str = locaciones;
        try {
            FileOutputStream fOut = openFileOutput("marcadores", MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            //---write the string to the file---
            try {
                osw.write(str);
            } catch (IOException e) {
                e.printStackTrace();
            }
            osw.flush();
            osw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public String onOpenLoad() {
        try {
            FileInputStream fIn = openFileInput("marcadores");
            InputStreamReader isr = new InputStreamReader(fIn);
            char[] inputBuffer = new char[100];
            String s = "";
            int charRead;
            while ((charRead = isr.read(inputBuffer)) > 0) {
                //---convert the chars to a String---
                String readString =String.copyValueOf(inputBuffer, 0,charRead);
                s += readString;
                inputBuffer = new char[100];
            }
            return s;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }
}
