package frontend.bemirfoodclient.model.entity;


import frontend.bemirfoodclient.model.Exception.ForbiddenException;

import java.time.LocalDateTime;

public class Coupon {

    private Long id;

    private String code;

    private CouponType type;
    private Long value;
    private Long minPrice;
    private Long userCount;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    public Coupon() {}

    public Coupon(String code, CouponType type, long value, Long minPrice, Long userCount, LocalDateTime startDate, LocalDateTime endDate) {
        this.code = code;
        this.type = type;
        this.value = value;
        this.minPrice = minPrice;
        this.userCount = userCount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void subtractUserCount(){
        if(this.userCount <= 0) throw new ForbiddenException("Coupon can't be used any more");
        this.userCount--;
    }

    public long getValue() {
        return value;
    }

    public CouponType getType() {
        return type;
    }

    @Override
    public String toString() {
        if (type == CouponType.fixed) {
            return String.valueOf(value);
        } else {
            return "%" + value;
        }
    }

}