package co.brainz.workflow.process

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository

@Repository
public interface ProcessMstRepository : JpaRepository<ProcessMst, String> {
}
