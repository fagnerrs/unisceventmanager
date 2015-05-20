package unisc.eventmanager.unisceventmanager.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.Inflater;

import unisc.eventmanager.unisceventmanager.R;
import unisc.eventmanager.unisceventmanager.adapters.EncontrosAdapter;
import unisc.eventmanager.unisceventmanager.adapters.EventosAdapter;
import unisc.eventmanager.unisceventmanager.classes.EncontroMO;
import unisc.eventmanager.unisceventmanager.classes.EventoMO;
import unisc.eventmanager.unisceventmanager.classes.NavigationManager;
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
    private Button m_ButtonSalvar;
    private EncontrosAdapter m_encontroAdapter;
    private IRefreshFragment RefreshFragment;
    private ArrayList<EventoMO> m_Eventos = null;
    private ArrayList<Long> m_EncontrosDeletados = new ArrayList<>();

    private void onRefreshFragment()
    {
        if (this.RefreshFragment != null)
        {
            this.RefreshFragment.RefreshListView();
        }
    }

    public void SetRefreshFragment(IRefreshFragment refreshFragment)
    {
        this.RefreshFragment = refreshFragment;
    }

    public MaintenanceEventFragment()
    {
    }

    public MaintenanceEventFragment(ArrayList<EventoMO> eventos, EventoMO eventoMO) {
        m_Evento = eventoMO;
        m_Eventos = eventos;
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
        m_ButtonSalvar = (Button)_view.findViewById(R.id.maintenance_event_BtnSalvar);
        m_ListViewEncontros = (ListView)_view.findViewById(R.id.maintenance_event_listView);

        m_ButtonEncontro.setOnClickListener(novoEncontro());
        m_ButtonSalvar.setOnClickListener(salvarEvento());

        SimpleDateFormat _dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat _timeFormat = new SimpleDateFormat("hh:mm");

        if (m_Evento == null) {
            m_Evento = new EventoMO();

            m_Evento.setDataInicial(new Date());
            m_Evento.setDataFinal(new Date());
        }

        m_EditTextDescricao.setText(m_Evento.getDescricao());

        try {
            m_EditTextDataDe.setText(_dateFormat.format(m_Evento.getDataInicial()));
            m_EditTextDataAte.setText(_dateFormat.format(m_Evento.getDataFinal()));

            m_EditTextHoraDe.setText(_timeFormat.format(m_Evento.getDataInicial()));
            m_EditTextHoraAte.setText(_timeFormat.format(m_Evento.getDataFinal()));
        }catch (Exception ex)
        {

        }

        m_encontroAdapter = new EncontrosAdapter(MaintenanceEventFragment.this.getActivity(), m_Evento.GetEncontros(), m_EncontrosDeletados);
        m_encontroAdapter.setRefreshListViewListener(new IRefreshFragment() {
            @Override
            public void RefreshListView() {
                atualizaListagemEncontros();
            }
        });

        m_ListViewEncontros.setAdapter(m_encontroAdapter);

         return _view;
    }

    private void atualizaListagemEncontros() {
          m_ListViewEncontros.setAdapter(m_encontroAdapter);
    }

    private View.OnClickListener novoEncontro() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaintenanceEncontroFragment _frag = new MaintenanceEncontroFragment(m_Evento, null);
                _frag.RefreshListView(new IRefreshFragment() {
                    @Override
                    public void RefreshListView() {
                        atualizaListagemEncontros();
                    }
                });

                NavigationManager.Navigate(_frag);

            }
        };
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
                            m_Evento.setDescricao(m_EditTextDescricao.getText().toString());

                            SimpleDateFormat _dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat _timeFormatter = new SimpleDateFormat("hh:mm");

                            Date _dataDe = _dateFormatter.parse(m_EditTextDataDe.getText().toString());
                            Date _horaDe = _timeFormatter.parse(m_EditTextHoraDe.getText().toString());

                            Date _dataAte = _dateFormatter.parse(m_EditTextDataAte.getText().toString());
                            Date _horaAte = _timeFormatter.parse(m_EditTextHoraAte.getText().toString());

                            _dataDe.setHours(_horaDe.getHours());
                            _dataDe.setMinutes(_horaDe.getMinutes());
                            _dataDe.setSeconds(0);

                            _dataAte.setHours(_horaAte.getHours());
                            _dataAte.setMinutes(_horaAte.getMinutes());
                            _dataAte.setSeconds(0);


                            m_Evento.setDataInicial(_dataDe);
                            m_Evento.setDataFinal(_dataAte);

                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(MaintenanceEventFragment.this.getActivity(), "Formato de data/hora inválido!", Toast.LENGTH_LONG).show();
                        }

                        for (EncontroMO _encMO : m_Evento.GetEncontros()){
                            if (_encMO.getID() < 0){
                                _encMO.setID(0);
                            }
                        }

                        if (m_Evento.getID()==0) {
                            new EventoMT(MaintenanceEventFragment.this.getActivity()).Salvar(m_Evento);
                        }
                        else{
                            new EventoMT(MaintenanceEventFragment.this.getActivity()).Update(m_Evento, m_EncontrosDeletados);
                        }

                        if (m_Eventos != null){
                            m_Eventos.add(m_Evento);
                        }

                        onRefreshFragment();

                        NavigationManager.Back();
                    }
                }
            }
        };
    }



}
