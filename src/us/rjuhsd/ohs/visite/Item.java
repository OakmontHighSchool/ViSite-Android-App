package us.rjuhsd.ohs.visite;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import us.rjuhsd.ohs.visite.activities.MainActivity;
import us.rjuhsd.ohs.visite.tasks.ItemChangeTask;
import us.rjuhsd.ohs.visite.tasks.ItemGetTask;

import java.util.HashMap;
import java.util.Map;

public class Item {
	private final MainActivity activity;
	public boolean checkedIn;
	public int pk;
	private String tag;
	public String person;
	public String name;

	public Item(MainActivity activity, String tag, int pk) {
		this.activity = activity;
		this.checkedIn = false;
		this.pk = pk;
		this.tag = tag;
		this.person = "";
		new ItemGetTask().execute(this);
		((TextView) activity.findViewById(R.id.status)).setText("");
		activity.findViewById(R.id.check_button).setVisibility(View.GONE);
		activity.findViewById(R.id.edit_name).setVisibility(View.GONE);
	}

	public void editButton() {
		if(((EditText)activity.findViewById(R.id.edit_name)).getText().toString().equals("") && checkedIn) {
			return;
		}
		name = ((EditText) activity.findViewById(R.id.edit_name)).getText().toString();
		checkedIn = !checkedIn;
		new ItemChangeTask().execute(this);
	}

	public void draw() {
		if(name == null) {
			activity.qrError();
			return;
		}
		if (checkedIn) {
			((TextView) activity.findViewById(R.id.status)).setText("'"+name+"' is checked in");
			((Button)activity.findViewById(R.id.check_button)).setText("Check out this item");
			activity.findViewById(R.id.edit_name).setVisibility(View.VISIBLE);
		} else {
			((TextView) activity.findViewById(R.id.status)).setText("'"+name+"' is checked out to '"+person+"'");
			((Button)activity.findViewById(R.id.check_button)).setText("Check in this item");
			activity.findViewById(R.id.edit_name).setVisibility(View.GONE);
		}
		activity.findViewById(R.id.check_button).setVisibility(View.VISIBLE);
	}

	public void process(String response) {
		try {
			JSONObject json = new JSONObject(response);
			name = json.getString("name");
			person = json.optString("person");
			checkedIn = !json.getBoolean("checkout");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private String getIP() {
		Map<String,String> map = new HashMap<String, String>();
		map.put("Media","10.1.121.80");
		map.put("Debug","10.1.121.38");
		return map.get(tag);
	}

	public String getURL() {
		return "http://"+getIP()+"/inv/item/";
	}
}
