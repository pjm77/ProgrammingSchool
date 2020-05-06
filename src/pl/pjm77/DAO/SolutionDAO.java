package pl.pjm77.DAO;

import pl.pjm77.model.Solution;

import java.util.List;

public interface SolutionDAO {

    void saveSolutionToDB(Solution solution);
    Solution loadSolutionById(long id);
    void deleteSolution(Solution solution);
    List<Solution> loadAllSolutions();
    List<Solution> loadAllSolutionsByUserId(long id);
}