package com.example.myapplication;

class UserInfo {
    private String role;
    private String first_name;
    private String last_name;
    private String phone_num;

    public UserInfo() {
    }

    public UserInfo(String first_name, String last_name, String phone_num, String role) {
        this.role = role;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_num = phone_num;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }
}
