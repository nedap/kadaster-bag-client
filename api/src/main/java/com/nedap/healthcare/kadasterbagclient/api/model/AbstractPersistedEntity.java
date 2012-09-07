package com.nedap.healthcare.kadasterbagclient.api.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Abstract entity object with id that is automaticly generated.
 * 
 * @author Dusko Vesin
 */
@MappedSuperclass
public class AbstractPersistedEntity implements Serializable {

    private static final long serialVersionUID = -1781818043703819503L;

    public static final String ID = "id";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Default contructor.
     */
    public AbstractPersistedEntity() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AbstractPersistedEntity [id=" + id + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractPersistedEntity other = (AbstractPersistedEntity) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

}
