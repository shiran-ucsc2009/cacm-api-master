package com.kpmg.cacm.api.repository.spring;

import com.kpmg.cacm.api.dto.constant.AttachmentType;
import com.kpmg.cacm.api.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    Attachment findByEntityIdAndAttachmentTypeAndDeletedFalse(@Param("entityId") Long entityId, @Param("attachmentType") AttachmentType attachmentType);

}
