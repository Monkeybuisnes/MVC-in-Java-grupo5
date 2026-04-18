package views;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import controllers.GuestController;
import core.Model;
import core.View;
import models.Guest;


/**
 * View responsible for invited guests registration.
 */
@SuppressWarnings("serial")
public class GuestView extends JPanel implements View
{
	//-----------------------------------------------------------------------
	//		Attributes
	//-----------------------------------------------------------------------
	private GuestController guestController;
	private JTable table;
	private JTextField tf_name;
	private JTextField tf_email;
	private JComboBox<String> cb_event;


	//-----------------------------------------------------------------------
	//		Constructor
	//-----------------------------------------------------------------------
	/**
	 * @param guestController Controller of this view
	 * @param table Table with saved guests
	 */
	public GuestView(GuestController guestController, JTable table)
	{
		this.guestController = guestController;
		this.table = table;

		make_frame();
		make_field_name();
		make_field_email();
		make_field_event();
		make_btn_save();
		make_btn_clean();
		make_btn_delete();
		make_table();
		loadEventOptions();
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
	 * Reloads event options from events persistence.
	 */
	private void loadEventOptions()
	{
		Vector<String> eventDescriptions = guestController.getEventDescriptions();
		cb_event.removeAllItems();

		if (eventDescriptions.isEmpty()) {
			cb_event.addItem("No events available");
			return;
		}

		for (String eventDescription : eventDescriptions) {
			cb_event.addItem(eventDescription);
		}
	}

	/**
	 * Reset all fields.
	 */
	private void cleanFields()
	{
		tf_name.setText("");
		tf_email.setText("");
		if (cb_event.getItemCount() > 0) {
			cb_event.setSelectedIndex(0);
		}
	}

	/**
	 * Creates view's frame.
	 */
	private void make_frame()
	{
		setLayout(null);
	}

	/**
	 * Creates guest name field.
	 */
	private void make_field_name()
	{
		JLabel lbl_name = new JLabel("Guest name");
		lbl_name.setFont(new Font("Tahoma", Font.BOLD, 11));
		lbl_name.setBounds(29, 20, 104, 14);
		add(lbl_name);

		tf_name = new JTextField();
		tf_name.setBounds(169, 17, 220, 20);
		add(tf_name);
		tf_name.setColumns(10);
	}

	/**
	 * Creates guest email field.
	 */
	private void make_field_email()
	{
		JLabel lbl_email = new JLabel("E-mail");
		lbl_email.setFont(new Font("Tahoma", Font.BOLD, 11));
		lbl_email.setBounds(29, 55, 104, 14);
		add(lbl_email);

		tf_email = new JTextField();
		tf_email.setBounds(169, 52, 220, 20);
		add(tf_email);
		tf_email.setColumns(10);
	}

	/**
	 * Creates event selector field.
	 */
	private void make_field_event()
	{
		JLabel lbl_event = new JLabel("Event");
		lbl_event.setFont(new Font("Tahoma", Font.BOLD, 11));
		lbl_event.setBounds(29, 90, 104, 14);
		add(lbl_event);

		cb_event = new JComboBox<String>();
		cb_event.setBounds(169, 86, 220, 22);
		add(cb_event);

		JButton btn_refreshEvents = new JButton("Refresh events");
		btn_refreshEvents.setBounds(401, 86, 140, 23);
		btn_refreshEvents.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadEventOptions();
			}
		});
		add(btn_refreshEvents);
	}

	/**
	 * Creates save button.
	 */
	private void make_btn_save()
	{
		JButton btn_save = new JButton("Save guest");
		btn_save.setBounds(169, 121, 110, 23);
		btn_save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = tf_name.getText().trim();
				String email = tf_email.getText().trim();
				Object selectedEvent = cb_event.getSelectedItem();

				if (name.isEmpty() || email.isEmpty()) {
					JOptionPane.showMessageDialog(GuestView.this, "Fill guest name and e-mail.");
					return;
				}

				if ((selectedEvent == null) || "No events available".equals(String.valueOf(selectedEvent))) {
					JOptionPane.showMessageDialog(GuestView.this, "Create at least one event before adding guests.");
					return;
				}

				Guest guest = new Guest();
				guest.setName(name);
				guest.setEmail(email);
				guest.setEventDescription(String.valueOf(selectedEvent));

				guestController.addGuest(guest);
				cleanFields();
			}
		});
		add(btn_save);
	}

	/**
	 * Creates clear button.
	 */
	private void make_btn_clean()
	{
		JButton btn_clean = new JButton("Clean");
		btn_clean.setBounds(289, 121, 100, 23);
		btn_clean.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cleanFields();
			}
		});
		add(btn_clean);
	}

	/**
	 * Creates delete button.
	 */
	private void make_btn_delete()
	{
		JButton btn_delete = new JButton("Delete selected");
		btn_delete.setBounds(401, 121, 140, 23);
		btn_delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				guestController.deleteSelectedGuests();
			}
		});
		add(btn_delete);
	}

	/**
	 * Creates guests table.
	 */
	private void make_table()
	{
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(29, 160, 570, 220);
		add(scrollPane);
	}
}
