package com.amangoes.order.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity // as this is a spring data JPA entity
@Table(name = "t_orders") // table name in the database
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNumber;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}


//--using flyway database migrations, we migrate particular database by writing our own sql queries and also have the option to rollback to any previous version
//-- the naming convention for naming the migration files is of the format
//-- V<number>__<name>.sql
//-- where <number> is the version number and <name> is the name of the migration
//-- the version number should be incremental and should be unique