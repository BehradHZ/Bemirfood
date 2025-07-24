package frontend.bemirfoodclient.controller.profile.buyer.details;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.entity.Coupon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

import static BuildEntity.EntityRequest.getCoupon;

public class BuyerDiscountCodesController {

    @FXML
    public VBox couponsSection;

    public void initialize() {
        List<Coupon> coupons = getCoupons();

        for (Coupon coupon : coupons) {
            try {
                FXMLLoader loader = new FXMLLoader(BemirfoodApplication.class.getResource(
                        "/frontend/bemirfoodclient/profile/buyer/details/coupon-card.fxml"
                ));
                Pane card = loader.load();

                CouponCardController cardController = loader.getController();
                cardController.setCouponData(coupon);

                couponsSection.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Coupon> getCoupons() {
        return getCoupon();
    }
}
