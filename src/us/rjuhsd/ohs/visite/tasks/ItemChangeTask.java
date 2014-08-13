package us.rjuhsd.ohs.visite.tasks;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import us.rjuhsd.ohs.visite.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemChangeTask extends AsyncTask<Item, Void, Void> {

	private Item[] items;

	@Override
	protected Void doInBackground(Item... items) {
		this.items = items;
		for(Item item : items) {
			try {
				HttpPost request;
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("id", item.pk+""));
				nvps.add(new BasicNameValuePair("name", item.name));
				nvps.add(new BasicNameValuePair("next", "/inv/item/"+item.pk+"/json"));

				if(item.checkedIn) {
					request = new HttpPost(item.getURL() + "/checkin");
				} else {
					request = new HttpPost(item.getURL() + "/checkout");
				}
				Log.d("Dragon",item.getURL() + "/checkin");
				request.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
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
