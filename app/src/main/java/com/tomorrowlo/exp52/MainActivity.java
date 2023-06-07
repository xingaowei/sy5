package com.tomorrowlo.exp52;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    Button bn = null;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir()
                .toString() + "/my.db3" , null);
        listView = (ListView)findViewById(R.id.show);
        bn = (Button)findViewById(R.id.button);
        bn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View source)
            {
                //获取用户输入
                String title = ((EditText)findViewById(R.id.title))
                        .getText().toString();
                String content = ((EditText)findViewById(R.id.content))
                        .getText().toString();
                try
                {
                    insertData(db , title , content);
//查询news_info表的所有记录
                    Cursor cursor = db.rawQuery ("select * from news_inf", null);
                    inflateList(cursor);
                }
                catch(SQLiteException se)
                {
                    //执行DDL创建数据表
                    db.execSQL("create table news_inf(_id integer primary key autoincrement,"
                            + " news_title varchar(50),"
                            + " news_content varchar(255))");
                    //执行insert语句插入数据
                    insertData(db , title , content);
//查询news_info表的所有记录
                    Cursor cursor =  db.rawQuery ("select * from news_inf", null);
                    inflateList(cursor);
                }
            }
        });

    }

    private void insertData(SQLiteDatabase db
            , String title , String content)
    {
        //执行插入语句
        db.execSQL("insert into news_inf values(null , ? , ?)"
                , new String[]{title , content});
    }
    private void inflateList(Cursor cursor)
    {
        //填充SimpleCursorAdapter,注意控件的对应关系
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                MainActivity.this , R.layout.line, cursor
                , new String[]{"news_title" , "news_content"}
                , new int[] {R.id.my_title, R.id.my_content });
        //显示数据
        listView.setAdapter(adapter);
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //退出程序时关闭SQLiteDatabase
        if (db != null && db.isOpen())
        {
            db.close();
        }
    }
}

