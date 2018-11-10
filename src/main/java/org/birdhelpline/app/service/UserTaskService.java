package org.birdhelpline.app.service;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class UserTaskService {
/*

	private final UserTaskRepository userTaskRepository;

	public UserTaskService(UserTaskRepository userTaskRepository) {
		this.userTaskRepository = userTaskRepository;
	}
	
	public List<UserTask> findAll(){
		List<UserTask> user_tasks = new ArrayList<>();
		user_tasks = userTaskRepository.findAll();
		return user_tasks;
	}
	
//	public UserTask findUserTask(int id){
//		return userTaskRepository.findOne(id);
//	}
//
	public void save(UserTask user_task){
		userTaskRepository.save(user_task);
	}
	
//	public void delete(int id){
//		userTaskRepository.delete(id);
//
//	}

	public List<UserTask> findByTask(Task task) {
		return userTaskRepository.findByTask(task);
	}
	public List<UserTask> findByUser(User user) {
		return userTaskRepository.findByUser(user);
	}

    public Object findUserTask(int id) {
	    return null;
    }

    public void delete(int id) {
    }
*/
}
