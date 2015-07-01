package unisc.eventmanager.unisceventmanager.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import unisc.eventmanager.unisceventmanager.R;
import unisc.eventmanager.unisceventmanager.classes.EncontroMO;
import unisc.eventmanager.unisceventmanager.classes.NavigationManager;
import unisc.eventmanager.unisceventmanager.classes.PresencaMO;
import unisc.eventmanager.unisceventmanager.fragments.IRefreshFragment;


/**
 * Created by FAGNER on 28/03/2015.
 */
public class PresencaAdapter extends BaseAdapter {

    private final Activity m_Context;
    private LayoutInflater m_BaseInflater;
    private final ArrayList<PresencaMO> m_BaseList;
    private IRefreshFragment RefreshListViewListener;

    public PresencaAdapter(Activity context, ArrayList<PresencaMO> baseList)
    {
        m_BaseList = baseList;
        m_BaseInflater = LayoutInflater.from(context);
        m_Context = context;
    }

    @Override
    public int getCount() {
        return (m_BaseList == null ? 0 : m_BaseList.size());
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View _view = m_BaseInflater.inflate(R.layout.presenca_adapter_view, null);
        PresencaMO _presencaMO = m_BaseList.get(position);

        TextView _eventoID = (TextView)_view.findViewById(R.id.presenca_adapter_IdEvento);
        TextView _matricula = (TextView)_view.findViewById(R.id.presenca_adapter_IdMatricula);
        TextView _pessoaID = (TextView)_view.findViewById(R.id.presenca_adapter_PessoaID);
        TextView _nomePessoa = (TextView)_view.findViewById(R.id.presenca_adapter_Nome);
        TextView _emailPEssoa = (TextView)_view.findViewById(R.id.presenca_adapter_Email);
        TextView _dataChegada = (TextView)_view.findViewById(R.id.presenca_adapter_DataChegada);
        TextView _dataSaida = (TextView)_view.findViewById(R.id.presenca_adapter_DataSaida);

        _eventoID.setText(String.valueOf(_presencaMO.getEvento().getID()));
        _matricula.setText(String.valueOf(_presencaMO.getPessoa().getMatricula()));
        _pessoaID.setText(String.valueOf(_presencaMO.getPessoa().getID()));
        _nomePessoa.setText(_presencaMO.getPessoa().getNome());
        _emailPEssoa.setText(_presencaMO.getPessoa().getEmail());

        //_dataChegada.setText(String.valueOf(_presencaMO.getEvento().getID()));
        //_dataSaida.setText(String.valueOf(_presencaMO.getEvento().getID()));





        return _view;
    }

    public IRefreshFragment getRefreshListViewListener() {
        return RefreshListViewListener;
    }

    public void setRefreshListViewListener(IRefreshFragment refreshListViewListener) {
        RefreshListViewListener = refreshListViewListener;
    }

}
