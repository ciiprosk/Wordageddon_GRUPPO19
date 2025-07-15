package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.SessioneDAO;
import it.unisa.diem.dao.interfacce.SessioneDocumentoDAO;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Documento;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.sessione.Sessione;
import it.unisa.diem.model.gestione.sessione.SessioneDocumento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessioneDocumentoDAOPostgres implements SessioneDocumentoDAO {

    private final String url;
    private final String user;
    private final String pass;

    public SessioneDocumentoDAOPostgres(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    @Override
    public List<Documento> selectDocumentsBySession(long idSessione) throws DBException {

        List<Documento> documenti = new ArrayList<>();

        String query = "SELECT d.* " +
                "FROM sessionedocumento s JOIN documento d ON s.documento = d.nome " +
                "WHERE sessione = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd = connection.prepareStatement (query) ){

            cmd.setLong(1, idSessione);
            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {

                documenti.add(getDocument(rs));

            }


        } catch(SQLException e){
            throw new DBException("ERRORE: impossibile trovare i documenti della sessione " + idSessione + "!",e);
        }

        return documenti;

    }

    @Override
    public List<SessioneDocumento> selectAll() throws DBException {

        List<SessioneDocumento> sessioneDocumenti = new ArrayList<>();

        String query = "SELECT * FROM sessionedocumento";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd = connection.prepareStatement (query) ){

            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {

                sessioneDocumenti.add(getSessionDocument(rs));

            }


        } catch(SQLException e){
            throw new DBException("ERRORE: impossibile caricare i documenti delle relative sessioni!",e);
        }

        return sessioneDocumenti;

    }

    @Override
    public void insert(SessioneDocumento sessioneDocumento) throws DBException {


        String query = "INSERT INTO SESSIONEDOCUMENTO VALUES(?,?)";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd=connection.prepareStatement (query) ){

            cmd.setLong(1, sessioneDocumento.getIdSessione());
            cmd.setString(2, sessioneDocumento.getNomeDocumento());

            int lines = cmd.executeUpdate();

            if(lines == 0)
                throw new DBException("Errore: nessuna riga modificata");

        } catch (SQLException e) {
            throw new DBException("ERRORE: Impossibile inserire sessione-documento!",e);
        }

    }

    @Override
    public void delete(SessioneDocumento SessioneDocumento) throws DBException {

        String query = "DELETE FROM SESSIONEDOCUMENTO WHERE sessione=? AND documento=?";

        try(Connection connection = DriverManager.getConnection(url, user, pass);

            PreparedStatement cmd = connection.prepareStatement(query)){

            cmd.setLong(1, SessioneDocumento.getIdSessione());
            cmd.setString(2, SessioneDocumento.getNomeDocumento());

            int lines= cmd.executeUpdate();
            if(lines == 0)
                throw new  DBException("ERRORE: nessuna riga modificata");

        }catch(SQLException e){

            throw new DBException("ERRORE: Impossibile cancellare sessione-documento!",e);

        }

    }

    private Documento getDocument(ResultSet rs) throws SQLException {

        Documento documento = null;

        String titolo = rs.getString("nome");
        Lingua lingua = Lingua.valueOf(rs.getString("lingua"));
        Difficolta difficolta = Difficolta.valueOf(rs.getString("difficolta"));
        String path = rs.getString("percorso");

        documento = new Documento(titolo, lingua, difficolta, path);

        return documento;

    }

    private SessioneDocumento getSessionDocument(ResultSet rs) throws SQLException {

        SessioneDocumento sessioneDocumento = null;

        long idSessione = rs.getLong("sessione");

        String documento = rs.getString("documento");

        sessioneDocumento = new SessioneDocumento(idSessione, documento);

        return sessioneDocumento;

    }

    public void deleteBySessioneId(long sessioneId) throws DBException {
        String query = "DELETE FROM SESSIONEDOCUMENTO WHERE sessione = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement cmd = connection.prepareStatement(query)) {

            cmd.setLong(1, sessioneId);

            int lines = cmd.executeUpdate();
            System.out.println("✅ SessioneDocumento eliminati: " + lines);

        } catch (SQLException e) {
            throw new DBException("Errore: impossibile eliminare sessione-documento della sessione " + sessioneId + "!", e);
        }
    }


}
