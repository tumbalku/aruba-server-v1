package com.bahteramas.app.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Embeddable
@AttributeOverrides({
        @AttributeOverride( name = "rank", column = @Column(name = "cs_rank")),
        @AttributeOverride( name = "group", column = @Column(name = "cs_group")),
        @AttributeOverride( name = "position", column = @Column(name = "cs_position")),
        @AttributeOverride( name = "workUnit", column = @Column(name = "cs_work_unit"))
})
public class CivilServant {
  private String rank;
  private String group;
  private String position;
  private String workUnit;

}
