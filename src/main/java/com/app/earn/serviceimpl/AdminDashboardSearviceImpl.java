package com.app.earn.serviceimpl;

import java.util.Date;
import java.util.List;

import com.app.earn.dao.AdminDashboardDao;
import com.app.earn.exception.ValidationException;
import com.app.earn.pojo.Task;
import com.app.earn.pojo.TaskStats;
import com.app.earn.pojo.User;
import com.app.earn.service.AdminDashboardSearvice;

public class AdminDashboardSearviceImpl implements AdminDashboardSearvice {
	private AdminDashboardDao adminDashboardDao;

	public void setAdminDashboardDao(AdminDashboardDao adminDashboardDao) {
		this.adminDashboardDao = adminDashboardDao;
	}

	// admin

	// Admin

//    @Override
//    public List<User> getPartners(){
//
//        return adminDashboardDao.getPartners();
//    }

	@Override
	public List<User> getPartners(int page, int pageSize, String searchText, String searchType, String sortField,
			String sortOrder) {

		return adminDashboardDao.getPartners(page, pageSize, searchText, searchType, sortField, sortOrder);
	}

	@Override
	public int getTotalPartners(String searchText, String searchType) {

		return adminDashboardDao.getTotalPartners(searchText, searchType);
	}

//    @Override
//    public void approvePartner(Long id){
//
//        userDao.updateStatus(id,"ACTIVE");
//    }

	@Override
	public void approvePartner(Long id) {

		User user = adminDashboardDao.getUserById(id);

		if ("ACTIVE".equals(user.getStatus())) {
			return;
		}

		adminDashboardDao.updateStatus(id, "ACTIVE");

	}

	@Override
	public void blockPartner(Long id) {

		adminDashboardDao.updateStatus(id, "BLOCKED");
	}

	public List<User> getAllUsers() {

		return adminDashboardDao.getAllUsers();

	}
	// search

//    @Override
//    public List<User> searchPartnersStarts(String text){
//
//    return adminDashboardDao.searchPartnersStarts(text);
//
//    }
//
//
//    @Override
//    public List<User> searchPartnersContains(String text){
//
//    return adminDashboardDao.searchPartnersContains(text);
//
//    }

	@Override
	public int getTotalUsersByRole(String role) {

		return adminDashboardDao.getTotalUsersByRole(role);

	}

	// tasks related

	@Override
	public List<Task> getTasksByStatus(String status) {

		return adminDashboardDao.getTasksByStatus(status);

	}

	@Override
	public int getTotalTasks() {

		return adminDashboardDao.getTotalTasks();

	}

	@Override
	public void approveTask(Task task, Long adminId) {

		if (task.getReward() == null) {
			throw new ValidationException("reward", "Reward is required to approve task");
		}

		if (task.getReward() <= 0) {
			throw new ValidationException("reward", "Reward must be greater than 0");
		}

		if (task.getRejectionReason() != null && !task.getRejectionReason().trim().isEmpty()) {
			throw new ValidationException("reason", "Remove rejection reason before approving");
		}

		if (!"DRAFT".equals(task.getStatus())) {
			throw new ValidationException("reward", "Only draft tasks can be approved");
		}

		if (task.getSubmissionLimit() == null) {
			throw new ValidationException("submissionLimit", "Submission limit is required");
		}

		if (task.getSubmissionLimit() <= 0) {
			throw new ValidationException("submissionLimit", "Submission limit must be greater than 0");
		}

		task.setStatus("LIVE");
		task.setApprovedBy(adminId);
		task.setApprovedAt(new Date());

		adminDashboardDao.updateTask(task);
	}

	// reject reason
	public void rejectTask(Task task, Long adminId) {

		if (task.getRejectionReason() == null || task.getRejectionReason().trim().isEmpty()) {
			throw new ValidationException("reason", "Rejection reason is required");
		}

		if (task.getReward() != null) {
			throw new ValidationException("reward", "Reward must be empty when rejecting");
		}

		if (!"DRAFT".equals(task.getStatus())) {
			throw new ValidationException("reason", "Only draft tasks can be rejected");
		}

		task.setStatus("REJECTED");
		task.setApprovedBy(adminId);
		task.setApprovedAt(new Date());

		adminDashboardDao.updateTask(task);
	}

	//

	public Task getNextPendingTask(Long currentTaskId) {

		return adminDashboardDao.getNextPendingTask(currentTaskId);

	}

	// users

	@Override
	public List<User> getUsers(int page, int pageSize, String searchText, String searchType, String sortField,
			String sortOrder) {

		return adminDashboardDao.getUsers(page, pageSize, searchText, searchType, sortField, sortOrder);
	}

	@Override
	public int getTotalUsers(String searchText, String searchType) {

		return adminDashboardDao.getTotalUsers(searchText, searchType);
	}

	@Override
	public void blockUser(Long id) {

		adminDashboardDao.blockUser(id);

	}

	@Override
	public void unblockUser(Long id) {

		adminDashboardDao.unblockUser(id);

	}

	/*
	 * --------------------------------- ADMIN CAMPAIGN ANALYTICS
	 * ---------------------------------
	 */

	@Override
	public List<TaskStats> getAllCampaignStats(int page, int pageSize, String searchText, String searchType,
			String sortField, String sortOrder) {

		return adminDashboardDao.getAllCampaignStats(page, pageSize, searchText, searchType, sortField, sortOrder);
	}

	@Override
	public int getTotalCampaignStats(String searchText, String searchType) {

		return adminDashboardDao.getTotalCampaignStats(searchText, searchType);
	}

	@Override
	public TaskStats getCampaignDetails(Long taskId) {

		return adminDashboardDao.getCampaignDetails(taskId);
	}

	@Override
	public void updateCampaignLimit(Long taskId, Integer newLimit) {

		TaskStats stats = adminDashboardDao.getCampaignDetails(taskId);

		int active = stats.getAccepted() + stats.getPending();
		if(newLimit <= 0){

			throw new RuntimeException(
			"Limit must be greater than zero"
			);
			}

		if (newLimit < active) {

			throw new ValidationException("limit", "Limit cannot be below active submissions");
		}

		adminDashboardDao.updateCampaignLimit(taskId, newLimit);

		/* AUTO REOPEN */

		if (active < newLimit && "CLOSED".equals(stats.getStatus())) {

			adminDashboardDao.updateCampaignStatus(taskId, "LIVE");
		}
	}

	@Override
	public void closeCampaign(Long taskId) {

		adminDashboardDao.updateCampaignStatus(taskId, "CLOSED");
	}

	@Override
	public void reopenCampaign(Long taskId) {

		adminDashboardDao.updateCampaignStatus(taskId, "LIVE");
	}
}
