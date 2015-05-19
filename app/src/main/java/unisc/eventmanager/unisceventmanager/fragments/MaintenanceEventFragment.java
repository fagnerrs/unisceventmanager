package unisc.eventmanager.unisceventmanager.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import unisc.eventmanager.unisceventmanager.R;
import unisc.eventmanager.unisceventmanager.adapters.EventosAdapter;
import unisc.eventmanager.unisceventmanager.classes.EventoMO;
import unisc.eventmanager.unisceventmanager.methods.EventoMT;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaintenanceEventFragment extends Fragment {


    private EditText m_EditTextDescricao;
    private EditText m_EditTextDataDe;
    private EditText m_EditTextDataAte;
    private Button m_ButtonEncontro;
    private ListView m_ListViewEncontros;
    private EventoMO m_Evento = null;


    public MaintenanceEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View _view = inflater.inflate(R.layout.fragment_maintenance_event, container, false);

        m_EditTextDescricao = (EditText)_view.findViewById(R.id.maintenance_event_EdtDescr);
        m_EditTextDataDe = (EditText)_view.findViewById(R.id.maintenance_event_EdtDataDe);
        m_EditTextDataAte = (EditText)_view.findViewById(R.id.maintenance_event_EdtDataAte);
        m_ButtonEncontro = (Button)_view.findViewById(R.id.maintenance_event_BtnNovoEncontro);
        m_ListViewEncontros = (ListView)_view.findViewById(R.id.maintenance_event_listView);

        m_ButtonEncontro.setOnClickListener(salvarEvento());


        return _view;
    }

    private View.OnClickListener salvarEvento() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (m_EditTextDescricao.getText().equals(""))
                {
                    Toast.makeText(MaintenanceEventFragment.this.getActivity(), "Informe a descrição!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if (m_EditTextDataDe.getText().equals("") || m_EditTextDataAte.getText().equals(""))
                    {
                        Toast.makeText(MaintenanceEventFragment.this.getActivity(), "Informe o período!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {


                    }

                }
            }
        };
    }


}
