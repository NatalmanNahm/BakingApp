package com.example.backingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.backingapp.Database.AppDatabase;
import com.example.backingapp.Database.AppExecutors;
import com.example.backingapp.Model.Recipe;
import com.example.backingapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipe = new ArrayList<>();
    private Recipe savedRecipe;
    private int savedId;
    private Context mContext;
    private AppDatabase mDb;
    private boolean isFav;
    private int mId;
    private int servings;
    private String mName;
    private int mImage;


    //Create an onClickHandler to make it easier for the
    // activity to interact with the recycleView
    private final RecipeAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface RecipeAdapterOnClickHandler{
        void onCLick(int id, String name, int image, int servings, boolean isFav);
    }

    public RecipeAdapter(Context context, List<Recipe> recipes,
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

    public void setRecipeData (List<Recipe> recipe){
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
        @Bind(R.id.fav_image) ImageView mFavIcon;
        private Context mContext;

        public RecipeViewHolder(View view) {
            super(view);
            mContext = view.getContext();
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            mDb= AppDatabase.getInstance(mContext);
        }

        //Populate the view for the main page
        public void bindRecipe(Recipe recipe){
            mRecipeImage.setImageResource(recipe.getImage());
            mRecipeName.setText(recipe.getName());
            mServings.setText(Integer.toString(recipe.getServings()));

            isFav = recipe.isFav();
            mId = recipe.getId();
            mName = recipe.getName();
            servings = recipe.getServings();
            mImage = recipe.getImage();

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    int savedId = mDb.recipesDao().idSaved(mId);
                    Log.d("JUSTID", Integer.toString(mId));
                    Log.d("JUSTID", Integer.toString(savedId));

                    if (savedId == mId){
                        mFavIcon.setVisibility(View.VISIBLE);
                        Log.d("JUSTID", "I'm liked");
                    }

                }
            });


        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = mRecipe.get(adapterPosition);

            int id = recipe.getId();
            String name = recipe.getName();
            int image = recipe.getImage();
            int servings = recipe.getServings();
            boolean isFav = recipe.isFav();
            mClickHandler.onCLick(id, name, image, servings, isFav);

        }
    }
}
