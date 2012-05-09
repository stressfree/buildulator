/*******************************************************************************
 * Copyright (c) 2012 David Harrison, Triptech Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *     David Harrison, Triptech Ltd - initial API and implementation
 ******************************************************************************/
package net.triptech.buildulator.model;

import net.sf.json.JSONObject;
import net.triptech.buildulator.BuildulatorException;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class Person.
 */
@RooJavaBean
@RooJpaActiveRecord(
        identifierColumn = "id",
        table = "person")
public class Person implements UserDetails {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 13213413L;

    /** The open id identifier. */
    @NotNull
    @Column(unique = true, name = "openid_identifier")
    private String openIdIdentifier;

    /** The user role. */
    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    /** The user status. */
    @NotNull
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    /** The first name. */
    @NotNull
    @Column(name = "first_name")
    private String firstName;

    /** The last name. */
    @NotNull
    @Column(name = "last_name")
    private String lastName;

    /** The email address. */
    @NotNull
    @Column(name = "email_address", unique = true)
    private String emailAddress;

    /** The projects. */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
    private List<Project> projects = new ArrayList<Project>();


    /**
     * Returns the username used to authenticate the user. Cannot return null.
     *
     * @return the username (never null)
     */
    @Override
    public final String getUsername() {
        return this.openIdIdentifier;
    }

    /**
     * Returns the password used to authenticate the user. Cannot return null.
     *
     * @return the password (never null)
     */
    @Override
    public final String getPassword() {
        return "";
    }

    /**
     * Indicates whether the user's account has expired.
     * An expired account cannot be authenticated.
     *
     * @return true if the user's account is valid (ie non-expired),
     *         false if no longer valid (ie expired)
     */
    @Override
    public final boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     * A locked user cannot be authenticated.
     *
     * @return true if the user is not locked, false otherwise
     */
    @Override
    public final boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired.
     * Expired credentials prevent authentication.
     *
     * @return true if the user's credentials are valid (ie non-expired),
     *         false if no longer valid (ie expired)
     */
    @Override
    public final boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     * A disabled user cannot be authenticated.
     *
     * @return true if the user is enabled, false otherwise
     */
    @Override
    public final boolean isEnabled() {
        return this.userStatus == UserStatus.ACTIVE;
    }

