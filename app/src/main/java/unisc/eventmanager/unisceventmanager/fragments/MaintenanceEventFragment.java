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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private EditText m_EditTextHoraAte;
    private EditText m_EditTextHoraDe;


    public MaintenanceEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View _view = inflater.inflate(R.layout.fragment_maintenance_event, container, false);

        m_EditTextDescricao = (EditText)_view.findViewById(R.id.maintenance_event_EdtDescr);
        m_EditTextDataDe = (EditText)_view.findViewById(R.id.maintenance_event_EdtDataDe);
        m_EditTextHoraAte = (EditText)_view.findViewById(R.id.maintenance_event_EdtHoraAte);
        m_EditTextHoraDe = (EditText)_view.findViewById(R.id.maintenance_event_EdtHoraDe);
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
                    if (m_EditTextDataDe.getText().equals("") || m_EditTextDataAte.getText().equals("") ||
                            m_EditTextHoraDe.getText().equals("") || m_EditTextHoraAte.getText().equals(""))
                    {
                        Toast.makeText(MaintenanceEventFragment.this.getActivity(), "Informe o período!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        try
                        {
                            SimpleDateFormat _dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat _timeFormatter = new SimpleDateFormat("hh:mm");

                            Date _dataDe = _dateFormatter.parse(m_EditTextDataDe.getText().toString());
                            Date _horaDe = _timeFormatter.parse(m_EditTextHoraDe.getText().toString());

                            _dataDe.setHours(_horaDe.getHours());
                            _dataDe.setMinutes(_horaDe.getMinutes());
                            _dataDe.setSeconds(0);


                        }
                        catch (Exception ex)
                        {

                        }


                    }

                }
            }
        };
    }


}
