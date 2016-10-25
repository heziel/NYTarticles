package com.codepath.nytarticles.viewHolder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.codepath.nytarticles.R;
import com.codepath.nytarticles.activity.DetailActivity;
import com.codepath.nytarticles.adapter.DynamicHeightImageView;
import com.codepath.nytarticles.model.Article;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.ivImage) DynamicHeightImageView articleImage;
    @BindView(R.id.tvTitle) TextView articleTitle;

    private List<Article> articles;
    private Context context;

    // return the Title
    public TextView getArticleTitle() {
        return articleTitle;
    }

    // return the Image
    public DynamicHeightImageView getArticleImage() {

        return articleImage;
    }

    public ArticleViewHolder(Context context,View itemView, List<Article> articlesList) {

        super(itemView);

        this.articles = articlesList;
        this.context = context;

        itemView.setOnClickListener(this);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void onClick(View view) {

        int position = getLayoutPosition();

        // get the article
        Article article = articles.get(position);

        // send the url to DetailActivity
        Intent i = new Intent(context, DetailActivity.class);
        i.putExtra("webUrl", article.webUrl);
        context.startActivity(i);
    }
}
