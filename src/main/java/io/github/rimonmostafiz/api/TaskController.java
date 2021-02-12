package io.github.rimonmostafiz.api;

import io.github.rimonmostafiz.component.exception.ValidationException;
import io.github.rimonmostafiz.model.common.RestResponse;
import io.github.rimonmostafiz.model.dto.TaskModel;
import io.github.rimonmostafiz.model.entity.common.TaskStatus;
import io.github.rimonmostafiz.model.request.TaskCreateRequest;
import io.github.rimonmostafiz.model.request.TaskUpdateRequest;
import io.github.rimonmostafiz.model.response.TaskResponse;
import io.github.rimonmostafiz.service.task.TaskService;
import io.github.rimonmostafiz.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * @author Rimon Mostafiz
 */
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/task")
    public ResponseEntity<RestResponse<TaskResponse>> createTask(HttpServletRequest request,
                                                                 @RequestBody TaskCreateRequest taskCreateRequest) {
        //TODO: need to update after enabling spring security
        String requestUser = "user";
        //String requestUser = Utils.getUserNameFromRequest(request);
        TaskModel task = taskService.createTask(taskCreateRequest, requestUser);
        TaskResponse taskResponse = TaskResponse.of(task);
        return ResponseUtils.buildSuccessResponse(HttpStatus.CREATED, taskResponse);
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<RestResponse<TaskResponse>> updateTask(@PathVariable Long id,
                                                               @RequestBody TaskUpdateRequest taskUpdateRequest) {
        //TODO: need to update after enabling spring security
        String requestUser = "user";
        //String requestUser = Utils.getUserNameFromRequest(request);
        TaskModel task = taskService.updateTask(id, taskUpdateRequest, requestUser);
        TaskResponse taskResponse = TaskResponse.of(task);
        return ResponseUtils.buildSuccessResponse(HttpStatus.OK, taskResponse);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<RestResponse<TaskResponse>> getTask(@PathVariable Long id) {
        TaskModel task = taskService.getTask(id);
        TaskResponse taskResponse = TaskResponse.of(task);
        return ResponseUtils.buildSuccessResponse(HttpStatus.OK, taskResponse);
    }

    @GetMapping("/task/search/{projectId}")
    public ResponseEntity<RestResponse<TaskResponse>> searchAllByProject(@PathVariable Long projectId) {
        List<TaskModel> tasks = taskService.getAllTaskByProject(projectId);
        TaskResponse taskResponse = TaskResponse.of(tasks);
        return ResponseUtils.buildSuccessResponse(HttpStatus.OK, taskResponse);
    }

    @GetMapping("/task/search/{userId}")
    public ResponseEntity<RestResponse<TaskResponse>> searchAllByUser(@PathVariable Long userId) {
        List<TaskModel> tasks = taskService.getAllTaskByUser(userId);
        TaskResponse taskResponse = TaskResponse.of(tasks);
        return ResponseUtils.buildSuccessResponse(HttpStatus.OK, taskResponse);
    }

    @GetMapping("/task/search/expired")
    public ResponseEntity<RestResponse<TaskResponse>> getExpiredTask() {
        List<TaskModel> expiredTasks = taskService.getAllExpiredTask();
        TaskResponse taskResponse = TaskResponse.of(expiredTasks);
        return ResponseUtils.buildSuccessResponse(HttpStatus.OK, taskResponse);
    }

    @GetMapping("/task/search/status/{status}")
    public ResponseEntity<RestResponse<TaskResponse>> searchAllByStatus(@PathVariable String status) {
        Optional<TaskStatus> taskStatus = TaskStatus.getStatus(status);
        List<TaskModel> tasks = taskStatus.map(taskService::getAllTaskByStatus)
                .orElseThrow(() -> new ValidationException(HttpStatus.BAD_REQUEST, "status", "Invalid Status"));
        TaskResponse taskResponse = TaskResponse.of(tasks);
        return ResponseUtils.buildSuccessResponse(HttpStatus.OK, taskResponse);
    }
}
