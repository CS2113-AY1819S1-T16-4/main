package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.GREETING_MESSAGE_NONEWLINE;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPARTMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYEEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHEDULE_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHEDULE_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHEDULE_YEAR;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.schedule.ScheduleBuilder.DEFAULT_DATE;
import static seedu.address.testutil.schedule.ScheduleBuilder.DEFAULT_EMPLOYEEID;
import static seedu.address.testutil.schedule.ScheduleBuilder.DEFAULT_TYPE;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddLeavesCommand;
import seedu.address.logic.commands.AddScheduleCommand;
import seedu.address.logic.commands.AddWorksCommand;
import seedu.address.logic.commands.CalculateLeavesCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.ClearExpensesCommand;
import seedu.address.logic.commands.ClearRecruitmentPostCommand;
import seedu.address.logic.commands.ClearScheduleCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteLeavesCommand;
import seedu.address.logic.commands.DeleteRecruitmentPostCommand;
import seedu.address.logic.commands.DeleteScheduleCommand;
import seedu.address.logic.commands.DeleteWorksCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.RemoveExpensesCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.SelectExpensesCommand;
import seedu.address.logic.commands.SelectRecruitmentPostCommand;
import seedu.address.logic.commands.SelectScheduleCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.addressbook.DayHourGreeting;
import seedu.address.model.person.DepartmentContainsKeywordsPredicate;
import seedu.address.model.person.EmployeeId;
import seedu.address.model.person.Person;
import seedu.address.model.person.PositionContainsKeywordsPredicate;
import seedu.address.model.schedule.Date;
import seedu.address.model.schedule.Schedule;
import seedu.address.model.schedule.Year;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;
import seedu.address.testutil.schedule.ScheduleBuilder;

