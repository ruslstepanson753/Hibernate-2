package org.example.com.javarush.domain;


public enum SpecialFeature {
    TRAILERS("Trailers"),
    COMMENTARIES("Commentaries"),
    DELETED_SCENES("Deleted Scenes"),
    BEHIND_THE_SCENES("Behind the Scenes");
    private String value;

    SpecialFeature(String value) {
        this.value = value;
    }

    public static SpecialFeature getFeatureByValue(String value) {
        if ((value.isEmpty()) || (value == null)) {
            return null;
        }
        SpecialFeature[] features = SpecialFeature.values();
        for (SpecialFeature feature : features) {
            if (feature.value.equals(value)) {
                return feature;
            }
        }
        return null;
    }
}
