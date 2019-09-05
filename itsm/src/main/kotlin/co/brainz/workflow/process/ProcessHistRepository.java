package co.brainz.workflow.process;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessHistRepository extends JpaRepository<ProcessHist, String> {
    List<ProcessHist> findByProcId(String procId);
    List<ProcessHist> findAll();

}
