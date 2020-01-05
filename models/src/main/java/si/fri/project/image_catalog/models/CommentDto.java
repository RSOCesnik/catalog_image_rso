package si.fri.project.image_catalog.models;

public class CommentDto {

    private Integer commentId;
    private String commentData;
    private String imageId;

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer comment_id) {
        this.commentId = comment_id;
    }

    public String getCommentData() {
        return commentData;
    }

    public void setCommentData(String comment_data) {
        this.commentData = comment_data;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String image_id) {
        this.imageId = image_id;
    }
}
