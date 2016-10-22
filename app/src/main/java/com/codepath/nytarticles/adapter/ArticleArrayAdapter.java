package com.codepath.nytarticles.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.nytarticles.model.Article;
import com.codepath.nytarticles.R;
import com.codepath.nytarticles.viewHolder.ArticleViewHolder;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by Hezi Eliyahu on 20/10/2016.
 */

//public class ArticleArrayAdapter extends ArrayAdapter<Article> {
 public class ArticleArrayAdapter extends RecyclerView.Adapter<ArticleViewHolder>  {

 //   @BindView(R.id.ivImage) ImageView ivImage;
 //   @BindView(R.id.tvTitle) TextView tvTitle;

    private List<Article> itemList;
    private Context context;

    public ArticleArrayAdapter(Context context, List<Article> articles){
        //super(context, android.R.layout.simple_list_item_1, articles);
        this.context = context;
        this.itemList = articles;
    }

    public Context getContext() {
        return context;
    }

    // inflate the item layout and create the holder
    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_article_results, parent, false);

        // Return a new holder instance
        ArticleViewHolder viewHolder = new ArticleViewHolder(context,contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {

        String itemHeadLine  = itemList.get(position).getHeadLine();
        // Set item views based on your views and data model
        holder.title.setText(itemHeadLine);

        //remote download image in background
        String thumbnail = itemList.get(position).getThumbNail();
        if ( !thumbnail.isEmpty() ) {
            Picasso.with(context).load(thumbnail).into(holder.articleImage);
        }
        else
            Picasso.with(context).load(R.mipmap.ic_launcher).into(holder.articleImage);

    }

    @Override
    public int getItemCount() {
       // return 0;
        return itemList.size();
    }
}
