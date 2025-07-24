package frontend.bemirfoodclient.model.entity;

public class Seller extends User {

//    public Seller() {
//        super();
//    }
    public Seller(String fullName,String mobile,
                  String email, String photo, String address, Bank_info bank_info,String password) {
        super(fullName, mobile, "seller", email, photo, address, bank_info, password);
    }
    public Seller(String mobile){
        super(mobile);
    }


    public Seller() {
        super();
    }
}