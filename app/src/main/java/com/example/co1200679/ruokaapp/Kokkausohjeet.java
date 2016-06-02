package com.example.co1200679.ruokaapp;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Kokkausohjeet extends AppCompatActivity {

    Tietokanta TK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kokkausohjeet);
        TK = new Tietokanta(this);
    }

    public void fillScrollView(String lause) {

        LinearLayout content = (LinearLayout) findViewById(R.id.content);
        View temp;
        ItemInfo item;
        ImageView icon;
        RoundImage roi;

        if(content.getChildCount() > 0)
            content.removeAllViews();

        Cursor tiedot = TK.HaeTiedot(lause);

        while(tiedot.moveToNext()) {
            temp = getLayoutInflater().inflate(R.layout.item, content, false);
            item = (ItemInfo) temp.findViewById(R.id.itemView);
            item.setNimi(tiedot.getString(0));
            item.setID(tiedot.getInt(1));
            icon = (ImageView) temp.findViewById(R.id.imageView);
            Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.tomaatti);
            roi = new RoundImage(bm);
            icon.setImageDrawable(roi);
            content.addView(temp);
        }
    }
}