    /**
     * Returns the authorities granted to the user. Cannot return null.
     *
     * @return the authorities, sorted by natural key (never null)
     */
    @Override
    public final Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorities =
                new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(
                new GrantedAuthorityImpl(this.userRole.name()));
        return grantedAuthorities;
    }

    /**
     * The person's formatted name.
     *
     * @return the formatted name of the person
     */
    public final String getFormattedName() {
        return this.firstName + " " + this.lastName;
    }

    /**
     * Sets the value for the supplied field id.
     *
     * @param fieldId the field id
     * @param value the value
     * @param context the context
     * @return the string
     * @throws BuildulatorException the buildulator exception
     */
    public String set(final int fieldId, final String value,
            final ApplicationContext context) throws BuildulatorException {
        String parsedValue = "";

        try {
            switch (fieldId) {
                case 0:
                    this.setEmailAddress(value);
                    parsedValue = this.getEmailAddress();
                    break;
                case 1:
                    this.setFirstName(value);
                    parsedValue = this.getFirstName();
                    break;
                case 2:
                    this.setLastName(value);
                    parsedValue = this.getLastName();
                    break;
                case 3:
                    UserRole pUserRole = getUserRole(value, context);
                    this.setUserRole(pUserRole);
                    parsedValue = getMessage(pUserRole.getMessageKey(), context);
                    break;
                case 4:
                    UserStatus pUserStatus = getUserStatus(value, context);
                    this.setUserStatus(pUserStatus);
                    parsedValue = getMessage(pUserStatus.getMessageKey(), context);
                    break;
            }
        } catch (Exception e) {
            throw new BuildulatorException("Error parsing updated value");
        }

        return parsedValue;
    }

    /**
     * Covert the people list to JSON.
     *
     * @param people the people
     * @return the string
     */
    public static final String toJson(final List<Person> people,
            final ApplicationContext context) {

        List<Map<String, Object>> peopleJson = new ArrayList<Map<String, Object>>();

        for (Person person : people) {
            Map<String, Object> pJson = new LinkedHashMap<String, Object>();

            pJson.put("DT_RowId", person.getId());
            pJson.put("0", person.getEmailAddress());
            pJson.put("1", person.getFirstName());
            pJson.put("2", person.getLastName());
            pJson.put("3", getMessage(person.getUserRole().getMessageKey(), context));
            pJson.put("4", getMessage(person.getUserStatus().getMessageKey(), context));

            peopleJson.add(pJson);
        }

        Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
        jsonMap.put("sEcho", 1);
        jsonMap.put("iTotalRecords", people.size());
        jsonMap.put("iTotalDisplayRecords", people.size());
        jsonMap.put("aaData", peopleJson);

        JSONObject jsonObject = JSONObject.fromObject(jsonMap);

        return jsonObject.toString();
    }

    /**
     * Find an ordered list of people.
     *
     * @return an ordered list of people
     */
    public static List<Person> findAllPeople() {
        return entityManager().createQuery(
                "SELECT o FROM Person o ORDER BY lastName, firstName",
                Person.class).getResultList();
    }

    /**
     * Find a person by their openId identifier. If none is found null is returned.
     *
     * @param openIdIdentifier the openId identifier
     * @return the person
     */
    public static Person findByOpenIdIdentifier(final String openIdIdentifier) {

        Person person = null;

        if (StringUtils.isBlank(openIdIdentifier)) {
            throw new IllegalArgumentException(
                    "The openIdIdentifier identifier argument is required");
        }

        TypedQuery<Person> q = entityManager().createQuery("SELECT p FROM Person"
                + " AS p WHERE LOWER(p.openIdIdentifier) = LOWER(:openIdIdentifier)",
                Person.class);
        q.setParameter("openIdIdentifier", openIdIdentifier);

        List<Person> people = q.getResultList();

        if (people != null && people.size() > 0) {
            person = people.get(0);
        }
        return person;
    }

    /**
     * Find a person by their email address. If none is found null is returned.
     *
     * @param emailAddress the email address
     * @return the person
     */
    public static Person findByEmailAddress(final String emailAddress) {

        Person person = null;

        if (StringUtils.isBlank(emailAddress)) {
            throw new IllegalArgumentException("The email address argument is required");
        }

        TypedQuery<Person> q = entityManager().createQuery("SELECT p FROM Person"
                + " AS p WHERE LOWER(p.emailAddress) = LOWER(:emailAddress)",
                Person.class);
        q.setParameter("emailAddress", emailAddress);

        List<Person> people = q.getResultList();

        if (people != null && people.size() > 0) {
            person = people.get(0);
        }
        return person;
    }

    /**
     * Gets the user role.
     *
     * @param value the value
     * @param context the context
     * @return the user role
     */
    public static UserRole getUserRole(final String value,
            final ApplicationContext context) {
        UserRole pUserRole = UserRole.ROLE_USER;

        HashMap<String, UserRole> roles = new HashMap<String, UserRole>();

        for (UserRole role : UserRole.values()) {
            roles.put(getMessage(role.getMessageKey(), context), role);
        }
        if (roles.containsKey(value)) {
            pUserRole = roles.get(value);
        }
        return pUserRole;
    }

    /**
     * Gets the user status.
     *
     * @param value the value
     * @param context the context
     * @return the user status
     */
    public static UserStatus getUserStatus(final String value,
            final ApplicationContext context) {
        UserStatus pUserStatus = UserStatus.ACTIVE;

        HashMap<String, UserStatus> statuses = new HashMap<String, UserStatus>();

        for (UserStatus status : UserStatus.values()) {
            statuses.put(getMessage(status.getMessageKey(), context), status);
        }
        if (statuses.containsKey(value)) {
            pUserStatus = statuses.get(value);
        }
        return pUserStatus;
    }

    /**
     * Gets the message.
     *
     * @param key the key
     * @param context the context
     * @return the message
     */
    private static String getMessage(final String key, final ApplicationContext context) {
        return context.getMessage(key, null, LocaleContextHolder.getLocale());
    }
}
