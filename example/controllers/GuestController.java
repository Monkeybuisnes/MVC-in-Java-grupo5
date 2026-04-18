package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import core.Controller;
import models.Guest;
import models.GuestIO;
import models.SchedulerIO;
import views.GuestView;


/**
 * Responsible for {@link GuestView} behavior.
 */
public class GuestController extends Controller
{
	//-----------------------------------------------------------------------
	//		Attributes
	//-----------------------------------------------------------------------
	private GuestView guestView;
	private JTable table;


	//-----------------------------------------------------------------------
	//		Methods
	//-----------------------------------------------------------------------
	@Override
	public void run()
	{
		table = new JTable(getDataColumns(), getNameColumns());
		guestView = new GuestView(this, table);
	}

	/**
	 * Adds a new guest to persistence and table.
	 *
	 * @param guest Guest to be added
	 */
	public void addGuest(Guest guest)
	{
		Object[] metadata = new Object[3];
		metadata[0] = guest.getName();
		metadata[1] = guest.getEmail();
		metadata[2] = guest.getEventDescription();

		try {
			GuestIO guestIO = new GuestIO();
			guestIO.attach(guestView);
			guestIO.saveGuest(guest);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(guestView, "Error while saving guest", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		((DefaultTableModel) table.getModel()).addRow(metadata);
	}

	/**
	 * Deletes selected guests from table and persistence.
	 */
	public void deleteSelectedGuests()
	{
		int[] selectedRows = table.getSelectedRows();

		if ((selectedRows == null) || (selectedRows.length == 0)) {
			JOptionPane.showMessageDialog(guestView, "Select at least one guest to delete.");
			return;
		}

		List<Integer> rowsToDelete = new ArrayList<>();
		for (int row : selectedRows) {
			rowsToDelete.add(row);
		}

		Collections.sort(rowsToDelete, Collections.reverseOrder());

		try {
			GuestIO guestIO = new GuestIO();
			guestIO.attach(guestView);
			guestIO.deleteGuestsByIndexes(rowsToDelete);

			DefaultTableModel model = (DefaultTableModel) table.getModel();
			for (int row : rowsToDelete) {
				if ((row >= 0) && (row < model.getRowCount())) {
					model.removeRow(row);
				}
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(guestView, "Error while deleting guest(s)", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Returns all event descriptions to associate guests to an event.
	 *
	 * @return Event descriptions
	 */
	public Vector<String> getEventDescriptions()
	{
		Set<String> eventDescriptions = new LinkedHashSet<>();

		try {
			SchedulerIO schedulerIO = new SchedulerIO();
			Vector<Vector<Object>> events = schedulerIO.getEvents();

			for (Vector<Object> eventInfo : events) {
				if (eventInfo.size() > 1) {
					eventDescriptions.add(String.valueOf(eventInfo.get(1)));
				}
			}
		} catch (Exception ex) { }

		return new Vector<>(eventDescriptions);
	}


	//-----------------------------------------------------------------------
	//		Getters
	//-----------------------------------------------------------------------
	/**
	 * Gets the {@link GuestView view associated with this controller}.
	 *
	 * @return View associated with this controller
	 */
	public GuestView getView()
	{
		return guestView;
	}

	/**
	 * Returns the names of the columns of the guests list.
	 *
	 * @return Table metadata in a list
	 */
	public Vector<String> getNameColumns()
	{
		Vector<String> nameColumns = new Vector<String>();

		nameColumns.add("Name");
		nameColumns.add("E-mail");
		nameColumns.add("Event");

		return nameColumns;
	}

	/**
	 * Returns guests list data.
	 *
	 * @return Table data in a list of lists (matrix)
	 */
	public Vector<Vector<Object>> getDataColumns()
	{
		Vector<Vector<Object>> dataColumns = null;

		try {
			GuestIO guestIO = new GuestIO();
			dataColumns = guestIO.getGuests();
		} catch (Exception ex) { }

		return dataColumns;
	}
}
