package it.unisa.diem.dao.postgres;
import it.unisa.diem.dao.interfacce.DAO;
import it.unisa.diem.dao.interfacce.DomandaDAO;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.sessione.Domanda;
import it.unisa.diem.model.gestione.sessione.Sessione;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.TipoDomanda;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DomandaDAOPostgres implements DomandaDAO {

    private final String url;
    private final String user;
    private final String pass;
    private final SessioneDAOPostgres sessioneDAO;

    public DomandaDAOPostgres(String url, String user, String pass) {

        this.url = url;
        this.user = user;
        this.pass = pass;
        this.sessioneDAO = new SessioneDAOPostgres(url, user, pass);

    }

    @Override
    public Optional<Domanda> selectById(long id) throws DBException {

        Optional<Domanda> result = Optional.empty();

        String query = "SELECT * FROM Domanda WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

            PreparedStatement cmd = connection.prepareStatement (query) ) {

            cmd.setLong(1, id);

            ResultSet rs = cmd.executeQuery();

            Domanda domanda = null;

            if (rs.next()) {
                domanda = getQuestion(rs);
            }

            result = Optional.ofNullable(domanda);

        }   catch(SQLException e){
                throw new DBException("Errore: impossibile selezionare la domanda!",e);
        }

        return result;

    }

    @Override
    public List<Domanda> selectAll() throws DBException {

        List<Domanda> domande = new ArrayList<>();

        String query = "SELECT * FROM Domanda";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd=connection.prepareStatement (query) ){

            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                domande.add ( getQuestion(rs) );
            }

        }  catch(SQLException e){
            throw new DBException("ERRORE: impossibile selezionare le sessioni attive!",e);
        }

        return domande;

    }

    @Override
    public void insert(Domanda domanda) throws DBException {

        String query = "INSERT INTO DOMANDA (id_sessione, numeroDomanda, rispostaCorretta, testo, time_stop, \"tipoDomanda\") VALUES (?,?,?,?,?,?)";


        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd=connection.prepareStatement (query, Statement.RETURN_GENERATED_KEYS) ){

            setQuestionForInsert(cmd, domanda);

            int lines = cmd.executeUpdate();
            if(lines == 0)
                throw new DBException("Errore: nessuna riga modificata");

            try (ResultSet rs = cmd.getGeneratedKeys()) {

                if (rs.next()) {
                    domanda.setId(rs.getLong(1));
                }
            }

        }    catch(SQLException e){
            throw new DBException("Errore: impossibile inserire la sessione attiva " + domanda.getId() + "!",e);
        }

    }

    @Override
    public void update(Domanda domanda) throws DBException {

        String query = "UPDATE DOMANDA " +
                "SET time_stop = ? " +
                "WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd=connection.prepareStatement (query) ){

            setQuestionForUpdate(cmd, domanda);

            int lines = cmd.executeUpdate();

            if(lines == 0)
                throw new DBException("Errore: nessuna riga modificata");

        }   catch(SQLException e){
            throw new DBException("Errore: impossibile aggiornare la domanda " + domanda.getId() + "!",e);
        }

    }



    @Override
    public void delete(Domanda domanda) throws DBException {

        String query = "DELETE FROM DOMANDA WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd = connection.prepareStatement(query)) {

            setQuestionForDelete(cmd, domanda);

            int lines = cmd.executeUpdate();

            if (lines == 0)
                throw new DBException("Errore: nessuna riga modificata");

        } catch (SQLException e) {
            throw new DBException("Errore: impossibile eliminare la domanda " + domanda.getId() + "!", e);
        }

    }

    private Domanda getQuestion(ResultSet rs) throws SQLException, DBException {

        Domanda domanda = null;

        long id = rs.getLong("id");

        long idSessione = rs.getLong("id_sessione");

        Sessione sessione = getSession(idSessione);

        int numeroDomanda = rs.getInt("numeroDomanda");

        String testo = rs.getString("testo");

        TipoDomanda tipo = TipoDomanda.valueOf(rs.getString("tipoDomanda"));

        String rispostaCorretta = rs.getString("rispostaCorretta");

        Time tempo = rs.getTime("time_stop");

        int secondi = 0;

        if (tempo != null)
            secondi = tempo.toLocalTime().toSecondOfDay();

        domanda = new Domanda(id, sessione, numeroDomanda, testo, tipo, rispostaCorretta, secondi);

        return domanda;

    }

    private Sessione getSession(long id) throws SQLException, DBException {

        Optional<Sessione> optionalSessione = sessioneDAO.selectById(id);

        return optionalSessione.orElseThrow(() ->
                new DBException("ERRORE: Sessione " + id + " non trovata!")
        );

    }

    private void setQuestionForInsert(PreparedStatement cmd, Domanda domanda) throws SQLException {

        cmd.setLong(1, domanda.getSessione().getId());
        cmd.setInt(2, domanda.getNumeroDomanda());
        cmd.setString(3, domanda.getRispostaCorretta());
        cmd.setString(4, domanda.getTestoDomanda());

        LocalTime tempo = LocalTime.ofSecondOfDay(domanda.getTempoLimiteSecondi());

        cmd.setTime(5, Time.valueOf(tempo));

        cmd.setObject(6, domanda.getTipo().name(), java.sql.Types.OTHER);

    }

    private void setQuestionForUpdate(PreparedStatement cmd, Domanda domanda) throws SQLException {

        LocalTime tempo = LocalTime.ofSecondOfDay(domanda.getTempoLimiteSecondi());

        cmd.setTime(1, Time.valueOf(tempo));

        cmd.setLong(2, domanda.getId());

    }

    private void setQuestionForDelete(PreparedStatement cmd, Domanda domanda) throws SQLException {

        cmd.setLong(1, domanda.getId());

    }

    public void deleteBySessioneId(long sessioneId) throws DBException {
        String query = "DELETE FROM DOMANDA WHERE id_sessione = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement cmd = connection.prepareStatement(query)) {

            cmd.setLong(1, sessioneId);

            int lines = cmd.executeUpdate();
            System.out.println("✅ Domande eliminate: " + lines);

        } catch (SQLException e) {
            throw new DBException("Errore: impossibile eliminare le domande della sessione " + sessioneId + "!", e);
        }
    }


}
