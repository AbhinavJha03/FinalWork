package com.example.newwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vikktorn.picker.City;
import com.vikktorn.picker.CityPicker;
import com.vikktorn.picker.Country;
import com.vikktorn.picker.CountryPicker;
import com.vikktorn.picker.OnCityPickerListener;
import com.vikktorn.picker.OnCountryPickerListener;
import com.vikktorn.picker.OnStatePickerListener;
import com.vikktorn.picker.State;
import com.vikktorn.picker.StatePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements OnStatePickerListener, OnCountryPickerListener, OnCityPickerListener {
    public static int countryID, stateID;
    private Button pickStateButton, pickCountry, pickCity;
    private TextView stateNameTextView, countryName,countryCode, countryPhoneCode, countryCurrency, cityName;
    private ImageView flagImage;
    // Pickers
    private CountryPicker countryPicker;
    private StatePicker statePicker;
    private CityPicker cityPicker;
    // arrays of state object
    public static List<State> stateObject;
    // arrays of city object
    public static List<City> cityObject;
    TextView weatherInfoView;
    AutoCompleteTextView editText;
    JSONArray arr;
    Button weatherInfoButton;

    public void initView(){
        //Buttons
        pickStateButton = (Button) findViewById(R.id.pickState);
        //set state picker invisible
        pickStateButton.setVisibility(View.INVISIBLE);
        pickCountry = (Button) findViewById(R.id.pickCountry);
        pickCity = (Button) findViewById(R.id.pick_city);
        // set city picker invisible
        pickCity.setVisibility(View.INVISIBLE);
        // Text Views
        countryName = (TextView) findViewById(R.id.countryNameTextView);
        countryCode = (TextView) findViewById(R.id.countryCodeTextView);
        countryPhoneCode = (TextView) findViewById(R.id.countryDialCodeTextView);
        countryCurrency = (TextView) findViewById(R.id.countryCurrencyTextView);
        stateNameTextView = (TextView) findViewById(R.id.state_name);
        //set state name text view invisible
        stateNameTextView.setVisibility(View.INVISIBLE);
        cityName = (TextView) findViewById(R.id.city_name);
        //set state name text view invisible
        cityName.setVisibility(View.INVISIBLE);

        // ImageView
        flagImage = (ImageView) findViewById(R.id.flag_image);

        // initiate state object, parser, and arrays
        stateObject = new ArrayList<>();
        cityObject = new ArrayList<>();
    }

    // SET STATE LISTENER
    private void setListener() {
        pickStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statePicker.showDialog(getSupportFragmentManager());
            }
        });
    }
    //SET COUNTRY LISTENER
    private void setCountryListener() {
        pickCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryPicker.showDialog(getSupportFragmentManager());
            }
        });
    }
    //SET CITY LISTENER
    private void setCityListener() {
        pickCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityPicker.showDialog(getSupportFragmentManager());
            }
        });
    }


    // GET STATE FROM ASSETS JSON
    public void getStateJson() throws JSONException {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("states.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }


        JSONObject jsonObject = new JSONObject(json);
        JSONArray events = jsonObject.getJSONArray("states");
        for (int j = 0; j < events.length(); j++) {
            JSONObject cit = events.getJSONObject(j);
            State stateData = new State();

            stateData.setStateId(Integer.parseInt(cit.getString("id")));
            stateData.setStateName(cit.getString("name"));
            stateData.setCountryId(Integer.parseInt(cit.getString("country_id")));
            stateObject.add(stateData);
        }
    }


    // GET CITY FROM ASSETS JSON
    public void getCityJson() throws JSONException {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("cities.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }


        JSONObject jsonObject = new JSONObject(json);
        JSONArray events = jsonObject.getJSONArray("cities");
        for (int j = 0; j < events.length(); j++) {
            JSONObject cit = events.getJSONObject(j);
            City cityData = new City();

            cityData.setCityId(Integer.parseInt(cit.getString("id")));
            cityData.setCityName(cit.getString("name"));
            cityData.setStateId(Integer.parseInt(cit.getString("state_id")));
            cityObject.add(cityData);
        }}


    public String readJSONFromAsset(String s) {
        String json = null;
        try {
            InputStream is = getAssets().open(s);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherInfoButton = findViewById(R.id.weatherInfoButton);
        editText =  findViewById(R.id.editText);

        // initialize view
        initView();
        // get state from assets JSON
        try {
            getStateJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // get City from assets JSON
        try {
            getCityJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // initialize country picker
        countryPicker = new CountryPicker.Builder().with(this).listener(this).build();

        // initialize listeners
        setListener();
        setCountryListener();
        setCityListener();


        try {
            final JSONObject jsonObject = new JSONObject(readJSONFromAsset("important.json"));
            final Iterator<String> iter = jsonObject.keys(); //This should be the iterator you want.
            final ArrayList<String> cities = new ArrayList<>();
            while (iter.hasNext()) {
                String key = iter.next();
                //country.add(key);
                final JSONArray iter2 = (JSONArray) jsonObject.get(key);
                for (int i=0;i<iter2.length();i++)
                    cities.add((String) iter2.get(i));
            }


            editText.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);

                }

            });

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, cities);
            editText.setAdapter(arrayAdapter);
            editText.setThreshold(1);

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }



    @Override
    public void onSelectCity(City city) {
        cityName.setText(city.getCityName());
        editText.setText("");

    }

    @Override
    public void onSelectCountry(Country country) {
        // get country name and country ID
        countryName.setText(country.getName());
        countryID = country.getCountryId();
        statePicker.equalStateObject.clear();
        cityPicker.equalCityObject.clear();

        //set state name text view and state pick button invisible
        pickStateButton.setVisibility(View.VISIBLE);
        stateNameTextView.setVisibility(View.VISIBLE);
        stateNameTextView.setText("Region");
        cityName.setText("City");
        // set text on main view
        countryCode.setText("Country code: " + country.getCode());
        countryPhoneCode.setText("Country dial code: " + country.getDialCode());
        countryCurrency.setText("Country currency: " + country.getCurrency());
        flagImage.setBackgroundResource(country.getFlag());


        // GET STATES OF SELECTED COUNTRY
        for(int i = 0; i < stateObject.size(); i++) {
            // init state picker
            statePicker = new StatePicker.Builder().with(this).listener(this).build();
            State stateData = new State();
            if (stateObject.get(i).getCountryId() == countryID) {

                stateData.setStateId(stateObject.get(i).getStateId());
                stateData.setStateName(stateObject.get(i).getStateName());
                stateData.setCountryId(stateObject.get(i).getCountryId());
                stateData.setFlag(country.getFlag());
                statePicker.equalStateObject.add(stateData);
            }
        }
    }



    @Override
    public void onSelectState(State state) {
        pickCity.setVisibility(View.VISIBLE);
        cityName.setVisibility(View.VISIBLE);
        cityName.setText("City");
        cityPicker.equalCityObject.clear();

        stateNameTextView.setText(state.getStateName());
        stateID = state.getStateId();



        for(int i = 0; i < cityObject.size(); i++) {
            cityPicker = new CityPicker.Builder().with(this).listener(this).build();
            City cityData = new City();
            if (cityObject.get(i).getStateId() == stateID) {
                cityData.setCityId(cityObject.get(i).getCityId());
                cityData.setCityName(cityObject.get(i).getCityName());
                cityData.setStateId(cityObject.get(i).getStateId());

                cityPicker.equalCityObject.add(cityData);
            }
        }
    }

    public void weatherInfo(View view) {

        editText = findViewById(R.id.editText);
        String done;

        try {
            DownloadTask task = new DownloadTask();
            // newDownload mainTask= new newDownload();

            String city = "https://api.openweathermap.org/data/2.5/weather?q=";
            String city2 = "&appid=ef594983a89b26b8738af7e99c8e5308";

            if(editText.getText().toString().isEmpty()) {
                done = city + cityName.getText().toString() + city2;
            }
            else
            {
                done=city+editText.getText().toString()+city2;

            }

            task.execute(done);
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(editText.getApplicationWindowToken(), 0);
             editText.setText("");


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }


    public class DownloadTask extends AsyncTask<String,Void,String>
    {

        // FOR URL EXTRACTION

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection urlConnection= null;
            try {
                url= new URL(urls[0]);
                urlConnection= (HttpURLConnection) url.openConnection();
                InputStream in =urlConnection.getInputStream();
                InputStreamReader reader= new InputStreamReader(in);
                int data= reader.read();
                while(data!=-1)
                {
                    char current= (char) data;
                    result+=current;
                    data=reader.read();
                }
                return  result;

            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try {

                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                // Log.i("Weather content", weatherInfo);

                // FOR CONVERTING JSON OBJECT TO JSON ARRAY AND USING WEATHER JSON


                arr = new JSONArray(weatherInfo);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);
                    Log.i("main", jsonPart.getString("main"));
                    String wi = jsonPart.getString("main");
                    String wiv = (jsonPart.getString("description"));
                    Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                    intent.putExtra("Info", wi);
                    intent.putExtra("InfoView", wiv);

                    // Log.i("description", jsonPart.getString("description"));


                    // FOR MAIN JSON INCLUDING TEMP, FEELS LIKE TEMP, HUMIDITY AND PRESSURE

                    JSONObject jsonObject1 = new JSONObject(s);
                    String mainInfo = jsonObject1.getString("main");
                    JSONObject tempInfo= new JSONObject(mainInfo);
                    //  Log.i("temp info",String.valueOf(tempInfo.get("temp")));
                    // TEMP
                    double tempInK= Double.parseDouble(String.valueOf(tempInfo.get("temp")));
                    double tempInC=tempInK-273.15;
                    double temp=Math.round(tempInC);

                    // FEELS LIKE
                    double feelsTempInK= Double.parseDouble(String.valueOf(tempInfo.get("feels_like")));
                    double feelsTempInC=feelsTempInK-273.15;
                    double feelsTemp=Math.round(feelsTempInC);

                    intent.putExtra("temp",String.valueOf(temp));
                    intent.putExtra("feels_like",String.valueOf(feelsTemp));
                    intent.putExtra("humidity",String.valueOf(tempInfo.get("humidity")));
                    intent.putExtra("pressure",String.valueOf(tempInfo.get("pressure")));

                    // FOR WIND JSON


                    JSONObject jsonObject2 = new JSONObject(s);
                    String wind = jsonObject2.getString("wind");
                    JSONObject windInfo= new JSONObject(wind);
                    intent.putExtra("speed",String.valueOf(windInfo.get("speed")));
                    intent.putExtra("degree",String.valueOf(windInfo.get("deg")));


                    // FOR SUNRISE AND SUNSET

                    JSONObject jsonObject3 = new JSONObject(s);
                    String sun= jsonObject3.getString("sys");
                    JSONObject sunInfo= new JSONObject(sun);

                    long sr= Long.parseLong(String.valueOf(sunInfo.get("sunrise")));
                    long ss= Long.parseLong(String.valueOf(sunInfo.get("sunset")));
                    Date date = new Date(sr*1000L);
                    Date datest= new Date(ss*1000L);
                    SimpleDateFormat jdf = new SimpleDateFormat("HH:mm:ss z");
                    SimpleDateFormat jdfst = new SimpleDateFormat("HH:mm:ss z");
                    // jdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
                    //jdfst.setTimeZone(TimeZone.getTimeZone("GMT-4"));
                    String java_date = jdf.format(date);
                    int subs= java_date.indexOf("GMT");
                    String sunTime=java_date.substring(0,subs);

                    Log.i("Location of", String.valueOf(subs));

                    String java_datest=jdfst.format(datest);
                    String sunsetTime=java_datest.substring(0,subs);

                    intent.putExtra("sunrise",sunTime);
                    intent.putExtra("sunset",sunsetTime);


                    // FOR NAME OF CITY

                    JSONObject jsonObject4= new JSONObject(s);
                    String nameOfCity=jsonObject4.getString("name");
                    intent.putExtra("name",nameOfCity);

                    // FOR CLOUDINESS %

                    JSONObject jsonObject5= new JSONObject(s);
                    String clouds = jsonObject5.getString("clouds");
                    JSONObject cloudInfo= new JSONObject(clouds);
                    intent.putExtra("all",String.valueOf(cloudInfo.get("all")));

                    // FOR COORDINATION

                    JSONObject jsonObject6= new JSONObject(s);
                    String coord = jsonObject6.getString("coord");
                    JSONObject coorInfo= new JSONObject(coord);
                    intent.putExtra("lat",String.valueOf(coorInfo.get("lat")));
                    intent.putExtra("lon",String.valueOf(coorInfo.get("lon")));






                    startActivity(intent);


                }
            }
            catch (Exception e) {
                Toast.makeText(MainActivity.this, "Enter a valid city name", Toast.LENGTH_SHORT).show();
                e.printStackTrace();



            }

        }

    }







}

