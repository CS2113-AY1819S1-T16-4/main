package seedu.address.logic.parser;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_OVERLOAD_PREFIX_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYEEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEDICAL_EXPENSES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MISCELLANEOUS_EXPENSES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRAVEL_EXPENSES;

import java.util.StringTokenizer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddExpensesCommand;
import seedu.address.logic.commands.AddExpensesCommand.EditExpensesDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.expenses.Expenses;
import seedu.address.model.expenses.ExpensesAmount;
import seedu.address.model.expenses.MedicalExpenses;
import seedu.address.model.expenses.MiscellaneousExpenses;
import seedu.address.model.expenses.TravelExpenses;
import seedu.address.model.person.EmployeeId;

/**
 * Parses input arguments and creates a new AddExpensesCommand object
 */
public class AddExpensesCommandParser implements Parser<AddExpensesCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the AddExpensesCommand
     * and returns an AddExpensesCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddExpensesCommand parse(String args) throws ParseException {
        NumberFormat formatter = new DecimalFormat("#0.00");
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_EMPLOYEEID, PREFIX_TRAVEL_EXPENSES,
                        PREFIX_MEDICAL_EXPENSES, PREFIX_MISCELLANEOUS_EXPENSES);

        int totalNumTokensSize = 4;
        StringTokenizer st = new StringTokenizer(args);
        if (st.countTokens() > totalNumTokensSize) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_OVERLOAD_PREFIX_FORMAT,
                    AddExpensesCommand.MESSAGE_USAGE));
        }
        if (args.isEmpty() || (!argMultimap.getValue(PREFIX_TRAVEL_EXPENSES).isPresent()
                && !argMultimap.getValue(PREFIX_MEDICAL_EXPENSES).isPresent()
                && !argMultimap.getValue(PREFIX_MISCELLANEOUS_EXPENSES).isPresent())) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddExpensesCommand.MESSAGE_USAGE));
        }
        EmployeeId employeeId = ParserUtil.parseEmployeeId(argMultimap.getValue
                (PREFIX_EMPLOYEEID).get());
        TravelExpenses travelExpenses = ParserUtil.parseTravelExpenses("0");
        MedicalExpenses medicalExpenses = ParserUtil.parseMedicalExpenses("0");
        MiscellaneousExpenses miscellaneousExpenses = ParserUtil.parseMiscellaneousExpenses("0");
        ExpensesAmount expensesAmount;

        if (argMultimap.getValue(PREFIX_TRAVEL_EXPENSES).isPresent()) {
            travelExpenses = ParserUtil.parseTravelExpenses(argMultimap.getValue(PREFIX_TRAVEL_EXPENSES)
                    .get());
        }
        if (argMultimap.getValue(PREFIX_MEDICAL_EXPENSES).isPresent()) {
            medicalExpenses = ParserUtil.parseMedicalExpenses(argMultimap.getValue(PREFIX_MEDICAL_EXPENSES).get());
        }
        if (argMultimap.getValue(PREFIX_MISCELLANEOUS_EXPENSES).isPresent()) {
            miscellaneousExpenses = ParserUtil.parseMiscellaneousExpenses(argMultimap.getValue(
                    PREFIX_MISCELLANEOUS_EXPENSES).get());
        }

        System.out.println("I am sorry!");
        double sumOfExpenses = Double.parseDouble((travelExpenses).toString())
                + Double.parseDouble((medicalExpenses).toString())
                + Double.parseDouble((miscellaneousExpenses).toString());
        System.out.println(String.valueOf(sumOfExpenses));
        expensesAmount = ParserUtil.parseExpensesAmount(String.valueOf(formatter.format(sumOfExpenses)));
        System.out.println("Alive!");
        Expenses expenses = new Expenses (employeeId, expensesAmount, travelExpenses, medicalExpenses,
                miscellaneousExpenses);

        EditExpensesDescriptor editExpensesDescriptor = new EditExpensesDescriptor();
        editExpensesDescriptor.setExpensesAmount(expensesAmount);
        editExpensesDescriptor.setTravelExpenses(travelExpenses);
        editExpensesDescriptor.setMedicalExpenses(medicalExpenses);
        editExpensesDescriptor.setMiscellaneousExpenses(miscellaneousExpenses);

        if (!editExpensesDescriptor.isAnyFieldEdited()) {
            throw new ParseException(AddExpensesCommand.MESSAGE_NOT_EDITED);
        }
        return new AddExpensesCommand(expenses, editExpensesDescriptor);
    }
    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
