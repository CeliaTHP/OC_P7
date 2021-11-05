package com.openclassrooms.oc_p7.models.pojo_models.prediction_pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StructuredFormatting {

    public String mainText;
    @SerializedName("main_text_matched_substrings")
    public List<MatchedSubstring> mainTextMatchedSubStrings;
    public String secondaryText;
}
