package ch.bbcag.badiapp.helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import ch.bbcag.badiapp.model.Badi;
import ch.bbcag.badiapp.model.Becken;

public class WieWarmJsonParser {

    public static Badi createBadiFromJsonString(String badiJsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(badiJsonString);
        return createBadiFromJsonObject(jsonObject);
    }

    public static List<Badi> createBadisFromJsonString(String badiJsonString) throws JSONException {
        JSONArray jsonArray = new JSONArray(badiJsonString);
        List<Badi> badis = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            badis.add(createBadiFromJsonObject(jsonArray.getJSONObject(i)));
        }
        Collections.sort(badis, new Comparator<Badi>() {
            @Override
            public int compare(Badi o1, Badi o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
        return badis;
    }

    private static Badi createBadiFromJsonObject(JSONObject jsonObject) throws JSONException {
        Badi badi = new Badi();
        badi.setId(Integer.parseInt(jsonObject.getString("badid")));
        badi.setName(jsonObject.getString("badname"));
        badi.setKanton(jsonObject.getString("kanton"));
        badi.setOrt(jsonObject.getString("ort"));
        JSONObject beckenJson = jsonObject.getJSONObject("becken");
        Iterator keys = beckenJson.keys();

        while (keys.hasNext())
        {
            Becken becken = new Becken();
            String key = (String) keys.next();
            JSONObject subObj = beckenJson.getJSONObject(key);
            becken.setName(subObj.getString("beckenname"));
            String temperature = subObj.getString("temp");
            if (!temperature.equals("null")) {
                becken.setTemperature(Double.parseDouble(temperature));
            }

            badi.addBecken(becken);
        }

        return badi;
    }
}