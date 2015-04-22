package com.defch.blogwbly.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final BlogPost post = aPosts.get(position);

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MaterialDialog.Builder dialog = new MaterialDialog.Builder(context);
                dialog.title(R.string.delete_view);
                dialog.callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        aPosts.remove(position);
                        ((MainActivity)context).app.deletePostOnDB(post.getId());
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.dismiss();
                    }
                });
                MaterialDialog d = dialog.build();
                d.setActionButton(DialogAction.POSITIVE, android.R.string.ok);
                d.setActionButton(DialogAction.NEGATIVE, android.R.string.no);

                dialog.show();
                return true;
            }
        });

        if(post.getThumbnails() != null) {
            holder.imageView.setImageBitmap(post.getThumbnails().get(0));
        }
        holder.title.setText(post.getTitle());
        holder.subtitle.setText(post.getSubtitle());
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).newIntent(PostActivity.class, PostActivity.PostValue.EDIT, post);
            }
        });
        holder.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).newIntent(PostActivity.class, PostActivity.PostValue.VIEW, post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return aPosts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View cardView;
        public ImageView imageView;
        public TextView title, subtitle;
        public Button editBtn, viewBtn;
        public ViewHolder(View v) {
             super(v);
             cardView = v.findViewById(R.id.card_view);
             imageView = (ImageView)v.findViewById(R.id.item_imageview);
             title = (TextView)v.findViewById(R.id.item_title);
             subtitle = (TextView)v.findViewById(R.id.item_subtitle);
             editBtn = (Button)v.findViewById(R.id.item_edit_btn);
             viewBtn = (Button)v.findViewById(R.id.item_view_btn);
         }
    }
}
