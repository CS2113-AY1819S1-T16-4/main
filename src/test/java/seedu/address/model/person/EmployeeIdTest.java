package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class EmployeeIdTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new EmployeeId(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidEmployeeId = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new EmployeeId(invalidEmployeeId));
    }

    @Test
    public void isValidEmployeeId() {
        // null employee id
        Assert.assertThrows(NullPointerException.class, () -> EmployeeId.isValidEmployeeId(null));

        // invalid employee id
        assertFalse(EmployeeId.isValidEmployeeId("")); // empty string
        assertFalse(EmployeeId.isValidEmployeeId(" ")); // spaces only
        assertFalse(EmployeeId.isValidEmployeeId("91")); // less than 3 numbers
        assertFalse(EmployeeId.isValidEmployeeId("phone")); // non-numeric
        assertFalse(EmployeeId.isValidEmployeeId("9011p041")); // alphabets within digits
        assertFalse(EmployeeId.isValidEmployeeId("9312 1534")); // spaces within digits

        // valid employee id
        assertTrue(Phone.isValidPhone("911111")); // exactly 6 numbers
        assertTrue(Phone.isValidPhone("931215")); // exactly 6 numbers
    }
}
