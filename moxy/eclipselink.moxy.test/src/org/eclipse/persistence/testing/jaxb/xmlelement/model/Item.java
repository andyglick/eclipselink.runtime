/*******************************************************************************
 * Copyright (c) 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Blaise Doughan - 2.4 - initial implementation
 ******************************************************************************/
package org.eclipse.persistence.testing.jaxb.xmlelement.model;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlSchemaType;

public class Item {

   @XmlSchemaType(name="String")
   private int id;
   private String[] description;
   private BigDecimal cost;
   private BigDecimal price;

   public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getDescription() {
        return description;
    }

    public void setDescription(String[] description) {
        this.description = description;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean equals(Object obj) {
        Item item;
        try {
            item = (Item) obj;
        } catch (ClassCastException cce) {
            return false;
        }
        if(!Arrays.equals(description, item.description)){
            return false;
        }
        return id == item.id && price.equals(item.price) && cost.equals(item.cost);
    }

}