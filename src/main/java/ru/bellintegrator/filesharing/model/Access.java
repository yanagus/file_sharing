package ru.bellintegrator.filesharing.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Objects;

/**
 * Доступ к файлам для других пользователей
 */
@Entity
public class Access implements Serializable {

    /**
     * Первичный ключ к таблице доступа к файлам
     */
    @EmbeddedId
    private AccessId id;

    /**
     * Служебное поле Hibernate
     */
    @Version
    private Integer version;

    /**
     * Пользователь, запрашивающий доступ к файлу
     */
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    /**
     * Пользователь, запрашивающий доступ к файлу
     */
    @MapsId("subscriber_id")
    @JoinColumn(name = "subscriber_id")
    @ManyToOne
    private User subscriber;

    /**
     * Доступ на чтение
     */
    @Column(name = "read_access")
    private Boolean readAccess = false;

    /**
     * Доступ на скачивание
     */
    @Column(name = "download_access")
    private Boolean downloadAccess = false;

    /**
     * Запрос
     */
    private Boolean request;

    public Access() {
    }

    public Access(User user, User subscriber) {
        this.id = new AccessId(user.getId(), subscriber.getId());
        this.user = user;
        this.subscriber = subscriber;
    }

    public AccessId getId() {
        return id;
    }

    public void setId(AccessId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(User subscriber) {
        this.subscriber = subscriber;
    }

    public boolean getReadAccess() {
        return readAccess;
    }

    public void setReadAccess(boolean readAccess) {
        this.readAccess = readAccess;
    }

    public boolean getDownloadAccess() {
        return downloadAccess;
    }

    public void setDownloadAccess(boolean downloadAccess) {
        this.downloadAccess = downloadAccess;
    }

    public boolean getRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Access access = (Access) o;
        return Objects.equals(id, access.id) &&
//                Objects.equals(user, access.user) &&
//                Objects.equals(subscriber, access.subscriber) &&
                Objects.equals(readAccess, access.readAccess) &&
                Objects.equals(downloadAccess, access.downloadAccess) &&
                Objects.equals(request, access.request);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, /*user, subscriber,*/ readAccess, downloadAccess, request);
    }

    @Override
    public String toString() {
        return "Access{" +
                "id=" + id +
                ", user=" + user +
                ", subscriber=" + subscriber +
                ", readAccess=" + readAccess +
                ", downloadAccess=" + downloadAccess +
                ", request=" + request +
                '}';
    }
}
