package in.studoob.nikhil.sign_up;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public static final String URL_URL ="https://studoob.000webhostapp.com/asd/json.php";
    public String key_email;
    public ImageView img;
    public TextView name;
    public TextView email;

    public static final String SHARED_PREF_NAME="nik";
    public static final String EMAIL_SHARED_PREF="email";
    public static final String LOGGEDIN_SHARED_PREF="loggedin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String email_intent=getIntent().getStringExtra("email");
        key_email=email_intent;
        Log.v("intent",key_email);
         img= (ImageView)findViewById(R.id.main_img);
        name= (TextView) findViewById(R.id.main_name);
        email= (TextView) findViewById(R.id.main_email);
        Button logout=(Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(LOGGEDIN_SHARED_PREF, false);
                editor.putString(EMAIL_SHARED_PREF, key_email);
                editor.commit();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        loaddata(URL_URL);
    }

    public void loaddata(String url){

        String urlSuffix = "?email=" + key_email;
        url+=urlSuffix;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            response="{hero:"+response+"}";
                            JSONObject jsonObject= new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("hero");

                            for (int i=0;i<array.length();i++)
                            {
                                JSONObject o =array.getJSONObject(i);
                                Glide.with(getApplication()).load(o.getString("imageurl"))
                                        .into(img);
                                name.setText(o.getString("username"));
                                email.setText(o.getString("email"));

                            }

                            Log.v("Array",array+"");

                        } catch (JSONException e) {
                            Toast.makeText(getApplication(),"error",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                            Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            message = "Authentication Error!!";
                            Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                            Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                        } else if (error instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                            Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                        }
                    }
                });


        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
