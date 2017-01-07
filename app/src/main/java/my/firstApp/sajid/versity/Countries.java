package my.firstApp.sajid.versity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Countries extends Fragment {


    private String url="http://akrhcb.esy.es/InitialConnection.php";
    private String jsonResult;
    ListView listView;
    private SearchView searchView;
    public final static String uniByCoun="getTheCode";
    SimpleAdapter simpleAdapter;
    private ProgressDialog loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.countries, container, false);

    }
    @Override
    public void onStart() {
        super.onStart();
        processWork();

    }

    public void processWork() {

        accessWebService();
        loading = ProgressDialog.show(getActivity(), "Loading Countries...", null, true, true);
        listView = (ListView) getView().findViewById(R.id.listView);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);

        listView.setStackFromBottom(false);
        searchView=(SearchView) getView().findViewById(R.id.searchView4);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                String code = ((TextView)view.findViewById(R.id.Name)).getText().toString();

                Intent intent = new Intent(getActivity(), UniByCoun.class);
                intent.putExtra(uniByCoun, code);
                startActivity(intent);


            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                //   Data.setFilterText(text);
                simpleAdapter.getFilter().filter(text.replace(" ", ""));
                return true;
            }
        });


    }


        private class JsonReadTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL ulrn = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) ulrn.openConnection();
                    InputStream response = con.getInputStream();
                    jsonResult = inputStreamToString(response).toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            private StringBuilder inputStreamToString(InputStream is) {
                String rLine = "";
                StringBuilder answer = new StringBuilder();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                try {
                    while ((rLine = rd.readLine()) != null) {
                        answer.append(rLine);
                    }
                } catch (IOException e) {
// e.printStackTrace();
                    Toast.makeText(getActivity(), "Error..." + e.toString(),
                            Toast.LENGTH_LONG).show();
                }
                return answer;
            }

            @Override
            protected void onPostExecute(String result) {
                ListDrawer();
                loading.dismiss();
            }
        }// end async task


    public void accessWebService() {
        JsonReadTask task = new JsonReadTask();
// passes values for the urls string array
        task.execute(new String[]{url});
    }

    // build hash set for list view
    public void ListDrawer() {
        List<Map<String, String>> List = new ArrayList<Map<String, String>>();
        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("countries");
            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String number = jsonChildNode.optString("iso3");
                String name = jsonChildNode.optString("name");
                String id=jsonChildNode.optString("id");
                String outPut = name.replaceAll(" ","-")  + "\n" + number;
                List.add(create("countries", outPut));
            }
        } catch (JSONException e) {
            Toast.makeText(getActivity(), "Error" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }


        simpleAdapter = new SimpleAdapter(getActivity(),List,
                R.layout.countrylist,
                new String[]{"countries"}, new int[]{R.id.Name});
        listView.setAdapter(simpleAdapter);
    }

    private HashMap<String, String> create(String name, String number) {
        HashMap<String, String> studentNameNo = new HashMap<String, String>();
        studentNameNo.put(name, number);
        return studentNameNo;


    }

}
