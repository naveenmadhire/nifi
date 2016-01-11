package org.apache.nifi.util;

import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.*;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.util.StandardValidators;
import org.junit.Test;

import java.util.*;

public class MockProcessorTestRelationship {

    @Test
    public void testProcessor() {
        TestRunners.newTestRunner(SimpleProcessor.class).run();
    }

    protected static class SimpleProcessor extends AbstractProcessor {

        static final PropertyDescriptor DEFAULTED_PROP = new PropertyDescriptor.Builder()
                .name("defaultedProp")
                .description("A Sample Default property")
                .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
                .defaultValue("default-value")
                .build();

        static final Relationship REL_SUCCESS = new Relationship.Builder()
                .name("success")
                .build();

        static final Relationship REL_FAILURE = new Relationship.Builder()
                .name("Failure")
                .build();

        public List<PropertyDescriptor> properties;
        public Set<Relationship> relationships = Collections.singleton(REL_SUCCESS);

        @Override
        public List<PropertyDescriptor> getSupportedPropertyDescriptors() {
            return properties;
        }

        @Override
        public Set<Relationship> getRelationships() {
            return relationships;
        }

        @Override
        public void init(final ProcessorInitializationContext context) {
            // relationships
            final Set<Relationship> procRels = new HashSet<>();
            procRels.add(REL_SUCCESS);
            relationships = Collections.unmodifiableSet(procRels);

            // descriptors
            final List<PropertyDescriptor> supDescriptors = new ArrayList<>();
            supDescriptors.add(DEFAULTED_PROP);
            properties = Collections.unmodifiableList(supDescriptors);
        }


        @Override
        public void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {
            final FlowFile file = session.create();

            //Using the correct relationship
            //session.transfer(file, REL_SUCCESS);

            //Using the incorrect relationship
            session.transfer(file, REL_FAILURE);


        }
    }
}


