package com.app.earn.controller;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import com.app.earn.pojo.Task;
import com.app.earn.pojo.TaskSubmission;
import com.app.earn.pojo.User;
import com.app.earn.service.UserTaskService;

public class UserTaskController implements Serializable {

private static final long serialVersionUID = 1L;

/* =========================
   SERVICE LAYER
========================= */

private UserTaskService userTaskService;

/* =========================
   TASK DATA
========================= */

private List<Task> liveTasks;      // all live tasks
private Task selectedTask;         // task user clicked
private Long taskId;               // taskId from URL param

/* =========================
   FILE UPLOAD
========================= */

private Part proofFile;            // uploaded image
private String proofDescription;   // description written by user

/* =========================
   LINK VISIT FLAG
========================= */

private boolean linkVisited = false;

/* ======================================================
   LOAD LIVE TASKS
   ====================================================== */

public List<Task> getLiveTasks(){

    if(liveTasks == null){
        liveTasks = userTaskService.getLiveTasks();
    }

    return liveTasks;
}

/* ======================================================
   LOAD TASK DETAILS FROM URL
   ====================================================== */

public void loadTask(){

    for(Task t : getLiveTasks()){

      //  if(t.getId().equals(taskId)){
    	if(taskId != null && t.getId().equals(taskId)){
            selectedTask = t;
            break;
        }
    }
}

/* ======================================================
   USER CLICKED TASK LINK
   Unlock upload
   ====================================================== */

public void markLinkVisited(){

    linkVisited = true;
}

/* ======================================================
   CHECK IF USER ALREADY SUBMITTED
   ====================================================== */

public boolean isAlreadySubmitted(Long taskId){

    User user = (User) FacesContext
    .getCurrentInstance()
    .getExternalContext()
    .getSessionMap()
    .get("loggedUser");

    return userTaskService.hasUserSubmitted(user.getId(), taskId);
}

/* ======================================================
   SUBMIT PROOF
   Handles file upload + DB insert
   ====================================================== */

public void submitProof(){

try{

/* ------------------------------------------------------
   VALIDATE TASK LINK VISIT
------------------------------------------------------ */

if(!linkVisited){
throw new RuntimeException("Please visit task link first");
}

/* ------------------------------------------------------
   VALIDATE FILE EXISTENCE
------------------------------------------------------ */

if(proofFile == null || proofFile.getSize()==0){
throw new RuntimeException("Please upload screenshot");
}

/* ------------------------------------------------------
   FILE SIZE VALIDATION (2MB)
------------------------------------------------------ */

if(proofFile.getSize() > 2 * 1024 * 1024){
throw new RuntimeException("File must be under 2MB");
}

/* ------------------------------------------------------
   VALIDATE FILE EXTENSION
------------------------------------------------------ */

String originalFile = proofFile.getSubmittedFileName().toLowerCase();

/* prevent path traversal */
originalFile = new File(originalFile).getName();

if(!(originalFile.endsWith(".jpg") ||
     originalFile.endsWith(".jpeg") ||
     originalFile.endsWith(".png"))){

throw new RuntimeException("Only JPG, JPEG or PNG allowed");
}

/* ------------------------------------------------------
   GET CURRENT LOGGED USER
------------------------------------------------------ */

User user = (User) FacesContext
.getCurrentInstance()
.getExternalContext()
.getSessionMap()
.get("loggedUser");

/* ------------------------------------------------------
   CHECK DUPLICATE SUBMISSION
------------------------------------------------------ */

if(userTaskService.hasUserSubmitted(user.getId(), taskId)){
throw new RuntimeException("You already submitted this task");
}

/* ------------------------------------------------------
   GENERATE UNIQUE FILE NAME
------------------------------------------------------ */

//String extension =
//originalFile.substring(originalFile.lastIndexOf("."));
//
//String fileName =
//System.currentTimeMillis()
//+ "_"
//+ Math.abs(originalFile.hashCode())
//+ extension;



String extension =
originalFile.substring(originalFile.lastIndexOf("."));

String fileName =
taskId+user.getId()+ "_" + UUID.randomUUID().toString() + extension;

/* ------------------------------------------------------
   STORAGE LOCATION (OUTSIDE TOMCAT)
------------------------------------------------------ */

String uploadPath = "D:/earn_storage/taskProof/";

File folder = new File(uploadPath);

if(!folder.exists()){
folder.mkdirs();
}

/* ------------------------------------------------------
   SAVE FILE
------------------------------------------------------ */

proofFile.write(uploadPath + fileName);

/* ------------------------------------------------------
   CREATE SUBMISSION OBJECT
------------------------------------------------------ */

if(selectedTask == null){
throw new RuntimeException("Invalid task");
}

TaskSubmission sub = new TaskSubmission();

sub.setTaskId(taskId);
sub.setUserId(user.getId());
sub.setProofPath("taskProof/" + fileName);
sub.setProofDescription(proofDescription);

/* ------------------------------------------------------
   SAVE TO DATABASE
------------------------------------------------------ */

userTaskService.submitTaskProof(sub);

/* ------------------------------------------------------
   SUCCESS MESSAGE
------------------------------------------------------ */

FacesContext.getCurrentInstance().addMessage(
null,
new FacesMessage("Proof submitted successfully")
);

/* reset form */

linkVisited = false;
proofFile = null;
proofDescription = null;

}catch(Exception e){

FacesContext.getCurrentInstance().addMessage(
null,
new FacesMessage(
FacesMessage.SEVERITY_ERROR,
e.getMessage(),
null));

}

}

private User getLoggedUser(){
return (User) FacesContext
.getCurrentInstance()
.getExternalContext()
.getSessionMap()
.get("loggedUser");
}

//new tasks
public List<Task> getNewTasks(){

return userTaskService.getNewTasks(getLoggedUser().getId());
}
//pending tasks

public List<TaskSubmission> getPendingTasks(){
return userTaskService.getSubmissionsByStatus(getLoggedUser().getId(),"PENDING");
}

// Approved by admin tasks
public List<TaskSubmission> getApprovedTasks(){
return userTaskService.getSubmissionsByStatus(getLoggedUser().getId(),"ACCEPTED");
}

// Rejected Tasks

public List<TaskSubmission> getRejectedTasks(){
return userTaskService.getSubmissionsByStatus(getLoggedUser().getId(),"REJECTED");
}


private TaskSubmission selectedSubmission;

public void loadSubmissionDetails(){

    User user = getLoggedUser();

    selectedSubmission = userTaskService
        .getSubmissionByUserAndTask(user.getId(), taskId);

    // also load task
    loadTask();
}

public TaskSubmission getSelectedSubmission(){
    return selectedSubmission;
}

/* ======================================================
   GETTERS / SETTERS
   ====================================================== */

public Part getProofFile() {
return proofFile;
}

public void setProofFile(Part proofFile) {
this.proofFile = proofFile;
}

public String getProofDescription() {
return proofDescription;
}

public void setProofDescription(String proofDescription) {
this.proofDescription = proofDescription;
}

public boolean isLinkVisited() {
return linkVisited;
}

public Long getTaskId(){
return taskId;
}

public void setTaskId(Long taskId){
this.taskId = taskId;
}

public Task getSelectedTask(){
return selectedTask;
}

public void setSelectedTask(Task selectedTask){
this.selectedTask = selectedTask;
}

public UserTaskService getUserTaskService(){
return userTaskService;
}

public void setUserTaskService(UserTaskService userTaskService){
this.userTaskService = userTaskService;
}

}