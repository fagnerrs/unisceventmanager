package unisc.eventmanager.unisceventmanager.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import unisc.eventmanager.unisceventmanager.R;
import unisc.eventmanager.unisceventmanager.adapters.EventosAdapter;
import unisc.eventmanager.unisceventmanager.classes.EventoMO;
import unisc.eventmanager.unisceventmanager.methods.EventoMT;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaintenanceEventFragment extends Fragment {


    public MaintenanceEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View _view = inflater.inflate(R.layout.fragment_maintenance_event, container, false);


        return _view;
    }


}
