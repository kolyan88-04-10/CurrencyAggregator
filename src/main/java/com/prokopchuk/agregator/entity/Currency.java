package com.prokopchuk.agregator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Currency entity
 *
 * @author N.Prokopchuk
 */

@EqualsAndHashCode(callSuper = true, exclude = {"currencyValueList"})
@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
@Getter @Setter
public class Currency extends AbstractEntity<Integer> {
    @Column(unique = true)
    private String name;
    @Column(name="order_sequence")
    private Integer order;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "currency")
    List<ExchangeRate> currencyValueList = new ArrayList<>();
}
