package com.woowup.user_alert_system.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;

public class Topic {

    @Getter
    private String name;
    @Getter
    private Set<User> usersSuscribed;
    @Getter
    private List<Alert> alerts;


    public Topic(String name) {

        this.name = name;
        this.usersSuscribed = new HashSet<>();
        this.alerts = new ArrayList<>();
    }

    public void subscribeNewUser(User user){
        this.usersSuscribed.add(user);
    }

    /**
     * Adds an alert to the topic's list of alerts, considering the alert type for proper ordering.
     * @param alert The alert to be added to the topic.
     */
    public void addAlert(Alert alert){
        if (alert.getType().equals(AlertType.Urgent))
            this.alerts.add(0, alert);
        else
            this.alerts.add(alert);
    }

    /**
     * Alert is sent to a single user based on their ID.
     * @param content The content of the alert to be sent.
     * @param userId  The user ID of the recipient.
     * @throws IllegalArgumentException If the user is not subscribed to the topic.
     */
    public void sendAlertToUser(String content,UUID userId){
        User userToAlert = usersSuscribed.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
            if(userToAlert!=null) {
                Alert newAlert = new Alert(content,this, LocalDateTime.now().plusWeeks(2));
                userToAlert.recieveAlert(newAlert);
                addAlert(newAlert);
            }
            else {
                throw new IllegalArgumentException("The user "+ userId+ " is not subscribed to this topic");
            }
    }

    public void sendAlertToUser(UUID userId,Alert newAlert){
        User userToAlert = usersSuscribed.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
        if(userToAlert!=null) {
            userToAlert.recieveAlert(newAlert);
            addAlert(newAlert);
        }
        else {
            throw new IllegalArgumentException("The user "+ userId+ " is not subscribed to this topic");
        }
    }
    /**
     * Sends an alert to all subscribed users of the topic with the specified content.
     * Creates a new alert and adds it to the topic's alerts. Then, notifies each subscribed user.
     * @param content The content of the alert to be sent.
     */
    public void sendAlertToAllSubscribedUsers(String content){
        Alert newAlert = new Alert(content,this, LocalDateTime.now().plusWeeks(2));
        addAlert(newAlert);
        for (User user: this.usersSuscribed){
            user.recieveAlert(newAlert);
        }
    }

    /**
     * Sends a pre-existing alert to all subscribed users of the topic.
     * Adds the alert to the topic's alerts and notifies each subscribed user.
     * @param newAlert The alert to be sent to subscribed users.
     */
    public void sendAlertToAllSubscribedUsers(Alert newAlert){
        addAlert(newAlert);
        for (User user: this.usersSuscribed){
            user.recieveAlert(newAlert);
        }
    }

    /**
     * Retrieves a list of all non-expired alerts associated with the topic from all subscribed users.
     * Iterates through the list of subscribed users to the topic and aggregates their non-expired alerts
     * into a single list.
     * @return A list of all non-expired alerts associated with the topic.
     */
    public List<Alert> getAllNonExpiredAlerts(){
        List<Alert> nonExpiredAlerts = new ArrayList<>();
        for (Alert alert : this.alerts ){
            if(!alert.isExpired()){
                nonExpiredAlerts.add(alert);
            }
        }

        return nonExpiredAlerts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return Objects.equals(name, topic.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }


}
