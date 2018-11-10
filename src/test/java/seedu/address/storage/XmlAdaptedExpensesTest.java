package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static seedu.address.storage.expenses.XmlAdaptedExpenses.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.expenses.TypicalExpenses.BENSON_CLAIM;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.expenses.ExpensesAmount;
import seedu.address.model.expenses.MedicalExpenses;
import seedu.address.model.expenses.MiscellaneousExpenses;
import seedu.address.model.expenses.TravelExpenses;
import seedu.address.storage.expenses.XmlAdaptedExpenses;
import seedu.address.testutil.Assert;

public class XmlAdaptedExpensesTest {
    private static final String INVALID_TRAVELEXPENSES = "555.125";
    private static final String INVALID_MEDICALEXPENSES = "3123651234";
    private static final String INVALID_MISCELLANEOUSEXPENSES = "12312323123";
    private static final String INVALID_EXPENSESAMOUNT = "123.001";

    private static final String VALID_EMPLOYEEID = BENSON_CLAIM.getEmployeeId().toString();
    private static final String VALID_TRAVELEXPENSES = BENSON_CLAIM.getTravelExpenses().toString();
    private static final String VALID_MEDICALEXPENSES = BENSON_CLAIM.getMedicalExpenses().toString();
    private static final String VALID_MISCELLANEOUSEXPENSES = BENSON_CLAIM.getMiscellaneousExpenses().toString();
    private static final String VALID_EXPENSESAMOUNT = BENSON_CLAIM.getExpensesAmount().toString();

    @Test
    public void toModelType_validExpensesDetails_returnsExpenses() throws Exception {
        XmlAdaptedExpenses expenses = new XmlAdaptedExpenses(BENSON_CLAIM);
        assertEquals(BENSON_CLAIM, expenses.toModelType());
    }

    @Test
    public void toModelType_invalidExpensesAmount_throwsIllegalValueException() {
        XmlAdaptedExpenses expenses = new XmlAdaptedExpenses(VALID_EMPLOYEEID, INVALID_EXPENSESAMOUNT,
                VALID_TRAVELEXPENSES, VALID_MEDICALEXPENSES, VALID_MISCELLANEOUSEXPENSES);
        String expectedMessage = ExpensesAmount.MESSAGE_EXPENSES_AMOUNT_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, expenses::toModelType);
    }

    @Test
    public void toModelType_nullExpensesAmount_throwsIllegalValueException() {
        XmlAdaptedExpenses expenses = new XmlAdaptedExpenses(VALID_EMPLOYEEID, null, VALID_TRAVELEXPENSES,
                VALID_MEDICALEXPENSES, VALID_MISCELLANEOUSEXPENSES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, ExpensesAmount.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, expenses::toModelType);
    }

    @Test
    public void toModelType_invalidTravelExpenses_throwsIllegalValueException() {
        XmlAdaptedExpenses expenses =
                new XmlAdaptedExpenses(VALID_EMPLOYEEID, VALID_EXPENSESAMOUNT, INVALID_TRAVELEXPENSES,
                        VALID_MEDICALEXPENSES, VALID_MISCELLANEOUSEXPENSES);
        String expectedMessage = TravelExpenses.MESSAGE_TRAVEL_EXPENSES_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, expenses::toModelType);
    }

    @Test
    public void toModelType_nullTravelExpenses_throwsIllegalValueException() {
        XmlAdaptedExpenses expenses = new XmlAdaptedExpenses(VALID_EMPLOYEEID, VALID_EXPENSESAMOUNT, null,
                VALID_MEDICALEXPENSES, VALID_MISCELLANEOUSEXPENSES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, TravelExpenses.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, expenses::toModelType);
    }

    @Test
    public void toModelType_invalidMedicalExpenses_throwsIllegalValueException() {
        XmlAdaptedExpenses expenses = new XmlAdaptedExpenses(VALID_EMPLOYEEID, VALID_EXPENSESAMOUNT,
                VALID_TRAVELEXPENSES, INVALID_MEDICALEXPENSES, VALID_MISCELLANEOUSEXPENSES);
        String expectedMessage = MedicalExpenses.MESSAGE_MEDICAL_EXPENSES_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, expenses::toModelType);
    }

    @Test
    public void toModelType_nullMedicalExpenses_throwsIllegalValueException() {
        XmlAdaptedExpenses expenses = new XmlAdaptedExpenses(VALID_EMPLOYEEID, VALID_EXPENSESAMOUNT,
                VALID_TRAVELEXPENSES, null, VALID_MISCELLANEOUSEXPENSES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, MedicalExpenses.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, expenses::toModelType);
    }

    @Test
    public void toModelType_invalidMiscellaneousExpenses_throwsIllegalValueException() {
        XmlAdaptedExpenses expenses = new XmlAdaptedExpenses(VALID_EMPLOYEEID, VALID_EXPENSESAMOUNT,
                VALID_TRAVELEXPENSES, VALID_MEDICALEXPENSES, INVALID_MISCELLANEOUSEXPENSES);
        String expectedMessage = MiscellaneousExpenses.MESSAGE_MISCELLANEOUS_EXPENSES_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, expenses::toModelType);
    }

    @Test
    public void toModelType_nullMiscellaneousExpenses_throwsIllegalValueException() {
        XmlAdaptedExpenses expenses = new XmlAdaptedExpenses(VALID_EMPLOYEEID, VALID_EXPENSESAMOUNT,
                VALID_TRAVELEXPENSES, VALID_MEDICALEXPENSES, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, MiscellaneousExpenses.class
                .getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, expenses::toModelType);
    }
}