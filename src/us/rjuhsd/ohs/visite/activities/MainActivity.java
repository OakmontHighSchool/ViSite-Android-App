package us.rjuhsd.ohs.visite.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import us.rjuhsd.ohs.visite.Item;
import us.rjuhsd.ohs.visite.R;
import us.rjuhsd.ohs.visite.tasks.ItemBarcodeLookupTask;
import us.rjuhsd.ohs.visite.zxing.IntentIntegrator;
import us.rjuhsd.ohs.visite.zxing.IntentResult;

public class MainActivity extends Activity {

	public Item item;
    public String barcode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.scan_code:
				new IntentIntegrator(this).initiateScan();
				break;
			case R.id.check_button:
				item.editButton();
				break;
			case R.id.web_button:
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.getURL()+item.pk)));
				break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
			case IntentIntegrator.REQUEST_CODE: {
				if (resultCode != RESULT_CANCELED) {
					IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
					if (scanResult != null) {
                        String upc = scanResult.getContents();
                        String[] qrdata = upc.split(":");
                        if (qrdata.length == 2) {
                            item = new Item(this, qrdata[0], Integer.parseInt(qrdata[1]));
                        } else if(qrdata.length == 1) {
                            barcode = qrdata[0];
                            new ItemBarcodeLookupTask().execute(this);
                        } else {
							qrError();
						}
					}
				}
				break;
			}
		}
	}

	public void qrError() {
		new AlertDialog.Builder(this)
				.setTitle("Invalid Item ID")
				.setMessage("This code appears invalid, try again")
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Hey they clicked me!
					}
				})
				.setIcon(android.R.drawable.ic_dialog_alert)
				.show();
	}
}
