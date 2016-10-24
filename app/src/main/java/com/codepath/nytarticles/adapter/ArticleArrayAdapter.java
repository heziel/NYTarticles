package com.codepath.nytarticles.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.codepath.nytarticles.model.Article;
import com.codepath.nytarticles.R;
import com.codepath.nytarticles.viewHolder.ArticleViewHolder;
import com.codepath.nytarticles.viewHolder.ArticleViewHolderTextOnly;
import com.squareup.picasso.Picasso;
import java.util.List;


 public class ArticleArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private List<Article> itemList;
    private Context context;

    // consts
    private final int TEXTONLY  = 0;
    private final int THUMBNAIL = 1;

    public ArticleArrayAdapter(Context context, List<Article> articles){

        this.context = context;
        this.itemList = articles;
    }

    public Context getContext() {
        return context;
    }

    // inflate the item layout and create the holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder ;
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType){
            case TEXTONLY:
                // Inflate the custom layout
                View contactViewTextOnly = inflater.inflate(R.layout.item_article_results_text_only, parent, false);

                viewHolder = new ArticleViewHolderTextOnly(context,contactViewTextOnly,itemList);
                break;
            case THUMBNAIL:
                // Inflate the custom layout
                View contactViewAll = inflater.inflate(R.layout.item_article_results, parent, false);

                viewHolder = new ArticleViewHolder(context,contactViewAll,itemList);
                break;
            default:
                // Inflate the custom layout
                View contactView = inflater.inflate(R.layout.item_article_results, parent, false);

                viewHolder = new ArticleViewHolder(context,contactView,itemList);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {

        // get the viewHolder type
        int type = holder.getItemViewType();

        String itemHeadLine  = itemList.get(position).getHeadLine();

        if  ( type== TEXTONLY) {
            ArticleViewHolderTextOnly articleViewHolderTextOnly = (ArticleViewHolderTextOnly) holder;
            // Set item views based on your views and data model
            articleViewHolderTextOnly.getArticleTitle().setText(itemHeadLine);
        }
        else if ( type== THUMBNAIL) {

            ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;

            // Set item views based on your views and data model
            articleViewHolder.getArticleTitle().setText(itemHeadLine);

            //remote download image in background
            String thumbnail = itemList.get(position).getThumbNail();

            // Set the height ratio before loading in image into Picasso
            articleViewHolder.getArticleImage().setHeightRatio((double)100/(double)100);
            //        .setHeightRatio(((double)photo.getHeight())/photo.getWidth());

            // populate the picture.
            Glide.with(context).load(thumbnail)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(articleViewHolder.getArticleImage());
        }
    }

     // clear recycled items for using Glide
     @Override
     public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
         int type = viewHolder.getItemViewType();
         if (type == THUMBNAIL) {
             ArticleViewHolder holder = (ArticleViewHolder) viewHolder;
             super.onViewRecycled(holder);
             Glide.clear(holder.getArticleImage());
         }

         super.onViewRecycled(viewHolder);
     }

     /*
         * This method return the item type
         */
    @Override
    public int getItemViewType(int position) {

        if ( itemList.get(position).getThumbNail().isEmpty() )
            return TEXTONLY;

        return THUMBNAIL;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

 }