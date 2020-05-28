package com.example.version.controller.javafx.teams;

import com.example.version.model.Team;
import com.example.version.service.TeamService;
import com.example.version.utils.Alerts;
import com.example.version.utils.PrinterPDF;
import com.example.version.utils.Stages;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


@Component
public class TeamsController implements Initializable {

    @Autowired
    private TeamService service;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private Alerts alerts;
    @Autowired
    private Stages stages;
    @Autowired
    private PrinterPDF printerPDF;

    @FXML
    private TableView<Team> tblTeams;
    @FXML
    private TableColumn colName;
    @FXML
    private TableColumn colDivision;
    @FXML
    private TableColumn colStadium;

    private Team team;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.colName.setCellValueFactory(new PropertyValueFactory(("name")));
        this.colDivision.setCellValueFactory(new PropertyValueFactory(("division")));
        this.colStadium.setCellValueFactory(new PropertyValueFactory(("stadium")));

        tblTeams.setItems(FXCollections.observableArrayList(service.listALL()));
        stages.setDoubleClickClear(tblTeams);
    }

    public Team saveTeam() {
        team = this.tblTeams.getSelectionModel().getSelectedItem();
        return team;
    }


    public void addTeams(ActionEvent actionEvent) throws IOException {
        stages.setStage(applicationContext, "/fxml/teams/addTeams.fxml");

    }


    public void removeTeams(ActionEvent actionEvent) {

        team = this.tblTeams.getSelectionModel().getSelectedItem();

        if (team == null) {
            alerts.AlertError("Choose one team first");
        } else {
            Long idTeam = team.getIdTeam();
            service.delete(idTeam);
            refresh(actionEvent);
            alerts.AlertInformation(String.format("Team: %s has been deleted",
                    team.getName()));
        }

    }

    public void showteamSquad(ActionEvent actionEvent) throws IOException {

        team = this.tblTeams.getSelectionModel().getSelectedItem();

        if (team == null) alerts.AlertError("Choose one team first");
        else stages.setStage(applicationContext, "/fxml/teams/teamSquad.fxml");

    }

    public void showTactics(ActionEvent actionEvent) throws IOException {
        team = this.tblTeams.getSelectionModel().getSelectedItem();

        if (team == null) alerts.AlertError("Choose one team first");
        else stages.setStage(applicationContext, "/fxml/teams/tactics.fxml");


    }

    public void addCoach(ActionEvent actionEvent) throws IOException {
        team = this.tblTeams.getSelectionModel().getSelectedItem();

        if (team == null) alerts.AlertError("Choose one team first");
        else stages.setStage(applicationContext, "/fxml/teams/coachSquad.fxml");


    }

    public void back(ActionEvent actionEvent) {
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();

    }

    public void refresh(ActionEvent actionEvent) {
        tblTeams.setItems(FXCollections.observableArrayList(service.listALL()));
    }

    public void printTeams(ActionEvent actionEvent) {
        printerPDF.printToPDF(tblTeams);
    }
}
