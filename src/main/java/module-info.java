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
    exports frontend.bemirfoodclient.controller.homepage;
    opens frontend.bemirfoodclient.controller.homepage to javafx.fxml;
    exports frontend.bemirfoodclient.controller.profile.buyer;
    exports frontend.bemirfoodclient.controller.profile.buyer.details;
    exports frontend.bemirfoodclient.controller.profile.admin;
    exports frontend.bemirfoodclient.controller.profile.courier;
    exports frontend.bemirfoodclient.controller.profile.courier.details;
    opens frontend.bemirfoodclient.controller.profile.courier.details to javafx.fxml;
    exports frontend.bemirfoodclient.controller.profile.seller;
    opens frontend.bemirfoodclient.controller.profile.seller to javafx.fxml;
    exports frontend.bemirfoodclient.controller.profile.seller.details;
    opens frontend.bemirfoodclient.controller.profile.seller.details to javafx.fxml;
    }