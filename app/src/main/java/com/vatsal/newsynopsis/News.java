package com.vatsal.newsynopsis;

import android.graphics.Color;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class News extends AppCompatActivity {

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        final ScrollView[] scrollView = {new ScrollView(getApplicationContext())};
        scrollView[0].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
//        final RelativeLayout[] relativeLayout = new RelativeLayout[1];

        queue = Volley.newRequestQueue(this);
        String url = "https://newsynopsis-api.herokuapp.com/v1/news";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("NEWS", response.toString());
                scrollView[0] = findViewById(R.id.relative_layout_news);
                for (int i = 0; i < response.length(); i++) {
                    CardView cView = new CardView(getApplicationContext());
                    LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

                    cView.setLayoutParams(cardParams);
                    cView.setRadius(9);
                    cView.setContentPadding(15, 240, 15, 15);
                    cView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));

                    LinearLayout views = new LinearLayout(getApplicationContext());
                    views.setOrientation(LinearLayout.VERTICAL);
                    // Image ImageView
                    ImageView iView = new ImageView(getApplicationContext());
                    try {
                        Picasso.get().load(response.getJSONObject(i).getString("image")).into(iView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    views.addView(iView);


                    // Headline TextView

                    TextView headline = new TextView(getApplicationContext());
//                    headline.setLayoutParams(params);
                    try {
                        headline.setText(response.getJSONObject(i).getString("headline"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    headline.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                    views.addView(headline);

                    //Summary Text View;
                    TextView summary = new TextView(getApplicationContext());
//                    summary.setLayoutParams(params);
                    try {
                        summary.setText(response.getJSONObject(i).getString("summary"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    summary.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                    views.addView(summary);

                    cView.addView(views);
                    scrollView[0].addView(cView);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("NEWS", error.toString());
                error.printStackTrace();
            }
        });
        queue.add(request);
    }

}
