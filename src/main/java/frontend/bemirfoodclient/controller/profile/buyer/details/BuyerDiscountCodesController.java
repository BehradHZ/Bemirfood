package frontend.bemirfoodclient.controller.profile.buyer.details;

import frontend.bemirfoodclient.BemirfoodApplication;
import frontend.bemirfoodclient.model.entity.Coupon;
import frontend.bemirfoodclient.model.entity.CouponType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        //do the stuff in backend

        //temporary
        List<Coupon> coupons = new ArrayList<>();

        coupons.add( new Coupon("SUMMER25", CouponType.percent, 25L, 50000L, 10L, LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(5)));
        coupons.add(new Coupon("WELCOME10K", CouponType.fixed, 10000L, 0L, 1L, LocalDateTime.now().minusHours(2), LocalDateTime.now().plusMinutes(30)));
        coupons.add(new Coupon("UPCOMING", CouponType.percent, 15L, 0L, 100L, LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(10)));

        return coupons;
    }
}
