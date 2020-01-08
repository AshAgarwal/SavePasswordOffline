package com.example.savepasswordoffline;

public class AccountModelData {
    String accountID;
    String applicationName;
    String accountName;
    String accountPassword;
    String userID;
    String accountRegisterDate;
    String accountUpdateDate;

    public AccountModelData(){}

    public AccountModelData(String accountID, String applicationName, String accountName, String accountPassword, String userID, String accountRegisterDate, String accountUpdateDate) {
        this.accountID = accountID;
        this.applicationName = applicationName;
        this.accountName = accountName;
        this.accountPassword = accountPassword;
        this.userID = userID;
        this.accountRegisterDate = accountRegisterDate;
        this.accountUpdateDate = accountUpdateDate;
    }

    public String getUserID() {
        return userID;
    }

    public String getAccountID() {
        return accountID;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public String getAccountRegisterDate() {
        return accountRegisterDate;
    }

    public String getAccountUpdateDate() {
        return accountUpdateDate;
    }

}
