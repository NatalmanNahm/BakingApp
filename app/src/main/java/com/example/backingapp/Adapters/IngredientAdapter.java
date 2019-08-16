package com.example.backingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.backingapp.Model.Ingredient;
import com.example.backingapp.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    ArrayList<Ingredient> mIngredient = new ArrayList<>();
    Context mContext;


    /**
     * Constructor fo the Adapter
     * @param context
     * @param ingredients
     */
    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredients){
        mContext = context;
        mIngredient = ingredients;
    }


    @NonNull
    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutListItem = R.layout.ingredients_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutListItem, viewGroup, shouldAttachToParentImmediately);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientViewHolder ingredientViewHolder, int i) {
        ingredientViewHolder.bindIngredients(mIngredient.get(i));

    }

    @Override
    public int getItemCount() {
        if (mIngredient == null) return 0;
        return mIngredient.size();
    }

    public void setmIngredient(ArrayList<Ingredient> ingredient){
        mIngredient = ingredient;
        notifyDataSetChanged();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.ingredient_name) TextView mIngredientName;
        @Bind(R.id.ingredient_measure) TextView mIngredientMeasure;
        @Bind(R.id.ingredient_quantity) TextView mIngredientQuantity;
        Context mContext;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bindIngredients(Ingredient ingredient){
            mIngredientName.setText(ingredient.getmIngredient());
            mIngredientQuantity.setText(ingredient.getmQuantity());
            mIngredientMeasure.setText(ingredient.getmMeasure());
        }
    }
}
