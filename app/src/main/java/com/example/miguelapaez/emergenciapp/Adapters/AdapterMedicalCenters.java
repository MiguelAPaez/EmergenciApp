package com.example.miguelapaez.emergenciapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.miguelapaez.emergenciapp.Entities.EntityMedicalCenter;
import com.example.miguelapaez.emergenciapp.R;

import java.util.ArrayList;

public class AdapterMedicalCenters extends BaseAdapter {

    private Context context;
    private ArrayList <EntityMedicalCenter> listItems;

    public AdapterMedicalCenters(Context context , ArrayList <EntityMedicalCenter> listItems) {
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

        EntityMedicalCenter item = (EntityMedicalCenter) getItem ( i );

        view = LayoutInflater.from( context).inflate( R.layout.item_medical_center, null);
        TextView name = (TextView) view.findViewById ( R.id.nameMedicalCenter );
        TextView direction = (TextView) view.findViewById ( R.id.addressMedicalCenter );
        TextView time = (TextView) view.findViewById ( R.id.timeToMedicalCenter );

        name.setText ( item.getName () );
        direction.setText ( item.getDirection () );
        int min = (item.getDuration ()*1)/60;
        time.setText ( "Tiempo estimado: " + min + " minutos");

        return view;
    }
}
