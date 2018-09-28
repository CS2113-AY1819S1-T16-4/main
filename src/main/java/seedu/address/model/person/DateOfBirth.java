package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's Date of Birth in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDateOfBirth(String)}
 */
public class DateOfBirth {
    public static final String MESSAGE_DATEOFBIRTH_CONSTRAINTS =
            "Date of Birth should only be in the format of DD/MM/YYYY and it should not be blank";
    public static final String DATEOFBIRTH_VALIDATION_REGEX = "[0-9/]{10}";
    public final String value;

    /**
     * Constructs a {@code dateOfBirth}.
     *
     * @param dateOfBirth A valid date of birth.
     */

    public DateOfBirth(String dateOfBirth) {
        requireNonNull(dateOfBirth);
        checkArgument(isValidDateOfBirth(dateOfBirth), MESSAGE_DATEOFBIRTH_CONSTRAINTS);
        value = dateOfBirth;
    }

    /**
     * Returns true if a given string is a valid date of birth.
     */
    public static boolean isValidDateOfBirth(String test) {
        return test.matches(DATEOFBIRTH_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DateOfBirth // instanceof handles nulls
                && value.equals(((DateOfBirth) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
