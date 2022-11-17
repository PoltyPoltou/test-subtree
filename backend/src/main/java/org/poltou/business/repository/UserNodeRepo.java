package org.poltou.business.repository;

import org.poltou.business.opening.user.UserChessNode;
import org.springframework.data.repository.CrudRepository;

public interface UserNodeRepo extends CrudRepository<UserChessNode, Long> {

}
