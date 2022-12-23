package nl.tudelft.sem.template.users.domain.handlers;

import nl.tudelft.sem.template.users.domain.User;

import java.util.List;

public class GenderValidationHandler implements UserValidationHandler {

    private transient UserValidationHandler next;

    public static final List<String> GENDER_OPTIONS = List.of("Male", "Female");

    /**
     * Sets next handler in chain of responsibility.
     *
     * @param handler next handler in chain
     */
    @Override
    public void setNext(UserValidationHandler handler) {
        this.next = handler;
    }

    /**
     * verifies whether gender is validated by system.
     *
     * @param user user with gender to validate
     * @return true if gender is validated and this is the last handler in chain, passes to next handler if validated
     *     and this is not the last handler. Throws exception if gender is not validated.
     */
    @Override
    public boolean handle(User user) {
        if (user.getGender() ==  null || GENDER_OPTIONS.contains(user.getGender())) {
            if (next != null) {
                return next.handle(user);
            } else {
                return true;
            }

        }
        throw new IllegalArgumentException("Please enter your gender as 'Male' or 'Female'");
    }
}
