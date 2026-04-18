package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import core.Controller;
import models.SchedulerIO;
import views.EventListView;


/**
 * Responsible for {@link EventListView} behavior.
 */
public class EventListController extends Controller 
{
	//-----------------------------------------------------------------------
	//		Attributes
	//-----------------------------------------------------------------------
	private EventListView eventListView;
	private JTable table;
	
	
	//-----------------------------------------------------------------------
	//		Methods
	//-----------------------------------------------------------------------
	@Override
	public void run() 
	{
		table = new JTable(getDataColumns(), getNameColumns());
		eventListView = new EventListView(this, table);
	}
	
	/**
	 * Adds a new row in a {@link JTable} with the values informed.
	 * 
	 * @param values Values to be add in {@link JTable}
	 */
	public void addNewRow(Object[] values) 
	{
		((DefaultTableModel) table.getModel()).addRow(values);
	}

	/**
	 * Deletes selected events from table and persistence.
	 */
	public void deleteSelectedEvents()
	{
		int[] selectedRows = table.getSelectedRows();

		if ((selectedRows == null) || (selectedRows.length == 0)) {
			JOptionPane.showMessageDialog(eventListView, "Select at least one event to delete.");
			return;
		}

		List<Integer> rowsToDelete = new ArrayList<>();

		for (int row : selectedRows) {
			rowsToDelete.add(row);
		}

		Collections.sort(rowsToDelete, Collections.reverseOrder());

		try {
			SchedulerIO schedulerIO = new SchedulerIO();
			schedulerIO.attach(eventListView);
			schedulerIO.deleteEventsByIndexes(rowsToDelete);

			DefaultTableModel model = (DefaultTableModel) table.getModel();
			for (int row : rowsToDelete) {
				if ((row >= 0) && (row < model.getRowCount())) {
					model.removeRow(row);
				}
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(eventListView, "Error while deleting event(s)", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	//-----------------------------------------------------------------------
	//		Getters
	//-----------------------------------------------------------------------
	/**
	 * Gets the {@link EventListView view associated with this controller}.
	 * 
	 * @return View associated with this controller
	 */
	public EventListView getView()
	{
		return eventListView;
	}
	
	/**
	 * Returns the names of the columns of the events list.
	 * 
	 * @return Table metadata in a list
	 */
	public Vector<String> getNameColumns() 
	{
		Vector<String> nameColumns = new Vector<String>();
		
		nameColumns.add("Date");
		nameColumns.add("Description");
		nameColumns.add("Frequency");
		nameColumns.add("E-mail");
		nameColumns.add("Alarm");
		
		return nameColumns;
	}
	
	/**
	 * Returns events list data.
	 * 
	 * @return Table data in a list of lists (matrix)
	 */
	public Vector<Vector<Object>> getDataColumns() 
	{
		Vector<Vector<Object>> dataColumns = null;

		try {
			SchedulerIO schedulerIO = new SchedulerIO();
			dataColumns = schedulerIO.getEvents();
		} catch (Exception ex) { }

		return dataColumns;
	}
}
