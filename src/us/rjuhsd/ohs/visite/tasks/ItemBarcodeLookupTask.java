package us.rjuhsd.ohs.visite.tasks;

import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import us.rjuhsd.ohs.visite.Item;
import us.rjuhsd.ohs.visite.activities.MainActivity;

public class ItemBarcodeLookupTask extends AsyncTask<MainActivity, Void, Void> {

	private MainActivity[] activities;

	@Override
	protected Void doInBackground(MainActivity... activities) {
		this.activities = activities;
		for(MainActivity activity : activities) {
			try {
				HttpGet request = new HttpGet(Item.getURL(Item.getMap().get("Media"))+"/barcode/"+activity.barcode);
				HttpClient client = new DefaultHttpClient();
				HttpResponse response = client.execute(request);
				String raw = EntityUtils.toString(response.getEntity(), "UTF-8");
                Item item  = new Item(activity);
                activity.item = item;
				item.process(raw);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		for(MainActivity activity : activities) {
			activity.item.draw();
		}
	}
}
