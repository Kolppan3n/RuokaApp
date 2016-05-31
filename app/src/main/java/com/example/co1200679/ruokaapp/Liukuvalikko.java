package com.example.co1200679.ruokaapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class Liukuvalikko extends AppCompatActivity {

    Tietokanta TK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TK = new Tietokanta(this);
        String lause = getIntent().getStringExtra("sqlqry");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liukuvalikko);
        fillScrollView(lause);
    }

    public void fillScrollView(String lause) {

        LinearLayout content = (LinearLayout) findViewById(R.id.content);
        View temp;
        itemInfo item;

        if(content.getChildCount() > 0)
            content.removeAllViews();

        Cursor tiedot = TK.HaeTiedot(lause); //WHERE aine IS ' + Aineen nimi tähän +'

        while(tiedot.moveToNext()) {
            temp = getLayoutInflater().inflate(R.layout.item, content, false);
            item = (itemInfo) temp.findViewById(R.id.itemView);
            item.setNimi(tiedot.getString(0));
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    whatthesht((itemInfo) v);
                }
            });
            content.addView(temp);
        }
    }

    public void whatthesht(itemInfo v){

        Intent intent = new Intent(this, Liukuvalikko.class);
        String lause = ("SELECT * FROM KaappiKanta WHERE aine IS '" + v.getNimi()+"'");
        intent.putExtra("sqlqry", lause);
        startActivity(intent);
    }
}
