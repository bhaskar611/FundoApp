package com.example.fundoapp.data_manager;

public class FirebaseLabelModel {
    private  String label;

    public FirebaseLabelModel(String label,String labelId) {
        this.label = label;
        this.labelId = labelId;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    private  String labelId;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
