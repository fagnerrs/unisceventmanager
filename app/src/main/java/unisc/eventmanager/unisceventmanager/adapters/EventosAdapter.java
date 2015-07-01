package unisc.eventmanager.unisceventmanager.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import unisc.eventmanager.unisceventmanager.MaintenanceEncontroActivity;
import unisc.eventmanager.unisceventmanager.MaintenanceEventActivity;
import unisc.eventmanager.unisceventmanager.R;
import unisc.eventmanager.unisceventmanager.classes.EncontroMO;
import unisc.eventmanager.unisceventmanager.classes.EventoMO;
import unisc.eventmanager.unisceventmanager.classes.NavigationManager;
import unisc.eventmanager.unisceventmanager.fragments.IRefreshFragment;
import unisc.eventmanager.unisceventmanager.methods.EventoMT;
import unisc.eventmanager.unisceventmanager.methods.EventoWS;
import unisc.eventmanager.unisceventmanager.qrcode.Contents;
import unisc.eventmanager.unisceventmanager.qrcode.QrCodeEncoder;


/**
 * Created by FAGNER on 28/03/2015.
 */
public class EventosAdapter extends BaseAdapter {

    private final Activity m_Context;
    private LayoutInflater m_BaseInflater;
    private final ArrayList<EventoMO> m_BaseList;
    private IRefreshFragment RefreshListViewListener;
    private ProgressDialog _progressD;

    public EventosAdapter(Activity context, ArrayList<EventoMO> baseList)
    {
        m_BaseList = baseList;
        m_BaseInflater = LayoutInflater.from(context);
        m_Context = context;
    }

    @Override
    public int getCount() {
        return m_BaseList == null?0: m_BaseList.size();
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

            Intent _int = new Intent(m_Context, MaintenanceEventActivity.class);
            _int.putExtra("id", _id);
            m_Context.startActivity(_int);
            }
        });

        _btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long _id = Long.valueOf(v.getTag().toString());

                new EventoWS(m_Context).Remover(_id, new EventoWS.IEventoResult() {
                    @Override
                    public void ListaEventosResult(ArrayList<EventoMO> eventos) {

                        if (RefreshListViewListener != null)
                        {
                            RefreshListViewListener.RefreshListView();
                        }
                    }
                });
            }
        });


        _btnUpdate.setTag(_rotaTO.getID());
        _btnRemove.setTag(_rotaTO.getID());

        return _view;
    }

    public IRefreshFragment getRefreshListViewListener() {
        return RefreshListViewListener;
    }

    public void setRefreshListViewListener(IRefreshFragment refreshListViewListener) {
        RefreshListViewListener = refreshListViewListener;
    }

}
