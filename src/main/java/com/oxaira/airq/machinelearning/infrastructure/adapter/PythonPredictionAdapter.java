package com.oxaira.airq.machinelearning.infrastructure.adapter;

import org.springframework.stereotype.Component;

@Component
public class PythonPredictionAdapter {

    public String predict() {

        return "Prediction generated successfully";

    }
}