package it.unisa.diem.main.controller;

import it.unisa.diem.dao.postgres.DocumentoDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.exceptions.DeleteException;
import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.analisi.Documento;
import it.unisa.diem.model.gestione.utenti.Utente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class ListTextsController {

    @FXML private ListView<String> ListView;
    @FXML private Button MenuButton;
    @FXML private Button deleteButton;
    private ObservableList<String> titoliList;
    private Utente utente;

    DocumentoDAOPostgres dao = new DocumentoDAOPostgres("jdbc:postgresql://database-1.czikiq82wrwk.eu-west-2.rds.amazonaws.com:5432/Wordageddon", "postgres", "Farinotta01_");

    @FXML
    public void initialize() {
        try {
            List<String> ListDao = dao.selectAllTitles();
            titoliList = FXCollections.observableArrayList(ListDao);
            ListView.setItems(titoliList);
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void goToMainMenu(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/HomeMenuView.fxml"));
            Parent root = loader.load();
            HomeMenuViewController controller = loader.getController();
            controller.setUtente(utente);
            Stage stage = (Stage) MenuButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Caricamento");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void deleteDocument(ActionEvent actionEvent) {
        String selectedTitle = ListView.getSelectionModel().getSelectedItem();

        if (selectedTitle != null) {
            System.out.println("Elemento selezionato: " + selectedTitle);

            try {
                Optional<Documento> documentoOpt = dao.selectByTitle(selectedTitle);

                if (documentoOpt.isPresent()) {
                    Documento documento = documentoOpt.get();

                    if (documento.getTitolo() == null || documento.getTitolo().isEmpty()) {
                        System.out.println("Attenzione: il nome del documento è nullo o vuoto.");
                        return;
                    }


                    // 1. Elimina il file di analisi associato al documento
                    Analisi analisi = new Analisi(documento);
                    analisi.eliminaAnalisi();

                    // 2. Elimina dal database (elimina anche l'analisi tramite ON CASCADE)
                    dao.delete(documento);

                    //3. Elimina il documento stesso
                    documento.eliminaDocumento();

                    // 4. Rimuovi dalla lista grafica
                    titoliList.remove(selectedTitle);

                    System.out.println("Documento eliminato correttamente: " + selectedTitle);

                } else {
                    System.out.println("Documento non trovato nel database.");
                }

            } catch (DBException | DeleteException e) {
                e.printStackTrace();
                System.out.println("Errore durante l'eliminazione del documento: " + e.getMessage());
            }
        } else {
            System.out.println("Nessun documento selezionato.");
        }
    }




    public void setUtente(Utente utente) {
        this.utente = utente;
    }


}
