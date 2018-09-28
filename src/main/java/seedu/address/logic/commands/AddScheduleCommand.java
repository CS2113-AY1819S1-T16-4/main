package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHEDULE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Schedule;


public class AddScheduleCommand extends Command {

    public static final String COMMAND_WORD = "schedule";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the remark of the person identified "
            + "by the index number used in the last person listing. "
            + "Existing schedule will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_SCHEDULE + "[REMARK]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_SCHEDULE + "Likes to swim.";

    public static final String MESSAGE_ADD_SCHEDULE_SUCCESS = "Added SCHEDULE to Person: %1$s";
    public static final String MESSAGE_DELETE_SCHEDULE_SUCCESS = "Removed SCHEDULE from Person: %1$s";

    private final Index index;
    private final Schedule schedule;

    /**
     * @param index    of the person in the filtered person list to edit the remark
     * @param schedule of the person to be updated to
     */
    public AddScheduleCommand(Index index, Schedule schedule) {
        requireAllNonNull(index, schedule);
        this.index = index;
        this.schedule = schedule;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), schedule, personToEdit.getTags());
        model.updatePerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.commitAddressBook();
        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    /**
     * Generates a command execution success message based on whether the remark is added to or removed from
     * {@code personToEdit}.
     */
    private String generateSuccessMessage(Person personToEdit) {
        String message = !schedule.value.isEmpty() ? MESSAGE_ADD_SCHEDULE_SUCCESS : MESSAGE_DELETE_SCHEDULE_SUCCESS;
        return String.format(message, personToEdit);
    }


    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof AddScheduleCommand)) {
            return false;
        }
        // state check
        AddScheduleCommand e = (AddScheduleCommand) other;
        return index.equals(e.index)
                && schedule.equals(e.schedule);
    }
}

