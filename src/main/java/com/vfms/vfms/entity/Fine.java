package com.vfms.vfms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "fines")
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_no", nullable = false)
    private String vehicleNo;

    @Column(name = "owner_username", nullable = false)
    private String ownerUsername;

    @Column(nullable = false)
    private String offense;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private boolean paid = false;

    @Column(nullable = false)
    private LocalDate date;

    public Fine() {
    }

    public Fine(Long id, String vehicleNo, String ownerUsername, String offense, int amount, boolean paid, LocalDate date) {
        this.id = id;
        this.vehicleNo = vehicleNo;
        this.ownerUsername = ownerUsername;
        this.offense = offense;
        this.amount = amount;
        this.paid = paid;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public String getOffense() {
        return offense;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isPaid() {
        return paid;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public void setOffense(String offense) {
        this.offense = offense;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
