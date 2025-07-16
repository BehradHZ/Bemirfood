package frontend.bemirfoodclient.model.dto;

import frontend.bemirfoodclient.model.entity.BankInfo;

public class bankInfoDto {
    String bank_name;
    String account_number;

    public bankInfoDto(BankInfo bankInfo) {
        this.bank_name = bankInfo.getBankName();
        this.account_number = bankInfo.getAccountNumber();
    }
    public bankInfoDto(String bank_name, String account_number) {
        this.bank_name = bank_name;
        this.account_number = account_number;
    }
}