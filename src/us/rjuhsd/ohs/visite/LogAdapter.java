package us.rjuhsd.ohs.visite;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LogAdapter extends ArrayAdapter<LogEntry> {
	private final Context context;
	private final int resourceId;
	private final ArrayList<LogEntry> logs;

	public LogAdapter(Context context, int textViewResourceId, ArrayList<LogEntry> logs) {
		super(context, textViewResourceId, logs);
		this.context = context;
		this.resourceId = textViewResourceId;
		this.logs = logs;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if(convertView==null){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(resourceId, parent, false);
		}

		LogEntry logEntry = logs.get(position);

		TextView textViewItem = (TextView) convertView.findViewById(android.R.id.text1);
		TextView textViewItem2 = (TextView) convertView.findViewById(android.R.id.text2);
		textViewItem.setText(logEntry.person+" for "+logEntry.daysCheckedout+" day(s)");
		textViewItem2.setText("("+logEntry.checkout+" to "+logEntry.checkin+")");

		return convertView;
	}
}
