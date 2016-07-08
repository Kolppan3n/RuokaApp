package com.example.co1200679.ruokaapp;

import android.view.View;

import java.security.PublicKey;

/**
 * Created by co1300608 on 8.7.2016.
 */
class LauseMoodi {
    LauseMoodi edellinen;
    String Lause;
    int Moodi;

    public LauseMoodi(LauseMoodi LM,String L, int M) {
        edellinen = LM;
        Lause = L;
        Moodi = M;
    }

    public LauseMoodi getEdellinen() {
        return edellinen;
    }

    public String getLause(){
        return Lause;
    }
    public int getMoodi(){
        return Moodi;
    }

}

public class LauseLista {

    LauseMoodi Viimeinen;

    public LauseLista()
    {
        Viimeinen = null;
    }
    public void UusiLause(String L, int M)
    {
        LauseMoodi Temp = new LauseMoodi(Viimeinen,L,M);
        Viimeinen = Temp;
    }
    public LauseMoodi Takaisin()
    {
        Viimeinen = Viimeinen.getEdellinen();
        return Viimeinen;
    }
}
