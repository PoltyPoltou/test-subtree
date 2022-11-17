package org.poltou.business.repository;

import org.poltou.business.opening.theory.TheoryOpening;
import org.springframework.data.repository.CrudRepository;

public interface TheoryOpeningRepo extends CrudRepository<TheoryOpening, Long> {
    public TheoryOpening findByName(String name);
}
