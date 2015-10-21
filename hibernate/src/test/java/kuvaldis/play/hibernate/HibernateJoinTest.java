package kuvaldis.play.hibernate;

import org.h2.Driver;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.sql.JoinType;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.transform.Transformers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

public class HibernateJoinTest {
    private static final String TEST_CATALOG = "test";

    @Rule
    public TestName name = new TestName();

    @Entity
    @Table(name = "bill", catalog = TEST_CATALOG)
    public static class Bill implements java.io.Serializable {
        private Integer id;
        private String label;
        private Set<BillEvent> billEvents = new HashSet<BillEvent>(0);

        public Bill() {
        }

        public Bill(String label) {
            this.label = label;
        }

        public Bill(String label, Set<BillEvent> billEvents) {
            this.label = label;
            this.billEvents = billEvents;
        }

        @Id
        @GeneratedValue(strategy = IDENTITY)
        @Column(name = "id", unique = true, nullable = false)
        public Integer getId() {
            return this.id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Column(name = "label", unique = true, nullable = false, length = 45)
        public String getLabel() {
            return this.label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "bill", cascade = {CascadeType.ALL})
        public Set<BillEvent> getBillEvents() {
            return this.billEvents;
        }

        public void setBillEvents(Set<BillEvent> billEvents) {
            this.billEvents = billEvents;
        }
    }

    @Entity
    @Table(name = "event", catalog = TEST_CATALOG)
    public static class Event implements java.io.Serializable {
        private Integer id;
        private Timestamp time;
        private Set<BillEvent> billEvents = new HashSet<>(0);

        public Event() {
        }

        public Event(Timestamp time) {
            this.time = time;
        }

        public Event(Timestamp time, Set<BillEvent> billEvents) {
            this.time = time;
            this.billEvents = billEvents;
        }

        @Id
        @GeneratedValue(strategy = IDENTITY)
        @Column(name = "id", unique = true, nullable = false)
        public Integer getId() {
            return this.id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Column(name = "time", nullable = false)
        public Timestamp getTime() {
            return this.time;
        }

        public void setTime(Timestamp time) {
            this.time = time;
        }

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", cascade = {CascadeType.ALL})
        public Set<BillEvent> getBillEvents() {
            return this.billEvents;
        }

        public void setBillEvents(Set<BillEvent> billEvents) {
            this.billEvents = billEvents;
        }
    }

    @Entity
    @Table(name = "billEvent", catalog = TEST_CATALOG, uniqueConstraints = @UniqueConstraint(columnNames = {"billId", "eventId"}))
    public static class BillEvent implements java.io.Serializable {

        private Integer id;
        private Bill bill;
        private Event event;
        private String note;

        public BillEvent() {
        }

        public BillEvent(Bill bill, Event event) {
            this.bill = bill;
            this.event = event;
        }

        public BillEvent(Bill bill, Event event, String note) {
            this.bill = bill;
            this.event = event;
            this.note = note;
        }

        @Id
        @GeneratedValue(strategy = IDENTITY)
        @Column(name = "id", unique = true, nullable = false)
        public Integer getId() {
            return this.id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
        @JoinColumn(name = "billId", nullable = false)
        public Bill getBill() {
            return this.bill;
        }

        public void setBill(Bill bill) {
            this.bill = bill;
        }

        @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
        @JoinColumn(name = "eventId", nullable = false)
        public Event getEvent() {
            return this.event;
        }

        public void setEvent(Event event) {
            this.event = event;
        }

        @Column(name = "note", unique = true, nullable = false, length = 120)
        public String getNote() {
            return this.note;
        }

        public void setNote(String note) {
            this.note = note;
        }
    }

    @Test
    public void testOuterJoin() {
        final SessionFactory sessionFactory = createSessionFactory();

        final String label = "B0001";
        final Timestamp ts = new Timestamp(System.currentTimeMillis());
        final Timestamp ts2 = new Timestamp(ts.getTime() + 1000);
        final String note1 = "First note";
        final String note2 = "Second note";

        final int billId;

        try (final Session session = sessionFactory.openSession();) {
            final Transaction tx = session.beginTransaction();

            final Bill bill = new Bill(label);
            session.save(bill);
            billId = bill.getId();

            final Event event1 = new Event(ts);
            session.save(event1);

            final Event event2 = new Event(ts2);
            session.save(event2);

            session.save(new BillEvent(bill, event1, note1));
            session.save(new BillEvent(bill, event2, note2));

            session.flush();
            tx.commit();
        }

        try (final Session session = sessionFactory.openSession()) {
            final Criteria criteria = session.createCriteria(Event.class, "event1")
                .createCriteria("event1.billEvents", "be1")
                .createCriteria("be1.bill", "bill1")
                .createCriteria("bill1.billEvents", "be2")
                .createCriteria("be2.event", "event2", JoinType.LEFT_OUTER_JOIN,
                        Restrictions.ltProperty("event1.time", "event2.time"))
                .add(Restrictions.eq("be1.id", billId))
                .add(Restrictions.isNull("event2.id"))
                .setProjection(Projections.projectionList()
                    .add(Projections.property("be1.event"), "event")
                    .add(Projections.property("be1.note"), "note"))
                .setResultTransformer(Transformers.aliasToBean(BillEvent.class));

//            final Criteria criteria = session.createCriteria(BillEvent.class, "be1")
//                    .createCriteria("be1.bill", "bill1")
//                    .createCriteria("be1.event", "event1")
//                    .createCriteria("bill1.billEvents", "be2")
//                    .createCriteria("be2.event", "event2", JoinType.LEFT_OUTER_JOIN,
//                            Restrictions.ltProperty("event1.time", "time"))
//                    .add(Restrictions.eq("be1.id", billId))
//                    .add(Restrictions.isNull("event2.id"));


            @SuppressWarnings("unchecked")
            final List<BillEvent> results = criteria.list();

            Assert.assertEquals(1, results.size());

            final BillEvent billEvent = results.get(0);
            Assert.assertEquals(note2, billEvent.getNote());
            Assert.assertEquals(ts2, billEvent.getEvent().getTime());
        }
    }

    private SessionFactory createSessionFactory() {
        final String dialectClassName = H2Dialect.class.getName();
        final Configuration config =
                new Configuration()
                        .addAnnotatedClass(Bill.class)
                        .addAnnotatedClass(Event.class)
                        .addAnnotatedClass(BillEvent.class);

        final String dbName = name.getMethodName();

        config.setProperty(Environment.DIALECT, dialectClassName);
        config.setProperty(Environment.DRIVER, Driver.class.getName());
        config.setProperty(Environment.URL, "jdbc:h2:mem:" + dbName + ";DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS TEST\\; SET SCHEMA TEST");
        config.setProperty(Environment.USER, "SA");
        config.setProperty(Environment.PASS, "");
        config.setProperty(Environment.SHOW_SQL, "true");
        config.setProperty(Environment.FORMAT_SQL, "true");

        final StandardServiceRegistry serviceRegistry = config.getStandardServiceRegistryBuilder().applySettings(config.getProperties()).build();

        final MetadataSources sources =
                new MetadataSources(serviceRegistry)
                        .addAnnotatedClass(Bill.class)
                        .addAnnotatedClass(Event.class)
                        .addAnnotatedClass(BillEvent.class);

        final Metadata metadata = sources.buildMetadata();

        final SchemaExport export = new SchemaExport((MetadataImplementor) metadata);
        export.create(false, true);

        final SessionFactory sessionFactory = config.buildSessionFactory();
        return sessionFactory;
    }
}