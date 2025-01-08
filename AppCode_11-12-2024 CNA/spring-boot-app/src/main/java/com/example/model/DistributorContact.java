package com.example.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Distributors")
public class DistributorContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "distributor_name", nullable = false)
    private String distributorName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @Column(name = "address", nullable = false)
    private String address;

    public DistributorContact() {
    }

    public DistributorContact(String accountNumber, String distributorName, String phoneNumber, String emailAddress, String address) {
        this.accountNumber = accountNumber;
        this.distributorName = distributorName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DistributorContact that = (DistributorContact) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(accountNumber, that.accountNumber) &&
                Objects.equals(distributorName, that.distributorName) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(emailAddress, that.emailAddress) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber, distributorName, phoneNumber, emailAddress, address);
    }

    @Override
    public String toString() {
        return "DistributorContact{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", distributorName='" + distributorName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
