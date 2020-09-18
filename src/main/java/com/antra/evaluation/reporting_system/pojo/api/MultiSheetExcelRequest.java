package com.antra.evaluation.reporting_system.pojo.api;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class MultiSheetExcelRequest {


    @NotNull
    @Size(min = 1)
    private String description;
    @NotNull
    @Size(min = 1)
    private List<String> headers;
    @NotNull
    @Size(min = 1)
    private List<List<Object>> data;

    private String submitter;
    @NotNull
    @Size(min = 1)
    private String splitBy;

    public MultiSheetExcelRequest(){

    }
    public MultiSheetExcelRequest(String description, List<String> headers, List<List<Object>> data,String submitter,String splitBy ){
        this.description = description;
        this.headers = headers;
        this.submitter = submitter;
        this.splitBy = splitBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { this.description = description; }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<List<Object>> getData() { return data; }

    public void setData(List<List<Object>> data) { this.data = data; }

    public String getSubmitter() { return submitter; }

    public void setSubmitter(String submitter) { this.submitter = submitter; }

    public String getSplitBy() { return splitBy; }

    public void setSplitBy(String splitBy) { this.splitBy = splitBy; }
}
