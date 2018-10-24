package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.model.ExpensesListChangedEvent;
import seedu.address.commons.events.model.RecruitmentListChangedEvent;
import seedu.address.commons.events.model.ScheduleListChangedEvent;
import seedu.address.model.addressbook.AddressBook;
import seedu.address.model.addressbook.ReadOnlyAddressBook;
import seedu.address.model.addressbook.VersionedAddressBook;
import seedu.address.model.expenses.Expenses;
import seedu.address.model.expenses.ExpensesList;
import seedu.address.model.expenses.ReadOnlyExpensesList;
import seedu.address.model.expenses.VersionedExpensesList;
import seedu.address.model.person.Person;
import seedu.address.model.recruitment.ReadOnlyRecruitmentList;
import seedu.address.model.recruitment.Recruitment;
import seedu.address.model.recruitment.RecruitmentList;
import seedu.address.model.recruitment.VersionedRecruitmentList;
import seedu.address.model.schedule.ReadOnlyScheduleList;
import seedu.address.model.schedule.Schedule;
import seedu.address.model.schedule.ScheduleList;
import seedu.address.model.schedule.VersionedScheduleList;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedModelList versionedStorageList;
    private final VersionedAddressBook versionedAddressBook;
    private final VersionedExpensesList versionedExpensesList;
    private final VersionedScheduleList versionedScheduleList;
    private final VersionedRecruitmentList versionedRecruitmentList;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Expenses> filteredExpenses;
    private final FilteredList<Schedule> filteredSchedules;
    private final FilteredList<Recruitment> filteredRecruitments;


    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyExpensesList expensesList,
                        ReadOnlyScheduleList scheduleList,
                        ReadOnlyRecruitmentList recruitmentList,
                        UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        versionedAddressBook = new VersionedAddressBook(addressBook);
        versionedExpensesList = new VersionedExpensesList(expensesList);
        versionedScheduleList = new VersionedScheduleList(scheduleList);
        versionedRecruitmentList = new VersionedRecruitmentList(recruitmentList);
        filteredExpenses = new FilteredList<>(versionedExpensesList.getExpensesRequestList());
        filteredPersons = new FilteredList<>(versionedAddressBook.getPersonList());
        filteredSchedules = new FilteredList<>(versionedScheduleList.getScheduleList());
        filteredRecruitments = new FilteredList<>(versionedRecruitmentList.getRecruitmentList());
        //versionedStorageList = VersionedModelList.getInstance();
        versionedStorageList = new VersionedModelList();

    }

    public ModelManager() {
        this(new AddressBook(), new ExpensesList(), new ScheduleList(), new RecruitmentList(), new UserPrefs());
    }

    //-----------------------------------------------------------------------------
    @Override
    public void resetAddressBookData(ReadOnlyAddressBook newData) {
        versionedAddressBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public void resetDataExpenses(ReadOnlyExpensesList newData) {
        versionedExpensesList.resetData(newData);
        indicateExpensesListChanged();
    }

    @Override
    public void resetScheduleListData(ReadOnlyScheduleList newData) {
        versionedScheduleList.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public void resetRecruitmentListData(ReadOnlyRecruitmentList newData) {
        versionedRecruitmentList.resetData(newData);
        indicateAddressBookChanged();
    }

    //-----------------------------------------------------------------------------
    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return versionedAddressBook;
    }

    @Override
    public ReadOnlyExpensesList getExpensesList() {
        return versionedExpensesList; }

    @Override
    public ReadOnlyScheduleList getScheduleList() {
        return versionedScheduleList; }

    @Override
    public ReadOnlyRecruitmentList getRecruitmentList() {
        return versionedRecruitmentList; }

    //-----------------------------------------------------------------------------

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(versionedAddressBook));
    }

    private void indicateExpensesListChanged() {
        raise(new ExpensesListChangedEvent(versionedExpensesList)); }

    private void indicateScheduleListChanged() {
        raise(new ScheduleListChangedEvent(versionedScheduleList));
    }

    private void indicateRecruitmentListChanged() {
        raise(new RecruitmentListChangedEvent(versionedRecruitmentList));
    }


    //-----------------------------------------------------------------------------
    @Override
    public boolean hasExpenses(Expenses expenses) {
        requireNonNull(expenses);
        return versionedExpensesList.hasExpenses(expenses);
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return versionedAddressBook.hasPerson(person);
    }

    @Override
    public boolean hasEmployeeId(Person person) {
        requireNonNull(person);
        return versionedAddressBook.hasEmployeeId(person);
    }

    @Override
    public boolean hasSchedule(Schedule target) {
        requireNonNull(target);
        return versionedScheduleList.hasSchedule(target);
    }

    @Override
    public boolean hasRecruitment(Recruitment target) {
        requireNonNull(target);
        return versionedRecruitmentList.hasRecruitment(target);
    }

    //-----------------------------------------------------------------------------
    @Override
    public void deleteExpenses(Expenses target) {
        versionedExpensesList.removeExpenses(target);
        indicateExpensesListChanged();
    }

    @Override
    public void deletePerson(Person target) {
        versionedAddressBook.removePerson(target);
        indicateAddressBookChanged();
        versionedStorageList.setDeletedPersonUndoRedoLoop(true);
    }

    @Override
    public void deleteSchedule(Schedule target) {
        versionedScheduleList.removeSchedule(target);
        indicateScheduleListChanged();
    }

    @Override
    public void deleteRecruitment(Recruitment target) {
        versionedRecruitmentList.removeRecruitment(target);
        indicateRecruitmentListChanged();
    }


    //-----------------------------------------------------------------------------
    @Override
    public void addExpenses(Expenses expenses) {
        versionedExpensesList.addExpenses(expenses);
        indicateExpensesListChanged();
    }


    @Override
    public void addPerson(Person person) {
        versionedAddressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

    @Override
    public void addSchedule(Schedule schedule) {
        versionedScheduleList.addSchedule(schedule);
        updateFilteredScheduleList(PREDICATE_SHOW_ALL_SCHEDULES);
        indicateScheduleListChanged();
    }

    @Override
    public void addRecruitment(Recruitment recruitment) {
        versionedRecruitmentList.addRecruitment(recruitment);
        updateFilteredRecruitmentList(PREDICATE_SHOW_ALL_RECRUITMENTS);
        indicateRecruitmentListChanged();
    }

    //-----------------------------------------------------------------------------
    @Override
    public void updateExpenses(Expenses target, Expenses editedExpenses) {
        requireAllNonNull(target, editedExpenses);
        versionedExpensesList.updateExpenses(target, editedExpenses);
        indicateExpensesListChanged();
    }

    @Override
    public void updatePerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);
        versionedAddressBook.updatePerson(target, editedPerson);
        indicateAddressBookChanged();
    }

    @Override
    public void updateSchedule(Schedule target, Schedule editedSchedule) {
        requireAllNonNull(target, editedSchedule);
        versionedScheduleList.updateSchedule(target, editedSchedule);
        indicateScheduleListChanged();
    }

    @Override
    public void updateRecruitment(Recruitment target, Recruitment editedRecruitment) {
        requireAllNonNull(target, editedRecruitment);

        versionedRecruitmentList.updateRecruitment(target, editedRecruitment);
        indicateRecruitmentListChanged();
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Expenses> getFilteredExpensesList() {
        return FXCollections.unmodifiableObservableList(filteredExpenses);
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(filteredPersons);
    }

    @Override
    public ObservableList<Schedule> getFilteredScheduleList() {
        return FXCollections.unmodifiableObservableList(filteredSchedules);
    }

    @Override
    public ObservableList<Recruitment> getFilteredRecruitmentList() {
        return FXCollections.unmodifiableObservableList(filteredRecruitments);
    }

    //=========== Filtered Person List Accessors =============================================================
    @Override
    public void updateFilteredExpensesList(Predicate<Expenses> predicate) {
        requireNonNull(predicate);
        filteredExpenses.setPredicate(predicate);
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public void updateFilteredScheduleList(Predicate<Schedule> predicate) {
        requireNonNull(predicate);
        filteredSchedules.setPredicate(predicate);
    }

    @Override
    public void updateFilteredRecruitmentList(Predicate<Recruitment> predicate) {
        requireNonNull(predicate);
        filteredRecruitments.setPredicate(predicate);
    }


    //=========== Undo =================================================================================

    @Override
    public boolean canUndoAddressBook() {
        return versionedAddressBook.canUndo();
    }

    @Override
    public boolean canUndoExpensesList() {
        return versionedExpensesList.canUndo();
    }

    @Override
    public boolean canUndoScheduleList() {
        return versionedScheduleList.canUndo();
    }

    @Override
    public boolean canUndoRecruitmentList() {
        return versionedRecruitmentList.canUndo();
    }

    //=========== Redo =================================================================================
    @Override
    public boolean canRedoAddressBook() {
        return versionedAddressBook.canRedo();
    }

    @Override
    public boolean canRedoExpensesList() {
        return versionedExpensesList.canRedo();
    }

    @Override
    public boolean canRedoScheduleList() {
        return versionedScheduleList.canRedo();
    }

    @Override
    public boolean canRedoRecruitmentList() {
        return versionedRecruitmentList.canRedo();
    }

    //-----------------------------------------------------------------------------

    @Override
    public void undoAddressBook() {
        versionedStorageList.checkCanUndoStorage();
        versionedAddressBook.undo();
        indicateAddressBookChanged();
    }

    @Override
    public void undoExpensesList() {
        versionedStorageList.checkCanUndoStorage();
        versionedExpensesList.undo();
        indicateExpensesListChanged();
    }

    @Override
    public void undoScheduleList() {
        versionedStorageList.checkCanUndoStorage();
        versionedScheduleList.undo();
        indicateScheduleListChanged();
    }

    @Override
    public void undoRecruitmentList() {
        versionedStorageList.checkCanUndoStorage();
        versionedRecruitmentList.undo();
        indicateRecruitmentListChanged();
    }


    //-----------------------------------------------------------------------------
    @Override
    public void redoAddressBook() {
        versionedStorageList.checkCanRedoStorage();
        versionedAddressBook.redo();
        indicateAddressBookChanged();
    }

    @Override
    public void redoExpensesList() {
        versionedStorageList.checkCanRedoStorage();
        versionedExpensesList.redo();
        indicateExpensesListChanged();
    }

    @Override
    public void redoScheduleList() {
        versionedStorageList.checkCanRedoStorage();
        versionedScheduleList.redo();
        indicateScheduleListChanged();
    }

    @Override
    public void redoRecruitmentList() {
        versionedStorageList.checkCanRedoStorage();
        versionedRecruitmentList.redo();
        indicateRecruitmentListChanged();
    }

    //-----------------------------------------------------------------------------
    /**
     * Commits the address book storage and sets the last commit storage type
     */
    @Override
    public void commitAddressBook() {
        versionedAddressBook.commit();
        versionedStorageList.add(ModelTypes.ADDRESS_BOOK);
    }

    /**
     * Commits the expenses list storage and sets the last commit storage type
     */
    public void commitExpensesList() {
        versionedExpensesList.commit();
        versionedStorageList.add(ModelTypes.EXPENSES_LIST);
    }

    /**
     * Commits the schedule list storage and sets the last commit storage type
     */
    public void commitScheduleList() {
        versionedScheduleList.commit();
        versionedStorageList.add(ModelTypes.SCHEDULES_LIST);
    }

    /**
     * Commits the recruitment list storage and sets the last commit storage type
     */
    public void commitRecruitmentList() {
        versionedRecruitmentList.commit();
        versionedStorageList.add(ModelTypes.RECRUITMENT_LIST);
    }

    //-----------------------------------------------------------------------------

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedAddressBook.equals(other.versionedAddressBook)
                && filteredPersons.equals(other.filteredPersons)
                && versionedRecruitmentList.equals(other.versionedRecruitmentList)
                && filteredRecruitments.equals(other.filteredRecruitments)
                && versionedScheduleList.equals(other.versionedScheduleList)
                && filteredSchedules.equals(other.filteredSchedules);
    }

}
