package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.UtenteDAO;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.utenti.Ruolo;
import it.unisa.diem.model.gestione.utenti.Utente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UtenteDAOPostgres implements UtenteDAO {

    private final String url;
    private final String user;
    private final String pass;

    public UtenteDAOPostgres(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    @Override
    public boolean emailAlreadyExists(String email) {

        String query = "SELECT COUNT(*) FROM utente WHERE email = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd = connection.prepareStatement (query) ){

            cmd.setString(1, email);

            ResultSet rs = cmd.executeQuery();

            if (rs.next()) {

                int count = rs.getInt(1);

                if (count > 0) {

                    return true;

                }
                else return false;

            } else return false;


        } catch (SQLException e) {

            throw new DBException("ERRORE: Impossibile recuperare info sull'email " + email, e);

        }

    }

    @Override
    public boolean usernameAlreadyExists(String username) {

        String query = "SELECT COUNT(*) FROM utente WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd = connection.prepareStatement (query) ){

            cmd.setString(1, username);

            ResultSet rs = cmd.executeQuery();

            if (rs.next()) {

                int count = rs.getInt(1);

                if (count > 0) {

                    return true;

                }
                else return false;

            } else return false;


        } catch (SQLException e) {

            throw new DBException("ERRORE: Impossibile recuperare info sull'username " + username, e);

        }

    }

    @Override
    public Optional<Utente> selectByUsername(String username) {

        Optional<Utente> result = Optional.empty();

        String query = "SELECT * FROM utente WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd = connection.prepareStatement (query) ){

                        cmd.setString(1, username);
                        ResultSet rs = cmd.executeQuery();

                        Utente utente = null;

                        if (rs.next()) {
                            utente = getUser(rs);
                        }

                        result = Optional.ofNullable(utente);

        } catch (SQLException e) {

            throw new DBException("ERRORE: Impossibile recuperare info sull'utente " + username, e);

        }

        return result;

    }

    @Override
    public List<Utente> selectAll() {

        List<Utente> utenti = new ArrayList<>();

        String query = "SELECT * FROM utente";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd=connection.prepareStatement (query) ){

                        ResultSet rs = cmd.executeQuery();

                        while (rs.next()) {
                            utenti.add ( getUser(rs) );
                        }

        } catch (SQLException e) {

                 throw new DBException("ERRPRE: Impossibile selezionare gli utenti",e);
        }

        return utenti;
    }

    @Override
    public void insert(Utente utente) {

        String query = "INSERT INTO utente (username,email,password,salt,ruolo) " +
                "VALUES (?,?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd=connection.prepareStatement (query) ){

                        setUserForInsert(cmd, utente);

                        cmd.executeUpdate();

        } catch (SQLException e) {

            throw new DBException("ERRORE: Impossibile inserire l'utente " + utente.getUsername(), e);

        }
    }

    @Override
    public void update(Utente utente) {     //NON PREVEDE l'aggiornamento di username

        String query = "UPDATE utente " +
                "SET email=?, password=?, ruolo=? " +
                "WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

                 PreparedStatement cmd=connection.prepareStatement (query) ){

                setUserForUpdate(cmd, utente);

                cmd.executeUpdate();

            } catch (SQLException e) {

                throw new DBException("ERRORE: Modifiche non riuscite all'utente " + utente.getUsername(), e);

            }

    }

    @Override
    public void update(String oldUsername, Utente utente) {     //PREVEDE aggiornamento username

        String query = "UPDATE utente " +
                "SET username=?, email=?, password=?, ruolo=? " +
                "WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd=connection.prepareStatement
                     (query)){

            setUserForUpdate(cmd, oldUsername, utente);

            cmd.executeUpdate();

        } catch (SQLException e) {

            throw new DBException("ERRORE: Modifiche non riuscite all'utente " + utente.getUsername(), e);

        }

    }


    public void delete(Utente utente) {

        String query = "DELETE FROM utente WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

            PreparedStatement cmd = connection.prepareStatement (query) ){

            setUserForDelete(cmd, utente);

            cmd.executeUpdate();

        } catch (SQLException e) {
                 throw new DBException("ERRORE: Impossibile cancellare l'utente " + utente.getUsername(), e);
        }

    }

    private Utente getUser(ResultSet rs) throws SQLException {

        Utente utente = null;

        String username = rs.getString("username");
        String email = rs.getString("email");
        String password = rs.getString("password");
        byte[] salt = rs.getBytes("salt");
        Ruolo ruolo = Ruolo.valueOf(rs.getString("ruolo"));

        utente = new Utente(username, email, password, salt, ruolo);

        return utente;

    }

    private void setUserForInsert(PreparedStatement cmd, Utente utente) throws SQLException {
        cmd.setString(1, utente.getUsername());
        cmd.setString(2, utente.getEmail());
        cmd.setString(3, utente.getHashedPassword());
        cmd.setBytes(4, utente.getSalt());
        cmd.setObject(5, utente.getRuolo().name(), java.sql.Types.OTHER);
    }

    private void setUserForUpdate(PreparedStatement cmd, Utente utente) throws SQLException {
        cmd.setString(1, utente.getEmail());
        cmd.setString(2, utente.getHashedPassword());
        cmd.setObject(3, utente.getRuolo().name(), java.sql.Types.OTHER);
        cmd.setString(4, utente.getUsername());
    }

    private void setUserForUpdate(PreparedStatement cmd, String oldUsername, Utente utente) throws SQLException {
        cmd.setString(1, utente.getUsername());
        cmd.setString(2, utente.getEmail());
        cmd.setString(3, utente.getHashedPassword());
        cmd.setObject(4, utente.getRuolo().name(), java.sql.Types.OTHER);
        cmd.setString(5, oldUsername);
    }


    private void setUserForDelete(PreparedStatement cmd, Utente utente) throws SQLException {
        cmd.setString(1, utente.getUsername());
    }

}
