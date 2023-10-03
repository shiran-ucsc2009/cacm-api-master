package com.kpmg.cacm.api.controller;

import javax.validation.Valid;

import com.kpmg.cacm.api.dto.request.UserGroupAddRequest;
import com.kpmg.cacm.api.dto.request.UserGroupUpdateRequest;
import com.kpmg.cacm.api.dto.response.CountResponse;
import com.kpmg.cacm.api.dto.response.EmptyResponse;
import com.kpmg.cacm.api.dto.response.UserGroupResponse;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface UserGroupController {

    @Secured("FIND_USER_GROUP_BY_ID")
    @GetMapping("/user-groups/{id}")
    UserGroupResponse findById(@PathVariable("id") Long id);

    @Secured("FIND_ALL_USER_GROUPS")
    @GetMapping("/user-groups")
    UserGroupResponse findAll();

    @Secured("DELETE_USER_GROUP")
    @DeleteMapping("/user-groups/{id}")
    EmptyResponse deleteById(@PathVariable("id") Long id);

    @Secured("ADD_USER_GROUP")
    @PostMapping("/user-groups")
    EmptyResponse save(@Valid @RequestBody UserGroupAddRequest userGroupAddRequest);

    @Secured("UPDATE_USER_GROUP")
    @PutMapping("/user-groups")
    EmptyResponse update(@RequestBody UserGroupUpdateRequest userGroupUpdateRequest);

    @Secured("COUNT_USER_GROUPS")
    @GetMapping("/user-groups/count")
    CountResponse countAll();

}
