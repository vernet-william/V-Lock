package fr.vlock.app.ui.map;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import fr.vlock.app.MyDatabase;
import fr.vlock.app.R;

public class MapsFragment extends Fragment {

    private FusedLocationProviderClient location;
    private SupportMapFragment mapFragment;
    private MyDatabase MyDb;
    private ArrayList<Integer> listId;
    private ArrayList<String> listTitre, listEtat, listDesc;
    private ArrayList<Double> listLatitude, listLongitude;
    private ArrayList<StationElement> listStation;
    private Dialog dialogMarker;

    private Drawable myDrawable;
    private Bitmap myPoint;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng utt = new LatLng(48.26961812597914, 4.070155509679473);
            googleMap.addMarker(new MarkerOptions().position(utt).title("On est la !"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(utt));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        myDrawable = getResources().getDrawable(R.drawable.point);

        MyDb = new MyDatabase(getContext());
        MyDb.addStation("Station 1", 48.863613, 2.351853, "Disponible", "Station à Paris");
        MyDb.addStation("Station 2", 45.632967, 4.932819, "Disponible", "Station à Lyon");
        MyDb.addStation("Station 3", 48.285536, 4.111759, "Reservé", "Station à Troyes");
        MyDb.addStation("Station 4", 47.210123, -1.550403, "Occupé", "Station à Nantes");
        MyDb.addStation("Station 5", 43.600799, 1.442480, "Hors Service", "Station à Toulouse");
        listStation = new ArrayList<>();
        listId = new ArrayList<>();
        listTitre = new ArrayList<>();
        listLatitude = new ArrayList<>();
        listLongitude = new ArrayList<>();
        listEtat = new ArrayList<>();
        listDesc = new ArrayList<>();

        location = (FusedLocationProviderClient) LocationServices.getFusedLocationProviderClient(getContext());

        Dexter.withContext(getContext()).withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getCurrentLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = location.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        if(location != null){
                            LatLng coord = new LatLng(location.getLatitude(), location.getLongitude());

                            readStation();

                            for(int value = 0; value < listStation.size(); value++) {
                                Log.w(TAG, "New Station : "+listStation.get(value).getTitre());
                                LatLng point = new LatLng(listStation.get(value).getLatitude(), listStation.get(value).getLongitude());
                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(point)
                                        .icon(BitmapDescriptorFactory.fromBitmap(((BitmapDrawable)myDrawable).getBitmap()))
                                        .title(listStation.get(value).getTitre())
                                        .snippet(listStation.get(value).getDesc());
                                googleMap.addMarker(markerOptions);
                            }

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coord, 15));
                            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(@NonNull Marker marker) {
                                    createDialog();
                                    dialogMarker.show();
                                    return false;
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    public void readStation() {
        Cursor cursor = MyDb.readAllData();
        if(cursor.getCount() == 0) {
            Toast.makeText(getContext(), "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                listId.add(cursor.getInt(0));
                listTitre.add(cursor.getString(1));
                listLatitude.add(cursor.getDouble(2));
                listLongitude.add(cursor.getDouble(3));
                listEtat.add(cursor.getString(4));
                listDesc.add(cursor.getString(5));
            }
        }
        for (int value = 0; value < listId.size(); value++) {
            listStation.add(new StationElement(listTitre.get(value), listEtat.get(value), listDesc.get(value), listLatitude.get(value), listLongitude.get(value), listId.get(value)));
        }
    }

    public void createDialog() {
        dialogMarker = new Dialog(getContext());
        dialogMarker.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMarker.setContentView(R.layout.dialog_marker);

        TextView titre = dialogMarker.findViewById(R.id.txt_titre);
        TextView etat = dialogMarker.findViewById(R.id.txt_etat);
        Button btn_reservation = dialogMarker.findViewById(R.id.btn_reservation);

        btn_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMarker.dismiss();
            }
        });
    }
}