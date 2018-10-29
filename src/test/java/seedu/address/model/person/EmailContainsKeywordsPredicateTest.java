package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.testutil.PersonBuilder;

public class EmailContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first@example.com");
        List<String> secondPredicateKeywordList = Arrays.asList("first@example.com", "second@example.com");

        EmailContainsKeywordsPredicate firstPredicate = new EmailContainsKeywordsPredicate(firstPredicateKeywordList);
        EmailContainsKeywordsPredicate secondPredicate = new EmailContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        EmailContainsKeywordsPredicate firstPredicateCopy =
                new EmailContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate == null);

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_emailContainsKeywords_returnsTrue() {
        // One keyword
        EmailContainsKeywordsPredicate predicate =
                new EmailContainsKeywordsPredicate(Collections.singletonList("Alice@example.com"));
        assertTrue(predicate.test(new PersonBuilder().withEmail("Alice@example.com").build()));

        // Multiple keywords
        predicate = new EmailContainsKeywordsPredicate(Arrays.asList("Alice@example.com", "Bob@example.com"));
        assertTrue(predicate.test(new PersonBuilder().withEmail("Alice@example.com").build()));

        // Only one matching keyword
        predicate = new EmailContainsKeywordsPredicate(Arrays.asList("Bob@example.com", "Carol@example.com"));
        assertTrue(predicate.test(new PersonBuilder().withEmail("Carol@example.com").build()));

        // Mixed-case keywords
        predicate = new EmailContainsKeywordsPredicate(Arrays.asList("aLIce@example.com", "bOB@example.com"));
        assertTrue(predicate.test(new PersonBuilder().withEmail("Alice@example.com").build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        EmailContainsKeywordsPredicate predicate = new EmailContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withEmail("Alice@example.com").build()));

        // Non-matching keyword
        predicate = new EmailContainsKeywordsPredicate(Arrays.asList("Carol@example.com"));
        assertFalse(predicate.test(new PersonBuilder().withEmail("Alice@example.com").build()));

        // Keywords match phone, name and address, but does not match email
        predicate = new EmailContainsKeywordsPredicate(Arrays.asList("12345", "Alice", "Main", "Street"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withPhone("12345")
                .withEmail("alice@email.com").withAddress("Main Street").build()));
    }
}