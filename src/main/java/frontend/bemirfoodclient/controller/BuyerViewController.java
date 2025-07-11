package frontend.bemirfoodclient.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class BuyerViewController {
    @FXML
    public ImageView homepageBemirfoodLogo;
    @FXML
    public TextField searchTextField;
    @FXML
    public ImageView searchIcon;
    @FXML
    public ImageView profileIcon;
    @FXML
    public Region toolbarSpacer;

    public void initialize() {
        homepageBemirfoodLogo.setFitHeight(40);
        homepageBemirfoodLogo.setPreserveRatio(true);

        searchTextField.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        searchTextField.setAlignment(Pos.CENTER_RIGHT);

        searchIcon.setFitHeight(17);
        searchIcon.setPreserveRatio(true);

        HBox.setHgrow(toolbarSpacer, Priority.ALWAYS);

        profileIcon.setFitHeight(27);
        profileIcon.setPreserveRatio(true);

    }

    @FXML
    public void homepageBemirfoodLogoClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void profileButtonClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void searchButtonClicked(ActionEvent actionEvent) {
    }

}
