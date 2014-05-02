package us.rjuhsd.ohs.visite.tasks;

import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import us.rjuhsd.ohs.visite.Item;

public class ItemGetTask extends AsyncTask<Item, Void, Void> {

	private Item[] items;

	@Override
	protected Void doInBackground(Item... items) {
		this.items = items;
		for(Item item : items) {
			try {
				HttpGet request = new HttpGet(item.getURL()+item.pk);
				HttpClient client = new DefaultHttpClient();
				HttpResponse response = client.execute(request);
				String raw = EntityUtils.toString(response.getEntity(), "UTF-8");
				item.process(raw);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		for(Item item : items) {
			item.draw();
		}
	}
}
