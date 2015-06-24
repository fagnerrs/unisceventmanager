package unisc.eventmanager.unisceventmanager;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import unisc.eventmanager.unisceventmanager.classes.EncontroMO;
import unisc.eventmanager.unisceventmanager.classes.EventoMO;
import unisc.eventmanager.unisceventmanager.classes.NavigationManager;
import unisc.eventmanager.unisceventmanager.fragments.IRefreshFragment;
import unisc.eventmanager.unisceventmanager.methods.EventoMT;


public class MaintenanceEncontroActivity extends Activity {

    private EditText m_EditTextDescricao;
    private EditText m_EditTextDataDe;
    private Button m_ButtonSalvar;
    private EditText m_EditTextDataAte;
    private EditText m_EditTextHoraDe;
    private EditText m_EditTextHoraAte;
    private EncontroMO m_Encontro = null;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_encontro);

        m_EditTextDescricao = (EditText)this.findViewById(R.id.maintenance_encontro_EdtDescr);
        m_EditTextDataDe = (EditText)this.findViewById(R.id.maintenance_encontro_EdtDataDe);
        m_EditTextHoraAte = (EditText)this.findViewById(R.id.maintenance_encontro_EdtHoraAte);
        m_EditTextHoraDe = (EditText)this.findViewById(R.id.maintenance_encontro_EdtHoraDe);
        m_EditTextDataAte = (EditText)this.findViewById(R.id.maintenance_encontro_EdtDataAte);
        m_ButtonSalvar = (Button)this.findViewById(R.id.maintenance_encontro_BtnSalvar);
        m_ButtonSalvar.setOnClickListener(salvarEncontro());

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        SimpleDateFormat _dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat _timeFormat = new SimpleDateFormat("hh:mm");

        long _id = this.getIntent().getLongExtra("id", 0);
        if (_id > 0){
            for (EncontroMO _encontr : MaintenanceEventActivity.m_Evento.GetEncontros()){
                if (_id == _encontr.getID()){
                    m_Encontro = _encontr;

                    break;
                }
            }

        }

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

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maintenance_encontro, menu);
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

        return super.onOptionsItemSelected(item);
    }


    private View.OnClickListener salvarEncontro() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (m_EditTextDescricao.getText().equals(""))
                {
                    Toast.makeText(MaintenanceEncontroActivity.this, "Informe a descrição!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if (m_EditTextDataDe.getText().equals("") || m_EditTextDataAte.getText().equals("") ||
                            m_EditTextHoraDe.getText().equals("") || m_EditTextHoraAte.getText().equals(""))
                    {
                        Toast.makeText(MaintenanceEncontroActivity.this, "Informe o período!", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(MaintenanceEncontroActivity.this, "Formato de data/hora inválido!", Toast.LENGTH_LONG).show();
                        }
                    }
                }


                if (MaintenanceEventActivity.m_Evento != null && m_Encontro.getID() <= 0)
                {
                    MaintenanceEventActivity.m_Evento.GetEncontros().add(m_Encontro);
                }

                onRefreshListView();

                MaintenanceEncontroActivity.this.onBackPressed();
            }
        };
    }


}
