package frontend.bemirfoodclient.controller.adminPanel.card;

import frontend.bemirfoodclient.model.entity.Coupon;
import frontend.bemirfoodclient.model.entity.CouponType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.time.LocalTime;

public class EditCouponDialogController {

    public TextField couponCodeTextField;
    public RadioButton fixedRadioButton;
    public RadioButton percentRadioButton;
    public TextField valueTextField;
    public TextField minPriceTextField;
    public TextField userCountTextField;
    public DatePicker startDate;
    public DatePicker endDate;

    private Coupon coupon;

    public void setData(Coupon coupon) {
        this.coupon = coupon;
        setScene();
    }

    public void initialize() {

    }

    public void setScene() {
        couponCodeTextField.setText(coupon.getCode());
        if (coupon.getType().equals(CouponType.fixed))
            fixedRadioButton.setSelected(true);
        if (coupon.getType().equals(CouponType.percent))
            percentRadioButton.setSelected(true);
        valueTextField.setText(String.valueOf(coupon.getValue()));
        minPriceTextField.setText(String.valueOf(coupon.getMinPrice()));
        userCountTextField.setText(String.valueOf(coupon.getUserCount()));
        startDate.setValue(coupon.getStartDate().toLocalDate());
        endDate.setValue(coupon.getEndDate().toLocalDate());
    }

    public Coupon processResult() {
        // Note: You should add validation here (e.g., for numeric fields)
        String code = couponCodeTextField.getText();
        CouponType type = fixedRadioButton.isSelected() ? CouponType.fixed : CouponType.percent;
        Long value = Long.parseLong(valueTextField.getText());
        Long minPrice = Long.parseLong(minPriceTextField.getText());
        Long userCount = Long.parseLong(userCountTextField.getText());

        // Create a new Coupon object with the original ID but updated fields
        return new Coupon(
                coupon.getId(),
                code,
                type,
                value,
                minPrice,
                userCount,
                startDate.getValue().atStartOfDay(),
                endDate.getValue().atTime(LocalTime.MAX)
        );
    }
}
