package models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import core.Model;
import core.View;


/**
 * Responsible for reading / writing invited guests saved.
 */
public class GuestIO implements Model
{
	//-----------------------------------------------------------------------
	//		Attributes
	//-----------------------------------------------------------------------
	private static final String DIRECTORY = ".";
	private static final String FILE = "guests.txt";
	private List<View> views = new ArrayList<>();
	private String notice;


	//-----------------------------------------------------------------------
	//		Methods
	//-----------------------------------------------------------------------
	@Override
	public void attach(View view)
	{
		views.add(view);
	}

	@Override
	public void detach(View view)
	{
		views.remove(view);
	}

	@Override
	public void notifyViews()
	{
		for (View v : views) {
			v.update(this, notice);
		}
	}

	/**
	 * Saves a {@link Guest} in disk in {@link #DIRECTORY}.{@link #FILE}.
	 *
	 * @param guest {@link Guest Guest} to be saved
	 * @throws Exception If it can't save the guest
	 */
	public void saveGuest(Guest guest) throws Exception
	{
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(DIRECTORY, FILE), true));
			writer.write(guest.toString(), 0, guest.toString().length());
			writer.newLine();
			writer.close();
		} catch (FileNotFoundException fnfe) {
			notice = "File not found";
			notifyViews();
		} catch (Exception ex) {
			notice = "Error while writing the file";
			notifyViews();
		}
	}

	/**
	 * Reads guests saved in disk with name {@link #FILE}.
	 *
	 * @return List of lists (matrix) of guests
	 * @throws Exception If it can't read guest file
	 */
	public Vector<Vector<Object>> getGuests() throws Exception
	{
		Vector<Vector<Object>> response = new Vector<Vector<Object>>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(DIRECTORY, FILE)));
			String line = reader.readLine();

			while (line != null) {
				Vector<Object> guestInfo = new Vector<Object>();
				String[] tokens = line.split(";");

				if (tokens.length >= 3) {
					guestInfo.add(tokens[0]);
					guestInfo.add(tokens[1]);
					guestInfo.add(tokens[2]);
					response.add(guestInfo);
				}

				line = reader.readLine();
			}

			reader.close();
		} catch (FileNotFoundException fnfe) {
			notice = "File not found";
			notifyViews();
		} catch (Exception ex) {
			notice = "There was a problem reading the guest file";
			notifyViews();
		}

		return response;
	}

	/**
	 * Deletes guests by their line indexes in {@link #FILE}.
	 *
	 * @param indexes List of indexes to remove
	 * @throws Exception If it can't rewrite guest file
	 */
	public void deleteGuestsByIndexes(List<Integer> indexes) throws Exception
	{
		if ((indexes == null) || indexes.isEmpty()) {
			return;
		}

		List<String> lines = new ArrayList<>();
		File guestsFile = new File(DIRECTORY, FILE);

		try {
			BufferedReader reader = new BufferedReader(new FileReader(guestsFile));
			String line = reader.readLine();

			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}

			reader.close();
		} catch (FileNotFoundException fnfe) {
			notice = "File not found";
			notifyViews();
			return;
		} catch (Exception ex) {
			notice = "There was a problem reading the guest file";
			notifyViews();
			return;
		}

		Collections.sort(indexes, Collections.reverseOrder());
		for (Integer index : indexes) {
			if ((index != null) && (index >= 0) && (index < lines.size())) {
				lines.remove((int) index);
			}
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(guestsFile, false));

			for (String line : lines) {
				writer.write(line, 0, line.length());
				writer.newLine();
			}

			writer.close();
		} catch (FileNotFoundException fnfe) {
			notice = "File not found";
			notifyViews();
		} catch (Exception ex) {
			notice = "Error while writing the file";
			notifyViews();
		}
	}
}
