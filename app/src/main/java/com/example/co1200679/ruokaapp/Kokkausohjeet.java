package com.example.co1200679.ruokaapp;

import android.app.ActionBar;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Kokkausohjeet extends AppCompatActivity {

    Tietokanta TK;
    int ruokaID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kokkausohjeet);
        TK = new Tietokanta(this);
        ruokaID = getIntent().getIntExtra("ruokaID",0);
        fillScrollView();
    }

    public void fillScrollView() {

        String lause = ("SELECT * FROM AineKanta AK, ReseptiKanta RK WHERE AK.aineID IS RK.aineID AND RK.RuokaID IS " + ruokaID);

        LinearLayout content = (LinearLayout) findViewById(R.id.ainesKontti);
        View temp;
        ItemInfo item;
        ImageView icon;
        RoundImage roi;

        if(content.getChildCount() > 0)
            content.removeAllViews();

        Cursor tiedot = TK.HaeTiedot(lause);

        while(tiedot.moveToNext()) {
            Log.d("dumpaa",tiedot.getString(0));
            temp = getLayoutInflater().inflate(R.layout.item, content, false);
            item = (ItemInfo) temp.findViewById(R.id.itemView);
            item.setNimi(tiedot.getString(0));
            icon = (ImageView) temp.findViewById(R.id.imageView);
            Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.tomaatti);
            roi = new RoundImage(bm);
            icon.setImageDrawable(roi);
            content.addView(temp);
        }
    }

    public void toggleAinekset(View view){
        LinearLayout lio = (LinearLayout) findViewById(R.id.ainesKontti);
        ViewGroup.LayoutParams lap = lio.getLayoutParams();

        if(lap.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            lap.height = 0; //new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
            lio.setLayoutParams(lap);
        } else {
            lap.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            lio.setLayoutParams(lap);
        }
    }
}
