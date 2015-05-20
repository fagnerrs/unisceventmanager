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
import unisc.eventmanager.unisceventmanager.classes.NavigationManager;
import unisc.eventmanager.unisceventmanager.methods.EventoMT;


public class EventFragment extends Fragment {

    private ListView m_ListView;
    private EventosAdapter _adapter;
    private ArrayList<EventoMO> m_Eventos;

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

            MaintenanceEventFragment _evFrag = new MaintenanceEventFragment(m_Eventos, null);
            _evFrag.SetRefreshFragment(new IRefreshFragment() {
                @Override
                public void RefreshListView() {
                    atualizaListaEventos();
                }
            });

            NavigationManager.Navigate(_evFrag);
            }
        });

        m_Eventos =  new EventoMT(this.getActivity()).BuscaEventos(null);
        _adapter = new EventosAdapter(this.getActivity(), m_Eventos);
        _adapter.setRefreshListViewListener(new IRefreshFragment() {
            @Override
            public void RefreshListView() {
                atualizaListaEventos();
            }
        });

        m_ListView.setAdapter(_adapter);

        return _view;
    }

    private void atualizaListaEventos()
    {
        m_ListView.setAdapter(_adapter);
    }
}
