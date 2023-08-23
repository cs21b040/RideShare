package com.example.rideshare;

import static android.content.ContentValues.TAG;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

public class CustomerHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    Button button;
    private final double RADIUS_OF_EARTH=6378.1e3;
    Place src,dst;
    private FirebaseAuth auth;
    private FirebaseFirestore fstore;
    Vector<User> allpaths;
    private LinearLayout linearLayout;
    private ArrayList<String> arrayList,uid, userUid;
    String custSrc="";
    String custDst="";
    String strAdd = "";
    String strDst="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home_page);
        drawerLayout = findViewById(R.id.drawer_layout1);
        navigationView = findViewById(R.id.nav_view1);
        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        linearLayout=findViewById(R.id.linear_layout);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home1);
        allpaths=new Vector<User>();
        uid=new ArrayList<>();
        userUid=new ArrayList<>();
        arrayList=new ArrayList<>();
        retrieveAllPathsFromDatabase();
        init();
    }
    void init(){
        String apiKey = getString(R.string.my_api_key);
        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(),apiKey);
        }
        PlacesClient placesClient= Places.createClient(this);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setCountries("IN");
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@androidx.annotation.NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }

            @Override
            public void onPlaceSelected(@androidx.annotation.NonNull Place place) {
                src=place;
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }
        });
        AutocompleteSupportFragment autocompleteFragment2 = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment2);
        autocompleteFragment2.setCountries("IN");
        // Specify the types of place data to return.
        autocompleteFragment2.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));
        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@androidx.annotation.NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }

            @Override
            public void onPlaceSelected(@androidx.annotation.NonNull Place place) {
                Log.i("PLACETEMP", "Place: " + place.getName() + ", " + place.getLatLng().latitude +" "+ place.getLatLng().longitude);
                dst=place;
            }
        });
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    check();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    public double haversine(LatLng p1, LatLng p2) {
        double lat1 = p1.latitude;
        double lon1 = p1.longitude;
        double lat2 = p2.latitude;
        double lon2 = p2.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RADIUS_OF_EARTH * c;
    }
    void check() throws IOException {
        Log.i("CHECK SIZe", "check: v size is" + allpaths.size());
        arrayList.clear();
        if(allpaths.size()>0){
            double total_dist=haversine(src.getLatLng(),dst.getLatLng());
            for(int i=0;i<allpaths.size();i++){
                double dist1=1e9;
                double dist2=1e9;
                for(int j=0;j<allpaths.get(i).route.size();j++){
                    double cur_dist1=haversine(src.getLatLng(),allpaths.get(i).route.get(j));
                    double cur_dist2=haversine(dst.getLatLng(),allpaths.get(i).route.get(j));
                    dist1=Math.min(dist1,cur_dist1);
                    dist2=Math.min(dist2,cur_dist2);
                }
                Log.i("TOTALDIST", "check: "+dist1+" "+dist2+" "+total_dist);
                if(dist1+dist2<=total_dist && dist1<=5000 && dist2<=5000){
                    //show the driver in the list
                    //remove this polyLine after completion
                    Geocoder geocoder=new Geocoder(this, Locale.getDefault());
                    LatLng l=allpaths.get(i).route.get(0);
                    LatLng l2=allpaths.get(i).route.get(allpaths.get(i).route.size()-1);


                    List<Address> addresses = geocoder.getFromLocation(l.latitude, l.longitude, 1);
                    List<Address> addresses1=geocoder.getFromLocation(l2.latitude,l2.longitude,1);
                    List<Address> addresses2=geocoder.getFromLocation(src.getLatLng().latitude,src.getLatLng().longitude,1);
                    List<Address> addresses3=geocoder.getFromLocation(dst.getLatLng().latitude,dst.getLatLng().longitude,1);
                    if (addresses != null) {
                        Address returnedAddress = addresses.get(0);
                        StringBuilder strReturnedAddress = new StringBuilder("");

                        for (int k = 0; k <= returnedAddress.getMaxAddressLineIndex(); k++) {
                            strReturnedAddress.append(returnedAddress.getAddressLine(k)).append("\n");
                        }
                        strAdd = strReturnedAddress.toString();
                    }
                    if(addresses1!=null){
                        Address returnedAddress = addresses1.get(0);
                        StringBuilder strReturnedAddress = new StringBuilder("");
                        for(int k=0;k<=returnedAddress.getMaxAddressLineIndex();k++){
                            strReturnedAddress.append(returnedAddress.getAddressLine(k)).append("\n");
                        }
                        strDst=strReturnedAddress.toString();
                    }
                    if(addresses2!=null){
                        Address returnedAddress = addresses2.get(0);
                        StringBuilder strReturnedAddress = new StringBuilder("");
                        for(int k=0;k<=returnedAddress.getMaxAddressLineIndex();k++){
                            strReturnedAddress.append(returnedAddress.getAddressLine(k)).append("\n");
                        }
                        custSrc=strReturnedAddress.toString();
                    }
                    if(addresses3!=null){
                        Address returnedAddress = addresses3.get(0);
                        StringBuilder strReturnedAddress = new StringBuilder("");
                        for(int k=0;k<=returnedAddress.getMaxAddressLineIndex();k++){
                            strReturnedAddress.append(returnedAddress.getAddressLine(k)).append("\n");
                        }
                        custDst=strReturnedAddress.toString();
                    }
                    Log.i("CHECKIFPOSS", "check: "+uid.get(i)+" "+dist1+" "+dist2+" "+total_dist+" ");
                    arrayList.add("Email " + uid.get(i) +"\n" +"Start :" + strAdd +"\n"+ "Dest :" + strDst +"\n" );
                    userUid.add(allpaths.get(i).uid);
                    Toast.makeText(this, "We have a route", Toast.LENGTH_SHORT).show();
                }
            }
        }
        linearLayout.removeAllViews();
        Toast.makeText(this, "hi "+arrayList.size(), Toast.LENGTH_SHORT).show();
        for (int k = 0; k < arrayList.size(); k++) {
            TextView tv = new TextView(this);
            tv.setText(arrayList.get(k));
            tv.setTextSize(18);
            tv.setPadding(340, 60, 60, 50);
            tv.setId(k);
            tv.setTextColor(Color.BLACK);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(15, 0, 15, 0);
            tv.setLayoutParams(lp);
            linearLayout.addView(tv);
        }

        for(int k=0; k<arrayList.size(); k++){
            String userId= userUid.get(k);
            DocumentReference documentReference= fstore.collection("users").document(userId);
            TextView tv = findViewById(k);
            int finalK = k;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    documentReference.update("costumerMail", auth.getCurrentUser().getEmail());
                    documentReference.update("from2", custSrc);
                    documentReference.update("to2",custDst);
                    documentReference.update("from1", strAdd);
                    documentReference.update("to1", strDst);

                    DocumentReference dr= fstore.collection("users").document(auth.getCurrentUser().getUid());
                    dr.update("costumerMail", uid.get(finalK));
                    dr.update("from1", custSrc);
                    dr.update("to1",custDst);
                    dr.update("from2", strAdd);
                    dr.update("to2", strDst);
                    Toast.makeText(CustomerHomePage.this, "The Ride is Successfully Booked.. Details in 'Your Trip'", Toast.LENGTH_SHORT).show();

                    deleteFromDatabase(userId);
                }
            });
        }

    }

    void deleteFromDatabase(String uid){
        fstore.collection("paths").document(uid).delete();
    }

    void retrieveAllPathsFromDatabase() {
        Vector<User> allPaths = new Vector<>();
        fstore.collection("paths")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String,Object>m=document.getData();
                                for(Map.Entry<String,Object> entry : m.entrySet()){
                                    uid.add(entry.getKey());
                                    Map<String,Object> m2=(Map<String, Object>) entry.getValue();
                                    String s=(String)m2.get("uid");
                                    Toast.makeText(CustomerHomePage.this, s, Toast.LENGTH_SHORT).show();
                                    List<LatLng>temp=new ArrayList<>();
                                    List<Object> path= (List<Object>) m2.get("route");
                                    for(int i=0;i<path.size();i++){
                                        Map<String,Object> m3=(Map<String, Object>) path.get(i);
                                        double lat=(double)m3.get("latitude");
                                        double lng=(double)m3.get("longitude");
                                        temp.add(new LatLng(lat,lng));
                                        Log.i("Longlat", "onComplete: "+temp.size());
                                        Log.i("Longlat", "onComplete: "+lat+" "+lng);
                                    }
                                    User u=new User();
                                    u.route=temp;
                                    u.uid=s;
                                    allPaths.add(u);
                                }
                                Log.i("CHECKANDTELL", "onComplete: "+document.getId() + " => " + allPaths.size());
                            }
                            Log.i("CHECKANDTELL", "retrieveAllPathsFromDatabase: "+allPaths.size());
                            allpaths=allPaths;
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.nav_home1) {
            Intent intent=new Intent (CustomerHomePage.this, OptionsActivity.class);
            startActivity(intent);
            finish();
        }
        else if(item.getItemId()==R.id.nav_profile1) {
            Intent intent=new Intent (CustomerHomePage.this, ProfilePage.class);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.nav_logout1) {
            auth.signOut();
            Toast.makeText(this, "Log Out SuccessFull", Toast.LENGTH_SHORT).show();
            Intent intent1=new Intent(CustomerHomePage.this, Login.class);
            startActivity(intent1);
            finish();
        }
        else if(item.getItemId()==R.id.nav_trip1)
        {
            Intent intent=new Intent (CustomerHomePage.this, Travel_Details.class);
            startActivity(intent);
        }
        return true;
    }
}


