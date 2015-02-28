package com.example.prabhu.cometgoods;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends Activity {
    int RESULT_LOAD_IMAGE;String imgsource;
    DBAdapter myDb;int prodprice;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(getScaledBitmap(picturePath, 400, 400));imageView.setScaleType(ImageView.ScaleType.MATRIX);
            imgsource=picturePath;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openDB();
        Button button = (Button) findViewById(R.id.add);
        ImageButton imgadd = (ImageButton) findViewById(R.id.imageButton);
        imgadd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
        });
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                EditText pname= (EditText)findViewById(R.id.pname);
                EditText pprice= (EditText)findViewById(R.id.pprice);
                EditText ptype= (EditText)findViewById(R.id.ptype);
                EditText pdesc= (EditText)findViewById(R.id.pdesc);
                CheckBox pbargain=(CheckBox)findViewById(R.id.pbargain);
                String imgsrc;
                String prodname=pname.getText().toString();
                String prodtype=ptype.getText().toString();
                String proddesc=pdesc.getText().toString();
                if(!emptyvalidate(pprice.getText().toString())){

                    prodprice=Integer.parseInt(pprice.getText().toString());

                }

                Boolean status=pbargain.isSelected();
                int pbargainstatus = (status)? 1 : 0;





                if(emptyvalidate(prodname.toString())||emptyvalidate(pprice.toString())||emptyvalidate(prodtype.toString())||emptyvalidate(proddesc.toString()))
                {
                    Toast.makeText(getApplicationContext(), "Enter All the Required Details",
                            Toast.LENGTH_LONG).show();
                }
            else
                {
                    String prodid=randInt(500,10000)+"";
                    long newId = myDb.insertRow(prodid,prodname,prodprice,proddesc,prodtype,imgsource,pbargainstatus);
                    Cursor cursor = myDb.getRow(newId);
                    //displayRecordSet(cursor);
                    Intent i = new Intent(MainActivity.this, Confirmation.class);

                    i.putExtra("prodname", prodname);
                    i.putExtra("prodprice", prodprice);
                    i.putExtra("proddesc", proddesc);
                    i.putExtra("prodtype", prodtype);
                    i.putExtra("imgsource", imgsource);
                    i.putExtra("pbargainstatus", pbargainstatus);
                    startActivity(i);
                    finish();

                }


            }
        });
    }
    private Bitmap getScaledBitmap(String picturePath, int width, int height) {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, sizeOptions);

        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(picturePath, sizeOptions);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

		        /*
		         *  Calculate ratios of height and width to requested height and
		         *  width
		         */

            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

		       /*
		        *  Choose the smallest ratio as inSampleSize value, this will
		        *  guarantee  a final image with both dimensions larger than or equal to the requested height and width.

		        */

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
    private boolean emptyvalidate(String s) {

        if(s.equals(""))
        {
            return true;

        }
        else
        {
            return false ;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }
    private void openDB() {

        myDb = new DBAdapter(this);
        myDb.open();

    }
    private void displayRecordSet(Cursor cursor) {
        String message = "";
        // populate the message from the cursor

        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                String pid = cursor.getString(DBAdapter.COL_PID);
                String pname = cursor.getString(DBAdapter.COL_PNAME);
                int pprice = cursor.getInt(DBAdapter.COL_PPRICE);
                String pdesc = cursor.getString(DBAdapter.COL_PDESC);
                String ptype = cursor.getString(DBAdapter.COL_PTYPE);
                String pimg = cursor.getString(DBAdapter.COL_PIMAGE);
                int pbargain = cursor.getInt(DBAdapter.COL_PBARGAIN);




                // Append data to the message:
                message += "id=" + pid
                        +", name=" + pname
                        +", price=" + pprice
                        +", desc=" + pdesc
                        +", type=" + ptype
                        +", img=" + pimg

                        +", Bargain=" + pbargain
                        +"\n";
            } while(cursor.moveToNext());displayText(message);
        }

        // Close the cursor to avoid a resource leak.
        cursor.close();


    }

    private void displayText(String message) {
        TextView record = (TextView) findViewById(R.id.details);
        record.setText(message);
    }


    private void closeDB() {
        myDb.close();
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
