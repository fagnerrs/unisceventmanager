package unisc.eventmanager.unisceventmanager.classes;

import java.util.Date;

/**
 * Created by FAGNER on 07/06/2015.
 */
public class PresencaMO {

    private long ID;
    private EventoMO Evento;
    private EncontroMO Encontro;
    private PessoaMO Pessoa;
    private Date DataChegada;
    private Date DataSaida;

    public Date getDataSaida() {
        return DataSaida;
    }

    public void setDataSaida(Date dataSaida) {
        DataSaida = dataSaida;
    }

    public Date getDataChegada() {
        return DataChegada;
    }

    public void setDataChegada(Date dataChegada) {
        DataChegada = dataChegada;
    }

    public PessoaMO getPessoa() {
        return Pessoa;
    }

    public void setPessoa(PessoaMO pessoa) {
        Pessoa = pessoa;
    }

    public EncontroMO getEncontro() {
        return Encontro;
    }

    public void setEncontro(EncontroMO encontro) {
        Encontro = encontro;
    }

    public EventoMO getEvento() {
        return Evento;
    }

    public void setEvento(EventoMO evento) {
        Evento = evento;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }
}
