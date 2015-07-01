package unisc.eventmanager.unisceventmanager.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import unisc.eventmanager.unisceventmanager.MaintenanceEncontroActivity;
import unisc.eventmanager.unisceventmanager.R;
import unisc.eventmanager.unisceventmanager.classes.EncontroMO;
import unisc.eventmanager.unisceventmanager.classes.EventoMO;
import unisc.eventmanager.unisceventmanager.classes.NavigationManager;
import unisc.eventmanager.unisceventmanager.fragments.IRefreshFragment;
import unisc.eventmanager.unisceventmanager.fragments.MaintenanceEncontroFragment;
import unisc.eventmanager.unisceventmanager.fragments.MaintenanceEventFragment;
import unisc.eventmanager.unisceventmanager.methods.EventoMT;


/**
 * Created by FAGNER on 28/03/2015.
 */
public class EncontrosAdapter extends BaseAdapter {

    private final Activity m_Context;
    private final ArrayList<Long> m_EncontrosDeletados;
    private LayoutInflater m_BaseInflater;
    private final ArrayList<EncontroMO> m_BaseList;
    private IRefreshFragment RefreshListViewListener;

    public EncontrosAdapter(Activity context, ArrayList<EncontroMO> baseList, ArrayList<Long> encontrosDeletados)
    {
        m_BaseList = baseList;
        m_BaseInflater = LayoutInflater.from(context);
        m_Context = context;
        m_EncontrosDeletados = encontrosDeletados;
    }

    @Override
    public int getCount() {
        return m_BaseList == null ? 0 : m_BaseList.size();
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

        View _view = m_BaseInflater.inflate(R.layout.eventos_adapter_view, null);
        EncontroMO _encontroTO = m_BaseList.get(position);

        TextView _tvNome = (TextView)_view.findViewById(R.id.adapter_evento_TvNome);

        _tvNome.setText(_encontroTO.getDescricao());

        ImageButton _btnUpdate = (ImageButton)_view.findViewById(R.id.adapter_pessoa_BtnAlterar);
        ImageButton _btnRemove = (ImageButton)_view.findViewById(R.id.adapter_pessoa_BtnRemover);

        _btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long _id = Long.valueOf(v.getTag().toString());

                for (EncontroMO _item : m_BaseList)
                {
                    if (_item.getID() == _id)
                    {
                        /*MaintenanceEncontroFragment _mntEnc = new MaintenanceEncontroFragment(null, _item);
                        NavigationManager.Navigate(_mntEnc); */

                        Intent _int = new Intent(m_Context, MaintenanceEncontroActivity.class);
                        _int.putExtra("id", _id);
                        m_Context.startActivity(_int);


                        break;
                    }
                }
            }
        });

        _btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long _id = Long.valueOf(v.getTag().toString());

                for (EncontroMO _item : m_BaseList) {
                    if (_item.getID() == _id) {
                        m_BaseList.remove(_item);


                        break;
                    }

                }

                if (_id > 0) {
                    m_EncontrosDeletados.add(_id);
                }

                if (RefreshListViewListener != null) {
                    RefreshListViewListener.RefreshListView();
                }
            }
        });


        _btnUpdate.setTag(_encontroTO.getID());
        _btnRemove.setTag(_encontroTO.getID());

        return _view;
    }

    public IRefreshFragment getRefreshListViewListener() {
        return RefreshListViewListener;
    }

    public void setRefreshListViewListener(IRefreshFragment refreshListViewListener) {
        RefreshListViewListener = refreshListViewListener;
    }

}
