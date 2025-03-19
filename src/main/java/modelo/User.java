/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author k0343
 */
public class User {
    private String username;
    private String email;
    private String role;
    private String status;
    private String lastAccess;
    private Boolean isActive;

    public User(String username, String email, String role, String status, String lastAccess, Boolean isActive) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.status = status;
        this.lastAccess = lastAccess;
        this.isActive = isActive;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public String getLastAccess() {
        return lastAccess;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLastAccess(String lastAccess) {
        this.lastAccess = lastAccess;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
