package iiitb.hobbit.util;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.content.res.AssetManager;

public class ParserDOM {

	public ArrayList<CriminalData> parse(Context context) {
		ArrayList<CriminalData> datapoints = new ArrayList<CriminalData>();
		AssetManager am = context.getAssets();
		double latitude;
		double longitude;
		int counter = 0;

		try {
			for (int j = 1; j <= 30; j++) {

				InputStream is = am.open(j + ".gpx");
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(is);

				doc.getDocumentElement().normalize();

				NodeList nList = doc.getElementsByTagName("trkpt");

				for (int temp = 0; temp < nList.getLength(); temp++) {
					CriminalData data = new CriminalData();
					Node nNode = nList.item(temp);

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {

						Element eElement = (Element) nNode;

						latitude = Double.parseDouble(eElement
								.getAttribute("lat"));
						longitude = Double.parseDouble(eElement
								.getAttribute("lon"));

						data.setNodeId(counter++);
						data.setCriminalId(j);
						data.setLatitude(latitude);
						data.setLongitude(longitude);
						data.setTime(new SimpleDateFormat(
								"yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
								.parse(eElement.getElementsByTagName("time")
										.item(0).getTextContent()));

						datapoints.add(data);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datapoints;
	}

}
