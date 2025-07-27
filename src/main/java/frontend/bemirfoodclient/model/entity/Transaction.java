package frontend.bemirfoodclient.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Transaction {

    private Long id;
    private PaymentMethod paymentMethod;
    private LocalDateTime timestamp;
    private PaymentStatus paymentStatus;

    private User sender;

    private Order order;


    public Transaction() {
    }


    public Transaction(String paymentMethod, LocalDateTime timestamp,
                       String status) {

        if (paymentMethod.equals("online"))
            this.paymentMethod = PaymentMethod.ONLINE;
        else
            this.paymentMethod = PaymentMethod.WALLET;

        this.timestamp = timestamp;

        if (status.equalsIgnoreCase("successful"))
            this.paymentStatus = PaymentStatus.SUCCESS;
        else
            this.paymentStatus = PaymentStatus.FAILED;
    }

    public Transaction(PaymentMethod paymentMethod, LocalDateTime timestamp, PaymentStatus paymentStatus, User sender, Order order) {
        this.paymentMethod = paymentMethod;
        this.timestamp = timestamp;
        this.paymentStatus = paymentStatus;
        this.sender = sender;
        this.order = order;
    }

    public String getPaymentMethod() {
        if (paymentMethod == PaymentMethod.WALLET) {
            return "wallet";
        } else {
            return "online";
        }
    }

    public String getStatus() {
        if (paymentStatus == PaymentStatus.SUCCESS) {
            return "successful";
        } else {
            return "failure";
        }
    }

    public void setStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}