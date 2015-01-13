package clearfaun.com.xmlparsertest;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends ActionBarActivity {

    PlaceholderFragment taskFragment;
    static Activity thisActivity = null;
    static EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity = this;
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            taskFragment = new PlaceholderFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(taskFragment, "MyFragment")
                    .commit();
        }else{
            taskFragment = (PlaceholderFragment) getSupportFragmentManager().findFragmentByTag("MyFragment");
        }

        taskFragment.startTask();
        editText = (EditText) findViewById(R.id.editText);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        TechCrunchTask downloadTask;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            setRetainInstance(true);

            return rootView;
        }

        public void startTask(){

            if(downloadTask != null){
                downloadTask.cancel(true);
            }else{
                downloadTask = new TechCrunchTask();
                downloadTask.execute();
            }
        }

    }

    public static class TechCrunchTask extends AsyncTask<Void, Void, Void>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Void doInBackground(Void... params) {
            //do your work here


            String downloadURL = "http://bustime.mta.info/api/where/stops-for-location.xml?key=05a5c2c8-432a-47bd-8f50-ece9382b4b28&lat=40.69059093&lon=-73.99620472";
            try {
                URL url = new URL(downloadURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();
                processXML(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }



            return null;
        }

        Element rootElement;

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //editText.setText(rootElement.getTagName());
            editText.setText(currentItem.getNodeName() + " " + currentChild.getTextContent());
            //ToastMe(rootElement.toString());
            // do something with data here-display it or send to mainactivity
        }

        Node currentItem;
        NodeList itemChildren;
        Node currentChild;

        public void processXML(InputStream inputStream) throws Exception{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document xmlDocument = documentBuilder.parse(inputStream);
            rootElement = xmlDocument.getDocumentElement();

            NodeList itemsList = rootElement.getElementsByTagName("route");
            currentItem = null;
            itemChildren = null;
            currentChild = null;
            for(int i = 0; i < itemsList.getLength(); i++){

                currentItem = itemsList.item(i);
                itemChildren = currentItem.getChildNodes();

                for(int j = 0; j < itemChildren.getLength(); j++){

                    currentChild = itemChildren.item(j);
                    if(currentChild.getNodeName().equalsIgnoreCase("title")){
                        //prints specified tag
                    }

                }
            }
            currentItem = itemsList.item(0);
            currentChild = itemChildren.item(1);

        }



    }

    public static void ToastMe(String string){

        Toast.makeText(thisActivity, string, Toast.LENGTH_SHORT).show();


    }



}
