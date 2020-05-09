import com.mockobjects.sql.MockMultiRowResultSet;
import org.junit.Before;
import org.junit.Test;
import pl.pjm77.DAO.LastSolutionDAO;
import pl.pjm77.DAO.RealLastSolutionDAO;
import pl.pjm77.model.LastSolution;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Timestamp.valueOf;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class RealLastSolutionDAOTests {

    private DataSource dataSource;
    private Connection connection;
    private PreparedStatement statement;
    String[] columns = new String[]{"title", "name", "modified", "id"};

    @Before
    public void setup() throws Exception {
        dataSource = createMock(DataSource.class);
        connection = createMock(Connection.class);
        expect(dataSource.getConnection()).andReturn(connection);
        statement = createMock(PreparedStatement.class);
    }

    @Test
    public void testLoadMostRecentSolutions() throws Exception {
        String sqlQuery = "SELECT exercise.title, user.username, IF(solution.updated > " +
          "solution.created, solution.updated, solution.created), solution.id FROM solution " +
          "LEFT JOIN exercise ON solution.exercise_id=exercise.id LEFT JOIN user ON " +
          "solution.user_id=user.id ORDER BY IF(updated > created, updated, created)" +
          " DESC LIMIT ?;";
        expect(connection.prepareStatement(sqlQuery)).andReturn(statement);
        statement.setLong(1, 3);

        MockMultiRowResultSet resultSet = new MockMultiRowResultSet();
        resultSet.setupColumnNames(columns);
        List<LastSolution> expectedLastSolutions = createManyLastSolutions();
        resultSet.setupRows(lastSolutionlistTo2dArray(expectedLastSolutions));
        expect(statement.executeQuery()).andReturn(resultSet);

        resultSet.setExpectedCloseCalls(1);
        statement.close();
        connection.close();

        replay(dataSource, connection, statement);

        LastSolutionDAO lastSolutionDAO = new RealLastSolutionDAO(dataSource);
        List<LastSolution> result = lastSolutionDAO.loadMostRecentSolutions(3);
        assertEquals(expectedLastSolutions.toString(), result.toString());
        verify(dataSource, connection, statement);
        resultSet.verify();
    }

    @Test
    public void testLoadMostRecentSolutionsByUserId() throws Exception {
        String sqlQuery = "SELECT exercise.title, user.username, IF(solution.updated > " +
          "solution.created, solution.updated, solution.created), solution.id FROM solution " +
          "LEFT JOIN exercise ON solution.exercise_id=exercise.id LEFT JOIN user ON " +
          "solution.user_id=user.id  WHERE solution.user_id=? ORDER BY IF(updated > created, " +
          "updated, created) DESC;";
        expect(connection.prepareStatement(sqlQuery)).andReturn(statement);
        statement.setLong(1, 2);

        MockMultiRowResultSet resultSet = new MockMultiRowResultSet();
        resultSet.setupColumnNames(columns);
        List<LastSolution> expectedLastSolutions = createManyLastSolutionsByUserId(2);
        resultSet.setupRows(lastSolutionlistTo2dArray(expectedLastSolutions));
        expect(statement.executeQuery()).andReturn(resultSet);

        resultSet.setExpectedCloseCalls(1);
        statement.close();
        connection.close();

        replay(dataSource, connection, statement);

        LastSolutionDAO lastSolutionDAO = new RealLastSolutionDAO(dataSource);
        List<LastSolution> result = lastSolutionDAO.loadMostRecentSolutionsByUserId(2);
        assertEquals(expectedLastSolutions.toString(), result.toString());
        System.out.println(expectedLastSolutions.toString());
        System.out.println(result.toString());
        verify(dataSource, connection, statement);
        resultSet.verify();
    }

    private List<LastSolution> createManyLastSolutions() {
        List<LastSolution> expectedLastSolutions = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            LastSolution lastSolution = new LastSolution("Test title " + i,
              "Test name " + i, valueOf("2020-04-20 23:25:23.0" + i));
            lastSolution.setId(i);
            expectedLastSolutions.add(lastSolution);
        }
        return expectedLastSolutions;
    }

    private List<LastSolution> createManyLastSolutionsByUserId(long id) {
        List<LastSolution> expectedLastSolutions = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            LastSolution lastSolution = new LastSolution("Test title " + i,
              "Test name " + i, valueOf("2020-04-20 23:25:23.0" + i));
            lastSolution.setId(id);
            expectedLastSolutions.add(lastSolution);
        }
        return expectedLastSolutions;
    }

    private Object[][] lastSolutionlistTo2dArray(List<LastSolution> lastSolutions) {
        Object[][] array = new Object[(lastSolutions.size())][6];
        for (int i = 0; i < array.length; i++) {
            LastSolution lastSolution = lastSolutions.get(i);
            array[i] = new Object[]{lastSolution.getTitle(),
              lastSolution.getName(), lastSolution.getModified(), lastSolution.getId()};
        }
        return array;
    }
}
