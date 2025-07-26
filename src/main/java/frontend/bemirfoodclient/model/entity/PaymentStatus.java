package frontend.bemirfoodclient.model.entity;

import frontend.bemirfoodclient.model.exception.InvalidInputException;

public enum PaymentStatus {
    SUCCESS,
    FAILED;

    public static PaymentStatus strToStatus(String str) {
        if (str == null) {
            throw new InvalidInputException("Status cannot be null");
        }
        try {
            return PaymentStatus.valueOf(str.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("This status is not valid: " + str);
        }
    }

    @Override
    public String toString() {
        return this.name();
    }
}
