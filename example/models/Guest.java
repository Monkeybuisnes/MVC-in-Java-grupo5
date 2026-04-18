package models;


/**
 * Represents an invited guest.
 */
public class Guest 
{
	//-----------------------------------------------------------------------
	//		Attributes
	//-----------------------------------------------------------------------
	private String name;
	private String email;
	private String eventDescription;


	//-----------------------------------------------------------------------
	//		Methods
	//-----------------------------------------------------------------------
	@Override
	public String toString()
	{
		return getName() + ";" + getEmail() + ";" + getEventDescription();
	}


	//-----------------------------------------------------------------------
	//		Getters & Setters
	//-----------------------------------------------------------------------
	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	public String getEmail() { return email; }

	public void setEmail(String email) { this.email = email; }

	public String getEventDescription() { return eventDescription; }

	public void setEventDescription(String eventDescription) { this.eventDescription = eventDescription; }
}
