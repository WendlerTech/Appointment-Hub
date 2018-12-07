package c195.project;

import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 * @author Nick
 */
public class CustomerTableRow {

    private int custId, addressId;
    private String custName, phoneNum, city;
    private SimpleBooleanProperty custIsActive;

    public CustomerTableRow() {
        this.custIsActive = new SimpleBooleanProperty();
    }

    public CustomerTableRow(String name, String phone, String city,
            boolean isActive) {
        this.custName = name;
        this.phoneNum = phone;
        this.city = city;
        this.custIsActive = new SimpleBooleanProperty(isActive);
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public SimpleBooleanProperty getCustIsActive() {
        return custIsActive;
    }

    public void setCustIsActive(boolean custIsActive) {
        this.custIsActive.set(custIsActive);
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
