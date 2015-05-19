package unisc.eventmanager.unisceventmanager.methods;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import unisc.eventmanager.unisceventmanager.classes.EncontroMO;
import unisc.eventmanager.unisceventmanager.classes.EventoMO;
import unisc.eventmanager.unisceventmanager.database.DataBaseEngine;

/**
 * Created by FAGNER on 22/04/2015.
 */
public class EventoMT {


    public SQLiteDatabase m_DataBase = null;
    public DataBaseEngine m_dbEngine = null;

    public EventoMT(Context context)
    {
        m_dbEngine = new DataBaseEngine(context);
    }


    public long Salvar(EventoMO value) {

        ContentValues _values = new ContentValues();

        //_values.put("id", value.getID());
        _values.put("descricao", value.getDescricao());
        _values.put("data_inicial", value.getDataInicial().getTime());
        _values.put("data_final", value.getDataFinal().getTime());

        m_DataBase = m_dbEngine.getWritableDatabase();

        long _insertedId = m_DataBase.insert("evento", null, _values);

        return _insertedId;
    }

    private void salvarEncontros(EventoMO value) {


        for  (EncontroMO _encontroMO: value.GetEncontros()) {

            ContentValues _values = new ContentValues();

            _values.put("evento_id", value.getID());
            _values.put("descricao", value.getDescricao());
            _values.put("data_inicial", value.getDataInicial().getTime());
            _values.put("data_final", value.getDataFinal().getTime());

            m_DataBase = m_dbEngine.getWritableDatabase();

            long _insertedId = m_DataBase.insert("encontro", null, _values);

        }
    }


    public void Atualizar(EventoMO value) {

         }

    public void Delete(long id)
    {

    }

    public ArrayList<EventoMO> BuscaPessoas(EventoMO pessoa)
    {
        ArrayList<EventoMO> _resp = new ArrayList<>();



        return _resp;
    }
}
