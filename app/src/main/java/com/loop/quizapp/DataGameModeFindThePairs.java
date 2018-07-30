package com.loop.quizapp;

import java.util.ArrayList;

public class DataGameModeFindThePairs {
    ArrayList<String> images;
    ArrayList<String> names;
    ArrayList<Integer> imagesIds;
    ArrayList<Integer> namesIds;

    DataGameModeFindThePairs(ArrayList<String> _images, ArrayList<String> _names,
                             ArrayList<Integer> _imagesIds, ArrayList<Integer> _namesIds) {
        images = _images;
        names = _names;
        imagesIds = _imagesIds;
        namesIds = _namesIds;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public ArrayList<Integer> getImagesIds() {
        return imagesIds;
    }

    public ArrayList<Integer> getNamesIds() {
        return namesIds;
    }

}
