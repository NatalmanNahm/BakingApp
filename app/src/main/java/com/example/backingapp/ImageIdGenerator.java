package com.example.backingapp;


public class ImageIdGenerator {

    /**
     * This is a helper method to get the image of each recipes
     * @param id
     * @return id
     */
    public static int imageId (int id){

        int image = 0;

        switch (id){
            case 1:
                image = R.drawable.nutella_pie;
                break;
            case 2:
                image = R.drawable.brownies;
                break;
            case 3:
                image = R.drawable.yellow_cake;
                break;
            case 4:
                image = R.drawable.cheesecake;
                break;
            default:
                break;
        }
        return image;
    }
}
