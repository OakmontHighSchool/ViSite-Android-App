package us.rjuhsd.ohs.visite;

import org.json.JSONException;
import org.json.JSONObject;

public class LogEntry {
	public Item item;
	public int daysCheckedout;
	public String person;
	public String checkout;
	public String checkin;

	public LogEntry(Item item, JSONObject json) {
		this.item = item;
		try {
			person = json.getString("person");
			daysCheckedout = json.getInt("daysCheckedout");
			checkout = json.getString("checkout");
			checkin = json.getString("checkin");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
