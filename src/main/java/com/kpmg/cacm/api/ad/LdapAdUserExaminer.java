/* PRODUCT : complete
 * PROJECT : complete
 * PACKAGE : com.example.authenticatingldap
 * ************************************************************************************************
 *
 * Copyright(C) 2019 KPMG Technology Service All rights reserved.
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF KPMG Technology solution.
 *
 * This copy of the Source Code is intended for KPMG Technology solution's internal use only
 * and is intended for view by persons duly authorized by the management of KPMG Technology solution. No
 * part of this file may be reproduced or distributed in any form or by any
 * means without the written approval of the Management of KPMG Technology solution.
 *
 * *************************************************************************************************
 *
 * REVISIONS:
 * Author : sdhanasena
 * Date : 7/28/2020 - 12:44 PM
 * Since : version 1.0
 * Description :
 * ************* */
package com.kpmg.cacm.api.ad;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

/**
 * Author: sdhanasena
 * Date : 9/2/2020
 */
@Component
public class LdapAdUserExaminer {

    @Value("${ad.url}")
    private String AD_URL;

    @Value("${ad.domain}")
    private String AD_DOMAIN;

    @Value("${ad.base}")
    private String AD_BASE;

    public LdapContext getLdapContext(String username, String password) {
        LdapContext ctx = null;

        try {
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY,
                    "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.SECURITY_AUTHENTICATION, "Simple");
            //NOTE: replace user@domain.com with a User that is present in your Active Directory/LDAP
            env.put(Context.SECURITY_PRINCIPAL, username +"@"+ AD_DOMAIN);
            //NOTE: replace userpass with passwd of this user.
            env.put(Context.SECURITY_CREDENTIALS, password);
            //NOTE: replace ADorLDAPHost with your Active Directory/LDAP Hostname or IP.
            env.put(Context.PROVIDER_URL, AD_URL);
            System.out.println("Attempting to Connect...");
            ctx = new InitialLdapContext(env, null);
            System.out.println("Connection Successful.");
        } catch (NamingException nex) {
            System.out.println("LDAP Connection: FAILED");
            nex.printStackTrace();
        }
        return ctx;
    }

    public AdDto getUserBasicAttributesByEmpNo(String empNo, LdapContext ctx) {

        AdDto adDto = new AdDto();
        try {
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
            //NOTE: The attributes mentioned in array below are the ones that will be retrieved, you can add more.
            String[] attrIDs ={"description","sAMAccountName","mail","mobile","employeeID","displayName"};

            constraints.setReturningAttributes(attrIDs);
               //NOTE: replace DC=domain,DC=com below with your domain info. It is essentially the Base Node for Search.
//            NamingEnumeration answer = ctx.search("DC=hnbgrameen,DC=lk", "description="
//                    + empNo , constraints);

            NamingEnumeration answer = ctx.search(AD_BASE, "employeeID="
                    + empNo , constraints);

            if (answer.hasMore()) {
                Attributes attrs = ((SearchResult) answer.next()).getAttributes();
//                adDto.setUserName(attrs.get("sAMAccountName").get().toString());
//                adDto.setMail(attrs.get("mail")==null? "":attrs.get("mail").get().toString());
//                adDto.setEmployeeNo(attrs.get("description")==null? "":attrs.get("description").get().toString());
//                adDto.setMobile(attrs.get("mobile")==null? "":attrs.get("mobile").get().toString());
//               // adDto.setEmployeeID(attrs.get("employeeID")==null? "":attrs.get("employeeID").get().toString());
//                adDto.setDescription(attrs.get("description")==null? "": attrs.get("description").get().toString());
//                adDto.setDisplayName(attrs.get("displayName")==null? "":attrs.get("displayName").get().toString());

                adDto.setUserName(attrs.get("sAMAccountName").get().toString());
                adDto.setMail(attrs.get("mail")==null? "": attrs.get("mail").get().toString());
                adDto.setEmployeeNo(attrs.get("employeeID")==null? "":attrs.get("employeeID").get().toString());
                adDto.setMobile(attrs.get("mobile")==null? "":attrs.get("mobile").get().toString());
                adDto.setDescription(attrs.get("description")==null? "": attrs.get("description").get().toString());
                adDto.setDisplayName(attrs.get("displayName")==null? "":attrs.get("displayName").get().toString());
                adDto.setDepartment(attrs.get("description")==null? "": attrs.get("description").get().toString());

            } else {
                throw new Exception("Invalid User");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return adDto;
    }

    public AdDto getUserBasicAttributesByUserName(String empName, LdapContext ctx) {

        AdDto adDto = new AdDto();
        try {
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
            //NOTE: The attributes mentioned in array below are the ones that will be retrieved, you can add more.
            String[] attrIDs ={"description","sAMAccountName","mail","mobile","employeeID","displayName"};

            constraints.setReturningAttributes(attrIDs);
            //NOTE: replace DC=domain,DC=com below with your domain info. It is essentially the Base Node for Search.
            NamingEnumeration answer = ctx.search(AD_BASE, "sAMAccountName="
                    + empName , constraints);

            if (answer.hasMore()) {
                Attributes attrs = ((SearchResult) answer.next()).getAttributes();
                adDto.setUserName(attrs.get("sAMAccountName").get().toString());
                adDto.setMail(attrs.get("mail")==null? "": attrs.get("mail").get().toString());
                adDto.setEmployeeNo(attrs.get("employeeID")==null? "":attrs.get("employeeID").get().toString());
                adDto.setMobile(attrs.get("mobile")==null? "":attrs.get("mobile").get().toString());
                adDto.setDescription(attrs.get("description")==null? "": attrs.get("description").get().toString());
                adDto.setDisplayName(attrs.get("displayName")==null? "":attrs.get("displayName").get().toString());
                adDto.setDepartment(attrs.get("description")==null? "": attrs.get("description").get().toString());

            } else {
                throw new Exception("Invalid User");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return adDto;
    }

}