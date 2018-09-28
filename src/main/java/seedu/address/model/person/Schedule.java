package seedu.address.model.person;
import static java.util.Objects.requireNonNull;
/**
 * Represents a Person's remark in the address book.
 * Guarantees: immutable; is always valid
 */
public class Schedule {

    public final String value;
    public Schedule(String remark) {
        requireNonNull(remark);
        value = remark;
    }
    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Schedule // instanceof handles nulls
                && value.equals(((Schedule) other).value)); // state check
    }
    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
