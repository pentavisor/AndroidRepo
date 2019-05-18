package com.example.myapplicationisbetter.ui.userpage;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationisbetter.App;
import com.example.myapplicationisbetter.R;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.ui.MyHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<UserDataModel> users;

    UserAdapter(Context context, List<UserDataModel> users) {
        this.users = users;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {
        UserDataModel user = users.get(position);
        if (position == 0) {
            holder.littleImage.setVisibility(View.INVISIBLE);
            holder.littleImageMask.setVisibility(View.INVISIBLE);
            holder.imageMask.setVisibility(View.INVISIBLE);
            holder.mainImage.setImageResource(user.imageLink);
        }else{
            if(user.imageName.equals("")){
                holder.mainImage.setImageDrawable(getCircleBitmap(user.imageLink));
            }else{
                Picasso.get()
                        .load(user.imageName)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.mainImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                holder.mainImage.setImageDrawable(
                                        getCircleBitmap(holder.mainImage.getDrawable())
                                );
                            }

                            @Override
                            public void onError(Exception e) {
                                //Try again online if cache failed
                                Picasso.get()
                                        .load(user.imageName)
                                        .error(R.drawable.mustache)
                                        .into(holder.mainImage, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                holder.mainImage.setImageDrawable(
                                                        getCircleBitmap(holder.mainImage.getDrawable())
                                                );
                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                Log.v("Picasso","Could not fetch image");
                                            }
                                        });
                            }
                        });
            }
        }

        if (user.googleMapLink.equals("")&(position>0)) {
            holder.littleImage.setImageResource(R.drawable.graycircle);
        } else {
            holder.littleImage.setImageResource(R.drawable.greencircle);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public UserDataModel getItem(int i) {
        return users.get(i);
    }
    public UserDataModel getItemOnIdinModel(int i) {
        for (UserDataModel udm:users) {
            if (udm.id == i)return udm;
        }
        return null;
    }

    public void addItem(UserDataModel userDataModel) {
              users.add(userDataModel);
    }

    public boolean deleteItem(UserDataModel userDataModel) {
        boolean operationAccess = false;
        if(userDataModel.id>=0)
            operationAccess = users.remove(userDataModel);
        return operationAccess;
    }
    public void deleteAllItems() {

       List<UserDataModel> ListForRemove = new ArrayList<UserDataModel>();

        for (UserDataModel x:users) {
            if(x.id>0)ListForRemove.add(x);
        }
        users.removeAll(ListForRemove);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView mainImage, littleImage, littleImageMask, imageMask;

        ViewHolder(View view) {
            super(view);
            mainImage = (ImageView) view.findViewById(R.id.imagemain);
            littleImage = (ImageView) view.findViewById(R.id.imagelittle);
            imageMask = (ImageView)view.findViewById(R.id.imagemask);
            littleImageMask = (ImageView) view.findViewById(R.id.imagemasklittle);
        }
    }

    private RoundedBitmapDrawable getCircleBitmap(int resourceLink) {

        Resources res = App.getInstance().getResources();
        Bitmap src = BitmapFactory.decodeResource(res, resourceLink);
        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(res, src);
        dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);

        return dr;
    }
    private RoundedBitmapDrawable getCircleBitmap(Drawable drawable) {

        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap src = null;
        if (bitmapDrawable!=null) src = bitmapDrawable.getBitmap();
        Resources res = App.getInstance().getResources();
        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(res, src);
        dr.setCornerRadius( dr.getIntrinsicWidth() /0.8f);

        return dr;
    }


}
