package unisc.eventmanager.unisceventmanager.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
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
                createCSVFile();
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

    private void createCSVFile()
    {
        String columnString =   "\"Evento ID\",\"Matrícula\",\"Pessoa ID\",\"Nome\",\"Email\",\"Data Chegada\",\"Data Saída\"";
        //String dataString   =   "\"" + "Fagner" +"\",\"" + "Male" + "\",\"" + "Henrique Schuster" + "\",\"" + "fagnerrs"+ "\",\"" + 29 + "\"";
        String combinedString = columnString + "\n";

        SimpleDateFormat _dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        for (PresencaMO _pres : m_Eventos){

            String dataString   =   "\"" + String.valueOf(_pres.getEvento().getID()) +"\",\"" + String.valueOf(_pres.getPessoa().getMatricula()) + "\",\"" + String.valueOf(_pres.getPessoa().getID()) + "\",\"" + _pres.getPessoa().getNome() + "\",\"" +  _pres.getPessoa().getEmail() + "\",\"" + _dateFormat.format(_pres.getDataChegada()) + "\",\"" + _dateFormat.format(_pres.getDataSaida()) + "\",\"" + 29 + "\"";
            combinedString+=dataString;
        }


        File file   = null;
        File root   = Environment.getExternalStorageDirectory();
        if (root.canWrite()){
            File dir    =   new File (root.getAbsolutePath() + "/PersonData");
            dir.mkdirs();
            file   =   new File(dir, "Data.csv");

            FileOutputStream out   =   null;
            try {
                out = new FileOutputStream(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                out.write(combinedString.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            EnviarEmail(file.getAbsolutePath());
        }
    }


    public void EnviarEmail(String fileImage)
    {

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "fagnerrs@gmail.com");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Relatório de Presenças");
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Trabalho Prog. Disp. Móveis");
        emailIntent.setType("application/image");

        Uri uri = Uri.parse("file://" + fileImage);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(emailIntent);


        startActivity(emailIntent);
    }

}
