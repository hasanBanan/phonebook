package com.example.phonebook.presenter.contacts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.phonebook.R;
import com.example.phonebook.domains.Contact;
import java.util.List;
import java.util.Random;

public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.MyViewHolder> {
    private List<Contact> list;
    public Context context;
    Random rnd = new Random();

    ContactsListAdapter(List<Contact> list) {
        this.list = list;
    }

    @Override
    public ContactsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact, parent, false);
        return  new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        holder.phone.setText(list.get(position).getNumber());

        holder.icon.setImageURI(list.get(position).getIconUri());
        if(holder.icon.getDrawable() == null) {
            holder.icon.setImageResource(R.drawable.ic_circle_fond);

            holder.icon.setColorFilter(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)), PorterDuff.Mode.MULTIPLY);
            if(!list.get(position).name.isEmpty())
                holder.iconSymbol.setText(String.valueOf(list.get(position).getName().charAt(0)));
        }

        if (list.get(position).getStarred().equals("1")) {
            holder.electBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_favorite));
        } else
            holder.electBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star24dp));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView icon;
        TextView iconSymbol;
        TextView name;
        TextView phone;
        ImageView electBtn;

        public MyViewHolder(View v) {
            super(v);
            icon = v.findViewById(R.id.icon_contact);
            iconSymbol = v.findViewById(R.id.icon_symbol);
            name = v.findViewById(R.id.display_name);
            phone = v.findViewById(R.id.phone);
            electBtn = v.findViewById(R.id.elect_btn);
        }
    }
}
