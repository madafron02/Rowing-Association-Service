package nl.tudelft.sem.template.matching.application;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Communication {

    private final transient UsersCommunication usersCommunication;
    private final transient NotificationCommunication notificationCommunication;
    private final transient ActivityCommunication activityCommunication;

    /**
     * Constructor for communication component.
     *
     * @param usersCommunication communication with the users microservice
     * @param notificationCommunication communication with the notification microservice
     * @param activityCommunication communication with the activity microservice
     */
    @Autowired
    public Communication(UsersCommunication usersCommunication,
                         NotificationCommunication notificationCommunication,
                         ActivityCommunication activityCommunication) {
        this.usersCommunication = usersCommunication;
        this.notificationCommunication = notificationCommunication;
        this.activityCommunication = activityCommunication;
    }


}
