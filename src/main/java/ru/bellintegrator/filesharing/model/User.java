package ru.bellintegrator.filesharing.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Пользователь
 */
@Entity
public class User implements UserDetails, Serializable {

    /**
     * Уникальный идентификатор пользователя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Служебное поле Hibernate
     */
    @Version
    private Integer version;

    /**
     * Имя пользователя
     */
    @NotEmpty(message = "Enter the name!")
    @Size(max = 50, message = "The length of the name must not exceed 50 characters")
    @Column(name = "user_name", unique = true)
    private String username;

    /**
     * Пароль пользователя
     */
    @NotEmpty(message = "Enter the password!")
    @Size(max = 50, message = "The length of the password must not exceed 50 characters")
    private String password;

    /**
     * E-mail
     */
    @Email(message = "E-mail is not correct!")
    @NotEmpty(message = "Enter the e-mail!")
    @Size(max = 50, message = "The length of the e-mail must not exceed 50 characters")
    private String email;

    /**
     * Код регистрации
     */
    @Size(max = 50, message = "The length of the activation code must not exceed 100 characters")
    @Column(name = "code")
    private String activationCode;

    /**
     * Роль
     */
    @ManyToMany(
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.PERSIST
            },
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "User_Role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    /**
     * Дата регистрации
     */
    @Column(name = "registration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    /**
     * Статус учетной записи (подтверждена или нет)
     */
    @Column(name = "is_confirmed")
    private Boolean isConfirmed = false;

    /**
     * Файлы
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserFile> files;

    /**
     * Подписки
     */
    @OneToMany(
            mappedBy = "subscriber",
            orphanRemoval = true
    )
    private Set<Access> subscriptions = new HashSet<>();

    /**
     * Подписчики
     */
    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private Set<Access> subscribers = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public Set<Role> getRoles() {
        if (roles == null) {
            roles = new HashSet<>();
        }
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        getRoles().add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        getRoles().remove(role);
        role.getUsers().remove(this);
    }

    public boolean isAdmin() {
        return getRoles()
                .stream()
                .anyMatch(role -> role.getRoleName().equals(RoleName.ADMIN));
    }

    public boolean isAnalyst() {
        return getRoles()
                .stream()
                .anyMatch(role -> role.getRoleName().equals(RoleName.ANALYST));
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Boolean getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
    }

    public Set<UserFile> getFiles() {
        if (files == null) {
            files = new HashSet<>();
        }
        return files;
    }

    public void setFiles(Set<UserFile> files) {
        this.files = files;
    }

    public void addFiles(UserFile file) {
        getFiles().add(file);
        file.setUser(this);
    }

    public void removeFile(UserFile file) {
        getFiles().remove(file);
        file.setUser(null);
    }

    public Set<Access> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Access> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Set<Access> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<Access> subscribers) {
        this.subscribers = subscribers;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getIsConfirmed();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                Objects.equals(activationCode, user.activationCode) &&
                Objects.equals(registrationDate, user.registrationDate) &&
                Objects.equals(isConfirmed, user.isConfirmed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, activationCode, registrationDate, isConfirmed);
    }
}
