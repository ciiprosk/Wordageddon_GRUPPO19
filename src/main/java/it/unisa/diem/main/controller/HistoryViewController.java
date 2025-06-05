package it.unisa.diem.main.controller;

import it.unisa.diem.main.Main;
import it.unisa.diem.utility.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HistoryViewController {
    @FXML private Button leaderboardButton;
    @FXML private Button backButton;

    @FXML
    public void initialize() {
        Image back = new Image(Main.class.getClassLoader().getResourceAsStream("immagini/yellowbackarrow.png"));
        ImageView backView = new ImageView(back);
        backView.setFitWidth(30);
        backView.setFitHeight(30);
        backButton.setGraphic(backView);

        Image leader = new Image(Main.class.getClassLoader().getResourceAsStream("immagini/trophy.png"));
        ImageView leaderView = new ImageView(leader);
        leaderView.setFitWidth(30);
        leaderView.setFitHeight(30);
        leaderboardButton.setGraphic(leaderView);
    }

    public void goToMainMenu(ActionEvent actionEvent) {
        SceneLoader.load("HomeMenuView.fxml", backButton);
    }

    public void goToLeaderboard(ActionEvent actionEvent) {
        SceneLoader.load("LeaderboardView.fxml", leaderboardButton);
    }
}
