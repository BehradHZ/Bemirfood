package frontend.bemirfoodclient.model.entity;

import frontend.bemirfoodclient.model.exception.InvalidInputException;

public enum UserStatus {

    approved, not_approved;

    public static UserStatus strToStatus(String str) {
        if (str == null) {
            throw new InvalidInputException("Status cannot be null");
        }
        try {
            return UserStatus.valueOf(str.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("This status is not valid: " + str);
        }
    }

    @Override
    public String toString() {
        return this.name();
    }
}