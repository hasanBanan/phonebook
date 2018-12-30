package com.example.phonebook.presenter.favorites;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phonebook.R;
import com.example.phonebook.domains.Contact;

import java.util.List;
import java.util.Random;

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.MyViewHolder> {
    public List<Contact> list;
    FavoritesListContract.Presenter mPresenter;
    public Context context;
    Random rnd = new Random();

    FavoritesListAdapter(List<Contact> list, FavoritesListContract.Presenter mPresenter) {
        this.list = list;
        this.mPresenter = mPresenter;
    }

    @Override
    public FavoritesListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact, parent, false);
        return  new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.name.setText(list.get(position).getName());
        holder.phone.setText(list.get(position).getNumber());

        holder.icon.setImageURI(list.get(position).getIconUri());
        if(holder.icon.getDrawable() == null) {
            holder.icon.setImageResource(R.drawable.ic_circle_fond);
            holder.icon.setColorFilter(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)), PorterDuff.Mode.MULTIPLY);
            if(!list.get(position).getName().isEmpty())
                holder.iconSymbol.setText(String.valueOf(list.get(position).getName().charAt(0)));
        }else {
            holder.iconSymbol.setText("");
            holder.icon.setColorFilter(null);
        }

        if (list.get(position).getStarred() == 1) {
            holder.electBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_favorite));
        } else
            holder.electBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star24dp));


        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(1, position, 0, "Delete");
            }
        });

        holder.electBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.get(position).getStarred() == 1) {
                    mPresenter.changeFavorite(context, 0, list.get(position).getId());
                    list.remove(position);
                    notifyItemRemoved(position);
                    if(!list.isEmpty())
                        notifyItemRangeRemoved(position, list.size());
//                    holder.electBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star24dp));
                }else {
                    holder.electBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_favorite));
                    list.get(position).setStarred(1);
                    mPresenter.changeFavorite(context, 1, list.get(position).getId());
                }
            }
        });
    }

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
