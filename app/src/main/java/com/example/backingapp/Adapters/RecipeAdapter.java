package com.example.backingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.backingapp.Model.Recipe;
import com.example.backingapp.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private ArrayList<Recipe> mRecipe = new ArrayList<>();
    private Context mContext;

    //Create an onClickHandler to make it easier for the
    // activity to interact with the recycleView
    private final RecipeAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface RecipeAdapterOnClickHandler{
        void onCLick(int id, String name);
    }

    public RecipeAdapter(Context context, ArrayList<Recipe> recipes,
                         RecipeAdapterOnClickHandler clickHandler){
        mContext = context;
        mRecipe = recipes;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecipeViewHolder recipeViewHolder, int i) {
        recipeViewHolder.bindRecipe(mRecipe.get(i));
    }

    @Override
    public int getItemCount() {
        if (null == mRecipe) return 0;
        return mRecipe.size();
    }

    public void setRecipeData (ArrayList<Recipe> recipe){
        mRecipe = recipe;
        notifyDataSetChanged();
    }

    /**
     * Cache of the children views for a Recipe list item.
     */
    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.recipe_image) ImageView mRecipeImage;
        @Bind(R.id.recipe_name) TextView mRecipeName;
        @Bind(R.id.recipe_servings) TextView mServings;
        private Context mContext;

        public RecipeViewHolder(View view) {
            super(view);
            mContext = view.getContext();
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        //Populate the view for the main page
        public void bindRecipe(Recipe recipe){
            mRecipeImage.setImageResource(recipe.getmImage());
            mRecipeName.setText(recipe.getmName());
            mServings.setText(Integer.toString(recipe.getmServings()));
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = mRecipe.get(adapterPosition);

            int id = recipe.getmId();
            String name = recipe.getmName();
            mClickHandler.onCLick(id, name);

        }
    }
}
