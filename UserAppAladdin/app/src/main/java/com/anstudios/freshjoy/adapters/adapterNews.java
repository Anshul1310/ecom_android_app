package com.anstudios.freshjoy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.freshjoy.R;
import com.anstudios.freshjoy.models.News;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapterNews extends RecyclerView.Adapter<adapterNews.holder> {

    private Context context;
    private ArrayList<News> arrayList;

    public adapterNews(Context context, ArrayList<News> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(context).inflate(R.layout.layout_news,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        holder.title.setText(arrayList.get(position).getTitle());
        holder.description.setText(arrayList.get(position).getDescription());
        Picasso.get().load(arrayList.get(position).getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        private ImageView image;
        TextView title, description;

        public holder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.news_image);
            title=itemView.findViewById(R.id.news_title);
            description=itemView.findViewById(R.id.news_description);
        }
    }
}
