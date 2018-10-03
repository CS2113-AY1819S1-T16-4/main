package seedu.address.model;
import javafx.collections.ObservableList;
import seedu.address.model.expenses.Expenses;
/**
 * Unmodifiable view of an Expenses list
 */
public interface ReadOnlyExpensesList {
    /**
     * Returns an unmodifiable view of the Expenses list.
     * This list will not contain any duplicate Expenses.
     */
    ObservableList<Expenses> getExpensesRequestList();

