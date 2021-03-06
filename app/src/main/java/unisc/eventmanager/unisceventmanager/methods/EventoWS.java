package unisc.eventmanager.unisceventmanager.methods;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import unisc.eventmanager.unisceventmanager.classes.EncontroMO;
import unisc.eventmanager.unisceventmanager.classes.EventoMO;
import unisc.eventmanager.unisceventmanager.database.DataBaseEngine;

/**
 * Created by FAGNER on 22/04/2015.
 */
public class EventoWS {

    private static final String BASE_URL = "http://brunopdm.jossandro.com/";
    public SQLiteDatabase m_DataBase = null;
    public DataBaseEngine m_dbEngine = null;
    private Context m_Context;
    private IEventoResult m_EventoResult;

    public EventoWS(Context context)
    {
        m_Context = context;
        m_dbEngine = new DataBaseEngine(context);
    }

    public void ExcluirEncontro(final long encontroID){

        AsyncTask<Void, Void, Void> _task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                StringBuilder _builderURL = new StringBuilder();

                _builderURL.append("http://brunopdm.jossandro.com/encontro/?acao=excluir");
                _builderURL.append("&cod_encontro=").append(encontroID);

                Log.d("WBS","URL: "+_builderURL.toString());

                URL url= null;
                URI uri = null;
                try {

                    url = new URL(_builderURL.toString());
                    uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                JsonArrayRequest req = new JsonArrayRequest(

                        uri.toASCIIString(), new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
                );

                RequestQueue queue = Volley.newRequestQueue(m_Context);
                queue.add(req);

                return null;
            }
        };

