package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHEDULE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddScheduleCommand;
import seedu.address.model.person.Schedule;

public class AddScheduleCommandParserTest {

    private AddScheduleCommandParser parser = new AddScheduleCommandParser();
    private final String nonEmptyRemark = "Some remark.";

    @Test
    public void parse_indexSpecified_success() {
        // have remark
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_SCHEDULE + nonEmptyRemark;
        AddScheduleCommand expectedCommand = new AddScheduleCommand(INDEX_FIRST_PERSON, new Schedule(nonEmptyRemark));
        assertParseSuccess(parser, userInput, expectedCommand);
        // no remark
        userInput = targetIndex.getOneBased() + " " + PREFIX_SCHEDULE;
        expectedCommand = new AddScheduleCommand(INDEX_FIRST_PERSON, new Schedule(""));
        assertParseSuccess(parser, userInput, expectedCommand);
    }
    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddScheduleCommand.MESSAGE_USAGE);
        // no parameters
        assertParseFailure(parser, AddScheduleCommand.COMMAND_WORD, expectedMessage);
        // no index
        assertParseFailure(parser, AddScheduleCommand.COMMAND_WORD + " " + nonEmptyRemark, expectedMessage);
    }
}
