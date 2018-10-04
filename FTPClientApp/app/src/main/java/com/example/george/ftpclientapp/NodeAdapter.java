package com.example.george.ftpclientapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NodeAdapter extends BaseAdapter {
    public NodeAdapter(Context c, CustomDirectory dir, CustomNode root) {
        explorerRoot = root;
        currentDir = dir;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return currentDir.contents.size();
    }

    @Override
    public Object getItem(int position) {
        return currentDir.contents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.listview_detail, parent, false);
        TextView fileNameTextView = (TextView) v.findViewById(R.id.fileNameTextView);
        TextView filePathTextView = (TextView) v.findViewById(R.id.filePathTextView);
        TextView fileSizeTextView = (TextView) v.findViewById(R.id.fileSizeTextView);

        fileNameTextView.setText(currentDir.contents.get(position).name);
        filePathTextView.setText(currentDir.contents.get(position).path);
        fileSizeTextView.setText(currentDir.contents.get(position).fileLink.getSize() + "B");

        return v;
    }

    public LayoutInflater mInflater;
    public ListView explorerListView;
    public CustomNode explorerRoot;
    public CustomDirectory currentDir;
}
