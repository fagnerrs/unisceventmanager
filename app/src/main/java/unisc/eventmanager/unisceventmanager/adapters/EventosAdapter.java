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

import unisc.eventmanager.unisceventmanager.R;
import unisc.eventmanager.unisceventmanager.classes.EventoMO;
import unisc.eventmanager.unisceventmanager.fragments.MaintenanceEventFragment;
import unisc.eventmanager.unisceventmanager.methods.EventoMT;


/**
 * Created by FAGNER on 28/03/2015.
 */
public class EventosAdapter extends BaseAdapter {

    private final Activity m_Context;
    private LayoutInflater m_BaseInflater;
    private final ArrayList<EventoMO> m_BaseList;
    private IRefreshListView RefreshListViewListener;

    public EventosAdapter(Activity context, ArrayList<EventoMO> baseList)
    {
        m_BaseList = baseList;
        m_BaseInflater = LayoutInflater.from(context);
        m_Context = context;
    }

    @Override
    public int getCount() {
        return m_BaseList.size();
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
        EventoMO _rotaTO = m_BaseList.get(position);

        TextView _tvNome = (TextView)_view.findViewById(R.id.adapter_evento_TvNome);

        _tvNome.setText(_rotaTO.getDescricao());

        ImageButton _btnUpdate = (ImageButton)_view.findViewById(R.id.adapter_pessoa_BtnAlterar);
        ImageButton _btnRemove = (ImageButton)_view.findViewById(R.id.adapter_pessoa_BtnRemover);

        _btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long _id = Long.valueOf(v.getTag().toString());

                for (EventoMO _item : m_BaseList)
                {
                    if (_item.getID() == _id)
                    {
                        Intent intent = new Intent(m_Context, MaintenanceEventFragment.class);
                        intent.putExtra("id", _item.getID());
                        /*intent.putExtra("nome", _item.getNome());
                        intent.putExtra("endereco", _item.getEndereco());
                        intent.putExtra("cidade", _item.getCidade());
                        intent.putExtra("numero", String.valueOf(_item.getNumero()));
                        intent.putExtra("telefone", _item.getTelefone()); */

                        m_Context.startActivity(intent);

                        break;
                    }
                }
            }
        });

        _btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long _id = Long.valueOf(v.getTag().toString());
                new EventoMT().Delete(_id);

                if (RefreshListViewListener != null)
                {
                    RefreshListViewListener.Refresh();
                }
            }
        });


        _btnUpdate.setTag(_rotaTO.getID());
        _btnRemove.setTag(_rotaTO.getID());

        return _view;
    }

    public IRefreshListView getRefreshListViewListener() {
        return RefreshListViewListener;
    }

    public void setRefreshListViewListener(IRefreshListView refreshListViewListener) {
        RefreshListViewListener = refreshListViewListener;
    }


    public interface IRefreshListView
    {
        void Refresh();
    }
}
