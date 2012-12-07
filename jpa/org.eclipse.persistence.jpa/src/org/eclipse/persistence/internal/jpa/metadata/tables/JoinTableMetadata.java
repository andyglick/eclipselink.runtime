/*******************************************************************************
 * Copyright (c) 1998, 2012 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Oracle - initial API and implementation from Oracle TopLink
 *     05/16/2008-1.0M8 Guy Pelletier 
 *       - 218084: Implement metadata merging functionality between mapping file
 *     02/06/2009-2.0 Guy Pelletier 
 *       - 248293: JPA 2.0 Element Collections (part 2)
 *     03/24/2011-2.3 Guy Pelletier 
 *       - 337323: Multi-tenant with shared schema support (part 1)
 *     11/19/2012-2.5 Guy Pelletier 
 *       - 389090: JPA 2.1 DDL Generation Support (foreign key metadata support)
 *     12/07/2012-2.5 Guy Pelletier 
 *       - 389090: JPA 2.1 DDL Generation Support (foreign key metadata support)
 ******************************************************************************/  
package org.eclipse.persistence.internal.jpa.metadata.tables;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.persistence.internal.jpa.metadata.MetadataLogger;
import org.eclipse.persistence.internal.jpa.metadata.accessors.MetadataAccessor;
import org.eclipse.persistence.internal.jpa.metadata.accessors.objects.MetadataAccessibleObject;
import org.eclipse.persistence.internal.jpa.metadata.accessors.objects.MetadataAnnotation;
import org.eclipse.persistence.internal.jpa.metadata.columns.ForeignKeyMetadata;
import org.eclipse.persistence.internal.jpa.metadata.columns.JoinColumnMetadata;
import org.eclipse.persistence.internal.jpa.metadata.xml.XMLEntityMappings;

/**
 * INTERNAL:
 * Object to hold onto join table metadata in a EclipseLink database table.
 * 
 * Key notes:
 * - any metadata mapped from XML to this class must be compared in the
 *   equals method.
 * - when loading from annotations, the constructor accepts the metadata
 *   accessor this metadata was loaded from. Used it to look up any 
 *   'companion' annotation needed for processing.
 * - methods should be preserved in alphabetical order.
 * 
 * @author Guy Pelletier
 * @since TopLink EJB 3.0 Reference Implementation
 */
public class JoinTableMetadata extends RelationalTableMetadata {
    // NOTE: The foreign key metadata is currently not mapped in the join-column 
    // XML element, rather it is mapped as a separate element in a sequence with 
    // it. Therefore, this element is only populated through annotation 
    // processing right now, BUT this should be made available from our 
    // eclipselink-orm.xml and therefore maintaining a better annoation/xml
    // mirror which was unfortunately not followed with JPA 2.1.
    private ForeignKeyMetadata m_inverseForeignKey;
    
    private List<JoinColumnMetadata> m_inverseJoinColumns = new ArrayList<JoinColumnMetadata>();
    
    /**
     * INTERNAL:
     * Used for XML loading.
     */
    public JoinTableMetadata() {
        super("<join-table>");
    }
    
    /**
     * INTERNAL:
     * Used for defaulting.
     */
    public JoinTableMetadata(MetadataAccessor accessor) {
        super(null, accessor);
    }
    
    /**
     * INTERNAL:
     * Used for annotation loading.
     */
    public JoinTableMetadata(MetadataAnnotation joinTable, MetadataAccessor accessor) {
        super(joinTable, accessor);
        
        if (joinTable != null) {
            for (Object inverseJoinColumn : joinTable.getAttributeArray("inverseJoinColumns")) {
                JoinColumnMetadata inverseJoinColumnMetadata = new JoinColumnMetadata((MetadataAnnotation) inverseJoinColumn, accessor);
                m_inverseJoinColumns.add(inverseJoinColumnMetadata);
                    
                // Set the inverse foreign key metadata from the inverse join column if available.
                if (inverseJoinColumnMetadata.hasForeignKey()) {
                    setInverseForeignKey(inverseJoinColumnMetadata.getForeignKey());
                }
            }
        }
    }
    
    /**
     * INTERNAL:
     */
    @Override
    public boolean equals(Object objectToCompare) {
        if (super.equals(objectToCompare) && objectToCompare instanceof JoinTableMetadata) {
            JoinTableMetadata joinTable = (JoinTableMetadata) objectToCompare;
            
            if (! valuesMatch(m_inverseJoinColumns, getInverseJoinColumns())) {
                return false;
            }
            
            return valuesMatch(m_inverseForeignKey, joinTable.getInverseForeignKey());
        }
        
        return false;
    }
    
    /**
     * INTERNAL:
     */
    @Override
    public String getCatalogContext() {
        return MetadataLogger.JOIN_TABLE_CATALOG;
    }
    
    /**
     * INTERNAL:
     * Used for OX mapping.
     */
    public ForeignKeyMetadata getInverseForeignKey() {
        return m_inverseForeignKey;
    }
    
    /**
     * INTERNAL:
     * Used for OX mapping.
     */
    public List<JoinColumnMetadata> getInverseJoinColumns() {
        return m_inverseJoinColumns;
    }
    
    /**
     * INTERNAL:
     */
    @Override
    public String getNameContext() {
        return MetadataLogger.JOIN_TABLE_NAME;
    }
    
    /**
     * INTERNAL:
     */
    @Override
    public String getSchemaContext() {
        return MetadataLogger.JOIN_TABLE_SCHEMA;
    }
    
    /**
     * INTERNAL:
     */
    @Override
    public void initXMLObject(MetadataAccessibleObject accessibleObject, XMLEntityMappings entityMappings) {
        super.initXMLObject(accessibleObject, entityMappings);

        // Initialize single objects.
        initXMLObject(m_inverseForeignKey, accessibleObject);
        
        // Initialize lists of ORMetadata objects.
        initXMLObjects(m_inverseJoinColumns, accessibleObject);
    }

    /**
     * INTERNAL:
     * Process any foreign key specification for this table.
     */
    @Override
    public void processForeignKey() {
        super.processForeignKey();
        
        if (m_inverseForeignKey != null) {
            getDatabaseTable().addForeignKeyConstraint(m_inverseForeignKey.process());
        }
    }
    
    /**
     * INTERNAL:
     * Used for OX mapping.
     */
    public void setInverseForeignKey(ForeignKeyMetadata inverseForeignKey) {
        m_inverseForeignKey = inverseForeignKey;
    }
    
    /**
     * INTERNAL:
     * Used for OX mapping.
     */
    public void setInverseJoinColumns(List<JoinColumnMetadata> inverseJoinColumns) {
        m_inverseJoinColumns = inverseJoinColumns;
    }
}
