package com.kpmg.cacm.api.ad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@RestController
public class AdUserController {

    @Autowired
    LdapAdUserExaminer ldapExaminer;

    @Autowired
    LdapTemplate ldTmp;

    @Value("${ad.username}")
    private String AD_USERNAME;

    @Value("${ad.password}")
    private String AD_PASSWORD;

    @Secured("FIND_CURRENT_USER")
    @GetMapping("/getUser")
    public AdDto getUserBasicAttributesByUserName(@RequestParam(value = "empNo", required = false) String empNo) {
        if(empNo==null){
            UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            empNo =ud.getUsername();
        }

        AdDto userData = new AdDto();

        try {
            userData = ldapExaminer.getUserBasicAttributesByEmpNo(empNo, ldapExaminer.getLdapContext(AD_USERNAME, AD_PASSWORD));
            //userData = ldapExaminer.getUserBasicAttributesByUserName(empNo, ldapExaminer.getLdapContext(AD_USERNAME, AD_PASSWORD));
        } catch (Exception e) {
            //e.getLocalizedMessage();
        }

        return userData;
    }

}
