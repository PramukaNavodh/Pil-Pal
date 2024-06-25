package com.s22010189.pil_pal;
////helper class for PharmacistSignUp with firebase
public class SupportPharmacist {
    String storename,pharmacistmobilenumber,storeowner,pharmacistemail,pharmacistpassword;

    public SupportPharmacist(String storename, String pharmacistmobilenumber, String storeowner, String pharmacistemail, String pharmacistpassword) {
        this.storename = storename;
        this.pharmacistmobilenumber = pharmacistmobilenumber;
        this.storeowner = storeowner;
        this.pharmacistemail = pharmacistemail;
        this.pharmacistpassword = pharmacistpassword;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getPharmacistmobilenumber() {
        return pharmacistmobilenumber;
    }

    public void setPharmacistmobilenumber(String pharmacistmobilenumber) {
        this.pharmacistmobilenumber = pharmacistmobilenumber;
    }
    public String getStoreowner() {
        return storeowner;
    }
    public void setStoreowner(String storeowner) {
        this.storeowner = storeowner;
    }
    public String getPharmacistemail() {
        return pharmacistemail;
    }

    public void setPharmacistemail(String pharmacistemail) {
        this.pharmacistemail = pharmacistemail;
    }
    public String getPharmacistpassword() {
        return pharmacistpassword;
    }
    public void setPharmacistpassword(String pharmacistpassword) {
        this.pharmacistpassword = pharmacistpassword;
    }

    public SupportPharmacist() {
    }
}
