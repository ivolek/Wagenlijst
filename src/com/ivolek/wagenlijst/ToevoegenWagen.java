package com.ivolek.wagenlijst;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ToevoegenWagen extends Activity {

	EditText Busnr, Bouwjaar, Chassisnummer, Kenteken, MaxPers, Merk, Motornummer, Soort, Telefoon, Type;
	Button buttonToevoegen;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.toevoegen_wagen);


		Busnr = (EditText) findViewById(R.id.toevoegenBusnr);
		Bouwjaar = (EditText) findViewById(R.id.toevoegenBouwjaar);
		Chassisnummer = (EditText) findViewById(R.id.toevoegenChassisnummer);
		Kenteken = (EditText) findViewById(R.id.toevoegenKenteken);
		MaxPers = (EditText) findViewById(R.id.toevoegenMaxpers);
		Merk = (EditText) findViewById(R.id.toevoegenMerk);
		Motornummer = (EditText) findViewById(R.id.toevoegenMotornummer);
		Soort = (EditText) findViewById(R.id.toevoegenSoort);
		Telefoon = (EditText) findViewById(R.id.toevoegenTelefoon);
		Type = (EditText) findViewById(R.id.toevoegenType);

		buttonToevoegen = (Button) findViewById(R.id.buttonToevoegen);

		buttonToevoegen.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String text= Busnr.getText().toString();
				if(!Busnr.getText().toString().matches(""))
					XMLwriter();
				else{
					Busnr.requestFocus();
					Toast msg = Toast.makeText(getBaseContext(),
							"Vul een Busnummer in.", Toast.LENGTH_LONG);
					msg.show();}

			}

		});
	}

	public void XMLwriter(){
		try{
			String filepath = "file:///" + getFilesDir().toString() + "/lijst.xml";
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("Bus");


			boolean busNrFree = true;
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;	

					if(Integer.parseInt(this.Busnr.getText().toString()) == Integer.parseInt(eElement.getAttribute("id"))){						
						
						busNrFree  = false;
					}
					
				}
			}
			if(busNrFree){
				// Get the root element
				Node wagens = doc.getFirstChild();

				// append a new node to staff		
				Element Bus = doc.createElement("Bus");
				wagens.appendChild(Bus);

				// set attribute to staff element
				Attr attr = doc.createAttribute("id");
				attr.setValue(this.Busnr.getText().toString());
				Bus.setAttributeNode(attr);

				// shorten way
				// staff.setAttribute("id", "1");

				Element Soort = doc.createElement("Soort");
				Soort.appendChild(doc.createTextNode(this.Soort.getText().toString()));
				Bus.appendChild(Soort);

				Element Kenteken = doc.createElement("Kenteken");
				Kenteken.appendChild(doc.createTextNode(this.Kenteken.getText().toString()));
				Bus.appendChild(Kenteken);

				Element Merk = doc.createElement("Merk");
				Merk.appendChild(doc.createTextNode(this.Merk.getText().toString()));
				Bus.appendChild(Merk);

				Element Type = doc.createElement("Type");
				Type.appendChild(doc.createTextNode(this.Type.getText().toString()));
				Bus.appendChild(Type);

				Element Bouwjaar = doc.createElement("Bouwjaar");
				Bouwjaar.appendChild(doc.createTextNode(this.Bouwjaar.getText().toString()));
				Bus.appendChild(Bouwjaar);

				Element Chassisnummer = doc.createElement("Chassisnummer");
				Chassisnummer.appendChild(doc.createTextNode(this.Chassisnummer.getText().toString()));
				Bus.appendChild(Chassisnummer);

				Element Motornummer = doc.createElement("Motornummer");
				Motornummer.appendChild(doc.createTextNode(this.Motornummer.getText().toString()));
				Bus.appendChild(Motornummer);

				Element MaxPers = doc.createElement("MaxPers");
				MaxPers.appendChild(doc.createTextNode(this.MaxPers.getText().toString()));
				Bus.appendChild(MaxPers);

				Element Telefoon = doc.createElement("Telefoon");
				Telefoon.appendChild(doc.createTextNode(this.Telefoon.getText().toString()));
				Bus.appendChild(Telefoon);
				
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(getFilesDir().toString() + "/lijst.xml");
			transformer.transform(source, result);

			System.out.println("Done");
			Toast.makeText(getBaseContext(),
					"Toegevoegd", Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(getBaseContext(),"Busnummer bestaat al.", Toast.LENGTH_LONG).show();
				Busnr.requestFocus();
			}
				

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
		//		try {			 
		//			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		//			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		//	 
		//			// root elements
		//			Document doc = docBuilder.newDocument();
		//			Element rootElement = doc.createElement("Wagens");
		//			doc.appendChild(rootElement);
		//	 
		//			// staff elements
		//			Element Bus = doc.createElement("Bus");
		//			rootElement.appendChild(Bus);
		//	 
		//			// set attribute to staff element
		//			Attr attr = doc.createAttribute("id");
		//			attr.setValue(this.Busnr.getText().toString());
		//			Bus.setAttributeNode(attr);
		//	 
		//			// shorten way
		//			// staff.setAttribute("id", "1");
		//	 
		//			Element Soort = doc.createElement("Soort");
		//			Soort.appendChild(doc.createTextNode(this.Soort.getText().toString()));
		//			Bus.appendChild(Soort);
		//	 
		//			Element Kenteken = doc.createElement("Kenteken");
		//			Kenteken.appendChild(doc.createTextNode(this.Kenteken.getText().toString()));
		//			Bus.appendChild(Kenteken);
		//	 
		//			Element Merk = doc.createElement("Merk");
		//			Merk.appendChild(doc.createTextNode(this.Merk.getText().toString()));
		//			Bus.appendChild(Merk);
		//	 
		//			Element Type = doc.createElement("Type");
		//			Type.appendChild(doc.createTextNode(this.Type.getText().toString()));
		//			Bus.appendChild(Type);
		//			
		//			Element Bouwjaar = doc.createElement("Bouwjaar");
		//			Bouwjaar.appendChild(doc.createTextNode(this.Bouwjaar.getText().toString()));
		//			Bus.appendChild(Bouwjaar);
		//			
		//			Element Chassisnummer = doc.createElement("Chassisnummer");
		//			Chassisnummer.appendChild(doc.createTextNode(this.Bouwjaar.getText().toString()));
		//			Bus.appendChild(Chassisnummer);
		//
		//			Element Motornummer = doc.createElement("Motornummer");
		//			Motornummer.appendChild(doc.createTextNode(this.Motornummer.getText().toString()));
		//			Bus.appendChild(Motornummer);
		//
		//			Element MaxPers = doc.createElement("MaxPers");
		//			MaxPers.appendChild(doc.createTextNode(this.MaxPers.getText().toString()));
		//			Bus.appendChild(MaxPers);
		//
		//			Element Telefoon = doc.createElement("Telefoon");
		//			Telefoon.appendChild(doc.createTextNode(this.Telefoon.getText().toString()));
		//			Bus.appendChild(Telefoon);
		//
		//			// write the content into xml file
		//			TransformerFactory transformerFactory = TransformerFactory.newInstance();
		//			Transformer transformer = transformerFactory.newTransformer();
		//			DOMSource source = new DOMSource(doc);
		//	 
		//			// Output to console for testing
		//			 StreamResult result = new StreamResult(getFilesDir().toString() + "/lijst.xml");
		//	 
		//			transformer.transform(source, result);
		//	 
		//		  } catch (ParserConfigurationException pce) {
		//			pce.printStackTrace();
		//		  } catch (TransformerException tfe) {
		//			tfe.printStackTrace();
		//		  }
	}

}
