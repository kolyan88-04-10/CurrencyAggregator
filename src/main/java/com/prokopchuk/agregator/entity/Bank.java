package com.prokopchuk.agregator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
@EqualsAndHashCode(callSuper = true, exclude = "rates")
@Entity
@Getter @Setter
public class Bank extends AbstractEntity<Integer> {
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            mappedBy = "bank")
    private List<ExchangeRate> rates = new ArrayList<>();
}
