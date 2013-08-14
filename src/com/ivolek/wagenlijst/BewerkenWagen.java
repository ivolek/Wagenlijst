package com.ivolek.wagenlijst;

import java.io.IOException;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BewerkenWagen extends Activity {

	EditText Busnr, Bouwjaar, Chassisnummer, Kenteken, MaxPers, Merk, Motornummer, Soort, Telefoon, Type;
	Button buttonBewerken, buttonVerwijderen;
	String busId;
	final Context context = this;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bewerken_wagen);

		Busnr = (EditText) findViewById(R.id.bewerkenBusnr);
		Bouwjaar = (EditText) findViewById(R.id.bewerkenBouwjaar);
		Chassisnummer = (EditText) findViewById(R.id.bewerkenChassisnummer);
		Kenteken = (EditText) findViewById(R.id.bewerkenKenteken);
		MaxPers = (EditText) findViewById(R.id.bewerkenMaxpers);
		Merk = (EditText) findViewById(R.id.bewerkenMerk);
		Motornummer = (EditText) findViewById(R.id.bewerkenMotornummer);
		Soort = (EditText) findViewById(R.id.bewerkenSoort);
		Telefoon = (EditText) findViewById(R.id.bewerkenTelefoon);
		Type = (EditText) findViewById(R.id.bewerkenType);

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		// Get data via the key
		busId = extras.getString("id");
		if (busId != null) {
			Busnr.setText(extras.getString("id"));
			Soort.setText(extras.getString("Soort"));
			Kenteken.setText(extras.getString("Kenteken"));
			Merk.setText(extras.getString("Merk"));
			Type.setText(extras.getString("Type"));
			Bouwjaar.setText(extras.getString("Bouwjaar"));
			Chassisnummer.setText(extras.getString("Chassisnummer"));
			Motornummer.setText(extras.getString("Motornummer"));
			MaxPers.setText(extras.getString("MaxPers"));
			Telefoon.setText(extras.getString("Telefoon"));
		} 

		buttonBewerken = (Button) findViewById(R.id.buttonBewerken);
		buttonVerwijderen = (Button) findViewById(R.id.buttonVerwijderen);

		buttonBewerken.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String text= Busnr.getText().toString();
				if(!Busnr.getText().toString().matches(""))
					XMLwriter(Integer.parseInt(busId), 0); // 0 = Action bewerken 1 = verwijderen
				else{
					Busnr.requestFocus();
					Toast msg = Toast.makeText(getBaseContext(),
							"Vul een Bus nummer in", Toast.LENGTH_LONG);
					msg.show();}

			}

		});
		buttonVerwijderen.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showDialog();
			}

		});
	}

	public void XMLwriter(int Id, int Action){ // 0 = Action bewerken 1 = verwijderen
		try{
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
						if(Action == 0){ //Bewerken
							eElement.getAttributes().getNamedItem("id").setTextContent(this.Busnr.getText().toString());
							eElement.getElementsByTagName("Soort").item(0).setTextContent(this.Soort.getText().toString());
							eElement.getElementsByTagName("Kenteken").item(0).setTextContent(this.Kenteken.getText().toString());
							eElement.getElementsByTagName("Merk").item(0).setTextContent(this.Merk.getText().toString());
							eElement.getElementsByTagName("Type").item(0).setTextContent(this.Type.getText().toString());
							eElement.getElementsByTagName("Bouwjaar").item(0).setTextContent(this.Bouwjaar.getText().toString());
							eElement.getElementsByTagName("Chassisnummer").item(0).setTextContent(this.Chassisnummer.getText().toString());
							eElement.getElementsByTagName("Motornummer").item(0).setTextContent(this.Motornummer.getText().toString());
							eElement.getElementsByTagName("MaxPers").item(0).setTextContent(this.MaxPers.getText().toString());
							eElement.getElementsByTagName("Telefoon").item(0).setTextContent(this.Telefoon.getText().toString());								
						}		
						else if(Action == 1){//verwijderen
							eElement.getParentNode().removeChild(eElement);
						}
					}
				}

				// write the content into xml file
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(getFilesDir().toString() + "/lijst.xml");
			transformer.transform(source, result);

			System.out.println("Done");
			if(Action == 0){
				Toast.makeText(getBaseContext(),
						"Opgeslagen", Toast.LENGTH_LONG).show();
				returnSingleWagenScreen();
			}				
			else if(Action == 1)
			Toast.makeText(getBaseContext(),
					"Verwijderd", Toast.LENGTH_LONG).show();
			returnOverzicht();

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
	}
	public void returnSingleWagenScreen(){
		Intent i = new Intent(getApplicationContext(), SingleWagen.class);
		i.putExtra("id", this.Busnr.getText().toString());
		startActivity(i);
		finish();
	}
	
	public void returnOverzicht(){
		Intent i = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(i);
		finish();
	}
	
	public void showDialog(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
 
			// set title
			alertDialogBuilder.setTitle("Bus verwijderen");
 
			// set dialog message
			alertDialogBuilder
				.setMessage("Weet je zeker het zeker?")
				.setCancelable(false)
				.setPositiveButton("Jazeker!",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						XMLwriter(Integer.parseInt(busId), 1);// 0 = Action bewerken 1 = verwijderen
					}
				  })
				.setNegativeButton("Nou eigenlijk niet..",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
	}
}

