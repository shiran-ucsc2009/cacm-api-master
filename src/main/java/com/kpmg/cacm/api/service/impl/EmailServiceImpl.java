package com.kpmg.cacm.api.service.impl;

import com.kpmg.cacm.api.dto.response.model.IncidentEmailDTO;
import com.kpmg.cacm.api.model.EmailProperty;
import com.kpmg.cacm.api.model.ExceptionDefinitionRunError;
import com.kpmg.cacm.api.model.User;
import com.kpmg.cacm.api.service.EmailService;
import com.kpmg.cacm.api.service.UserService;
import com.kpmg.cacm.api.util.Email;
import com.kpmg.cacm.api.util.email.template.EmailTemplate;
import com.kpmg.cacm.api.util.email.template.ErrorNotificationScheduleEmailTemplate;
import com.kpmg.cacm.api.util.email.template.IncidentNotificationScheduleEmailTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private final Email email;

    private final UserService userService;

    @Autowired
    public EmailServiceImpl(final Email email, UserService userService) {
        this.email = email;
        this.userService = userService;
    }

    @Override
    public boolean sendErrorNotificationScheduleEmail(
            final List<ExceptionDefinitionRunError> expDefRunErrors,
            final EmailProperty emailProperty) {
        final EmailTemplate errorNotificationScheduleTemplate = ErrorNotificationScheduleEmailTemplate
            .builder()
            .expDefRunErrors(expDefRunErrors)
            .build();
        return this.email.send(
            "KTMS Error Notifications",
            emailProperty.getRecipients().split(","),
            errorNotificationScheduleTemplate
        );
    }

    @Override
    public boolean sendIncidentNotificationScheduleEmail(List<IncidentEmailDTO> incidentEmailDTOS) {

        Map<Long, List<IncidentEmailDTO>> userMapMap=  new HashMap<>();

        for (IncidentEmailDTO incidentEmailDTO : incidentEmailDTOS) {
            if (userMapMap.containsKey(incidentEmailDTO.getIncidentUser())) {

                userMapMap.get(incidentEmailDTO.getIncidentUser()).add(incidentEmailDTO);

            } else {
                List<IncidentEmailDTO> incidentEmailDTOS1 = new ArrayList<>();
                incidentEmailDTOS1.add(incidentEmailDTO);

                userMapMap.put(incidentEmailDTO.getIncidentUser(), incidentEmailDTOS1);
            }
        }

      userMapMap.forEach((userDTO, incidentEmailDTOS2) -> {
          User user=this.userService.findById(userDTO);
          String[] to={user.getEmail()};
          final EmailTemplate incidentNotificationScheduleEmailTemplate= IncidentNotificationScheduleEmailTemplate
                  .builder()
                  .incidentEmailDTOS(incidentEmailDTOS2)
                  .incidentUser(user.getName().split(" ")[0])
                  .build();
          this.email.send("Incident Notification",to,incidentNotificationScheduleEmailTemplate);
      });
        return false;
    }

}
