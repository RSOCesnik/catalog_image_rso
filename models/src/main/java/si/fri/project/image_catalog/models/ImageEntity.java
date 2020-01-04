package si.fri.project.image_catalog.models;

import javax.persistence.*;
import java.util.List;

@Entity(name = "image_data")
@NamedQueries(value =
        {
                @NamedQuery(name = "Photo.getAll", query = "SELECT p FROM image_data p")
        }
)
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private Integer description;

    @Column(name = "created")
    private String created;

    @Column(name = "data")
    private String data;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDescription() {
        return description;
    }

    public void setDescription(Integer description) {
        this.description = description;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
