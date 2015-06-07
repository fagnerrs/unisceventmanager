package unisc.eventmanager.unisceventmanager.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import unisc.eventmanager.unisceventmanager.R;
import unisc.eventmanager.unisceventmanager.adapters.EventosAdapter;
import unisc.eventmanager.unisceventmanager.adapters.PresencaAdapter;
import unisc.eventmanager.unisceventmanager.classes.EventoMO;
import unisc.eventmanager.unisceventmanager.classes.NavigationManager;
import unisc.eventmanager.unisceventmanager.classes.PresencaMO;
import unisc.eventmanager.unisceventmanager.methods.EventoMT;

/**
 * A simple {@link Fragment} subclass.
 */
public class PresencaFragment extends Fragment {


    private ListView m_ListView;
    private ArrayList<PresencaMO> m_Eventos;
    private PresencaAdapter _adapter;

    public PresencaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View _view = inflater.inflate(R.layout.fragment_presenca, container, false);

        m_ListView = (ListView)_view.findViewById(R.id.fragment_presenca_ListView);

        Button _btnAdd = (Button)_view.findViewById(R.id.fragment_presenca_btnExp);
        _btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        m_Eventos =  new EventoMT(this.getActivity()).BuscaPresencas();
        _adapter = new PresencaAdapter(this.getActivity(), m_Eventos);
        _adapter.setRefreshListViewListener(new IRefreshFragment() {
            @Override
            public void RefreshListView() {

            }
        });

        m_ListView.setAdapter(_adapter);


        return _view;
    }


}
