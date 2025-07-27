package frontend.bemirfoodclient.model.entity;


import com.google.gson.annotations.SerializedName;
import frontend.bemirfoodclient.model.exception.ForbiddenException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class
Coupon {

    private Long id;

    private String code;

    private CouponType type;
    private Long value;
    @SerializedName("min_price")
    private Long minPrice;
    @SerializedName("user_count")
    private Long userCount;
    //@SerializedName("start_date")
    private LocalDateTime startDate;
    //@SerializedName("end_date")
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

    public Coupon(Long id, String code, CouponType type, Long value, Long minPrice, Long userCount, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public Long getMinPrice() {
        return minPrice;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Long getUserCount() {
        return userCount;
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