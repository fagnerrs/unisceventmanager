package unisc.eventmanager.unisceventmanager.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import unisc.eventmanager.unisceventmanager.MaintenanceEventActivity;
import unisc.eventmanager.unisceventmanager.R;
import unisc.eventmanager.unisceventmanager.adapters.EventosAdapter;
import unisc.eventmanager.unisceventmanager.classes.EventoMO;
import unisc.eventmanager.unisceventmanager.classes.NavigationManager;
import unisc.eventmanager.unisceventmanager.methods.EventoMT;
import unisc.eventmanager.unisceventmanager.methods.EventoWS;


public class EventFragment extends Fragment {

    private ListView m_ListView;
    private EventosAdapter _adapter;
    private EventoWS m_EventoWS;

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

        m_ListView = (ListView)_view.findViewById(R.id.fragment_event_ListView);

        Button _btnAdd = (Button)_view.findViewById(R.id.fragment_event_btnAdd);
        _btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            /*MaintenanceEventFragment _evFrag = new MaintenanceEventFragment(m_Eventos, null);
            _evFrag.SetRefreshFragment(new IRefreshFragment() {
                @Override
                public void RefreshListView() {
                    atualizaListaEventos();
                }
            });

            NavigationManager.Navigate(_evFrag); */

                Intent _int = new Intent(EventFragment.this.getActivity(), MaintenanceEventActivity.class);
                EventFragment.this.startActivity(_int);

            }
        });


        m_EventoWS = new EventoWS(this.getActivity());
        m_EventoWS.setEventoResult(new EventoWS.IEventoResult() {
            @Override
            public void ListaEventosResult(ArrayList<EventoMO> eventos) {

                _adapter = new EventosAdapter(EventFragment.this.getActivity(), eventos);
                _adapter.setRefreshListViewListener(new IRefreshFragment() {
                    @Override
                    public void RefreshListView() {
                        atualizaListaEventos();
                    }
                });

                if (m_ListView != null) {
                    m_ListView.setAdapter(_adapter);
                }
            }
        });

        //m_ListView.setAdapter(_adapter);

        return _view;
    }

    @Override
    public void onResume() {
        super.onResume();
        atualizaListaEventos();


    }

    private void atualizaListaEventos() {
        if (m_EventoWS != null){
            m_EventoWS.ListaEventos();
        }
    }

    public EventoWS getM_EventoWS() {
        return m_EventoWS;
    }

    public void setM_EventoWS(EventoWS m_EventoWS) {
        this.m_EventoWS = m_EventoWS;
    }
}