        _task.execute();
    }


    public void AtualizarEncontro(final long eventoID, final EncontroMO encontro, final IEventoResult result){

        AsyncTask<Void, Void, Void> _task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                StringBuilder _builderURL = new StringBuilder();

                DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat fmtHora = new SimpleDateFormat("hh:mm");

                _builderURL.append("http://brunopdm.jossandro.com/encontro/?acao=alterar");
                _builderURL.append("&cod_encontro=").append(encontro.getID());
                _builderURL.append("&nome_encontro=").append(encontro.getDescricao());

                try {
                    if (encontro.getDataInicial() != null){
                        _builderURL.append("&data_encontro=").append(fmt.format(encontro.getDataInicial()));
                    }

                    if (encontro.getDataInicial() != null){
                        _builderURL.append("&hora_inicio=").append(fmtHora.format(encontro.getDataInicial()));
                    }

                    if (encontro.getDataFinal() != null){
                        _builderURL.append("&hora_fim=").append(fmtHora.format(encontro.getDataFinal()));
                    }
                }
                catch (Exception ex){

                }


                _builderURL.append("&cod_evento=").append(eventoID);

                Log.d("WBS","URL: "+_builderURL.toString());

                URL url= null;
                URI uri = null;
                try {

                    url = new URL(_builderURL.toString());
                    uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                JsonArrayRequest req = new JsonArrayRequest(

                        uri.toASCIIString(), new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response){


                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
                );

                RequestQueue queue = Volley.newRequestQueue(m_Context);
                queue.add(req);

                return null;
            }
        };

        _task.execute();
    }

    public void ListaEncontros(long eventoID, final IEncontroResult result) {

        StringBuilder _builderURL = new StringBuilder();

        _builderURL.append("http://brunopdm.jossandro.com/encontro/?acao=lista_encontros");
        _builderURL.append("&cod_evento=").append(eventoID);

        Log.d("WBS", "URL: " + _builderURL.toString());

        URL url = null;
        URI uri = null;
        try {

            url = new URL(_builderURL.toString());
            uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        final URI finalUri = uri;
        AsyncTask<Void, Void, Void> _task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                JsonArrayRequest req = new JsonArrayRequest(
                        finalUri.toASCIIString(), new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        ArrayList<EncontroMO> _encontros = new ArrayList<EncontroMO>();

                        //Fazendo PARSE do JSON
                        String valores = "";
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonKeyValue = response.getJSONObject(i);
                                EncontroMO _encontro = jsobjToListEncontro(jsonKeyValue);

                                _encontros.add(_encontro);
                            }

                            result.Result(_encontros);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("WBS", _encontros.toString());
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                    }
                });

                RequestQueue queue = Volley.newRequestQueue(m_Context);
                queue.add(req);

                return null;
            }
        };

        _task.execute();
    }

    private EncontroMO jsobjToListEncontro(JSONObject jsonKeyValue) {

        EncontroMO _encontro = new EncontroMO();

        try{
            _encontro.setID(jsonKeyValue.getInt("cod_encontro"));
            _encontro.setDescricao(jsonKeyValue.getString("nome_encontro"));

            try{

                String _dataEncontro = jsonKeyValue.getString("data_encontro");

                if (!_dataEncontro.equals("")) {

                    String _horaDe = jsonKeyValue.getString("hora_inicio");
                    if (!_horaDe.equals("")) {
                        Date _dataHoraDe = parseDateTime(_dataEncontro+" " + _horaDe);

                        _encontro.setDataInicial(_dataHoraDe);
                    }

                    String _horaAte = jsonKeyValue.getString("hora_fim");
                    if (!_horaAte.equals("")) {
                        Date _dataHoraAte = parseDateTime(_dataEncontro+" " + _horaAte);

                        _encontro.setDataFinal(_dataHoraAte);
                    }



                }

            }
            catch (Exception ex){

            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return _encontro;
    }

    public void ListaEventos() {


        StringBuilder _builderURL = new StringBuilder();

        _builderURL.append(BASE_URL);
        _builderURL.append("evento");

        Log.d("WBS", "URL: " + _builderURL.toString());

        URL url = null;
        URI uri = null;
        try {

            url = new URL(_builderURL.toString());
            uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        final URI finalUri = uri;
        AsyncTask<Void, Void, Void> _task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                JsonArrayRequest req = new JsonArrayRequest(
                        finalUri.toASCIIString(), new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        ArrayList<EventoMO> _eventos = new ArrayList<EventoMO>();

                        //Fazendo PARSE do JSON
                        String valores = "";
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonKeyValue = response.getJSONObject(i);
                                EventoMO _evento = jsobjToListEvento(jsonKeyValue);
                                _eventos.add(_evento);
                            }


                            if (m_EventoResult != null){
                                m_EventoResult.ListaEventosResult(_eventos);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("WBS", _eventos.toString());
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(m_Context, "Problema ao buscar dados da web", Toast.LENGTH_SHORT).show();
                        Log.d("WBS", error.toString());
                    }
                }
                );


                RequestQueue queue = Volley.newRequestQueue(m_Context);
                queue.add(req);


                return null;
            }
        };

        _task.execute();


    }

    public void RetornaEvento(long id, final IGetEventoResult result) {

        StringBuilder _builderURL = new StringBuilder();

        _builderURL.append(BASE_URL);
        _builderURL.append("evento/?acao=ver&");
        _builderURL.append("cod_evento=").append(id);

        Log.d("WBS", "URL: " + _builderURL.toString());

        URL url = null;
        URI uri = null;
        try {

            url = new URL(_builderURL.toString());
            uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        final URI finalUri = uri;
        AsyncTask<Void, Void, Void> _task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                JsonArrayRequest req = new JsonArrayRequest(

                        finalUri.toASCIIString(), new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        EventoMO _evento = null;

                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonKeyValue = response.getJSONObject(i);
                                _evento = jsobjToRetornaEvento(jsonKeyValue);

                                break;
                            }

                            if (result != null){
                                result.Result(_evento);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(m_Context, "Problema ao buscar dados da web", Toast.LENGTH_SHORT).show();
                        Log.d("WBS", error.toString());
                    }
                });

                RequestQueue queue = Volley.newRequestQueue(m_Context);
                queue.add(req);

                return null;
            }
        };

        _task.execute();
    }

    public void Atualizar(EventoMO evento, final IEventoResult result){

        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

        StringBuilder _builderURL = new StringBuilder();

        _builderURL.append(BASE_URL);
        _builderURL.append("evento/?acao=update");
        _builderURL.append("&cod_evento=").append(evento.getID());
        _builderURL.append("&nome_evento=").append(evento.getDescricao());

        if (evento.getDataInicial() != null){
            _builderURL.append("&data_inicio=").append(fmt.format(evento.getDataInicial()));
        }

        if (evento.getDataFinal() != null){
            _builderURL.append("&data_fim=").append(fmt.format(evento.getDataFinal()));
        }


        Log.d("WBS", "URL: " + _builderURL.toString());

        URL url = null;
        URI uri = null;
        try {

            url = new URL(_builderURL.toString());
            uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        final URI finalUri = uri;
        AsyncTask<Void, Void, Void> _task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                JsonArrayRequest req = new JsonArrayRequest(

                        finalUri.toASCIIString(), new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        result.ListaEventosResult(null);

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       error.printStackTrace();

                        result.ListaEventosResult(null);
                    }
                }
                );


                RequestQueue queue = Volley.newRequestQueue(m_Context);
                queue.add(req);


                return null;
            }
        };

        _task.execute();
    }

    public void Remover(long eventoID, final IEventoResult result){

        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

        StringBuilder _builderURL = new StringBuilder();

        _builderURL.append(BASE_URL);
        _builderURL.append("evento/?acao=excluir");
        _builderURL.append("&cod_evento=").append(eventoID);

        Log.d("WBS", "URL: " + _builderURL.toString());

        URL url = null;
        URI uri = null;
        try {

            url = new URL(_builderURL.toString());
            uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        final URI finalUri = uri;
        AsyncTask<Void, Void, Void> _task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                JsonArrayRequest req = new JsonArrayRequest(

                        finalUri.toASCIIString(), new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        result.ListaEventosResult(null);
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        result.ListaEventosResult(null);
                        }
                }
                );


                RequestQueue queue = Volley.newRequestQueue(m_Context);
                queue.add(req);


                return null;
            }
        };

        _task.execute();
    }

    public void InserirEncontro(final long eventoID, final EncontroMO encontro, final IEventoResult result){

        AsyncTask<Void, Void, Void> _task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                StringBuilder _builderURL = new StringBuilder();

                DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat fmtHora = new SimpleDateFormat("hh:mm");

                _builderURL.append("http://brunopdm.jossandro.com/encontro/?acao=inserir");
                _builderURL.append("&nome_encontro=").append(encontro.getDescricao());

                try {
                    if (encontro.getDataInicial() != null){
                        _builderURL.append("&data_encontro=").append(fmt.format(encontro.getDataInicial()));
                    }

                    if (encontro.getDataInicial() != null){
                        _builderURL.append("&hora_inicio=").append(fmtHora.format(encontro.getDataInicial()));
                    }

                    if (encontro.getDataFinal() != null){
                        _builderURL.append("&hora_fim=").append(fmtHora.format(encontro.getDataFinal()));
                    }
                }
                catch (Exception ex){

                }


                _builderURL.append("&cod_evento=").append(eventoID);

                Log.d("WBS","URL: "+_builderURL.toString());

                URL url= null;
                URI uri = null;
                try {

                    url = new URL(_builderURL.toString());
                    uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                JsonArrayRequest req = new JsonArrayRequest(

                        uri.toASCIIString(), new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response){


                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       error.printStackTrace();
                    }
                }
                );

                RequestQueue queue = Volley.newRequestQueue(m_Context);
                queue.add(req);

                return null;
            }
        };

        _task.execute();
    }

    public void Salvar(final EventoMO p, final IEventoResult result){

        AsyncTask<Void, Void, Void> _task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                StringBuilder _builderURL = new StringBuilder();

                DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

                _builderURL.append(BASE_URL);
                _builderURL.append("evento/?acao=inserir");
                _builderURL.append("&nome_evento=").append(p.getDescricao());
                _builderURL.append("&data_inicio=").append(fmt.format(p.getDataInicial()));
                _builderURL.append("&data_fim=").append(fmt.format(p.getDataFinal()));

                Log.d("WBS","URL: "+_builderURL.toString());

                URL url= null;
                URI uri = null;
                try {

                    url = new URL(_builderURL.toString());
                    uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                JsonArrayRequest req = new JsonArrayRequest(
                        uri.toASCIIString(), new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response){

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonKeyValue = response.getJSONObject(i);

                                p.setID(jsonKeyValue.getLong("cod_evento"));

                                break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        result.ListaEventosResult(null);

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(m_Context, "Problema ao buscar dados da web", Toast.LENGTH_SHORT).show();
                        Log.d("WBS", error.toString());
                    }
                }
                );

                RequestQueue queue = Volley.newRequestQueue(m_Context);
                queue.add(req);

                return null;
            }
        };

        _task.execute();
    }

    private EventoMO jsobjToRetornaEvento(JSONObject jsonKeyValue) {
        EventoMO p = new EventoMO();
        try{
            p.setID(jsonKeyValue.getInt("cod_evento"));
            p.setDescricao(jsonKeyValue.getString("nome_evento"));

            try{

                String _data_inicio = jsonKeyValue.getString("data_inicio");
                if (!_data_inicio.equals("")){
                    p.setDataInicial(parseDateTime(_data_inicio +" 08:00"));
                }

                String _data_fim = jsonKeyValue.getString("data_fim");
                if (!_data_fim.equals("")){
                    p.setDataFinal(parseDateTime(_data_fim+" 08:00"));
                }



            }
            catch (Exception ex){

            }


          //  p.setEmail(json.getString("email"));
          //  p.setMatricula(json.getInt("matricula"));
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return p;
    }

    public static Date parseDateTime(String dateString) {
        if (dateString == null || dateString.equals(""))
            return null;

        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        try {
            return fmt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    private EventoMO jsobjToListEvento(JSONObject jsonKeyValue) {
        EventoMO p = new EventoMO();
        try{
            p.setID(jsonKeyValue.getInt("cod_evento"));
            p.setDescricao(jsonKeyValue.getString("nome_evento"));

        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return p;
    }

    public IEventoResult getM_EventoResult() {
        return m_EventoResult;
    }

    public void setEventoResult(IEventoResult m_EventoResult) {
        this.m_EventoResult = m_EventoResult;
    }


    public interface IEventoResult{
        void ListaEventosResult(ArrayList<EventoMO> eventos);
    }

    public interface IGetEventoResult{
        void Result(EventoMO evento);
    }

    public interface IEncontroResult{
        void Result(ArrayList<EncontroMO> encontros);
    }

}
