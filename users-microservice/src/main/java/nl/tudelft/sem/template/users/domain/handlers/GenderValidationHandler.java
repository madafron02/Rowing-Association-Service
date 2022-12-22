package nl.tudelft.sem.template.users.domain.handlers;

import nl.tudelft.sem.template.users.domain.User;

import java.util.List;

public class GenderValidationHandler implements UserValidationHandler {

    private transient UserValidationHandler next;

    public static final List<String> GENDER_OPTIONS = List.of("Male", "Female", "Other");


    @Override
    public void setNext(UserValidationHandler handler) {
        this.next = handler;
    }

    @Override
    public boolean handle(User user) {
        if (user.getGender() ==  null || GENDER_OPTIONS.contains(user.getGender())) {
            if (next != null) {
                return next.handle(user);
            } else {
                return true;
            }

        }
        throw new IllegalArgumentException("Please enter your gender as 'Male', 'Female' or 'Other'.");
    }
}
