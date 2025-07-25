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
        /*return List.of(
                // An active percentage-based coupon that ends in a few days
                new Coupon(
                        "SUMMER25",
                        CouponType.percent,
                        25L,
                        50000L,
                        99L,
                        LocalDateTime.now().minusDays(10),
                        LocalDateTime.now().plusDays(5)
                ),

                // An active fixed-amount coupon that ends in a few hours
                new Coupon(
                        "WELCOME10K",
                        CouponType.fixed,
                        10000L,
                        0L,
                        1L,
                        LocalDateTime.now().minusHours(1),
                        LocalDateTime.now().plusHours(12)
                ),

                // An already expired coupon
                new Coupon(
                        "EXPIREDDEAL",
                        CouponType.fixed,
                        5000L,
                        0L,
                        0L,
                        LocalDateTime.now().minusDays(20),
                        LocalDateTime.now().minusDays(2)
                ),

                // An upcoming coupon that hasn't started yet
                new Coupon(
                        "NEXTWINTER",
                        CouponType.percent,
                        15L,
                        100000L,
                        500L,
                        LocalDateTime.now().plusDays(3),
                        LocalDateTime.now().plusDays(15)
                )
        );*/
    }
}
