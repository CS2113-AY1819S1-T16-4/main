package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_SCHEDULES;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelTypes;
import seedu.address.model.expenses.EmployeeIdExpensesContainsKeywordsPredicate;
import seedu.address.model.expenses.Expenses;
import seedu.address.model.person.Person;
import seedu.address.model.schedule.EmployeeIdScheduleContainsKeywordsPredicate;
import seedu.address.model.schedule.Schedule;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {
    public static final String COMMAND_WORD = "delete";
    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    private final Index targetIndex;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());

        Set<ModelTypes> set = new HashSet<>();
        set.add(ModelTypes.ADDRESS_BOOK);
        model.deletePerson(personToDelete);

        if (deleteAllExpensesFromPerson (model, personToDelete)) {
            set.add(ModelTypes.EXPENSES_LIST);
        }

        if (deleteAllSchedulesFromPerson(model, personToDelete)) {
            set.add(ModelTypes.SCHEDULES_LIST);
        }

        model.commitMultipleLists(set);

        model.updateFilteredScheduleList(PREDICATE_SHOW_ALL_SCHEDULES);

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && targetIndex.equals(((DeleteCommand) other).targetIndex)); // state check
    }

    /**
     *  Deletes all expenses related to person
     */
    public boolean deleteAllExpensesFromPerson (Model model, Person personToDelete) {
        EmployeeIdExpensesContainsKeywordsPredicate predicatEmployeeId;
        List<String> employeeIdList = new ArrayList<>();
        List<Expenses> lastShownListExpenses;

        employeeIdList.add(personToDelete.getEmployeeId().value);
        predicatEmployeeId = new EmployeeIdExpensesContainsKeywordsPredicate(employeeIdList);
        model.updateFilteredExpensesList(predicatEmployeeId);
        lastShownListExpenses = model.getFilteredExpensesList();
        if (lastShownListExpenses.size() == 0) {
            return false;
        }

        while (lastShownListExpenses.size() != 0) {
            Expenses expenseToDelete = lastShownListExpenses.get(0);
            model.deleteExpenses(expenseToDelete);
        }
        return true;
    }

    /**
     *  Deletes all schedules related to person
     */
    public boolean deleteAllSchedulesFromPerson (Model model, Person personToDelete) {
        EmployeeIdScheduleContainsKeywordsPredicate predicatEmployeeId;
        List<String> employeeIdList = new ArrayList<>();
        List<Schedule> lastShownListSchedule;

        employeeIdList.add(personToDelete.getEmployeeId().value);
        predicatEmployeeId = new EmployeeIdScheduleContainsKeywordsPredicate(employeeIdList);
        model.updateFilteredScheduleList(predicatEmployeeId);
        lastShownListSchedule = model.getFilteredScheduleList();
        if (lastShownListSchedule.size() == 0) {
            return false;
        }

        while (lastShownListSchedule.size() != 0) {
            Schedule scheduleToDelete = lastShownListSchedule.get(0);
            model.deleteSchedule(scheduleToDelete);
        }
        return true;
    }
}
