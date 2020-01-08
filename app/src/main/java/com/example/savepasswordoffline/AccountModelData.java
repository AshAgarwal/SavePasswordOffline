package com.example.savepasswordoffline;

class AccountModelData {
    String accountID;
    String applicationName;
    String accountName;
    String accountPassword;
    String accountRegisterDate;
    String accountUpdateDate;

    public AccountModelData(String accountID, String applicationName, String accountName, String accountPassword, String accountRegisterDate, String accountUpdateDate) {
        this.accountID = accountID;
        this.applicationName = applicationName;
        this.accountName = accountName;
        this.accountPassword = accountPassword;
        this.accountRegisterDate = accountRegisterDate;
        this.accountUpdateDate = accountUpdateDate;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public String getAccountRegisterDate() {
        return accountRegisterDate;
    }

    public void setAccountRegisterDate(String accountRegisterDate) {
        this.accountRegisterDate = accountRegisterDate;
    }

    public String getAccountUpdateDate() {
        return accountUpdateDate;
    }

    public void setAccountUpdateDate(String accountUpdateDate) {
        this.accountUpdateDate = accountUpdateDate;
    }
}
