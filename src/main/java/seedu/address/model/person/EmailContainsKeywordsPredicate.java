package seedu.address.model.person;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

/**
 * Tests that a {@code Person}'s {@code Email} matches any of the keywords given.
 */
public class EmailContainsKeywordsPredicate implements Predicate<Person> {
    private List<String> keywords = new ArrayList<>();
    private String keyword = "";

    public EmailContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }
    public EmailContainsKeywordsPredicate(String keyword) { this.keyword = keyword; }

    @Override
    public boolean test(Person person) {
        if(!keyword.isEmpty()) {
            return keyword.equals(person.getEmail().value);
        }

        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getEmail().value, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EmailContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((EmailContainsKeywordsPredicate) other).keywords)); // state check
    }
}
