    module frontend.bemirfoodclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;
    requires java.desktop;

    opens frontend.bemirfoodclient to javafx.fxml;
    exports frontend.bemirfoodclient;
    exports frontend.bemirfoodclient.controller;
    opens frontend.bemirfoodclient.controller to javafx.fxml;
    exports frontend.bemirfoodclient.controller.border;
    opens frontend.bemirfoodclient.controller.border to javafx.fxml;
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
    opens frontend.bemirfoodclient.model.dto to com.google.gson;
    opens frontend.bemirfoodclient.model.entity to com.google.gson;
    exports frontend.bemirfoodclient.controller.restaurant.seller;
    opens frontend.bemirfoodclient.controller.restaurant.seller to javafx.fxml;
        exports frontend.bemirfoodclient.controller.restaurant.seller.item;
        opens frontend.bemirfoodclient.controller.restaurant.seller.item to javafx.fxml;
        exports frontend.bemirfoodclient.controller.restaurant.seller.menu;
        opens frontend.bemirfoodclient.controller.restaurant.seller.menu to javafx.fxml;
        exports frontend.bemirfoodclient.controller.restaurant.seller.order;
        opens frontend.bemirfoodclient.controller.restaurant.seller.order to javafx.fxml;

    }