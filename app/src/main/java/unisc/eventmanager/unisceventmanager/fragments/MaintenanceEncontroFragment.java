package unisc.eventmanager.unisceventmanager.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import unisc.eventmanager.unisceventmanager.R;
import unisc.eventmanager.unisceventmanager.classes.EncontroMO;
import unisc.eventmanager.unisceventmanager.classes.EventoMO;
import unisc.eventmanager.unisceventmanager.classes.NavigationManager;

public class MaintenanceEncontroFragment extends Fragment {

    private EditText m_EditTextDescricao;
    private EditText m_EditTextDataDe;
    private Button m_ButtonSalvar;
    private EditText m_EditTextDataAte;
    private EditText m_EditTextHoraDe;
    private EditText m_EditTextHoraAte;
    private EncontroMO m_Encontro = null;
    private EventoMO m_Evento = null;

    private IRefreshFragment RefreshListView;

    private void onRefreshListView()
    {
        if (this.RefreshListView != null){
            this.RefreshListView.RefreshListView();
        }
    }

    public void RefreshListView(IRefreshFragment refreshFragment)
    {
        this.RefreshListView = refreshFragment;
    }

    public MaintenanceEncontroFragment() {
    }

    public MaintenanceEncontroFragment(EventoMO eventoMO, EncontroMO encontro)
    {
        m_Evento = eventoMO;
        m_Encontro = encontro;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View _view = inflater.inflate(R.layout.fragment_maintenance_encontro, container, false);

        m_EditTextDescricao = (EditText)_view.findViewById(R.id.maintenance_encontro_EdtDescr);
        m_EditTextDataDe = (EditText)_view.findViewById(R.id.maintenance_encontro_EdtDataDe);
        m_EditTextHoraAte = (EditText)_view.findViewById(R.id.maintenance_encontro_EdtHoraAte);
        m_EditTextHoraDe = (EditText)_view.findViewById(R.id.maintenance_encontro_EdtHoraDe);
        m_EditTextDataAte = (EditText)_view.findViewById(R.id.maintenance_encontro_EdtDataAte);
        m_ButtonSalvar = (Button)_view.findViewById(R.id.maintenance_encontro_BtnSalvar);
        m_ButtonSalvar.setOnClickListener(salvarEncontro());

        SimpleDateFormat _dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat _timeFormat = new SimpleDateFormat("hh:mm");

        if (m_Encontro == null) {
            m_Encontro = new EncontroMO();
            m_Encontro.setID(new Date().getTime()*-1);

            m_Encontro.setDataInicial(new Date());
            m_Encontro.setDataFinal(new Date());
        }


        m_EditTextDescricao.setText(m_Encontro.getDescricao());
        m_EditTextDataDe.setText(_dateFormat.format(m_Encontro.getDataInicial()));
        m_EditTextDataAte.setText(_dateFormat.format(m_Encontro.getDataFinal()));

        m_EditTextHoraDe.setText(_timeFormat.format(m_Encontro.getDataInicial()));
        m_EditTextHoraAte.setText(_timeFormat.format(m_Encontro.getDataFinal()));


        return _view;
    }

    private View.OnClickListener salvarEncontro() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (m_EditTextDescricao.getText().equals(""))
                {
                    Toast.makeText(MaintenanceEncontroFragment.this.getActivity(), "Informe a descrição!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if (m_EditTextDataDe.getText().equals("") || m_EditTextDataAte.getText().equals("") ||
                            m_EditTextHoraDe.getText().equals("") || m_EditTextHoraAte.getText().equals(""))
                    {
                        Toast.makeText(MaintenanceEncontroFragment.this.getActivity(), "Informe o período!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        try
                        {
                            m_Encontro.setDescricao(m_EditTextDescricao.getText().toString());

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

                            m_Encontro.setDataFinal(_dataDe);
                            m_Encontro.setDataInicial(_dataAte);
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(MaintenanceEncontroFragment.this.getActivity(), "Formato de data/hora inválido!", Toast.LENGTH_LONG).show();
                        }
                    }
                }


                if (m_Evento != null && m_Encontro.getID() <= 0)
                {
                    m_Evento.GetEncontros().add(m_Encontro);
                }

                onRefreshListView();

                NavigationManager.Back();

            }
        };
    }

}
