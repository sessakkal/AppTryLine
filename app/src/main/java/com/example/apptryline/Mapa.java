package com.example.apptryline;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class Mapa extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.mapa, container, false);

        MapView mapView = rootView.findViewById(R.id.mapView);

        mapView.getController().setCenter(new GeoPoint(41.453809, 2.204991));
        mapView.getController().setZoom(22.0);

        mapView.setMultiTouchControls(true);

        GeoPoint markerPoint = new GeoPoint(41.453809, 2.204991);
        Marker marker = new Marker(mapView);
        marker.setPosition(markerPoint);
        mapView.getOverlays().add(marker);

        return rootView;
    }
}
