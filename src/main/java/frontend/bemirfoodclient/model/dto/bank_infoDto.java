package frontend.bemirfoodclient.model.dto;

import frontend.bemirfoodclient.model.entity.Bank_info;

public class bank_infoDto {
    String bank_name;
    String account_number;

    public bank_infoDto(Bank_info bank_info) {
        this.bank_name = bank_info.getBank_name();
        this.account_number = bank_info.getAccount_number();
    }
    public bank_infoDto(String bank_name, String account_number) {
        this.bank_name = bank_name;
        this.account_number = account_number;
    }
}