package io.github.rimonmostafiz.api;

import io.github.rimonmostafiz.model.common.RestResponse;
import io.github.rimonmostafiz.model.dto.TaskModel;
import io.github.rimonmostafiz.model.entity.common.TaskStatus;
import io.github.rimonmostafiz.model.request.TaskCreateRequest;
import io.github.rimonmostafiz.model.request.TaskUpdateRequest;
import io.github.rimonmostafiz.model.response.TaskResponse;
import io.github.rimonmostafiz.service.task.TaskService;
import io.github.rimonmostafiz.utils.ResponseUtils;
import io.github.rimonmostafiz.utils.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Rimon Mostafiz
 */
@Slf4j
@RestController
@Api(tags = "Task")
@RequestMapping("/v1")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/task")
    public ResponseEntity<RestResponse<TaskResponse>> createTask(HttpServletRequest request,
                                                                 @RequestBody @Valid TaskCreateRequest taskCreateRequest) {
        String requestUser = Utils.getUserNameFromRequest(request);
        TaskModel task = taskService.createTask(taskCreateRequest, requestUser);
        TaskResponse taskResponse = TaskResponse.of(task);
        return ResponseUtils.buildSuccessResponse(HttpStatus.CREATED, taskResponse);
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<RestResponse<TaskResponse>> updateTask(HttpServletRequest request,
                                                                 @PathVariable Long id,
                                                                 @RequestBody TaskUpdateRequest taskUpdateRequest) {
        String requestUser = Utils.getUserNameFromRequest(request);
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
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(
            value = "Get all tasks by user",
            notes = "An ADMIN User has the privilege to call this API"
    )
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
        var taskStatus = TaskStatus.getStatus(status);
        List<TaskModel> tasks = taskService.getAllTaskByStatus(taskStatus);
        TaskResponse taskResponse = TaskResponse.of(tasks);
        return ResponseUtils.buildSuccessResponse(HttpStatus.OK, taskResponse);
    }
}
