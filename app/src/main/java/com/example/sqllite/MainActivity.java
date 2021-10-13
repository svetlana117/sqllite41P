package com.example.sqllite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnAdd, btnClear;
    EditText etName, etElement, etWeapon, etRegion;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.etName);
        etElement = (EditText) findViewById(R.id.etElement);
        etWeapon = (EditText) findViewById(R.id.etWeapon);
        etRegion = (EditText) findViewById(R.id.etRegion);
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        UpdateTable();
    }
    public void UpdateTable(){
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int elementIndex = cursor.getColumnIndex(DBHelper.KEY_ELEMENT);
            int weaponIndex = cursor.getColumnIndex(DBHelper.KEY_WEAPON);
            int regionIndex = cursor.getColumnIndex(DBHelper.KEY_REGION);
            TableLayout dbOutPut = findViewById(R.id.dpOutput);
            dbOutPut.removeAllViews();
            do{
                TableRow dbOutPutRow = new TableRow (this);
                dbOutPutRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                LinearLayout.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT);
                TextView outputID = new TextView(this);
                params.weight=1.0f;
                outputID.setLayoutParams(params);
                outputID.setText(cursor.getString(idIndex));
                dbOutPutRow.addView(outputID);

                TextView outputName = new TextView(this);
                params.weight=3.0f;
                outputName.setLayoutParams(params);
                outputName.setText(cursor.getString(nameIndex));
                dbOutPutRow.addView(outputName);


                TextView outputElement = new TextView(this);
                params.weight=3.0f;
                outputElement.setLayoutParams(params);
                outputElement.setText(cursor.getString(elementIndex));
                dbOutPutRow.addView(outputElement);

                TextView outputWeapon = new TextView(this);
                params.weight=3.0f;
                outputWeapon.setLayoutParams(params);
                outputWeapon.setText(cursor.getString(weaponIndex));
                dbOutPutRow.addView(outputWeapon);

                TextView outputRegion = new TextView(this);
                params.weight=3.0f;
                outputRegion.setLayoutParams(params);
                outputRegion.setText(cursor.getString(regionIndex));
                dbOutPutRow.addView(outputRegion);

                Button deleteBtn = new Button(this);
                deleteBtn.setOnClickListener(this);
                params.weight=1.0f;
                deleteBtn.setLayoutParams(params);
                deleteBtn.setText("удалить");
                deleteBtn.setId(cursor.getInt(idIndex));
                dbOutPutRow.addView(deleteBtn);

                dbOutPut.addView(dbOutPutRow);


            }while(cursor.moveToNext());
        }
        cursor.close();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.btnAdd:
                String name = etName.getText().toString();
                String weapon = etWeapon.getText().toString();
                String element = etElement.getText().toString();
                String region = etRegion.getText().toString();
                contentValues = new ContentValues();

                contentValues.put(DBHelper.KEY_NAME, name);
                contentValues.put(DBHelper.KEY_ELEMENT, element);
                contentValues.put(DBHelper.KEY_WEAPON, weapon);
                contentValues.put(DBHelper.KEY_REGION, region);
                etName.setText("");
                etWeapon.setText("");
                etElement.setText("");
                etRegion.setText("");
                database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
                UpdateTable();
                break;

            case R.id.btnClear:
                database.delete(DBHelper.TABLE_CONTACTS, null, null);
                TableLayout dbOutPut = findViewById(R.id.dpOutput);
                dbOutPut.removeAllViews();
                UpdateTable();
                break;
            default:
                View outputDBRow = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                outputDB.removeView(outputDBRow);
                outputDB.invalidate();

                database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_ID + " = ?", new String[]{String.valueOf((v.getId()))});
                contentValues = new ContentValues();
                Cursor cursorUpdater = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
                if (cursorUpdater.moveToFirst()) {
                    int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_NAME);
                    int elementIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ELEMENT);
                    int weaponlIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_WEAPON);
                    int regionlIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_REGION);
                    int realID = 1;
                    do{
                        if(cursorUpdater.getInt(idIndex)>realID){
                            contentValues.put(DBHelper.KEY_ID,realID);
                            contentValues.put(DBHelper.KEY_NAME,cursorUpdater.getString(nameIndex));
                            contentValues.put(DBHelper.KEY_ELEMENT,cursorUpdater.getString(elementIndex));
                            contentValues.put(DBHelper.KEY_WEAPON,cursorUpdater.getString(weaponlIndex));
                            contentValues.put(DBHelper.KEY_REGION,cursorUpdater.getString(regionlIndex));
                            database.replace(DBHelper.TABLE_CONTACTS, null, contentValues);
                        }
                        realID++;
                    }while(cursorUpdater.moveToNext());
                    if(cursorUpdater.moveToLast()){
                        database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_ID + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                    }
                }
                break;
        }

    }
}
