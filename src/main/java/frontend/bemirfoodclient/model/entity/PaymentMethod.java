package frontend.bemirfoodclient.model.entity;

import frontend.bemirfoodclient.model.exception.InvalidInputException;

public enum PaymentMethod {
    ONLINE,
    WALLET;

    public static PaymentMethod strToStatus(String str) {
        if (str == null) {
            throw new InvalidInputException("Status cannot be null");
        }
        try {
            return PaymentMethod.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("This status is not valid: " + str);
        }
    }

    @Override
    public String toString() {
        return this.name();
    }
}
