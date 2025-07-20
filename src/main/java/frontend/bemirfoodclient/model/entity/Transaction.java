package frontend.bemirfoodclient.model.entity;

import java.time.LocalDateTime;

enum TransactionType {
    PAYMENT,
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

    public Transaction(String title, double amount, String paymentMethod, String type, LocalDateTime timestamp,
                       String status) {
        this.title = title;
        this.amount = amount;

        if (paymentMethod.equals("online"))
            this.paymentMethod = PaymentMethod.ONLINE;
        else
            this.paymentMethod = PaymentMethod.WALLET;

        if (type.equalsIgnoreCase("payment"))
            this.type = TransactionType.PAYMENT;
        else
            this.type = TransactionType.WALLET_TOPUP;

        this.timestamp = timestamp;

        if (status.equalsIgnoreCase("successful"))
            this.status = Status.SUCCESS;
        else
            this.status = Status.FAILED;
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
        if (status == Status.SUCCESS) {
            return "successful";
        } else {
            return "failure";
        }
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getTransactionType() {
        if (type == TransactionType.PAYMENT) {
            return "payment";
        } else {
            return "wallet top up";
        }
    }
}