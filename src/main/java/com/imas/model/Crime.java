package com.imas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by spoko on 22.06.16.
 */
@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Crime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    long id;
    @JsonProperty("lat")
    Float lat;
    @JsonProperty("lng")
    Float lng;
    @JsonProperty("count")
    int count;
    @Transient
    @JsonProperty(value = "crimeType", access = JsonProperty.Access.WRITE_ONLY)
    String crimeType;
    @OneToOne
    @JsonIgnore
    CrimeCategory crimeCategory;
}
