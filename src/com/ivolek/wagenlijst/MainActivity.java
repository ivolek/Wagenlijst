package com.ivolek.wagenlijst;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	static final String[] FRUITS = new String[] { "Apple", "Avocado", "Banana",
		"Blueberry", "Coconut", "Durian", "Guava", "Kiwifruit",
		"Jackfruit", "Mango", "Olive", "Pear", "Sugar-apple" };
	List<String> BUSSEN = new ArrayList<String>();
	List<Map<String, String>> busList = new ArrayList<Map<String,String>>();
	SimpleAdapter simpleAdpt;
	EditText Busnr;
	public static Activity mainActivity;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		mainActivity = this;
		setContentView(R.layout.activity_main);


		Busnr = (EditText) findViewById(R.id.zoekenBusNr);

		Button buttonZoeken = (Button) findViewById(R.id.buttonZoeken);

		buttonZoeken.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String text= Busnr.getText().toString();
				if(!Busnr.getText().toString().matches(""))
					getXML(Integer.parseInt(Busnr.getText().toString()));
				else{
					Busnr.requestFocus();
					Toast msg = Toast.makeText(getBaseContext(),
							"Vul een Busnummer in.", Toast.LENGTH_LONG);
					msg.show();}

			}

		});


	}
	@Override
	public void onResume() {
		super.onResume();
		ListView lv = (ListView) findViewById(R.id.listView);

		simpleAdpt = new SimpleAdapter(this, busList, android.R.layout.simple_list_item_1, new String[] {"bus"}, new int[] {android.R.id.text1});
		
		lv.setAdapter(simpleAdpt);
		// no more this
		//setContentView(R.layout.activity_main);
		getXML(9999);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		     public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
		                             long id) {

		         // We know the View is a TextView so we can cast it
		         TextView clickedView = (TextView) view;

		         Intent i = new Intent(getApplicationContext(), SingleWagen.class);
		 						i.putExtra("id", clickedView.getText().toString());
		 						System.out.println("the id i give is "+ clickedView.getText());
		 						startActivity(i);
		     }
		});
		// We get the ListView component from the layout 

//		Toast.makeText(getApplicationContext(),
//				"yeee", Toast.LENGTH_SHORT).show();

	}
	public void getXML(int Id){
		boolean busExist = false;
		try {
			String filepath = "file:///" + getFilesDir().toString() + "/lijst.xml";
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("Bus");

			//Clear the bussen list befor adding item to it
			busList.clear();
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					if(Id != 9999){
						if(Id == Integer.parseInt(eElement.getAttribute("id"))){	
							System.out.println("----------------------");
							//add the ID's to the bussenlist.
							busList.add(createBus("bus", eElement.getAttribute("id")));
							busExist = true;							
						}
							
					}
					else{
						busList.add(createBus("bus", eElement.getAttribute("id")));
						busExist = true;
					}
				}
			}	
			if(!busExist)
				Toast.makeText(getApplicationContext(),"Dit busnummer bestaat niet.", Toast.LENGTH_SHORT).show();				
			else
			createListview();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private HashMap<String, String> createBus(String key, String name) {
		HashMap<String, String> bus = new HashMap<String, String>();
		bus.put(key, name);

		return bus;
	}

	public void createListview(){
		simpleAdpt.notifyDataSetChanged();
		
		
		//		setListAdapter(new ArrayAdapter<String>(this, R.layout.listview, BUSSEN));
		//
		//		ListView listView = getListView();
		//		listView.setTextFilterEnabled(true);
		//
		//		listView.setOnItemClickListener(new OnItemClickListener() {
		//			public void onItemClick(AdapterView<?> parent, View view,
		//					int position, long id) {
		//
		//				Intent i = new Intent(getApplicationContext(), SingleWagen.class);
		//				i.putExtra("id", BUSSEN.get(position));
		//				System.out.println("the id i give is "+ BUSSEN.get(position));
		//				startActivity(i);
		//				// When clicked, show a toast with the TextView text
		//				Toast.makeText(getApplicationContext(),
		//						((TextView) view).getText(), Toast.LENGTH_SHORT).show();
		//			}
		//		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.layout.menu_lijst, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.menu_toevoegen:
			Intent i = new Intent(getApplicationContext(), ToevoegenWagen.class);
			startActivity(i);
			//finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

}