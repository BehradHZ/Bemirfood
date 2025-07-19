package frontend.bemirfoodclient.model.entity;

import java.time.LocalDateTime;

enum TransactionType {
    PAYMENT,
    WITHDRAWAL,
    WALLET_TOPUP
}

enum PaymentMethod {
    ONLINE,
    WALLET
}

enum Status {
    SUCCESS,
    FAILED
}


public class Transaction {
    private String title;

    private Long id;
    private TransactionType type;
    private double amount;
    private PaymentMethod paymentMethod;
    private LocalDateTime timestamp;
    private Status status;
    private Bank_info bank_info;

    public Transaction() {
        this.timestamp = LocalDateTime.now();
        this.status = frontend.bemirfoodclient.model.entity.Status.FAILED;
    }

    public Transaction(String title, double amount) {
        this.title = title;
        this.type = TransactionType.PAYMENT;
        this.amount = amount;
        this.paymentMethod = PaymentMethod.ONLINE;
        this.timestamp = LocalDateTime.now();
        this.status = Status.SUCCESS;
    }


    public Long getId() { return id; }

    public TransactionType getType() {
        return type;
    }
    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}