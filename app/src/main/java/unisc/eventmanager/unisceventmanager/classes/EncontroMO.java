package unisc.eventmanager.unisceventmanager.classes;

import java.util.Date;

/**
 * Created by FAGNER on 12/05/2015.
 */
public class EncontroMO {
    private long ID;
    private String Descricao;
    private Date DataInicial;
    private Date DataFinal;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public Date getDataInicial() {
        return DataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        DataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return DataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        DataFinal = dataFinal;
    }
}
