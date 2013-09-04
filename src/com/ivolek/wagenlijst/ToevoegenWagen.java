package com.ivolek.wagenlijst;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
	Button buttonFoto1Maken, buttonFoto2Maken, buttonFoto3Maken, buttonFoto1Bekijken, buttonFoto2Bekijken, buttonFoto3Bekijken, buttonToevoegen;
	int fotoId;
	private final int CAMERA_RESULT = 1;	
	private final String Tag = getClass().getName();
	ImageView imageView1;
	Camera camera;
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

		buttonFoto1Maken = (Button) findViewById(R.id.buttonFoto1Maken);
		buttonFoto2Maken = (Button) findViewById(R.id.buttonFoto2Maken);
		buttonFoto3Maken = (Button) findViewById(R.id.buttonFoto3Maken);

		buttonFoto1Maken.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{hiddenEditText.setText("1");
			cameraActivate();		
			}
		});

		buttonFoto2Maken.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{hiddenEditText.setText("2");
			cameraActivate();
			}
		});

		buttonFoto3Maken.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				hiddenEditText.setText("3");
				cameraActivate();
			}

		});



		buttonFoto1Bekijken = (Button) findViewById(R.id.buttonFoto1Bekijken);
		buttonFoto2Bekijken = (Button) findViewById(R.id.buttonFoto2Bekijken);
		buttonFoto3Bekijken = (Button) findViewById(R.id.buttonFoto3Bekijken);

		buttonFoto1Bekijken.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(!Busnr.getText().toString().matches("")){
					if(XMLcheckIdFree()){
						File foto3 = new File(getFilesDir(), "/Images/" + Busnr.getText().toString() + "1.jpg");
						if(foto3.exists()) {
						Intent i = new Intent(getApplicationContext(), FotoPincher.class);
						i.putExtra("image", Busnr.getText().toString() +  "1");
						startActivity(i);
						}
						else
							Toast.makeText(getBaseContext(), "Maak eerst een foto.", Toast.LENGTH_LONG).show();
							
					}
					else
						Toast.makeText(getBaseContext(), "Busnr bestaat al.", Toast.LENGTH_LONG).show();
				}
				else 
					Toast.makeText(getBaseContext(), "Voer eerst een busnummer in.", Toast.LENGTH_LONG).show();
			}

		});
		buttonFoto2Bekijken.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(!Busnr.getText().toString().matches("")){
					if(XMLcheckIdFree()){
						File foto3 = new File(getFilesDir(), "/Images/" + Busnr.getText().toString() + "2.jpg");
						if(foto3.exists()) {
						Intent i = new Intent(getApplicationContext(), FotoPincher.class);
						i.putExtra("image", Busnr.getText().toString() +  "2");
						startActivity(i);
						}
						else
							Toast.makeText(getBaseContext(), "Maak eerst een foto.", Toast.LENGTH_LONG).show();
					}
					else
						Toast.makeText(getBaseContext(), "Busnr bestaat al.", Toast.LENGTH_LONG).show();
				}
				else 
					Toast.makeText(getBaseContext(), "Voer eerst een busnummer in.", Toast.LENGTH_LONG).show();
			}

		});
		buttonFoto3Bekijken.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{					
				if(!Busnr.getText().toString().matches("")){
					if(XMLcheckIdFree()){
						File foto3 = new File(getFilesDir(), "/Images/" + Busnr.getText().toString() + "3.jpg");
						if(foto3.exists()) {
						Intent i = new Intent(getApplicationContext(), FotoPincher.class);
						i.putExtra("image", Busnr.getText().toString() +  "3");
						startActivity(i);
						}
						else
							Toast.makeText(getBaseContext(), "Maak eerst een foto.", Toast.LENGTH_LONG).show();
					}
					else
						Toast.makeText(getBaseContext(), "Busnr bestaat al.", Toast.LENGTH_LONG).show();
				}
				else 
					Toast.makeText(getBaseContext(), "Voer eerst een busnummer in.", Toast.LENGTH_LONG).show();
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
//		File foto1 = new File(getFilesDir(), "/Images/" + Busnr.getText().toString() + "1.jpg");
//		if(foto1.exists()) {
//			System.out.println("foto bestaat heeeeujjj!");
//			buttonFoto1Bekijken.setVisibility(View.VISIBLE);
//		}
//		File foto2 = new File(getFilesDir(), "/Images/" + Busnr.getText().toString() + "2.jpg");
//		if(foto2.exists()) {
//			buttonFoto2Bekijken.setVisibility(View.VISIBLE);
//		}
//		File foto3 = new File(getFilesDir(), "/Images/" + Busnr.getText().toString() + "3.jpg");
//		if(foto3.exists()) {
//			buttonFoto3Bekijken.setVisibility(View.VISIBLE);
//		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == CAMERA_RESULT) {
			File out = new File(getFilesDir(), "newImage.jpg");
			if(!out.exists()) {
				Toast.makeText(getBaseContext(),
						"Error while capturing image", Toast.LENGTH_LONG)
						.show();
				return;
			}
			else{
				//try {
				compressImage(getFilesDir() + "/newImage.jpg");
				//} catch (IOException e) {
				// TODO Auto-generated catch block
				//	e.printStackTrace();
				//}
			}
			//			Bitmap mBitmap = BitmapFactory.decodeFile(out.getAbsolutePath());
			//			imageView1.setImageBitmap(mBitmap);

		}
	}

	//	public void copy() throws IOException {
	//	    InputStream in = new FileInputStream(getFilesDir() + "/newImage.jpg");
	////		Bitmap mBitmap = BitmapFactory.decodeStream(in);
	////		mBitmap = Bitmap.createScaledBitmap(mBitmap, 160, 160, true);
	//	    
	//	    OutputStream out = new FileOutputStream(getFilesDir() + "/" + Busnr.getText().toString() + hiddenEditText.getText().toString() + ".jpg");
	//
	//	    Bitmap mBitmap = BitmapFactory.decodeStream(in);
	//        mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
	//	    // Transfer bytes from in to out
	//	    byte[] buf = new byte[1024];
	//	    int len;
	//	    while ((len = in.read(buf)) > 0) {
	//	        out.write(buf, 0, len);
	//	    }
	//	    in.close();
	//	    out.close();
	//	}

	public String compressImage(String imageUri) {

		String filePath = getRealPathFromURI(imageUri);
		Bitmap scaledBitmap = null;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;						
		Bitmap bmp = BitmapFactory.decodeFile(filePath,options);

		int actualHeight = options.outHeight;
		int actualWidth = options.outWidth;
		float maxHeight = 816.0f;
		float maxWidth = 612.0f;
		float imgRatio = actualWidth / actualHeight;
		float maxRatio = maxWidth / maxHeight;

		if (actualHeight > maxHeight || actualWidth > maxWidth) {
			if (imgRatio < maxRatio) {
				imgRatio = maxHeight / actualHeight;
				actualWidth = (int) (imgRatio * actualWidth);
				actualHeight = (int) maxHeight;
			} else if (imgRatio > maxRatio) {
				imgRatio = maxWidth / actualWidth;
				actualHeight = (int) (imgRatio * actualHeight);
				actualWidth = (int) maxWidth;
			} else {
				actualHeight = (int) maxHeight;
				actualWidth = (int) maxWidth;     

			}
		}

		options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inTempStorage = new byte[16*1024];

		try{	
			bmp = BitmapFactory.decodeFile(filePath,options);
		}
		catch(OutOfMemoryError exception){
			exception.printStackTrace();

		}
		try{
			scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
		}
		catch(OutOfMemoryError exception){
			exception.printStackTrace();
		}

		float ratioX = actualWidth / (float) options.outWidth;
		float ratioY = actualHeight / (float)options.outHeight;
		float middleX = actualWidth / 2.0f;
		float middleY = actualHeight / 2.0f;

		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bmp, middleX - bmp.getWidth()/2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


		ExifInterface exif;
		try {
			exif = new ExifInterface(filePath);

			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
			Log.d("EXIF", "Exif: " + orientation);
			Matrix matrix = new Matrix();
			if (orientation == 6) {
				matrix.postRotate(90);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 3) {
				matrix.postRotate(180);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 8) {
				matrix.postRotate(270);
				Log.d("EXIF", "Exif: " + orientation);
			}
			scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileOutputStream out = null;
		String filename = getFilename();
		try {
			out = new FileOutputStream(filename);
			scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return filename;

	}

	public String getFilename() {
		File file = new File(getFilesDir(), "Images");
		if (!file.exists()) {
			file.mkdirs();
		}
		String uriSting = (file.getAbsolutePath() + "/" +  Busnr.getText().toString() + hiddenEditText.getText().toString() + ".jpg");
	    //bekijkenVisible(Integer.parseInt(hiddenEditText.getText().toString()));
		return uriSting;

	}

	private String getRealPathFromURI(String contentURI) {
		Uri contentUri = Uri.parse(contentURI);
		Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
		if (cursor == null) {
			return contentUri.getPath();
		} else {
			cursor.moveToFirst();
			int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			return cursor.getString(index);
		}
	}

	public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;      }       final float totalPixels = width * height;       final float totalReqPixelsCap = reqWidth * reqHeight * 2;       while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}

			return inSampleSize;
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

				Element Foto1 = doc.createElement("Foto1");
				Foto1.appendChild(doc.createTextNode(getFilesDir() + "/Images/" + Busnr.getText().toString() + "1.jpg"));
				Bus.appendChild(Foto1);

				Element Foto2 = doc.createElement("Foto2");
				Foto2.appendChild(doc.createTextNode(getFilesDir() + "/Images/" + Busnr.getText().toString() + "2.jpg"));
				Bus.appendChild(Foto2);

				Element Foto3 = doc.createElement("Foto3");
				Foto3.appendChild(doc.createTextNode(getFilesDir() + "/Images/" + Busnr.getText().toString() + "3.jpg"));
				Bus.appendChild(Foto3);

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
