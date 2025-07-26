package frontend.bemirfoodclient.controller.adminPanel.card;

import frontend.bemirfoodclient.model.entity.Coupon;
import frontend.bemirfoodclient.model.entity.CouponType;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.time.LocalTime;

public class AddCouponDialogController {

    public TextField couponCodeTextField;
    public RadioButton fixedRadioButton;
    public RadioButton percentRadioButton;
    public TextField valueTextField;
    public TextField minPriceTextField;
    public TextField userCountTextField;
    public DatePicker startDate;
    public DatePicker endDate;

    private final ToggleGroup typeToggleGroup = new ToggleGroup();

    @FXML
    public void initialize() {
        // Group the radio buttons so only one can be selected
        fixedRadioButton.setToggleGroup(typeToggleGroup);
        percentRadioButton.setToggleGroup(typeToggleGroup);

        // Set a default selection for the radio button
        fixedRadioButton.setSelected(true);
    }

    public Coupon processResult() {
        // Note: You should add validation here (e.g., for numeric fields)
        String code = null;
        if (couponCodeTextField.getText().isEmpty())
            return null;
        else
            code = couponCodeTextField.getText();

        CouponType type = fixedRadioButton.isSelected() ? CouponType.fixed : CouponType.percent;

        Long value = null;
        if (!valueTextField.getText().isEmpty()) {
            value = Long.parseLong(valueTextField.getText());
        } else {
            return null;
        }

        Long minPrice = null;
        if (!minPriceTextField.getText().isEmpty()) {
            minPrice = Long.parseLong(minPriceTextField.getText());
        } else {
            return null;
        }

        Long userCount = null;
        if (!userCountTextField.getText().isEmpty()) {
            userCount = Long.parseLong(userCountTextField.getText());
        } else {
            return null;
        }


        if (valueTextField.getText().isEmpty())
            return null;

        return new Coupon(
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
