package org.birdhelpline.app.model;

public class UserImage {
    private Long userId;
    private byte[] image;
    private byte[] oldImage;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getOldImage() {
        return oldImage;
    }

    public void setOldImage(byte[] oldImage) {
        this.oldImage = oldImage;
    }
}
