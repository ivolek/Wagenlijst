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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ToevoegenWagen extends Activity {

	EditText Busnr, Bouwjaar, Chassisnummer, Kenteken, MaxPers, Merk, Motornummer, Soort, Telefoon, Type, hiddenEditText;
	Button button1, button2, button3, buttonToevoegen;
	int fotoId;
	private final int CAMERA_RESULT = 1;	
	private final String Tag = getClass().getName();
	ImageView imageView1;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.toevoegen_wagen);

		File file = new File(getFilesDir().toString() +  "/lijst.xml");
		if(!file.exists())
			createXML();

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
		
		hiddenEditText = (EditText) findViewById(R.id.hiddenEditText);
		imageView1 = (ImageView)findViewById(R.id.imageView1);

		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
		
		button1.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{hiddenEditText.setText("1");
			cameraActivate();		
			}
		});
		
		button2.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{hiddenEditText.setText("2");
			cameraActivate();
			}
		});
		
		button3.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				hiddenEditText.setText("3");
				cameraActivate();
			}

		});
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.i(Tag, "Receive the camera result");
		System.out.println(" HET resultCode IS " + resultCode);
		System.out.println(" HET RESULT OK CODE IS " + RESULT_OK);
		System.out.println(" HET requestCode IS " + requestCode);
		System.out.println(" HET CAMERA RESULT IS " + CAMERA_RESULT);
		System.out.println(RESULT_OK); 
		if (resultCode == RESULT_OK && requestCode == CAMERA_RESULT) {
			File out = new File(getFilesDir(), "newImage.jpg");
			Log.i(Tag, "STEP 1");
			if(!out.exists()) {
				Log.i(Tag, "Error while capturing image");
				Toast.makeText(getBaseContext(),
						"Error while capturing image", Toast.LENGTH_LONG)
						.show();
				return;				 
			}
			else{
				System.out.println("Het fotoId is !!!!!!!!!!! " + fotoId);
				File niew = new File(getFilesDir(), Busnr.getText().toString() + hiddenEditText.getText().toString() + ".jpg");
				out.renameTo(niew);
				Log.i(Tag, "Changed the file name");
			}
//			Bitmap mBitmap = BitmapFactory.decodeFile(out.getAbsolutePath());
//			imageView1.setImageBitmap(mBitmap);
		}

		Log.i(Tag, "Receive the camera result");

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		imageView1 = null;
	}
	
	public void cameraActivate(){
		if(!Busnr.getText().toString().matches("")){
			if(XMLcheckIdFree()){
				PackageManager pm = getPackageManager();
				if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
					Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					i.putExtra(MediaStore.EXTRA_OUTPUT, MyFileContentProvider.CONTENT_URI);
					startActivityForResult(i, CAMERA_RESULT);

				} else {
					Toast.makeText(getBaseContext(), "Camera is not available", Toast.LENGTH_LONG).show();
				} 
			}
			else
				Toast.makeText(getBaseContext(), "Busnr bestaat al.", Toast.LENGTH_LONG).show();
		}
		else 
			Toast.makeText(getBaseContext(), "Voer eerst een busnummer in.", Toast.LENGTH_LONG).show();
	}

	private void createXML() {
		try {			 
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Wagens");
			doc.appendChild(rootElement);				 

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			// Output to console for testing
			StreamResult result = new StreamResult(getFilesDir().toString() + "/lijst.xml");

			transformer.transform(source, result);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}

	}

	public boolean XMLcheckIdFree(){
		boolean busNrFree = true;
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

					if(Integer.parseInt(this.Busnr.getText().toString()) == Integer.parseInt(eElement.getAttribute("id"))){						

						busNrFree  = false;
					}

				}
			}
			return busNrFree;
		}		
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return busNrFree;
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
				MainActivity.mainActivity.finish();
				finish();
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(i);

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
