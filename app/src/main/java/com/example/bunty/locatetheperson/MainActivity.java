package com.example.bunty.locatetheperson;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.bunty.locatetheperson.adapters.AdapterBusInfo;
import com.example.bunty.locatetheperson.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private AdapterBusInfo adapterBusInfo;
    String profile="student";
    String name;
    private RecyclerView personinfo;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ArrayList<Person> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        personinfo= (RecyclerView) findViewById(R.id.personinfo);
        personinfo.setLayoutManager(new LinearLayoutManager(this));
        adapterBusInfo=new AdapterBusInfo(this);
        personinfo.setAdapter(adapterBusInfo);
        loaddata(profile);
    }

    private void loaddata(String profile) {

        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        String get_source_destination = "http://whereisthebus.in/locatetheperson.php?profile="+profile;
        JsonObjectRequest b = new JsonObjectRequest(Request.Method.GET, get_source_destination, "null", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                JSONArray items = null;
                try {
                          Toast.makeText(getApplicationContext(), response+"", Toast.LENGTH_SHORT).show();
                    Log.i("data",response+"");
                    items = response.getJSONArray("row");

                    for(int i=0;i<items.length();i++) {

                        name= items.getJSONObject(i).getString("username");
                        double lat=items.getJSONObject(i).getDouble("latitude");
                        double long1=items.getJSONObject(i).getDouble("longitude");
//                        Person p=new Person();
//                        p.setName(name);
//                        list.add(p);
//

                        getdistance(lat,long1,name);





                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(b);
    }

    private void getdistance(double lat, double long1, final String name) {

        double userlat=19.064202;
        double user_long=72.835483;

        String url1 = "http://maps.googleapis.com/maps/api/distancematrix/json?" +
                "origins=" + userlat + "," + user_long + "&destinations=" + lat+ "," + long1 + "&mode=bus";






        Log.i("url", url1);

        StringRequest request1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.equals("null")) {
                        //   Toast.makeText(getActivity(), "No busses found", Toast.LENGTH_SHORT).show();
                        //progressDialog.dismiss();
                    } else {
                        //parse the google distance api
                        //                Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        Log.i("the response from google api is",response+"");
                        //              Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_SHORT).show();
                        JSONObject ob = new JSONObject(response);

                        JSONArray originadd=ob.getJSONArray("origin_addresses");
                        String x1=originadd.getString(0);
                        JSONArray rows = ob.getJSONArray("rows");
                        for (int k = 0; k < rows.length(); k++) {

                            JSONObject ob1 = rows.getJSONObject(k);
                            JSONArray elements = ob1.getJSONArray("elements");
                            for (int k1 = 0; k1 < elements.length(); k1++) {
                                JSONObject dis = elements.getJSONObject(k1);
                                JSONObject distance1 = dis.getJSONObject("distance");
                               String textkms = distance1.getString("text");
                                JSONObject duration = dis.getJSONObject("duration");
                               String texttime = duration.getString("text");
                                Person p=new Person();
                               // b.setLocation(x1);
                                p.setDistance(textkms);
                                p.setTime(texttime);
                                p.setName(name);
                                list.add(p);
                                adapterBusInfo.setData(list);
                                //progressDialog.cancel();

                                //distancetime[z] = textkms;
                                //distancetime[z + 1] = texttime;
                                //                    Toast.makeText(getActivity(), textkms + "" + texttime, Toast.LENGTH_SHORT).show();
//                                    Log.i("distancetime", distancetime[z] + "" + distancetime[z + 1]);


                            }
                            //z++;


                        }

                    }

                } /*catch (JSONException e) {

                }*/ catch (Exception e) {

                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//
            }
        });

        requestQueue.add(request1);

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
}
