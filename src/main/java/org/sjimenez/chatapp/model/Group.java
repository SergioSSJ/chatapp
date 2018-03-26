package org.sjimenez.chatapp.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Group {


    private int idgroup;
    private String name;
    private LocalDate creation;
    private String admin;


    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    //UserList
    private List<User> userList;

    public int getIdgroup() {
        return idgroup;
    }

    public void setIdgroup(int idgroup) {
        this.idgroup = idgroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreation() {
        return creation;
    }

    public void setCreation(LocalDate creation) {
        this.creation = creation;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return idgroup == group.idgroup &&
                Objects.equals(name, group.name) &&
                Objects.equals(creation, group.creation) &&
                Objects.equals(admin, group.admin) &&
                Objects.equals(userList, group.userList);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idgroup, name, creation, admin, userList);
    }

    @Override
    public String toString() {
        return "Group{" +
                "idgroup=" + idgroup +
                ", name='" + name + '\'' +
                ", creation=" + creation +
                ", admin='" + admin + '\'' +
                ", userList=" + userList +
                '}';
    }
}
