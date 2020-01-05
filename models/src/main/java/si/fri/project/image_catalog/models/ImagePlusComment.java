package si.fri.project.image_catalog.models;

import java.util.List;

public class ImagePlusComment {

    private List<CommentDto> comments;
    private ImageEntity image;

    public List<CommentDto>  getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    public ImageEntity getImage() {
        return image;
    }

    public void setImage(ImageEntity image) {
        this.image = image;
    }
}
