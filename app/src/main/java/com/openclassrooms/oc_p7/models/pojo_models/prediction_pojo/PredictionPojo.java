package com.openclassrooms.oc_p7.models.pojo_models.prediction_pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PredictionPojo {
    public String description;
    @SerializedName("matched_substrings")
    public List<MatchedSubstring> matchedSubstring;
    @SerializedName("place_id")
    public String placeId;
    public String reference;
    @SerializedName("structured_formatting")
    public StructuredFormatting structuredFormatting;
    public List<Terms> terms;
    public List<String> types;


}
