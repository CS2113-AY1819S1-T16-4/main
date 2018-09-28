package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ScheduleTest {
    @Test
    public void equals() {
        Schedule remark = new Schedule("Hello");
        // same object -> returns true
        assertTrue(remark.equals(remark));
        // same values -> returns true
        Schedule remarkCopy = new Schedule(remark.value);
        assertTrue(remark.equals(remarkCopy));
        // different types -> returns false
        assertFalse(remark.equals(1));
        // null -> returns false
        assertFalse(remark.equals(null));
        // different remark -> returns false
        Schedule differentRemark = new Schedule("Bye");
        assertFalse(remark.equals(differentRemark));
    }

}