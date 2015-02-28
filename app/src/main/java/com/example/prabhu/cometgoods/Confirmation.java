package com.example.prabhu.cometgoods;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Prabhu on 2/27/2015.
 */
public class Confirmation  extends Activity {

    ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation);

        TextView prodtype = (TextView)findViewById(R.id.ptype);
        TextView prodprice = (TextView)findViewById(R.id.pprice);
        TextView proddesc = (TextView)findViewById(R.id.pdesc);
        TextView prodname = (TextView)findViewById(R.id.pname);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String pname =(String) b.get("prodname");
            int pprice =(int) b.get("prodprice");
            String pdesc =(String) b.get("proddesc");
            String ptype =(String) b.get("prodtype");
            String imgsource =(String) b.get("imgsource");
           prodtype.setText(ptype);
           prodname.setText(pname);
           proddesc.setText(pdesc);
           prodprice.setText(pprice);
        }

    }

}
