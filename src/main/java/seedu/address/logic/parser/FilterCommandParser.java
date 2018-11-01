package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPARTMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Department;
import seedu.address.model.person.DepartmentContainsKeywordsPredicate;
import seedu.address.model.person.Position;
import seedu.address.model.person.PositionContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser {

    public static final List<String> ACCEPTED_ORDERS = new ArrayList<>(Arrays.asList(FilterCommand.ASCENDING,
            FilterCommand.DESCENDING));
    private final int INDEX_ONE = 0;

    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns an FilterCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmedArgs = args.trim().toLowerCase();
        String sortOrder = trimmedArgs.split("\\s")[INDEX_ONE];

        if (!ACCEPTED_ORDERS.contains(sortOrder)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_DEPARTMENT, PREFIX_POSITION);
        StringTokenizer st = new StringTokenizer(args);

        if (trimmedArgs.isEmpty() || (!argMultimap.getValue(PREFIX_DEPARTMENT).isPresent()
                && !argMultimap.getValue(PREFIX_POSITION).isPresent())) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        String trimmedDepartment;
        String trimmedPosition;
        String[] departmentKeywords = new String[]{""};
        String[] positionKeywords = new String[]{""};
        FilterCommand filterCommand = new FilterCommand(new DepartmentContainsKeywordsPredicate(Arrays
                .asList(departmentKeywords)), new PositionContainsKeywordsPredicate(Arrays.asList(positionKeywords)),
                sortOrder);

        if (argMultimap.getValue(PREFIX_DEPARTMENT).isPresent()) {
            trimmedDepartment = (argMultimap.getValue(PREFIX_DEPARTMENT).get()).trim();
            departmentKeywords = trimmedDepartment.split("\\s+");
            if (!didPrefixAppearOnlyOnce(trimmedArgs, PREFIX_DEPARTMENT.toString())) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
            } else if (!areDepartmentKeywordsValid(departmentKeywords)) {
                throw new ParseException(Department.MESSAGE_DEPARTMENT_KEYWORD_CONSTRAINTS);
            }else if (areDepartmentKeywordsValid(departmentKeywords)) {
                filterCommand.setIsDepartmentPrefixPresent(true);
                filterCommand.setDepartmentPredicate(new DepartmentContainsKeywordsPredicate(Arrays
                        .asList(departmentKeywords)));
            }
        } else if (!argMultimap.getValue(PREFIX_DEPARTMENT).isPresent()) {
            filterCommand.setIsDepartmentPrefixPresent(false);
        }

        if (argMultimap.getValue(PREFIX_POSITION).isPresent()) {
            trimmedPosition = (argMultimap.getValue(PREFIX_POSITION).get()).trim();
            positionKeywords = trimmedPosition.split("\\s+");
            if (!didPrefixAppearOnlyOnce(trimmedArgs, PREFIX_POSITION.toString())) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
            } else if (!arePositionKeywordsValid(positionKeywords)) {
                throw new ParseException(Position.MESSAGE_POSITION_KEYWORD_CONSTRAINTS);
            } else if (arePositionKeywordsValid(positionKeywords)) {
                filterCommand.setIsPositionPrefixPresent(true);
                filterCommand.setPositionPredicate(new PositionContainsKeywordsPredicate(Arrays
                        .asList(positionKeywords)));
            }
        } else if (!argMultimap.getValue(PREFIX_POSITION).isPresent()) {
            filterCommand.setIsPositionPrefixPresent(false);
        }

        return filterCommand;
    }

    /**
     * Checks whether given keywords are valid department(s)
     */
    public boolean areDepartmentKeywordsValid(String[] keywords) {
        for (String keyword: keywords) {
            if (!Department.isValidDepartment(keyword)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether given keywords are valid position(s)
     */
    public boolean arePositionKeywordsValid(String[] keywords) {
        for (String keyword: keywords) {
            if (!Position.isValidPosition(keyword)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether prefixes appeared more than once within the argument
     */
    public boolean didPrefixAppearOnlyOnce(String argument, String prefix) {
        return argument.indexOf(prefix) == argument.lastIndexOf(prefix);
    }
}
