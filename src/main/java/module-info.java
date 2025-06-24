module frontend.bemirfoodclient {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens frontend.bemirfoodclient to javafx.fxml;
    exports frontend.bemirfoodclient;
    exports frontend.bemirfoodclient.controller;
    opens frontend.bemirfoodclient.controller to javafx.fxml;
}