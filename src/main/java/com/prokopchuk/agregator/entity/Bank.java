package com.prokopchuk.agregator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Bank entity
 *
 * @author N.Prokopchuk
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@EqualsAndHashCode(callSuper = true, exclude = "rates")
@Entity
@Getter @Setter
public class Bank extends AbstractEntity<Integer> {
    @NotBlank(message = "Bank name is mandatory")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            mappedBy = "bank")
    private List<ExchangeRate> rates = new ArrayList<>();
}
