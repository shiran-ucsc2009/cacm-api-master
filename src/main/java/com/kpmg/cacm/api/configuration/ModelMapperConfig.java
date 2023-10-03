package com.kpmg.cacm.api.configuration;

import java.util.List;
import java.util.stream.Collectors;

import com.kpmg.cacm.api.dto.request.ExceptionDefinitionAddRequest;
import com.kpmg.cacm.api.dto.request.ExceptionDefinitionUpdateRequest;
import com.kpmg.cacm.api.dto.request.IncidentCommentAddRequest;
import com.kpmg.cacm.api.dto.request.UserAddRequest;
import com.kpmg.cacm.api.dto.request.UserUpdateRequest;
import com.kpmg.cacm.api.model.Category;
import com.kpmg.cacm.api.model.ExceptionDefinition;
import com.kpmg.cacm.api.model.ExceptionDefinitionRun;
import com.kpmg.cacm.api.model.Incident;
import com.kpmg.cacm.api.model.IncidentComment;
import com.kpmg.cacm.api.model.User;
import com.kpmg.cacm.api.model.UserGroup;
import com.kpmg.cacm.api.service.CategoryService;
import com.kpmg.cacm.api.service.IncidentService;
import com.kpmg.cacm.api.service.UserGroupService;
import com.kpmg.cacm.api.service.UserService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    private final UserService userService;

    private final UserGroupService userGroupService;

    private final CategoryService categoryService;

    private final IncidentService incidentService;

    public ModelMapperConfig(
            final UserService userService,
            final UserGroupService userGroupService,
            final CategoryService categoryService,
            final IncidentService incidentService) {
        this.userService = userService;
        this.userGroupService = userGroupService;
        this.categoryService = categoryService;
        this.incidentService = incidentService;
    }

    private ModelMapper config(final ModelMapper modelMapper) {

        modelMapper.getConfiguration().setAmbiguityIgnored(true).setMatchingStrategy(MatchingStrategies.STRICT);

        final Converter<List<Long>, List<Category>> categoryLstConverter = context -> context.getSource().stream().map(this.categoryService::findById).collect(Collectors.toList());
        final Converter<Long, Category> categoryConverter = context -> this.categoryService.findById(context.getSource());
        final Converter<Long, UserGroup> userGroupConverter = context -> this.userGroupService.findById(context.getSource());
        final Converter<Long, User> userConverter = context -> this.userService.findById(context.getSource());
        final Converter<Long, Incident> incidentConverter = context -> this.incidentService.findById(context.getSource());

        modelMapper.typeMap(UserAddRequest.class, User.class).addMappings(mapper -> {
            mapper.using(categoryLstConverter).map(UserAddRequest::getCategoryIds, User::setCategories);
            mapper.using(userGroupConverter).map(UserAddRequest::getUserGroupId, User::setUserGroup);
        });

        modelMapper.typeMap(UserUpdateRequest.class, User.class).addMappings(mapper -> {
            mapper.using(categoryLstConverter).map(UserUpdateRequest::getCategoryIds, User::setCategories);
            mapper.using(userGroupConverter).map(UserUpdateRequest::getUserGroupId, User::setUserGroup);
        });

        modelMapper.typeMap(ExceptionDefinitionAddRequest.class, ExceptionDefinition.class).addMappings(mapper -> {
            mapper.using(categoryConverter).map(ExceptionDefinitionAddRequest::getCategoryId, ExceptionDefinition::setCategory);
            mapper.using(userConverter).map(ExceptionDefinitionAddRequest::getOwnerId, ExceptionDefinition::setOwner);
            mapper.using(userConverter).map(ExceptionDefinitionAddRequest::getDefaultAssigneeId, ExceptionDefinition::setDefaultAssignee);
        });

        modelMapper.typeMap(ExceptionDefinitionUpdateRequest.class, ExceptionDefinition.class).addMappings(mapper -> {
            mapper.using(userConverter).map(ExceptionDefinitionUpdateRequest::getOwnerId, ExceptionDefinition::setOwner);
            mapper.using(userConverter).map(ExceptionDefinitionUpdateRequest::getDefaultAssigneeId, ExceptionDefinition::setDefaultAssignee);
        });

        modelMapper.typeMap(IncidentCommentAddRequest.class, IncidentComment.class).addMappings(mapper -> {
            mapper.using(incidentConverter).map(IncidentCommentAddRequest::getIncidentId, IncidentComment::setIncident);
        });

        modelMapper.typeMap(ExceptionDefinition.class, ExceptionDefinitionRun.class).addMappings(mapper -> {
            mapper.skip(ExceptionDefinitionRun::setId);
            mapper.skip(ExceptionDefinitionRun::setCreatedBy);
            mapper.skip(ExceptionDefinitionRun::setCreationTimestamp);
            mapper.skip(ExceptionDefinitionRun::setUpdatedBy);
            mapper.skip(ExceptionDefinitionRun::setUpdateTimestamp);
        });

        return modelMapper;
    }

    @Bean
    public ModelMapper modelMapper() {
        return this.config(new ModelMapper());
    }
}
