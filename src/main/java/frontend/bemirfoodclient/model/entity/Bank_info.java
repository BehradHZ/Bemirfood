package frontend.bemirfoodclient.model.entity;

public class Bank_info {

    private String account_number;
    private String bank_name;
    private Double balance;


    public Bank_info() {}

    public Bank_info(String bank_name, String account_number) {
        this.bank_name = bank_name;
        this.account_number = account_number;
        this.balance = 0.0;
    }

    public Bank_info(Bank_info bankInfo) {

    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public void withdraw(double amount) {
        this.balance -= amount;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return bank_name + ", " + account_number;
    }
}
