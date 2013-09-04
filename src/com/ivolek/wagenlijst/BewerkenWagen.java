package com.ivolek.wagenlijst;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BewerkenWagen extends Activity {


	private final int CAMERA_RESULT = 1;	
	private final String Tag = getClass().getName();
	EditText Busnr, Bouwjaar, Chassisnummer, Kenteken, MaxPers, Merk, Motornummer, Soort, Telefoon, Type, hiddenEditText;
	Button buttonBewerken, buttonVerwijderen, buttonFoto1Bewerken, buttonFoto2Bewerken, buttonFoto3Bewerken, buttonFoto1Bekijken, buttonFoto2Bekijken, buttonFoto3Bekijken;
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

		hiddenEditText = (EditText) findViewById(R.id.hiddenEditText);
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

		//----------------------------
		buttonFoto1Bewerken = (Button) findViewById(R.id.buttonFoto1Bewerken);
		buttonFoto1Bewerken.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				hiddenEditText.setText("1");
				cameraActivate();	
			}

		});
		buttonFoto2Bewerken = (Button) findViewById(R.id.buttonFoto2Bewerken);		
		buttonFoto2Bewerken.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				hiddenEditText.setText("2");
				cameraActivate();	
			}

		});
		buttonFoto3Bewerken = (Button) findViewById(R.id.buttonFoto3Bewerken);
		buttonFoto3Bewerken.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				hiddenEditText.setText("3");
				cameraActivate();	
			}

		});
		buttonFoto1Bekijken = (Button) findViewById(R.id.buttonFoto1Bekijken);
		buttonFoto1Bekijken.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				File foto3 = new File(getFilesDir(), "/Images/" + Busnr.getText().toString() + "1.jpg");
				if(foto3.exists()) {
				Intent i = new Intent(getApplicationContext(), FotoPincher.class);
				i.putExtra("image", Busnr.getText().toString() +  "1");
				startActivity(i);
				}
				else
					Toast.makeText(getBaseContext(), "Maak eerst een foto.", Toast.LENGTH_SHORT).show();
			}

		});
		
		buttonFoto2Bekijken = (Button) findViewById(R.id.buttonFoto2Bekijken);		
		buttonFoto2Bekijken.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				File foto3 = new File(getFilesDir(), "/Images/" + Busnr.getText().toString() + "2.jpg");
				if(foto3.exists()) {
				Intent i = new Intent(getApplicationContext(), FotoPincher.class);
				i.putExtra("image", Busnr.getText().toString() +  "2");
				startActivity(i);
				}
				else
					Toast.makeText(getBaseContext(), "Maak eerst een foto.", Toast.LENGTH_SHORT).show();
			}

		});
		buttonFoto3Bekijken = (Button) findViewById(R.id.buttonFoto3Bekijken);
		buttonFoto3Bekijken.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				File foto3 = new File(getFilesDir(), "/Images/" + Busnr.getText().toString() + "3.jpg");
				if(foto3.exists()) {
				Intent i = new Intent(getApplicationContext(), FotoPincher.class);
				i.putExtra("image", Busnr.getText().toString() +  "3");
				startActivity(i);
				}
				else
					Toast.makeText(getBaseContext(), "Maak eerst een foto.", Toast.LENGTH_SHORT).show();
			}

		});
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
	
	public void cameraActivate(){
		if(!Busnr.getText().toString().matches("")){
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
			Toast.makeText(getBaseContext(), "Voer eerst een busnummer in.", Toast.LENGTH_LONG).show();
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
	    String uriSting = (file.getAbsolutePath() + "/" +  Busnr.getText().toString() +  hiddenEditText.getText().toString() + ".jpg");
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

}

