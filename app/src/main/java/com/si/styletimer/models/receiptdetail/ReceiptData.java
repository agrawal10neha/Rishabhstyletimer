
package com.si.styletimer.models.receiptdetail;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReceiptData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("booking_id")
    @Expose
    private String bookingId;
    @SerializedName("emp_id")
    @Expose
    private String empId;
    @SerializedName("subtotal")
    @Expose
    private String subtotal;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("paytotal")
    @Expose
    private String paytotal;
    @SerializedName("tip")
    @Expose
    private String tip;
    @SerializedName("discount")
    @Expose
    private String discount;

    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("pay_recieved_by")
    @Expose
    private String payRecievedBy;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("updated_on")
    @Expose
    private String updatedOn;
    @SerializedName("updated_by")
    @Expose
    private String updatedBy;
    @SerializedName("employee_id")
    @Expose
    private String employeeId;
    @SerializedName("merchant_id")
    @Expose
    private String merchantId;
    @SerializedName("book_id")
    @Expose
    private String bookId;
    @SerializedName("book_by")
    @Expose
    private String bookBy;
    @SerializedName("total_time")
    @Expose
    private String totalTime;
    @SerializedName("booking_time")
    @Expose
    private String bookingTime;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("zip")
    @Expose
    private String zip;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("booking_type")
    @Expose
    private String bookingType;
    @SerializedName("emp_name")
    @Expose
    private String empName;
    @SerializedName("m_city")
    @Expose
    private String mCity;
    @SerializedName("m_zip")
    @Expose
    private String mZip;
    @SerializedName("m_address")
    @Expose
    private String mAddress;
    @SerializedName("receipt_url")
    @Expose
    private String receiptUrl;
    @SerializedName("book_detail")
    @Expose
    private List<BookDetail> bookDetail = null;

    @SerializedName("taxes")
    @Expose
    private List<TaxesModel> taxesModel = null;


    @SerializedName("m_country")
    @Expose
    private String mCountry;

    @SerializedName("tax_number")
    @Expose
    private String taxNumber;

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getmCountry() {
        return mCountry;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public List<TaxesModel> getTaxesModel() {
        return taxesModel;
    }

    public void setTaxesModel(List<TaxesModel> taxesModel) {
        this.taxesModel = taxesModel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPaytotal() {
        return paytotal;
    }

    public void setPaytotal(String paytotal) {
        this.paytotal = paytotal;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPayRecievedBy() {
        return payRecievedBy;
    }

    public void setPayRecievedBy(String payRecievedBy) {
        this.payRecievedBy = payRecievedBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookBy() {
        return bookBy;
    }

    public void setBookBy(String bookBy) {
        this.bookBy = bookBy;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getMCity() {
        return mCity;
    }

    public void setMCity(String mCity) {
        this.mCity = mCity;
    }

    public String getMZip() {
        return mZip;
    }

    public void setMZip(String mZip) {
        this.mZip = mZip;
    }

    public String getMAddress() {
        return mAddress;
    }

    public void setMAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }

    public List<BookDetail> getBookDetail() {
        return bookDetail;
    }

    public void setBookDetail(List<BookDetail> bookDetail) {
        this.bookDetail = bookDetail;
    }

}
