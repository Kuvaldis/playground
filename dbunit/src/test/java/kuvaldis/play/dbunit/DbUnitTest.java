package kuvaldis.play.dbunit;

import org.dbunit.Assertion;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.assertion.DiffCollectingFailureHandler;
import org.dbunit.assertion.Difference;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mssql.InsertIdentityOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DbUnitTest {

    private static final String URL = "jdbc:h2:mem:dbunit";

    private JdbcDatabaseTester tester;

    @Before
    public void setUp() throws Exception {
        final Connection connection = DriverManager.getConnection(URL, "sa", "");
        this.tester = new JdbcDatabaseTester("org.h2.Driver", URL, "sa", "");
        connection.createStatement().execute("CREATE TABLE person (name VARCHAR (255))");
    }

    @After
    public void tearDown() throws Exception {
        final Connection connection = DriverManager.getConnection(URL, "sa", "");
        this.tester = new JdbcDatabaseTester("org.h2.Driver", URL, "sa", "");
        connection.createStatement().execute("DROP TABLE person");
    }

    @Test
    public void testAddFromFile() throws Exception {
        setDataSet("dbUnitAddFromFile.xml");
        final Connection connection = DriverManager.getConnection(URL, "sa", "");
        final ResultSet countRS = connection.createStatement().executeQuery("SELECT count(*) FROM person");
        countRS.first();
        final long count = countRS.getLong(1);
        assertEquals(1L, count);
        final ResultSet resultSet = connection.createStatement()
                .executeQuery("SELECT name FROM person");
        resultSet.first();
        final String billy = resultSet.getString("name");
        assertEquals("mr. bean", billy);
    }

    @Test
    public void testVerifyFromFile() throws Exception {
        final Connection connection = DriverManager.getConnection(URL, "sa", "");
        connection.createStatement().executeUpdate("INSERT INTO person (name) VALUES ('ashley')");

        final IDataSet actualDataSet = tester.getConnection().createDataSet();
        final ITable actualTable = actualDataSet.getTable("person");

        final IDataSet expectedDataSet = createDataSet("dbUnitVerifyFromFile.xml");
        final ITable expectedTable = expectedDataSet.getTable("person");

        Assertion.assertEquals(expectedTable, actualTable);
    }

    @Test
    public void testDifference() throws Exception {
        final Connection connection = DriverManager.getConnection(URL, "sa", "");
        connection.createStatement().executeUpdate("INSERT INTO person (name) VALUES ('potato')");

        final IDataSet actualDataSet = tester.getConnection().createDataSet();
        final ITable actualTable = actualDataSet.getTable("person");

        final IDataSet expectedDataSet = createDataSet("dbUnitDifference.xml");
        final ITable expectedTable = expectedDataSet.getTable("person");

        final DiffCollectingFailureHandler diffHandler = new DiffCollectingFailureHandler();
        Assertion.assertEquals(expectedTable, actualTable, diffHandler);
        final List diffList = diffHandler.getDiffList();
        final Difference difference = (Difference) diffList.get(0);
        assertEquals("potato", difference.getActualValue());
        assertEquals("potahto", difference.getExpectedValue());
    }

    private void setDataSet(final String dataFile) throws Exception {
        final IDataSet dataSet = createDataSet(dataFile);
        tester.setDataSet(dataSet);
        tester.setSetUpOperation(InsertIdentityOperation.CLEAN_INSERT);
        tester.onSetup();
    }

    private IDataSet createDataSet(final String dataFile) throws DataSetException {
        return new FlatXmlDataSetBuilder()
                .build(DbUnitTest.class.getClassLoader()
                        .getResourceAsStream(dataFile));
    }
}
