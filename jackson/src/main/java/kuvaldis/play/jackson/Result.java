package kuvaldis.play.jackson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class Result {

    @JsonDeserialize(using = ListOrObjectResultItemJsonDeserializer.class)
    private List<ResultItem> result;

    public void setResult(final List<ResultItem> result) {
        this.result = result;
    }

    public List<ResultItem> getResult() {
        return result;
    }
}
