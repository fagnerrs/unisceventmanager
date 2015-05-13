package unisc.eventmanager.unisceventmanager.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import unisc.eventmanager.unisceventmanager.classes.NavigationManager;
import unisc.eventmanager.unisceventmanager.methods.EventoMT;


public class EventFragment extends Fragment {


    public EventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View _view = inflater.inflate(R.layout.fragment_event, container, false);


        Button _btnAdd = (Button)_view.findViewById(R.id.fragment_event_btnAdd);
        _btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavigationManager.Navigate(new MaintenanceEventFragment());

            }
        });


        ListView _listView = (ListView)_view.findViewById(R.id.fragment_event_ListView);



        ArrayList<EventoMO> _eventos =  new EventoMT().BuscaPessoas(null);

        EventosAdapter _adapter = new EventosAdapter(this.getActivity(), _eventos);


        // Inflate the layout for this fragment
        return _view;
    }
}
