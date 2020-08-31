package com.google.firebase.codelab.friendlychat.NuevoPedido;

public class Pedido {
    private int amount;
    private boolean isconfirmed;
    private String idBuyer;


    public Pedido(int amount, boolean isconfirmed, String idBuyer) {
        this.amount = amount;
        this.isconfirmed = isconfirmed;
        this.idBuyer = idBuyer;
    }
    public Pedido(){

    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isIsconfirmed() {
        return isconfirmed;
    }

    public void setIsconfirmed(boolean isconfirmed) {
        this.isconfirmed = isconfirmed;
    }

    public String getIdBuyer() {
        return idBuyer;
    }

    public void setIdBuyer(String idBuyer) {
        this.idBuyer = idBuyer;
    }
}
