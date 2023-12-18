package com.woowup.user_alert_system.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class User {
    @Getter
    private UUID userId;
    @Getter
    private String name;
    @Getter
    private List<Alert> alerts;


    public User(String name) {
        this.userId = UUID.randomUUID();
        this.name = name;
        this.alerts = new ArrayList<>();

    }

    public void subscribeToTopic(Topic topic){
        topic.subscribeNewUser(this);
    }

    public void addAlert(Alert alert){
        if (alert.getType().equals(AlertType.Urgent))
            this.alerts.add(0, alert);
        else
            this.alerts.add(alert);
    }
    /**
     * Recieves an alert if the user is subscribed to the topic of the alert and
     * adds it to the user's list of alerts in an ordered manner based on the alert type.
     * @param alert
     * @throws IllegalArgumentException If the user is not subscribed to the topic of the alert.
     *
     */
    public void recieveAlert(Alert alert) {
        Topic alertTopic = alert.getTopic();
        if(alertTopic.getUsersSuscribed().contains(this)) {
            alert.sendToUser(this);
            addAlert(alert);
        }else throw new IllegalArgumentException("The user is not subscribed to the alert topic");
    }



    /**
     * Marks the alert as read for the user. If the alert is present in the user's
     * list of alerts, it is marked as read using the corresponding method. Otherwise, no
     * action is taken.
     * @param alert
     */
    public void readAlert(Alert alert) {
        try {
            if (alerts.contains(alert)) {
                alert.markAlertAsRead(this);
            }
        } catch (IllegalAccessException e) {
                e.getMessage();
        }
    }


    /**
     * Retrieves a list of unread and non-expired alerts associated with the user.
     * Iterates through the user's list of alerts and adds those that meet the criteria
     * to the result list.
     * @return A list of unread and non-expired alerts for the user.
     */
    public List<Alert> getUnreadNonExpiredAlerts(){
        List<Alert> unreadNonExpiredAlerts = new ArrayList<Alert>();
        for (Alert alert : this.alerts ){
            if(!alert.isExpired() && !alert.isRead(this)){
                unreadNonExpiredAlerts.add(alert);
            }
        }
        return unreadNonExpiredAlerts;
    }


    /**
     * Retrieves a list of non-expired alerts associated with a specific topic for the user.
     * @param topic The topic for which to retrieve non-expired alerts.
     * @return A list of non-expired alerts associated with the specified topic for the user.
     */
    public List<Alert> getNonExpiredAlertsForTopic(Topic topic){
        List<Alert> unreadNonExpiredAlerts = new ArrayList<Alert>();
        for (Alert alert : this.alerts ){
            if(!alert.isExpired() && alert.getTopic().equals(topic)){
                unreadNonExpiredAlerts.add(alert);
            }
        }
        return unreadNonExpiredAlerts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name);
    }
}
