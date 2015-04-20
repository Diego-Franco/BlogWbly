package com.defch.blogwbly.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import com.defch.blogwbly.R;
import com.defch.blogwbly.activities.PostActivity;
import com.defch.blogwbly.ui.BlogPictureView;

import java.util.ArrayList;

/**
 * Created by DiegoFranco on 4/18/15.
 */
public class AdapterPostPictures extends RecyclerView.Adapter<AdapterPostPictures.ViewHolder>{

    private Context context;
    private ArrayList<BlogPictureView> pictures;
    private PostActivity.PostValue pValue;


    public AdapterPostPictures(ArrayList<BlogPictureView> objects, PostActivity.PostValue postValue) {
        this.pictures = objects;
        this.pValue = postValue;
    }

    @Override
    public AdapterPostPictures.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_post_blog, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        BlogPictureView pictureView = pictures.get(position);
        if (pictureView != null) {
            if (pictureView.getPicture() != null) {
                holder.videoView.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.VISIBLE);
                holder.imageView.setImageBitmap(pictureView.getPicture());
            } else if (pictureView.getVideo() != null) {
                holder.imageView.setVisibility(View.GONE);
                holder.videoView.setVisibility(View.VISIBLE);
                holder.videoView.setVideoURI(pictureView.getVideo());
            }
            if (pValue == PostActivity.PostValue.VIEW) {
                holder.bntClose.setVisibility(View.GONE);
            } else {
                holder.bntClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pictures.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }

            holder.containerView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //TODO implement dialog for edit the file
                    /**
                     *  MaterialDialog.Builder dialog = new MaterialDialog.Builder(context);
                     dialog.title(R.string.delete_view);
                     dialog.callback(new MaterialDialog.ButtonCallback() {
                    @Override public void onPositive(MaterialDialog dialog) {
                    super.onPositive(dialog);
                    aPosts.remove(position);
                    notifyDataSetChanged();
                    }

                    @Override public void onNegative(MaterialDialog dialog) {
                    super.onNegative(dialog);
                    dialog.dismiss();
                    }
                    });
                     MaterialDialog d = dialog.build();
                     d.setActionButton(DialogAction.POSITIVE, android.R.string.ok);
                     d.setActionButton(DialogAction.NEGATIVE, android.R.string.no);

                     dialog.show();
                     */
                    return true;
                }
            });
        }
    }

    //TODO check the videoView, doesn't show the video


    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View containerView;
        ImageView imageView;
        VideoView videoView;
        ImageView bntClose;
        public ViewHolder(View v) {
            super(v);
            containerView = v.findViewById(R.id.container_widget_post_blog);
            imageView = (ImageView)v.findViewById(R.id.widget_img);
            videoView = (VideoView)v.findViewById(R.id.widget_video);
            bntClose = (ImageView)v.findViewById(R.id.widget_close_btn);
        }
    }
}