public class AddressBookParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        String keyword = ("foo");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keyword);
        assertEquals(new FindCommand(keyword), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_history() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            throw new AssertionError("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            DayHourGreeting greeting = new DayHourGreeting();
            assertEquals(MESSAGE_UNKNOWN_COMMAND + greeting.getGreeting()
                    + GREETING_MESSAGE_NONEWLINE, pe.getMessage());
        }
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_selectPerson() throws Exception {
        SelectCommand command = (SelectCommand) parser.parseCommand(
                SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_PERSON), command);
        command = (SelectCommand) parser.parseCommand(
                SelectCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_addSchedule() throws Exception {
        Schedule schedule = new ScheduleBuilder().build();
        AddScheduleCommand command = (AddScheduleCommand) parser.parseCommand(
                AddScheduleCommand.COMMAND_WORD + " " + PREFIX_EMPLOYEEID + DEFAULT_EMPLOYEEID
                + " " + PREFIX_SCHEDULE_TYPE + DEFAULT_TYPE + " " + PREFIX_SCHEDULE_DATE + DEFAULT_DATE);
        assertEquals(new AddScheduleCommand(schedule), command);
        command = (AddScheduleCommand) parser.parseCommand(
                AddScheduleCommand.COMMAND_ALIAS + " " + PREFIX_EMPLOYEEID + DEFAULT_EMPLOYEEID
                        + " " + PREFIX_SCHEDULE_TYPE + DEFAULT_TYPE + " " + PREFIX_SCHEDULE_DATE + DEFAULT_DATE);
        assertEquals(new AddScheduleCommand(schedule), command);
    }

    @Test
    public void parseCommand_addLeaves() throws Exception {
        Set<Date> dates = new HashSet<>(Arrays.asList(new Date("31/12/2018")));
        AddLeavesCommand command = (AddLeavesCommand) parser.parseCommand(
                AddLeavesCommand.COMMAND_WORD + " " + PREFIX_SCHEDULE_DATE + "31/12/2018");
        assertEquals(new AddLeavesCommand(dates), command);
        command = (AddLeavesCommand) parser.parseCommand(
                AddLeavesCommand.COMMAND_ALIAS + " " + PREFIX_SCHEDULE_DATE + "31/12/2018");
        assertEquals(new AddLeavesCommand(dates), command);
    }

    @Test
    public void parseCommand_deleteLeaves() throws Exception {
        Set<Date> dates = new HashSet<>(Arrays.asList(new Date("31/12/2018")));
        DeleteLeavesCommand command = (DeleteLeavesCommand) parser.parseCommand(
                DeleteLeavesCommand.COMMAND_WORD + " " + PREFIX_SCHEDULE_DATE + "31/12/2018");
        assertEquals(new DeleteLeavesCommand(dates), command);
        command = (DeleteLeavesCommand) parser.parseCommand(
                DeleteLeavesCommand.COMMAND_ALIAS + " " + PREFIX_SCHEDULE_DATE + "31/12/2018");
        assertEquals(new DeleteLeavesCommand(dates), command);
    }

    @Test
    public void parseCommand_addWorks() throws Exception {
        Set<Date> dates = new HashSet<>(Arrays.asList(new Date("31/12/2018")));
        AddWorksCommand command = (AddWorksCommand) parser.parseCommand(
                AddWorksCommand.COMMAND_WORD + " " + PREFIX_SCHEDULE_DATE + "31/12/2018");
        assertEquals(new AddWorksCommand(dates), command);
        command = (AddWorksCommand) parser.parseCommand(
                AddWorksCommand.COMMAND_ALIAS + " " + PREFIX_SCHEDULE_DATE + "31/12/2018");
        assertEquals(new AddWorksCommand(dates), command);
    }

    @Test
    public void parseCommand_deleteWorks() throws Exception {
        Set<Date> dates = new HashSet<>(Arrays.asList(new Date("31/12/2018")));
        DeleteWorksCommand command = (DeleteWorksCommand) parser.parseCommand(
                DeleteWorksCommand.COMMAND_WORD + " " + PREFIX_SCHEDULE_DATE + "31/12/2018");
        assertEquals(new DeleteWorksCommand(dates), command);
        command = (DeleteWorksCommand) parser.parseCommand(
                DeleteWorksCommand.COMMAND_ALIAS + " " + PREFIX_SCHEDULE_DATE + "31/12/2018");
        assertEquals(new DeleteWorksCommand(dates), command);
    }

    @Test
    public void parseCommand_calculateLeaves() throws Exception {
        CalculateLeavesCommand command = (CalculateLeavesCommand) parser.parseCommand(
                CalculateLeavesCommand.COMMAND_WORD + " " + PREFIX_EMPLOYEEID + "000001"
                + " " + PREFIX_SCHEDULE_YEAR + "2018");
        assertEquals(new CalculateLeavesCommand(new EmployeeId("000001"), new Year("2018")), command);
    }

    @Test
    public void parseCommand_deleteSchedule() throws Exception {
        DeleteScheduleCommand command = (DeleteScheduleCommand) parser.parseCommand(
                DeleteScheduleCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteScheduleCommand(INDEX_FIRST_PERSON), command);
        command = (DeleteScheduleCommand) parser.parseCommand(
                DeleteScheduleCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteScheduleCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_removeExpenses() throws Exception {
        RemoveExpensesCommand command = (RemoveExpensesCommand) parser.parseCommand(
                RemoveExpensesCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new RemoveExpensesCommand(INDEX_FIRST_PERSON), command);
        command = (RemoveExpensesCommand) parser.parseCommand(
                RemoveExpensesCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new RemoveExpensesCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_deleteRecruitmentPost() throws Exception {
        DeleteRecruitmentPostCommand command = (DeleteRecruitmentPostCommand) parser.parseCommand(
                DeleteRecruitmentPostCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteRecruitmentPostCommand(INDEX_FIRST_PERSON), command);
        command = (DeleteRecruitmentPostCommand) parser.parseCommand(
                DeleteRecruitmentPostCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteRecruitmentPostCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_selectSchedule() throws Exception {
        SelectScheduleCommand command = (SelectScheduleCommand) parser.parseCommand(
                SelectScheduleCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectScheduleCommand(INDEX_FIRST_PERSON), command);
        command = (SelectScheduleCommand) parser.parseCommand(
                SelectScheduleCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectScheduleCommand(INDEX_FIRST_PERSON), command);
    }
    @Test
    public void parseCommand_selectExpenses() throws Exception {
        SelectExpensesCommand command = (SelectExpensesCommand) parser.parseCommand(
                SelectExpensesCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectExpensesCommand(INDEX_FIRST_PERSON), command);
        command = (SelectExpensesCommand) parser.parseCommand(
                SelectExpensesCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectExpensesCommand(INDEX_FIRST_PERSON), command);
    }
    @Test
    public void parseCommand_selectRecruitmentPost() throws Exception {
        SelectRecruitmentPostCommand command = (SelectRecruitmentPostCommand) parser.parseCommand(
                SelectRecruitmentPostCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectRecruitmentPostCommand(INDEX_FIRST_PERSON), command);
        command = (SelectRecruitmentPostCommand) parser.parseCommand(
                SelectRecruitmentPostCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectRecruitmentPostCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_clearSchedule() throws Exception {
        assertTrue(parser.parseCommand(ClearScheduleCommand.COMMAND_WORD) instanceof ClearScheduleCommand);
        assertTrue(parser.parseCommand(ClearScheduleCommand.COMMAND_WORD + " 3")
                instanceof ClearScheduleCommand);
        assertTrue(parser.parseCommand(ClearScheduleCommand.COMMAND_ALIAS) instanceof ClearScheduleCommand);
        assertTrue(parser.parseCommand(ClearScheduleCommand.COMMAND_ALIAS + " 3")
                instanceof ClearScheduleCommand);
    }

    @Test
    public void parseCommand_clearExpenses() throws Exception {
        assertTrue(parser.parseCommand(ClearExpensesCommand.COMMAND_WORD) instanceof ClearExpensesCommand);
        assertTrue(parser.parseCommand(ClearExpensesCommand.COMMAND_WORD + " 3")
                instanceof ClearExpensesCommand);
        assertTrue(parser.parseCommand(ClearExpensesCommand.COMMAND_ALIAS) instanceof ClearExpensesCommand);
        assertTrue(parser.parseCommand(ClearExpensesCommand.COMMAND_ALIAS + " 3")
                instanceof ClearExpensesCommand);
    }

    @Test
    public void parseCommand_clearRecruitmentPost() throws Exception {
        assertTrue(parser.parseCommand(ClearRecruitmentPostCommand.COMMAND_WORD)
                instanceof ClearRecruitmentPostCommand);
        assertTrue(parser.parseCommand(ClearRecruitmentPostCommand.COMMAND_WORD + " 3")
                instanceof ClearRecruitmentPostCommand);
        assertTrue(parser.parseCommand(ClearRecruitmentPostCommand.COMMAND_ALIAS)
                instanceof ClearRecruitmentPostCommand);
        assertTrue(parser.parseCommand(ClearRecruitmentPostCommand.COMMAND_ALIAS + " 3")
                instanceof ClearRecruitmentPostCommand);
    }

    @Test
    public void parseCommand_filter() throws Exception {
        String sortOrder = "asc";
        FilterCommand command = (FilterCommand) parser.parseCommand(
                FilterCommand.COMMAND_WORD + " " + sortOrder + " " + PREFIX_DEPARTMENT + "Finance "
                + PREFIX_POSITION + "Intern");
        DepartmentContainsKeywordsPredicate departmentPredicate =
                new DepartmentContainsKeywordsPredicate(Collections.singletonList("Finance"));
        PositionContainsKeywordsPredicate positionPredicate =
                new PositionContainsKeywordsPredicate(Collections.singletonList("Intern"));
        assertEquals(new FilterCommand(departmentPredicate, positionPredicate, sortOrder), command);
    }

    @Test
    public void parseCommand_redoCommandWord_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD) instanceof RedoCommand);
        assertTrue(parser.parseCommand("redo 1") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_undoCommandWord_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        DayHourGreeting greeting = new DayHourGreeting();
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                greeting.getGreeting() + GREETING_MESSAGE_NONEWLINE));
        parser.parseCommand("");
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        parser.parseCommand("unknownCommand");
    }
}
