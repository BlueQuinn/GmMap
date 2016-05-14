package AsyncTask;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import DTO.Menu;
import Listener.OnLoadListener;
import Utils.UnicodeConverter;
import vmwares.in.lequan.gmmap.R;

/**
 * Created by Quan-DevTeam on 10/8/15.
 */
public class MenuAst extends AsyncTask<String, Integer, ArrayList<Menu>> {

    ProgressBar prbLoading;
    public MenuAst(View view) {
        prbLoading = (ProgressBar) view.findViewById(R.id.prbLoading);
    }

    @Override
    protected ArrayList<Menu> doInBackground(String... params)
    {
        ArrayList<Menu> listArticle = new ArrayList<>();
        try {

            HtmlCleaner htmlCleaner = new HtmlCleaner();
            CleanerProperties cleanerProperties = htmlCleaner.getProperties();
            cleanerProperties.setAllowHtmlInsideAttributes(true);
            cleanerProperties.setAllowMultiWordAttributes(true);
            cleanerProperties.setRecognizeUnicodeChars(true);
            cleanerProperties.setOmitComments(true);

            URL link = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) link.openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36");

            TagNode root = htmlCleaner.clean(urlConnection.getInputStream());

            Object[] node_title = root.evaluateXPath("//img[@class='materialboxed']/@title");
            Object[] node_url = root.evaluateXPath("//span[@class='txt-blue font16 bold']/text()");
            Object[] nodeImg = root.evaluateXPath("//img[@class='materialboxed']/@src");

            for (int i = 0; i < nodeImg.length; ++i)
            {
                String title = UnicodeConverter.convert((String) node_title[i]);
                String url = node_url[i].toString();
                String img = (String) nodeImg[i];
                img = img.replace("/content/images/no-image.png", "");

                listArticle.add(new Menu(title, url, img));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPatherException e) {
            e.printStackTrace();
        }
        return listArticle;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        prbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(ArrayList<Menu> album) {
        super.onPostExecute(album);
        listener.onLoaded(album);
        prbLoading.setVisibility(View.GONE);
    }

    OnLoadListener listener;

    public void setOnLoaded(OnLoadListener onLoaded)
    {
        this.listener = onLoaded;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}
