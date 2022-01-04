package com.example.thermometer2;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class GetThermometerValueAlt{
    String ipAdress;
    GetThermometerValueAlt(String ipAdress) {
        this.ipAdress = ipAdress;
    }

    public String readStringHtml() {
        String html;
        try {
            html = Jsoup.connect(this.ipAdress).get().html();
            Document document = Jsoup.parse(html);
            Element link = document.select("p").first();
            String strNumber = link.text().split(" ")[0];
            Log.i("AltThreadActivity", "" + Thread.currentThread());
            Log.i("AltThreadActivity", "Result is " + Integer.parseInt(strNumber));
            return strNumber;

        } catch (Exception e) {
            Log.e("AltThreadActivity", e.getMessage(), e);
            return null;
        }

    }
}
