package com.example.george.ftpclientapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class ExplorerActivity extends AppCompatActivity {

    ListView explorerListView;
    CustomNode explorerRoot;
    CustomDirectory currentDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);
        explorerListView = (ListView) findViewById(R.id.explorerListView);
        explorerRoot = CustomNode.root;
        currentDir = (CustomDirectory) explorerRoot;

        NodeAdapter adapter = new NodeAdapter(this, currentDir, explorerRoot);
        explorerListView.setAdapter(adapter);
    }
}
