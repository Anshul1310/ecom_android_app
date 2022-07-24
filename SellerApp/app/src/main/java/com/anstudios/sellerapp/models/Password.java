package com.anstudios.sellerapp.models;

public class Password {
    String oldPassword, newPassword, id;

    public String getOldPassword() {
        return oldPassword;
    }

    public Password() {
    }

    public Password(String oldPassword, String newPassword, String id) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.id = id;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
