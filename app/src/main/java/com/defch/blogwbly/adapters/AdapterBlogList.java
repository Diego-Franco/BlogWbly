package com.defch.blogwbly.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.defch.blogwbly.R;
import com.defch.blogwbly.activities.MainActivity;
import com.defch.blogwbly.activities.PostActivity;
import com.defch.blogwbly.model.BlogPost;

import java.util.ArrayList;

/**
 * Created by DiegoFranco on 4/17/15.
 */
public class AdapterBlogList extends RecyclerView.Adapter<AdapterBlogList.ViewHolder> {

    private Context context;
    private ArrayList<BlogPost> aPosts;

    public AdapterBlogList(Context context, ArrayList<BlogPost> posts) {
        this.context = context;
        this.aPosts = posts;
    }

    @Override
    public AdapterBlogList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_main, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BlogPost post = aPosts.get(position);

        holder.imageView.setImageBitmap(post.getThumbnail());
        holder.title.setText(post.getTitle());
        holder.subtitle.setText(post.getSubtitle());
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).newIntent(PostActivity.class, PostActivity.PostValue.EDIT);
            }
        });
        holder.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).newIntent(PostActivity.class, PostActivity.PostValue.VIEW);
            }
        });
    }

    @Override
    public int getItemCount() {
        return aPosts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView title, subtitle;
        public Button editBtn, viewBtn;
        public ViewHolder(View v) {
             super(v);
             imageView = (ImageView)v.findViewById(R.id.item_imageview);
             title = (TextView)v.findViewById(R.id.item_title);
             subtitle = (TextView)v.findViewById(R.id.item_subtitle);
             editBtn = (Button)v.findViewById(R.id.item_edit_btn);
             viewBtn = (Button)v.findViewById(R.id.item_view_btn);
         }
    }
}
