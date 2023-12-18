package com.woowup.user_alert_system;

import com.woowup.user_alert_system.model.Alert;
import com.woowup.user_alert_system.model.AlertType;
import com.woowup.user_alert_system.model.Topic;
import com.woowup.user_alert_system.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTests {
	Topic topicTestWhithUrgentContent;
	Topic topicTestWhithInformativeContent;
	User userTest;
	User userTest2;
	User userTest3;
	Alert alertTest;

	//This method executed before each test to initialize some instances of the model
	@BeforeEach
	public void initTest(){
		topicTestWhithUrgentContent = new Topic("This is Urgent Content");
		topicTestWhithInformativeContent = new Topic("This is Informative Content");
		alertTest = new Alert("U1",topicTestWhithUrgentContent, LocalDateTime.now().plusWeeks(2), AlertType.Urgent);
		userTest = new User("UserTest");
		userTest2 = new User("UserTest2");
		userTest3 = new User("UserTest3");
	}

	@Test
	void UserSubscriptionTest (){
		//the userTest subscribe to urgent topic
		userTest.subscribeToTopic(topicTestWhithUrgentContent);

		//check that the user was subscribed with 0 alerts
		assertEquals(1,topicTestWhithUrgentContent.getUsersSuscribed().size());
		assertEquals(0, userTest.getAlerts().size());

	}

	@Test
	void alertSpecificSubscribedUserTest(){
		//the userTest subscribe to urgent topic
		userTest.subscribeToTopic(topicTestWhithUrgentContent);

		// The test topic sends an alert to the specific user given their ID
		topicTestWhithUrgentContent.sendAlertToUser("Urgent",userTest.getUserId());

		// Verify that the test user receives the alert and
		// that userTest2 is not subscribed to the topic
		assertEquals(1,userTest.getAlerts().size());
		assertFalse(topicTestWhithUrgentContent.getUsersSuscribed().contains(userTest2));

		// Verify that an IllegalArgumentException is thrown when trying to send an alert to a non-subscribed user
		assertThrows(IllegalArgumentException.class,
				() -> topicTestWhithUrgentContent.sendAlertToUser("Urgent",userTest2.getUserId())
		);
	}

	@Test
	void alertAllSubscribedUsersTest(){
		// userTest and userTest2 subscribe to the urgent topic
		userTest.subscribeToTopic(topicTestWhithUrgentContent);
		userTest2.subscribeToTopic(topicTestWhithUrgentContent);

		// The urgent topic sends an alert to all subscribed users
		topicTestWhithUrgentContent.sendAlertToAllSubscribedUsers("Urgent");

		// Verify that alerts are received by subscribed users and userTest3 is not subscribed
		assertEquals(2,topicTestWhithUrgentContent.getUsersSuscribed().size());
		assertEquals(1, userTest.getAlerts().size());
		assertEquals(1, userTest2.getAlerts().size());
		assertEquals(0, userTest3.getAlerts().size());
		assertThrows(IllegalArgumentException.class,
				() -> userTest3.recieveAlert(alertTest)
		);
	}

	@Test
	void getUserAlertsReadAndExpiredTest(){
		// creating test alerts
		Alert alertTest2 = new Alert("U2",topicTestWhithUrgentContent, LocalDateTime.now().plusWeeks(3));
		Alert alertTest3 = new Alert("I1",topicTestWhithInformativeContent, LocalDateTime.now().plusWeeks(2));
		Alert alertTest4 = new Alert("I2",topicTestWhithInformativeContent, LocalDateTime.now().minusWeeks(1));

		// userTest subscribes to both urgent and informative topics
		userTest.subscribeToTopic(topicTestWhithUrgentContent);
		userTest.subscribeToTopic(topicTestWhithInformativeContent);

		// userTest receives alerts
		userTest.recieveAlert(alertTest);
		userTest.recieveAlert(alertTest2);
		userTest.recieveAlert(alertTest3);
		userTest.recieveAlert(alertTest4);

		// userTest reads an informative alert
		userTest.readAlert(alertTest3);

		// Verify that there are 2 unread and non-expired alerts for user test
		assertEquals(2, userTest.getUnreadNonExpiredAlerts().size());

	}

	@Test
	void getAllNonExpiredAlertsForTopicTest(){
		// userTest and UserTest2 subscribe to the urgent topic
		userTest.subscribeToTopic(topicTestWhithUrgentContent);
		userTest2.subscribeToTopic(topicTestWhithUrgentContent);

		// Creating an expired alert and sending a new alert to all subscribed users
		Alert alertTest2 = new Alert("Urgent Content Expired",topicTestWhithUrgentContent, LocalDateTime.now().minusWeeks(1));
		topicTestWhithUrgentContent.sendAlertToUser(userTest.getUserId(), alertTest2);
		topicTestWhithUrgentContent.sendAlertToAllSubscribedUsers("Urgent Content");

		// Retrieve all non-expired alerts for the urgent topic
		List<Alert> nonExpiredAlerts = topicTestWhithUrgentContent.getAllNonExpiredAlerts();

		// Verify that there are 1 non-expired alerts for the topic and confirm that:
		// 1. The second alert in userTest's list is specific to userTest and is marked as such
		// 2. The first alert in userTest2's list is not specific to userTest2
		assertEquals(1, nonExpiredAlerts.size());
		assertTrue(alertTest2.isForSpecificUser());
		assertTrue(userTest.getAlerts().get(1).isForSpecificUser());
		assertFalse(userTest2.getAlerts().get(0).isForSpecificUser());
	}

	@Test
	void markAlertAsReadTest(){
		// UserTest subscribes to the urgent topic
		userTest.subscribeToTopic(topicTestWhithUrgentContent);

		// UserTest receives and reads an alert
		userTest.recieveAlert(alertTest);
		userTest.readAlert(alertTest);

		// Verify that only one alert is present and it has been marked as read
		assertEquals(1,userTest.getAlerts().size());
		assertTrue(alertTest.isRead(userTest));
	}


	@Test
	void sortingUserAlertsTest(){
		// UserTest subscribes to both urgent and informative topics
		userTest.subscribeToTopic(topicTestWhithUrgentContent);
		userTest.subscribeToTopic(topicTestWhithInformativeContent);
		userTest2.subscribeToTopic(topicTestWhithUrgentContent);

		// Creating alerts with different content
		Alert alertTest2 = new Alert("U2",topicTestWhithUrgentContent, LocalDateTime.now().plusWeeks(2));
		Alert alertTest3 = new Alert("I1",topicTestWhithInformativeContent, LocalDateTime.now().plusWeeks(2));
		Alert alertTest4 = new Alert("I2",topicTestWhithInformativeContent, LocalDateTime.now().plusWeeks(2));
		Alert alertTest5 = new Alert("I3",topicTestWhithInformativeContent, LocalDateTime.now().plusWeeks(2));
		Alert alertTest6 = new Alert("I4",topicTestWhithInformativeContent, LocalDateTime.now().plusWeeks(2));

		topicTestWhithUrgentContent.sendAlertToUser(userTest.getUserId(),alertTest);
		topicTestWhithUrgentContent.sendAlertToUser(userTest.getUserId(),alertTest2);
		topicTestWhithInformativeContent.sendAlertToUser(userTest.getUserId(),alertTest3);
		topicTestWhithInformativeContent.sendAlertToUser(userTest.getUserId(),alertTest4);
		topicTestWhithInformativeContent.sendAlertToUser(userTest.getUserId(),alertTest5);
		topicTestWhithInformativeContent.sendAlertToUser(userTest.getUserId(),alertTest6);

		List<Alert> alerts = topicTestWhithUrgentContent.getAllNonExpiredAlerts();
		// Verify that alerts are sorted in the desired order
		// U2, U1, I1, I2, I3, I4
		assertEquals("U2", userTest.getAlerts().get(0).getContent());
		assertEquals("U1", userTest.getAlerts().get(1).getContent());
		assertEquals("I1", userTest.getAlerts().get(2).getContent());
		assertEquals("I2", userTest.getAlerts().get(3).getContent());
		assertEquals("I3", userTest.getAlerts().get(4).getContent());
		assertEquals("I4", userTest.getAlerts().get(5).getContent());

		assertEquals("U2", alerts.get(0).getContent());
		assertEquals("U1", alerts.get(1).getContent());

	}





}
