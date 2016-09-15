package kuvaldis.play.java;

import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class JAXBCyclicReferences {

    @XmlRootElement
    private static class Department {
        @XmlAttribute
        private String name;
        @XmlElement(name = "employee")
        private List<Employee> employees;
    }

    private static class Employee {
        @XmlTransient
        Department department;  // parent pointer
        @XmlAttribute
        String name;

        public void afterUnmarshal(Unmarshaller u, Object parent) {
            this.department = (Department) parent;
        }
    }

    @Test
    public void testTransient() throws Exception {
        final Department department = new Department();
        final Employee employee1 = new Employee();
        employee1.department = department;
        employee1.name = "employee1";
        final Employee employee2 = new Employee();
        employee2.department = department;
        employee2.name = "employee2";
        department.employees = Arrays.asList(employee1, employee2);
        department.name = "dept";

        final StringWriter writer = new StringWriter();
        final JAXBContext context = JAXBContext.newInstance(Department.class);
        final Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(department, writer);

        final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><department name=\"dept\"><employee name=\"employee1\"/><employee name=\"employee2\"/></department>";
        assertEquals(expected, writer.toString());

        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final Department unmarshalledDepartment = (Department) unmarshaller.unmarshal(new StringReader(expected));
        assertSame(unmarshalledDepartment, unmarshalledDepartment.employees.get(0).department);
    }

    @XmlRootElement
    private static class Root {
        @XmlElement(name = "foo")
        private List<Foo> foos;
        @XmlElement(name = "bar")
        private List<Bar> bars;
    }

    private static class Foo {

        @XmlAttribute
        @XmlID
        private String id;

        @XmlAttribute
        @XmlIDREF
        private Bar bar;
    }

    private static class Bar {
        @XmlAttribute
        @XmlID
        private String id;

        @XmlAttribute
        @XmlIDREF
        private Foo foo;
    }

    @Test
    public void testXmlIdRef() throws Exception {
        final Foo foo1 = new Foo();
        foo1.id = "foo1";
        final Bar bar1 = new Bar();
        bar1.id = "bar1";
        foo1.bar = bar1;
        bar1.foo = foo1;

        final Foo foo2 = new Foo();
        foo2.id = "foo2";
        final Bar bar2 = new Bar();
        bar2.id = "bar2";
        foo2.bar = bar2;
        bar2.foo = foo2;

        final Root root = new Root();
        root.foos = Arrays.asList(foo1, foo2);
        root.bars = Arrays.asList(bar1, bar2);

        final StringWriter writer = new StringWriter();
        final JAXBContext context = JAXBContext.newInstance(Root.class);
        final Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(root, writer);

        // @formatter:off
        final String expected = "" +
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<root>" +
                    "<foo id=\"foo1\" bar=\"bar1\"/>" +
                    "<foo id=\"foo2\" bar=\"bar2\"/>" +
                    "<bar id=\"bar1\" foo=\"foo1\"/>" +
                    "<bar id=\"bar2\" foo=\"foo2\"/>" +
                "</root>";
        // @formatter:on
        assertEquals(expected, writer.toString());

        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final Root unmarshalledRoot = (Root) unmarshaller.unmarshal(new StringReader(expected));
        assertSame(unmarshalledRoot.foos.get(0), unmarshalledRoot.bars.get(0).foo);
        assertSame(unmarshalledRoot.bars.get(0), unmarshalledRoot.foos.get(0).bar);
        assertSame(unmarshalledRoot.foos.get(1), unmarshalledRoot.bars.get(1).foo);
        assertSame(unmarshalledRoot.bars.get(1), unmarshalledRoot.foos.get(1).bar);
    }
}
