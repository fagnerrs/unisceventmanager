package unisc.eventmanager.unisceventmanager.classes;

/**
 * Created by FAGNER on 07/06/2015.
 */
public class PessoaMO {

    private long ID;
    private long Matricula;
    private String Nome;
    private String Email;
    private byte[] Foto;

    public byte[] getFoto() {
        return Foto;
    }

    public void setFoto(byte[] foto) {
        Foto = foto;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public long getMatricula() {
        return Matricula;
    }

    public void setMatricula(long matricula) {
        Matricula = matricula;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }
}
