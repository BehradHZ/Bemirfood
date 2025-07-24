package frontend.bemirfoodclient.model.entity;

public class Customer extends User {

    public Customer(String fullName, String mobile, String role, String email, String photo,
                    String address, Bank_info bank_info, String password) {
        super(fullName,mobile, role, email, photo,
                address, bank_info, password);
    }
    public Customer(String mobile){
        super(mobile);
    }
    public Customer(){
        super();
    }
}