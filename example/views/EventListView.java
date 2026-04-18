package views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import controllers.EventListController;
import core.Model;
import core.View;


/**
 * View responsible for the list of events.
 */
@SuppressWarnings("serial")
public class EventListView extends JPanel implements View
{
	//-----------------------------------------------------------------------
	//		Attributes
	//-----------------------------------------------------------------------
	private EventListController eventListController;
	private JTable table;
	
	
	//-----------------------------------------------------------------------
	//		Constructor
	//-----------------------------------------------------------------------
	/**
	 * It will show the list of saved events.
	 * 
	 * @param eventListController Controller responsible for this view
	 * @param table Table with saved events
	 */
	public EventListView(EventListController eventListController, JTable table)
	{
		this.eventListController = eventListController;
		this.table = table;
		
		make_frame();
	}
	
	
	//-----------------------------------------------------------------------
	//		Methods
	//-----------------------------------------------------------------------
	@Override
	public void update(Model model, Object data) 
	{
		if (data != null) {
			String notice = (String) data;
			JOptionPane.showMessageDialog(this, notice);
		}
	}
	
	/**
	 * Creates view's frame.
	 */
	private void make_frame()
	{
		setLayout(new BorderLayout());

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		JButton btnDelete = new JButton("Delete selected");
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eventListController.deleteSelectedEvents();
			}
		});

		JPanel panelActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelActions.add(btnDelete);
		add(panelActions, BorderLayout.SOUTH);
	}
}
