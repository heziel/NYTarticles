package com.codepath.nytarticles.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.nytarticles.Model.Article;
import com.codepath.nytarticles.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hezi Eliyahu on 20/10/2016.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    @BindView(R.id.ivImage) ImageView ivImage;
    @BindView(R.id.tvTitle) TextView tvTitle;

    public ArticleArrayAdapter(Context context, List<Article> articles){
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get data item for current position
        Article article = getItem(position);
        // if not using recycle view inflate the layout.
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.item_article_results,parent,false);
        }

        ButterKnife.bind(this,convertView);
        ivImage.setImageResource(0);
        tvTitle.setText(article.getHeadLine());

        //remote download image in background
        String thumbnail = article.getThumbNail();

        if (!TextUtils.isEmpty(thumbnail)){
            Picasso.with(getContext()).load(thumbnail).into(ivImage);
        }

        return  convertView;
    }
}
