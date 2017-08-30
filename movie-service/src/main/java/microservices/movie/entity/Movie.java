package microservices.movie.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by amardeep2551 on 8/19/2017.
 */
@Entity
@Data
public class Movie {

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "ID")
    private String id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NAME")
    private String name;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DIRECTOR_NAME")
    private String directorName;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "GENRE")
    private String genre;

    @Basic(optional = false)
    @NotNull
    @Column(name = "ADDED_ON", insertable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date addedOn;
}
