package seedu.address.model.recruitment;
import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a RecruitmentPost's remark in the address book.
 * Guarantees: immutable; is always valid
 */
public class JobDescription {

    public static final String MESSAGE_JOB_DESCRIPTION_CONSTRAINTS =
            "Job description should not be blank/numbers.";


    /*
     * The first character of the recruitment post must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String JOB_DESCRIPTION_VALIDATION_REGEX = "[\\p{Alpha}][\\w{1,200}\b ]*";


    public final String value;
    public JobDescription(String jobDescription) {
        requireNonNull(jobDescription);
        checkArgument(isValidJobDescription(jobDescription), MESSAGE_JOB_DESCRIPTION_CONSTRAINTS);
        value = jobDescription;
    }
    @Override
    public String toString() {
        return value;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidJobDescription(String test) {
        return test.matches(JOB_DESCRIPTION_VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Post // instanceof handles nulls
                && value.equals(((Post) other).value)); // state check
    }
    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
