package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

public class Expenses {
    public static final String MESSAGE_EXPENSES_CONSTRAINTS =
            "Expenses can only take in integers, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String EXPENSES_VALIDATION_REGEX = "[0-9]*";

    public final String value;

    /**
     * Constructs an {@code Address}.
     *
     * @param expenses A valid address.
     */
    public Expenses(String expenses) {
        requireNonNull(expenses);
        checkArgument(isValidExpenses(expenses), MESSAGE_EXPENSES_CONSTRAINTS);
        value = expenses;
    }

    /**
     * Returns true if a given string is a valid email.
     */
    public static boolean isValidExpenses(String test) {
        return test.matches(EXPENSES_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Expenses // instanceof handles nulls
                && value.equals(((Expenses) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
