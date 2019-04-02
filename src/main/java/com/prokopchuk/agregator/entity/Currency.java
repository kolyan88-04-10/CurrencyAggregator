package com.prokopchuk.agregator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true, exclude = {"currencyValueList"})
@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
public class Currency extends AbstractEntity<Integer> {

    @Column(unique = true)
    private String name;
    @Column(name="order_sequence")
    private Integer order;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "type")
    List<ExchangeRate> currencyValueList = new ArrayList<>();

}
