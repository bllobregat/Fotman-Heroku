package com.example.version.controller.javafx.users;

import com.example.version.model.User;
import com.example.version.service.UserService;
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
public class UsersController implements Initializable {

    @Autowired
    private UserService service;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private Alerts alerts;
    @Autowired
    private Stages stages;
    @Autowired
    private PrinterPDF printer;


    @FXML
    private TableView<User> tblUsers;
    @FXML
    private TableColumn colName;
    @FXML
    private TableColumn colSurname;
    @FXML
    private TableColumn colRole;

    private User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.colName.setCellValueFactory(new PropertyValueFactory(("name")));
        this.colSurname.setCellValueFactory(new PropertyValueFactory(("surname")));
        this.colRole.setCellValueFactory(new PropertyValueFactory(("role")));

        tblUsers.setItems(FXCollections.observableArrayList(service.listALL()));
        stages.setDoubleClickClear(tblUsers);
    }

    public void addUsers(ActionEvent actionEvent) throws IOException {
        stages.setStage(applicationContext, "/fxml/users/addUsers.fxml");

    }

    public void removeUsers(ActionEvent actionEvent) {
        user = this.tblUsers.getSelectionModel().getSelectedItem();
        if (user == null) {
            alerts.AlertError("Choose one user first");
        } else {
            Long idUser = user.getIdUser();
            service.delete(idUser);
            refresh(actionEvent);
            alerts.AlertInformation(String.format("User: %s %s has been deleted",
                    user.getName(), user.getSurname()));
        }
    }

    public void showContactDetails(ActionEvent actionEvent) throws IOException {
        user = this.tblUsers.getSelectionModel().getSelectedItem();

        if (user == null) alerts.AlertError("Choose one user first");
        else stages.setStage(applicationContext, "/fxml/users/contactDetails.fxml");
    }


    public void printUsers(ActionEvent actionEvent) {
        printer.printToPDF(tblUsers);
    }

    public void refresh(ActionEvent actionEvent) {
        tblUsers.setItems(FXCollections.observableArrayList(service.listALL()));
    }

    public void back(ActionEvent actionEvent) throws IOException {
        stages.setStage(applicationContext, "/fxml/menu.fxml");
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();


    }

    public User saveUser() {
        user = this.tblUsers.getSelectionModel().getSelectedItem();
        return user;
    }
}
