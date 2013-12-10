package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by chenyu2 on 13-12-10.
 */
@Entity
public class UserModel extends Model implements BasicModel<Long> {

    @Id
    private Long key;

    @Basic
    @Required
    private String name;

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}