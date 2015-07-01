package unisc.eventmanager.unisceventmanager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import unisc.eventmanager.unisceventmanager.adapters.EncontrosAdapter;
import unisc.eventmanager.unisceventmanager.classes.EncontroMO;
import unisc.eventmanager.unisceventmanager.classes.EventoMO;
import unisc.eventmanager.unisceventmanager.classes.NavigationManager;
import unisc.eventmanager.unisceventmanager.fragments.IRefreshFragment;
import unisc.eventmanager.unisceventmanager.fragments.MaintenanceEncontroFragment;
import unisc.eventmanager.unisceventmanager.methods.EventoMT;
import unisc.eventmanager.unisceventmanager.methods.EventoWS;


public class MaintenanceEventActivity extends Activity {

    private EditText m_EditTextDescricao;
    private EditText m_EditTextDataDe;
    private EditText m_EditTextDataAte;
    private Button m_ButtonEncontro;
    private ListView m_ListViewEncontros;
    public static EventoMO m_Evento = null;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_event);

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        m_EditTextDescricao = (EditText)this.findViewById(R.id.maintenance_event_EdtDescr);
        m_EditTextDataDe = (EditText)this.findViewById(R.id.maintenance_event_EdtDataDe);
        m_EditTextHoraAte = (EditText)this.findViewById(R.id.maintenance_event_EdtHoraAte);
        m_EditTextHoraDe = (EditText)this.findViewById(R.id.maintenance_event_EdtHoraDe);
        m_EditTextDataAte = (EditText)this.findViewById(R.id.maintenance_event_EdtDataAte);
        m_ButtonEncontro = (Button)this.findViewById(R.id.maintenance_event_BtnNovoEncontro);
        m_ButtonSalvar = (Button)this.findViewById(R.id.maintenance_event_BtnSalvar);
        m_ListViewEncontros = (ListView)this.findViewById(R.id.maintenance_event_listView);

        m_ButtonEncontro.setOnClickListener(novoEncontro());
        m_ButtonSalvar.setOnClickListener(salvarEvento());

        final SimpleDateFormat _dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat _timeFormat = new SimpleDateFormat("hh:mm");


        long _id = this.getIntent().getLongExtra("id", 0);
        if (_id > 0) {
            new EventoWS(this).RetornaEvento(_id, new EventoWS.IGetEventoResult() {
                @Override
                public void Result(EventoMO evento) {

                    m_Evento = evento;

                    m_EditTextDescricao.setText(m_Evento.getDescricao());

                    try {
                        m_EditTextDataDe.setText(_dateFormat.format(m_Evento.getDataInicial()));
                        m_EditTextDataAte.setText(_dateFormat.format(m_Evento.getDataFinal()));

                        m_EditTextHoraDe.setText(_timeFormat.format(m_Evento.getDataInicial()));
                        m_EditTextHoraAte.setText(_timeFormat.format(m_Evento.getDataFinal()));
                    } catch (Exception ex) {

                    }

                    new EventoWS(MaintenanceEventActivity.this).ListaEncontros(m_Evento.getID(), new EventoWS.IEncontroResult() {
                        @Override
                        public void Result(ArrayList<EncontroMO> encontros) {

                           m_Evento.SetEncontros(encontros);
                            
                            atualizaListagemEncontros();
                        }
                    });
                }
            });
        }
        else {

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
            } catch (Exception ex) {


            }

            atualizaListagemEncontros();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizaListagemEncontros();
    }

    private void atualizaListagemEncontros() {

        if (m_ListViewEncontros != null && m_Evento!= null ) {

            m_encontroAdapter = null;
            m_encontroAdapter = new EncontrosAdapter(MaintenanceEventActivity.this, m_Evento.GetEncontros(), m_EncontrosDeletados);
            m_encontroAdapter.setRefreshListViewListener(new IRefreshFragment() {
                @Override
                public void RefreshListView() {
                    atualizaListagemEncontros();
                }
            });

            m_ListViewEncontros.setAdapter(m_encontroAdapter);
        }
    }

    private View.OnClickListener novoEncontro() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*MaintenanceEncontroFragment _frag = new MaintenanceEncontroFragment(m_Evento, null);
                _frag.RefreshListView(new IRefreshFragment() {
                    @Override
                    public void RefreshListView() {
                        atualizaListagemEncontros();
                    }
                });

                NavigationManager.Navigate(_frag); */

                Intent _int = new Intent(MaintenanceEventActivity.this, MaintenanceEncontroActivity.class);
                MaintenanceEventActivity.this.startActivity(_int);


            }
        };
    }


    private View.OnClickListener salvarEvento() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (m_EditTextDescricao.getText().equals(""))
                {
                    Toast.makeText(MaintenanceEventActivity.this, "Informe a descrição!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if (m_EditTextDataDe.getText().equals("") || m_EditTextDataAte.getText().equals("") ||
                            m_EditTextHoraDe.getText().equals("") || m_EditTextHoraAte.getText().equals(""))
                    {
                        Toast.makeText(MaintenanceEventActivity.this, "Informe o período!", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(MaintenanceEventActivity.this, "Formato de data/hora inválido!", Toast.LENGTH_LONG).show();
                        }

                        for (EncontroMO _encMO : m_Evento.GetEncontros()){
                            if (_encMO.getID() < 0){
                                _encMO.setID(0);
                            }
                        }

                        if (m_Evento.getID()==0) {
                            new EventoWS(MaintenanceEventActivity.this).Salvar(m_Evento, new EventoWS.IEventoResult() {
                                @Override
                                public void ListaEventosResult(ArrayList<EventoMO> eventos) {


                                    for (EncontroMO _encontro : m_Evento.GetEncontros() ){

                                        new EventoWS(MaintenanceEventActivity.this).InserirEncontro(m_Evento.getID(), _encontro, null);

                                    }

                                    MaintenanceEventActivity.this.onBackPressed();
                                }
                            });
                        }
                        else{
                            //m_EncontrosDeletados
                            new EventoWS(MaintenanceEventActivity.this).Atualizar(m_Evento, new EventoWS.IEventoResult() {
                                @Override
                                public void ListaEventosResult(ArrayList<EventoMO> eventos) {


                                    for (EncontroMO _encontro : m_Evento.GetEncontros() ){

                                        new EventoWS(MaintenanceEventActivity.this).InserirEncontro(m_Evento.getID(), _encontro, null);

                                    }


                                    MaintenanceEventActivity.this.onBackPressed();
                                }
                            });
                        }

                        if (m_Eventos != null){
                            m_Eventos.add(m_Evento);
                        }


                    }
                }
            }
        };
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maintenance_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else
        {
            this.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
