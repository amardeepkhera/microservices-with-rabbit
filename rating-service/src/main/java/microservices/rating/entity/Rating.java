package microservices.rating.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by amardeep2551 on 8/30/2017.
 */
@Entity
@Data
public class Rating {
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "ID")
    private String id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "RATING")
    private String rating;

    @Basic(optional = false)
    @NotNull
    @Column(name = "ADDED_ON", insertable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date addedOn;

    @Basic
    @Column(name = "UPDATED_ON", insertable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;


}
