package com.imas.model;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrimeCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    @Column(unique = true)
    String crimeName;
}
