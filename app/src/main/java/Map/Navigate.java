package Map;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by lequan on 4/22/2016.
 */
public class Navigate
{
    public static JSONObject getJSON(LatLng start, LatLng end, String mode)
    {
        String urlString = "http://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + start.latitude + "," + start.longitude
                + "&destination=" + end.latitude + "," + end.longitude
                + "&sensor=false&units=metric&mode=" + mode;

        try
        {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            InputStream stream = urlConnection.getInputStream();

            return JsonParser(stream);
        }
        catch (Exception e)
        {
            String v = e.getMessage();
            Log.e("123", e.getMessage());

        }

        return null;
    }

    static JSONObject JsonParser(InputStream stream) throws IOException, JSONException
    {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = streamReader.readLine()) != null)
            builder.append(line);
        return new JSONObject(builder.toString());
    }

    public static ArrayList<LatLng> getDirection(JSONObject object)
    {
        ArrayList<LatLng> directionPoints = new ArrayList<>();
        try
        {
            JSONObject direction = object.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0);

            // start
            JSONObject start = direction.getJSONObject("start_location");
            double lat = start.getDouble("lat");
            double lng = start.getDouble("lng");
            directionPoints.add(new LatLng(lat, lng));

            // steps
            JSONArray steps = direction.getJSONArray("steps");
            for (int i = 0; i < steps.length(); ++i)
            {
                String polyline = steps.getJSONObject(i).getJSONObject("polyline").getString("points");
                ArrayList<LatLng> points = decodePoly(polyline);
                directionPoints.addAll(points);
            }

            // end
            JSONObject end = direction.getJSONObject("end_location");
            lat = end.getDouble("lat");
            lng = end.getDouble("lng");
            directionPoints.add(new LatLng(lat, lng));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return directionPoints;
    }

    public static ArrayList<LatLng> getDirection(Document doc)
    {
        NodeList nl1, nl2, nl3;
        ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();
        nl1 = doc.getElementsByTagName("step");
        if (nl1.getLength() > 0)
        {
            for (int i = 0; i < nl1.getLength(); i++)
            {
                Node node1 = nl1.item(i);
                nl2 = node1.getChildNodes();

                // start
                Node locationNode = nl2.item(getNodeIndex(nl2, "start_location"));
                nl3 = locationNode.getChildNodes();
                Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
                double lat = Double.parseDouble(latNode.getTextContent());
                Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                double lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));

                // way
                locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "points"));
                ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());
                for (int j = 0; j < arr.size(); j++)
                {
                    listGeopoints.add(new LatLng(arr.get(j).latitude, arr.get(j).longitude));
                }


                // end
                locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "lat"));
                lat = Double.parseDouble(latNode.getTextContent());
                lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));
            }
        }

        return listGeopoints;
    }

    private static int getNodeIndex(NodeList nl, String nodename)
    {
        for (int i = 0; i < nl.getLength(); i++)
        {
            if (nl.item(i).getNodeName().equals(nodename))
            {
                return i;
            }
        }
        return -1;
    }

    static ArrayList<LatLng> decodePoly(String encoded)
    {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len)
        {
            int b, shift = 0, result = 0;
            do
            {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }
            while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do
            {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }
            while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }
}
