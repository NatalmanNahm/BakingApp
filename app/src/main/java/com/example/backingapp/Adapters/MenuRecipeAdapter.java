package com.example.backingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.backingapp.Model.MenuRecipes;
import com.example.backingapp.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MenuRecipeAdapter extends RecyclerView.Adapter<MenuRecipeAdapter.MenuViewHolder>{


    private ArrayList<MenuRecipes> mMenuRecipe = new ArrayList<>();
    Context mContext;

    //Create an onClickHandler to make it easier for the
    // activity to interact with the recycleView
    private final MenuAdapterOnCLickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface MenuAdapterOnCLickHandler {
        void onClick(int id);
    }

    public MenuRecipeAdapter (Context context, ArrayList<MenuRecipes> menuRecipes,
                       MenuAdapterOnCLickHandler cLickHandler){
        mContext = context;
        mMenuRecipe = menuRecipes;
        mClickHandler = cLickHandler;
    }

    @NonNull
    @Override
    public MenuRecipeAdapter.MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutListItem = R.layout.step_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutListItem, viewGroup, shouldAttachToParentImmediately);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuRecipeAdapter.MenuViewHolder menuViewHolder, int i) {
        menuViewHolder.bindMenuRecipes(mMenuRecipe.get(i));
    }

    @Override
    public int getItemCount() {
        if (null == mMenuRecipe) return 0;
        return mMenuRecipe.size();
    }

    public void setMenuRecipeData (ArrayList<MenuRecipes> menuRecipes){
        mMenuRecipe = menuRecipes;
        notifyDataSetChanged();
    }

    /**
     * Cache of the children views for a MenuRecipe list item.
     */
    public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind(R.id.step_number) TextView mStepNumber;
        @Bind(R.id.step_descp) TextView mStepDescp;
        Context mContext;

        public MenuViewHolder(@NonNull View view) {
            super(view);
            mContext = view.getContext();
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        public void bindMenuRecipes(MenuRecipes menuRecipes){
            mStepNumber.setText(menuRecipes.getmStepNumber());
            mStepDescp.setText(menuRecipes.getmStepName());
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            MenuRecipes menuRecipes = mMenuRecipe.get(adapterPosition);

            int id = menuRecipes.getmStepId();
            mClickHandler.onClick(id);

        }
    }
}
