package nl.tudelft.sem.template.users.domain.handlers;

import nl.tudelft.sem.template.users.domain.User;

public interface UserValidationHandler {

    public void setNext(UserValidationHandler handler);

    public boolean handle(User user);

    public UserValidationHandler getNext();
}
