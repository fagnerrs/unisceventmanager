package unisc.eventmanager.unisceventmanager.methods;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import unisc.eventmanager.unisceventmanager.classes.EncontroMO;
import unisc.eventmanager.unisceventmanager.classes.EventoMO;
import unisc.eventmanager.unisceventmanager.classes.PessoaMO;
import unisc.eventmanager.unisceventmanager.classes.PresencaMO;
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

    public ArrayList<PresencaMO> BuscaPresencas() {

        ArrayList<PresencaMO> _resp = new ArrayList<>();

        PessoaMO _pessoa = new PessoaMO();
        _pessoa.setID(1);
        _pessoa.setMatricula(56321);
        _pessoa.setEmail("fagnerrs@gmail.com");
        _pessoa.setNome("Fagner L Oliveira");

        PessoaMO _pessoa1 = new PessoaMO();
        _pessoa1.setID(1);
        _pessoa1.setMatricula(33533);
        _pessoa1.setEmail("carlosrs@gmail.com");
        _pessoa1.setNome("Carlos João");

        PessoaMO _pessoa2 = new PessoaMO();
        _pessoa2.setID(1);
        _pessoa2.setMatricula(85855);
        _pessoa2.setEmail("felipers@gmail.com");
        _pessoa2.setNome("Felipe Antonio");

        EventoMO _evento = new EventoMO();
        _evento.setDescricao("JAC - 2015");

        EncontroMO _encontro = new EncontroMO();
        _encontro.setDescricao("Segunda-Feira - Jogos e Mídia");


        PresencaMO _presensa1 = new PresencaMO();
        _presensa1.setID(1);
        _presensa1.setDataChegada(new Date());
        _presensa1.setDataSaida(new Date());
        _presensa1.setEncontro(_encontro);
        _presensa1.setEvento(_evento);
        _presensa1.setPessoa(_pessoa);


        PresencaMO _presensa2 = new PresencaMO();
        _presensa2.setID(2);
        _presensa2.setDataChegada(new Date());
        _presensa2.setDataSaida(new Date());
        _presensa2.setEncontro(_encontro);
        _presensa2.setEvento(_evento);
        _presensa2.setPessoa(_pessoa1);


        PresencaMO _presensa3 = new PresencaMO();
        _presensa3.setID(3);
        _presensa3.setDataChegada(new Date());
        _presensa3.setDataSaida(new Date());
        _presensa3.setEncontro(_encontro);
        _presensa3.setEvento(_evento);
        _presensa3.setPessoa(_pessoa2);

        _resp.add(_presensa1);
        _resp.add(_presensa2);
        _resp.add(_presensa3);

        return _resp;
    }

    public EncontroMO BuscaEncontroID(long id) {

        EncontroMO _resp = new EncontroMO();

        StringBuilder _querySQL = new StringBuilder().
                append(" select id, ")
                .append("  descricao,   ")
                .append("  data_inicial,   ")
                .append("  data_final   ")
                .append(" from encontro ")
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

        return _resp;
    }
}
