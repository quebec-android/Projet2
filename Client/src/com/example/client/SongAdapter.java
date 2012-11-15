package com.example.client;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ui.PlayListElement;

public class SongAdapter extends ArrayAdapter<Song> {
	Context context;
	int resource;
	List<Song> list;
	
	public SongAdapter(Context context, int resource, List<Song> list){
		super(context, resource, list);
		this.resource = resource;
		this.list = list;
		this.context = context;
		
	}
	

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlayListElement row = (PlayListElement) convertView;
        //SongHolder holder = null;
        
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        row = (PlayListElement) inflater.inflate(resource, parent, false);
        
        if(row == null)
        {
           // LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            //row = (PlayListElement) inflater.inflate(resource, parent, false);
            
           // holder = new SongHolder();
           // holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            //holder.txtTitle = (PlayListElement) row.findViewById(R.layout.playlist_song);
            
            //row.setTag(holder);
        }
        else
        {
            //holder = (SongHolder)row.getTag();
        }
        
        Song song = list.get(position);
        row.setText(song.title);
        row.setId(song.id);
        
        return row;
    }
    
    static class SongHolder
    {
       // ImageView imgIcon;
        TextView txtTitle;
    }
}

