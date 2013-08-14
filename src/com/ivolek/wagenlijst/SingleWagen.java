package com.ivolek.wagenlijst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.content.ClipData.Item;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SingleWagen extends Activity {

	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	//List<String> BUSSPECS = new ArrayList<String>();
	List<String> BUSSPECS = new ArrayList<String>();
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

createListView();
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		// Get data via the key
		String id = extras.getString("id");
		if (id != null) {
			getXML(Integer.parseInt(id));
		} 

	}

	public void getXML(int Id){
		try {
			String filepath = "file:///" + getFilesDir().toString() + "/lijst.xml";
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("Bus");


			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					if(Id == Integer.parseInt(eElement.getAttribute("id"))){						
						data.add(createBusSpecs(eElement.getElementsByTagName("Soort").item(0).getTextContent(), "Soort"));
						data.add(createBusSpecs(eElement.getElementsByTagName("Kenteken").item(0).getTextContent(), "Kenteken"));
						data.add(createBusSpecs(eElement.getElementsByTagName("Merk").item(0).getTextContent(), "Merk"));
						data.add(createBusSpecs(eElement.getElementsByTagName("Type").item(0).getTextContent(), "Type"));
						data.add(createBusSpecs(eElement.getElementsByTagName("Bouwjaar").item(0).getTextContent(), "Bouwjaar"));
						data.add(createBusSpecs(eElement.getElementsByTagName("Chassisnummer").item(0).getTextContent(), "Chassisnummer"));
						data.add(createBusSpecs(eElement.getElementsByTagName("Motornummer").item(0).getTextContent(), "Motornummer"));
						data.add(createBusSpecs(eElement.getElementsByTagName("MaxPers").item(0).getTextContent(), "MaxPers"));
						data.add(createBusSpecs(eElement.getElementsByTagName("Telefoon").item(0).getTextContent(), "Telefoon"));
						
						createListView();
						System.out.println("jawel");

						BUSSPECS.add(0, eElement.getAttribute("id"));
						BUSSPECS.add(1, eElement.getElementsByTagName("Soort").item(0).getTextContent());
						BUSSPECS.add(2, eElement.getElementsByTagName("Kenteken").item(0).getTextContent());
						BUSSPECS.add(3, eElement.getElementsByTagName("Merk").item(0).getTextContent());
						BUSSPECS.add(4, eElement.getElementsByTagName("Type").item(0).getTextContent());
						BUSSPECS.add(5, eElement.getElementsByTagName("Bouwjaar").item(0).getTextContent());
						BUSSPECS.add(6, eElement.getElementsByTagName("Chassisnummer").item(0).getTextContent());
						BUSSPECS.add(7, eElement.getElementsByTagName("Motornummer").item(0).getTextContent());
						BUSSPECS.add(8, eElement.getElementsByTagName("MaxPers").item(0).getTextContent());
						BUSSPECS.add(9, eElement.getElementsByTagName("Telefoon").item(0).getTextContent());
						return;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private HashMap<String, String>createBusSpecs(String value, String name){
		HashMap<String, String> busSpecs = new HashMap<String, String>();
		busSpecs.put("titel", value);
		busSpecs.put("sub", name);
		return busSpecs;
	}
	
	public void createListView(){


//		Map<String, String> datum = new HashMap<String, String>(2);
//		for (int i=0; i<10; i++) {
//			datum.put("title", titel);
//			datum.put("date", "date");
//			data.add(datum);
//		}
		setContentView(R.layout.singlewagen);
		ListView itemlist = (ListView) findViewById(R.id.listview_singlewagen);
		SimpleAdapter adapter = new SimpleAdapter(this, data,
				android.R.layout.simple_list_item_2,
				new String[] {"titel", "sub"},
				new int[] {android.R.id.text1,
				android.R.id.text2});
		itemlist.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.layout.menu_single, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.menu_bewerken:
			Intent i = new Intent(getApplicationContext(), BewerkenWagen.class);
			i.putExtra("id", BUSSPECS.get(0));
			i.putExtra("Soort", BUSSPECS.get(1));
			i.putExtra("Kenteken", BUSSPECS.get(2));
			i.putExtra("Merk", BUSSPECS.get(3));
			i.putExtra("Type", BUSSPECS.get(4));
			i.putExtra("Bouwjaar", BUSSPECS.get(5));
			i.putExtra("Chassisnummer", BUSSPECS.get(6));
			i.putExtra("Motornummer", BUSSPECS.get(7));
			i.putExtra("MaxPers", BUSSPECS.get(8));
			i.putExtra("Telefoon", BUSSPECS.get(9));
			startActivity(i);
			//finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}
}