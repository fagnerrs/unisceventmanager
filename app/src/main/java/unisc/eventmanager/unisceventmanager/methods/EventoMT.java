package unisc.eventmanager.unisceventmanager.methods;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

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

        value.setID(_insertedId);

        salvarNovosEncontros(value);

        return _insertedId;
    }

    public long Update(EventoMO evento, ArrayList<Long> encontrosDeletados)
    {
        ContentValues _values = new ContentValues();

        _values.put("id", evento.getID());
        _values.put("descricao", evento.getDescricao());
        _values.put("data_inicial", evento.getDataInicial().getTime());
        _values.put("data_final", evento.getDataFinal().getTime());

        m_DataBase = m_dbEngine.getWritableDatabase();
        m_DataBase.update("evento", _values, "id = ?", new String[] { String.valueOf(evento.getID()) });

        salvarNovosEncontros(evento);

        for (EncontroMO _encontroMO : evento.GetEncontros())
        {
            if (_encontroMO.getID() > 0)
            {
                updateEncontro(_encontroMO);
            }
        }

        for (long _encontroID : encontrosDeletados)
        {
            if (_encontroID > 0)
            {
                deleteEncontro(_encontroID);
            }
        }

        return evento.getID();
    }

    private void deleteEncontro(long id)
    {
        m_DataBase = m_dbEngine.getWritableDatabase();
        m_DataBase.delete("encontro", "id = " + String.valueOf(id) , null);
    }

    private long updateEncontro(EncontroMO encontro)
    {
        ContentValues _values = new ContentValues();

        _values.put("id", encontro.getID());
        _values.put("descricao", encontro.getDescricao());
        _values.put("data_inicial", encontro.getDataInicial().getTime());
        _values.put("data_final", encontro.getDataFinal().getTime());

        m_DataBase = m_dbEngine.getWritableDatabase();
        m_DataBase.update("encontro", _values, "id = ?", new String[] { String.valueOf(encontro.getID()) });

        return encontro.getID();
    }

    private void salvarNovosEncontros(EventoMO value) {


        for  (EncontroMO _encontroMO: value.GetEncontros()) {

            if (_encontroMO.getID() > 0)
                continue;

            ContentValues _values = new ContentValues();

            _values.put("evento_id", value.getID());
            _values.put("descricao", _encontroMO.getDescricao());
            _values.put("data_inicial", _encontroMO.getDataInicial().getTime());
            _values.put("data_final", _encontroMO.getDataFinal().getTime());

            m_DataBase = m_dbEngine.getWritableDatabase();

            long _insertedId = m_DataBase.insert("encontro", null, _values);

        }
    }

    public void Atualizar(EventoMO value) {

         }

    public void Delete(long id)
    {
        m_DataBase = m_dbEngine.getWritableDatabase();

        m_DataBase.delete("encontro", "evento_id = " + String.valueOf(id) , null);
        m_DataBase.delete("evento", "id = " + String.valueOf(id) , null);
    }

    public EventoMO BuscaEventoById(long id)
    {
        EventoMO _resp = new EventoMO();

        StringBuilder _querySQL = new StringBuilder().
                append(" select id, ")
                .append("  descricao,   ")
                .append("  data_inicial,   ")
                .append("  data_final   ")
                .append(" from evento ")
                .append(" where id = ").append(String.valueOf(id));


        m_DataBase = m_dbEngine.getReadableDatabase();

        Cursor _cursorEvento = m_DataBase.
                rawQuery(_querySQL.toString(), null);

        while (_cursorEvento.moveToNext()) {

            _resp.setID(_cursorEvento.getLong(0));
            _resp.setDescricao(_cursorEvento.getString(1));
            _resp.setDataInicial(new Date(_cursorEvento.getLong(2)));
            _resp.setDataFinal(new Date(_cursorEvento.getLong(3)));

            break;
        }

        m_DataBase.close();

        if (_resp.getID() > 0)
        {
            _querySQL = new StringBuilder().
                    append(" select id, ")
                    .append("  descricao,   ")
                    .append("  data_inicial,   ")
                    .append("  data_final   ")
                    .append(" from encontro ")
                    .append(" where evento_id = ").append(String.valueOf(id));

            m_DataBase = m_dbEngine.getReadableDatabase();

            Cursor _cursorEncontros = m_DataBase.
                    rawQuery(_querySQL.toString(), null);

            while (_cursorEncontros.moveToNext()) {

                EncontroMO _encontroMO = new EncontroMO();
                _encontroMO.setID(_cursorEncontros.getLong(0));
                _encontroMO.setDescricao(_cursorEncontros.getString(1));
                _encontroMO.setDataInicial(new Date(_cursorEncontros.getLong(2)));
                _encontroMO.setDataFinal(new Date(_cursorEncontros.getLong(3)));

                _resp.GetEncontros().add(_encontroMO);
            }
        }

        m_DataBase.close();

        return _resp;
    }

    public ArrayList<EventoMO> BuscaEventos(EventoMO pessoa)
    {
        ArrayList<EventoMO> _resp = new ArrayList<>();

        StringBuilder _querySQL = new StringBuilder().
                append(" select id, ")
                .append("  descricao,   ")
                .append("  data_inicial,   ")
                .append("  data_final   ")
                .append(" from evento ");

        if (pessoa != null && !pessoa.getDescricao().equals(""))
        {
            _querySQL.append(" where descricao like '%"+pessoa.getDescricao()+"%'");
        }

        m_DataBase = m_dbEngine.getReadableDatabase();

        Cursor _cursorPedidos = m_DataBase.
                rawQuery(_querySQL.toString(), null);

        while (_cursorPedidos.moveToNext()) {

            EventoMO _eventoMO = new EventoMO();
            _eventoMO.setID(_cursorPedidos.getLong(0));
            _eventoMO.setDescricao(_cursorPedidos.getString(1));
            _eventoMO.setDataInicial(new Date(_cursorPedidos.getLong(2)));
            _eventoMO.setDataFinal(new Date(_cursorPedidos.getLong(3)));

            _resp.add(_eventoMO);
        }

        m_DataBase.close();

        return _resp;
    }
}
