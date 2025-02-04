package com.les.db2.entity.order;

import com.les.db2.entity.AbstractEntity;
import com.les.db2.entity.Address;
import com.les.db2.entity.Customer;
import lombok.Data;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName: Order
 * @Description:
 * @Author: king
 * @CreateDate: 2018/11/29 14:41
 */
@Entity
@Table(name = "Orders")
@Data
public class Order extends AbstractEntity {

    @ManyToOne(optional = false)
    private Customer customer;

    @ManyToOne
    private Address billingAddress;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Address shippingAddress;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private Set<LineItem> lineItems = new HashSet<LineItem>();

    /**
     * Creates a new {@link Order} for the given {@link Customer}.
     *
     * @param customer must not be {@literal null}.
     * @param shippingAddress must not be {@literal null}.
     */
    public Order(Customer customer, Address shippingAddress) {
        this(customer, shippingAddress, null);
    }

    /**
     * Creates a new {@link Order} for the given customer, shipping and billing {@link Address}.
     *
     * @param customer must not be {@literal null}.
     * @param shippingAddress must not be {@literal null}.
     * @param billingAddress can be {@@iteral null}.
     */
    public Order(Customer customer, Address shippingAddress, Address billingAddress) {

        Assert.notNull(customer);
        Assert.notNull(shippingAddress);

        this.customer = customer;
        this.shippingAddress = shippingAddress.getCopy();
        this.billingAddress = billingAddress == null ? null : billingAddress.getCopy();
    }

    protected Order() {

    }

    /**
     * Adds the given {@link LineItem} to the {@link Order}.
     *
     * @param lineItem
     */
    public void add(LineItem lineItem) {
        this.lineItems.add(lineItem);
    }

    /**
     * Returns the {@link Customer} who placed the {@link Order}.
     *
     * @return
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Returns the billing {@link Address} for this order.
     *
     * @return
     */
    public Address getBillingAddress() {
        return billingAddress != null ? billingAddress : shippingAddress;
    }

    /**
     * Returns the shipping {@link Address} for this order;
     *
     * @return
     */
    public Address getShippingAddress() {
        return shippingAddress;
    }

    /**
     * Returns all {@link LineItem}s currently belonging to the {@link Order}.
     *
     * @return
     */
    public Set<LineItem> getLineItems() {
        return Collections.unmodifiableSet(lineItems);
    }

    /**
     * Returns the total of the {@link Order}.
     *
     * @return
     */
    public BigDecimal getTotal() {

        BigDecimal total = BigDecimal.ZERO;

        for (LineItem item : lineItems) {
            total = total.add(item.getTotal());
        }

        return total;
    }
}