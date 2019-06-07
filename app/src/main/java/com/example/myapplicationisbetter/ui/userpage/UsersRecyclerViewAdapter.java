package com.example.myapplicationisbetter.ui.userpage;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.MvpFacade;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.myapplicationisbetter.BR;
import com.example.myapplicationisbetter.R;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.databinding.ListItemBinding;
import com.example.myapplicationisbetter.ui.MyHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.view.View.INVISIBLE;


public class UsersRecyclerViewAdapter extends RecyclerView.Adapter<UsersRecyclerViewAdapter.ViewHolder> implements CustomClickListenerRecyclerView {

    private List<UserDataModel> dataModelList;
    private Context context;
    private MainPresenter mainPresenter;

    public UsersRecyclerViewAdapter(List<UserDataModel> dataModelList, Context ctx, MainPresenter presenter) {
        this.dataModelList = dataModelList;
        context = ctx;
        mainPresenter = presenter;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public UsersRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.list_item, parent, false);
        if (viewType == 0) {
            binding.imagelittle.setVisibility(INVISIBLE);
            binding.imagemask.setVisibility(INVISIBLE);
            binding.imagemasklittle.setVisibility(INVISIBLE);
        }

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        UserDataModel dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.listItemBinding.setItemClickListener(this);

        if (dataModel.id != BindingAdapters.ADD_BUTTON_ID){
            if (dataModel.imageName.equals("")) {
                holder.listItemBinding.imagemain.setImageDrawable(MyHelper.getCircleBitmap(dataModel.imageLink, 0.8f));
            } else {
                Picasso.get()
                        .load(dataModel.imageName)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.listItemBinding.imagemain, new Callback() {
                            @Override
                            public void onSuccess() {
                                holder.listItemBinding.imagemain.setImageDrawable(MyHelper.getCircleBitmap(holder.listItemBinding.imagemain.getDrawable(), 0.8f));
                            }

                            @Override
                            public void onError(Exception e) {
                                //Try again online if cache failed
                                Picasso.get()
                                        .load(dataModel.imageName)
                                        .error(R.drawable.mustache)
                                        .into(holder.listItemBinding.imagemain, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                holder.listItemBinding.imagemain.setImageDrawable(MyHelper.getCircleBitmap(holder.listItemBinding.imagemain.getDrawable(), 0.8f));
                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                Log.v("Picasso", "Could not fetch image");
                                            }
                                        });
                            }
                        });
            }
        }

    }


    @Override
    public int getItemCount() {
        return dataModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ListItemBinding listItemBinding;

        public ViewHolder(ListItemBinding listItemBinding) {
            super(listItemBinding.getRoot());
            this.listItemBinding = listItemBinding;
        }

        public void bind(Object obj) {
            listItemBinding.setVariable(BR.model, obj);
            listItemBinding.executePendingBindings();
        }
    }

    @Override
    public void cardClicked(UserDataModel model) {
        if (mainPresenter != null) {
            switch (model.id) {
                case (BindingAdapters.ADD_BUTTON_ID):
                    mainPresenter.goToCreateUserPage();
                    break;
                default:
                    mainPresenter.setTextInFeature(model);
            }
        }


    }

}
