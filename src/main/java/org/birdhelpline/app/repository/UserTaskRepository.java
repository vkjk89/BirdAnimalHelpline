package org.birdhelpline.app.repository;

import org.birdhelpline.app.model.Task;
import org.birdhelpline.app.model.User;
import org.birdhelpline.app.model.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

//@Repository("userTaskRepository")
public interface UserTaskRepository {//extends JpaRepository<UserTask, Integer> {
    //List<UserTask> findByTask (Task task);
    //List<UserTask> findByUser (User user);
}
