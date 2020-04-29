package com.example.miguelapaez.emergenciapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miguelapaez.emergenciapp.Entities.EntityFamilyGroup;
import com.example.miguelapaez.emergenciapp.R;

import java.util.ArrayList;

public class AdapterFamilyGroup extends BaseAdapter {

    private Context context;
    private ArrayList<EntityFamilyGroup> listItems;

    public AdapterFamilyGroup(Context context , ArrayList <EntityFamilyGroup> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size ();
    }

    @Override
    public Object getItem(int i) {
        return listItems.get ( i );
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i , View view , ViewGroup viewGroup) {

        EntityFamilyGroup item = (EntityFamilyGroup) getItem ( i );

        view = LayoutInflater.from(context).inflate( R.layout.item_family_group,null);
        ImageView imgParent = (ImageView) view.findViewById ( R.id.imageViewFamilyGroup );
        TextView name = (TextView) view.findViewById ( R.id.nameFamilyGroup );
        TextView parent = (TextView) view.findViewById ( R.id.relationshipFamilyGroup );

        imgParent.setImageResource ( item.getImgParent () );
        name.setText ( item.getName () );
        parent.setText ( item.getParent () );

        return view;
    }
}
