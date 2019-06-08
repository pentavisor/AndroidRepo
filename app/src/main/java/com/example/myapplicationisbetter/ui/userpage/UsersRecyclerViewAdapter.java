package com.example.myapplicationisbetter.ui.userpage;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.example.myapplicationisbetter.BR;
import com.example.myapplicationisbetter.R;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.databinding.ListItemBinding;


import java.util.List;

import static android.view.View.INVISIBLE;


public class UsersRecyclerViewAdapter extends RecyclerView.Adapter<UsersRecyclerViewAdapter.ViewHolder> implements CustomClickListenerRecyclerView {

    private List<UserDataModel> dataModelList;
    private MainPresenter mainPresenter;

    public UsersRecyclerViewAdapter(List<UserDataModel> dataModelList, MainPresenter presenter) {
        this.dataModelList = dataModelList;
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
