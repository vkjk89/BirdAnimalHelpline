package org.birdhelpline.app.model;

import java.util.ArrayList;
import java.util.List;

public class CaseImage {
    private Long caseId;
    private List<byte[]> images = new ArrayList<>();

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public List<byte[]> getImages() {
        return images;
    }

    public void setImages(List<byte[]> images) {
        this.images = images;
    }

    public void addImage(byte[] toByteArray) {
        images.add(toByteArray);
    }
}
