package frontend.bemirfoodclient.model.entity;

import frontend.bemirfoodclient.model.exception.InvalidInputException;

import java.time.LocalDateTime;


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

    public Long getId() { return id; }

    public String getPaymentMethod() {
        if (paymentMethod == PaymentMethod.WALLET) {
            return "wallet";
        } else {
            return "online";
        }
    }
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
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

    public PaymentStatus getPaymentStatus() {

        return paymentStatus;
    }

    public User getSender() {
        return sender;
    }

    public Order getOrder() {
        return order;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}