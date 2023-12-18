package com.woowup.user_alert_system.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

public class Alert {

    @Getter
    private String content;
    @Getter
    private Topic topic;
    @Getter
    private LocalDateTime expirationDate;
    @Getter
    private AlertType type;
    @Getter
    private HashMap<User, AlertStatus> usersAlerted;

    public Alert(String content,Topic topic, LocalDateTime expirationDate) {
        this.content = content;
        this.topic = topic;
        this.expirationDate = expirationDate;
        this.usersAlerted = new HashMap<>();
        if (content.contains("U")) {
            this.type = AlertType.Urgent;
        } else {
            this.type = AlertType.Informative;
        }

    }

    public Alert(String content, Topic topic, LocalDateTime expirationDate, AlertType type) {
        this.content = content;
        this.topic = topic;
        this.expirationDate = expirationDate;
        this.type = type;
        this.usersAlerted = new HashMap<>();

    }


    /**
     *This method is used when a user receives an alert
     * it is used to determine which User has read the alert.
     * @param user The user for whom the alert status is being checked.
     */
    public void sendToUser(User user){
        this.usersAlerted.put(user,new AlertStatus());
    }


    /**
     * Checks if the alert has expired based on its expiration date.
     * @return true if the alert has expired, false otherwise.
     */
    public boolean isExpired(){
        return this.expirationDate.isBefore(LocalDateTime.now());
    }


    /**
     * Marks alert as read for the given user. If the user read the alert,
     * its status is set to read.
     * @param user The user for whom to mark the alert as read.
     * @throws IllegalAccessException If the user does not have the specified alert.
     */
    public void markAlertAsRead(User user) throws IllegalAccessException {
        var statusAlert = this.getUsersAlerted().get(user);
        if(statusAlert != null){
            statusAlert.setStatus(true);
        }
        else{
            throw new IllegalAccessException("The User: "+user.getUserId()+" does not have this alert");
        }
    }


    /**
     * Checks if the alert has been read by the specified user.
     * @param user The user to check for alert read status.
     * @return true if the alert has been read by the user, false otherwise.
     */
    public boolean isRead(User user){
        return usersAlerted.get(user).isStatus();
    }


    /**
     * Checks if the alert is intended for a specific user.
     * @return true if the alert is intended for a specific user, false otherwise.
     */
    public boolean isForSpecificUser() {
        return usersAlerted.size() == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alert alert = (Alert) o;
        return Objects.equals(content, alert.content) && Objects.equals(topic, alert.topic) && Objects.equals(expirationDate, alert.expirationDate) && type == alert.type && Objects.equals(usersAlerted, alert.usersAlerted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, topic, expirationDate, type, usersAlerted);
    }
}
