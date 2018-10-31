package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.EmployeeId;
import seedu.address.model.person.Name;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    private static final int INDEX_FIRST_CHARACTER = 0;
    private boolean isInputName = false;
    private boolean isInputEmployeeId = false;

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns an FindCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String keyword = args.trim();
        if (keyword.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        FindCommand findCommand = new FindCommand(keyword);
        isNameOrEmployeeId(keyword);

        if (!isInputName && !isInputEmployeeId) {
            throw new ParseException(FindCommand.MESSAGE_VALID_INPUT);
        } else if (isInputName && !isInputEmployeeId) {
            findCommand.setIsInputName(true);
            findCommand.setIsInputEmployeeId(false);
        } else if (!isInputName && isInputEmployeeId) {
            findCommand.setIsInputName(false);
            findCommand.setIsInputEmployeeId(true);
        }

        return findCommand;
    }

    /**
     * Checks whether the given keyword is a valid name
     */
    public boolean isNameValid(String keyword) {
        if (!Name.isValidName(keyword)) {
            return false;
        }
        return true;
    }

    /**
     * Checks whether the given keyword is a valid employeeId
     */
    public boolean isEmployeeIdValid(String keyword) {
        if (!EmployeeId.isValidEmployeeId(keyword)) {
            return false;
        }
        return true;
    }

    /**
     * Check whether given keyword is a name or employeeId
     */
    public void isNameOrEmployeeId(String keyword) {
        if (Character.isLetter(keyword.charAt(INDEX_FIRST_CHARACTER))) {
            isInputName = isNameValid(keyword);
            isInputEmployeeId = false;
        } else if (Character.isDigit(keyword.charAt(INDEX_FIRST_CHARACTER))) {
            isInputEmployeeId = isEmployeeIdValid(keyword);
            isInputName = false;
        }
    }

}
