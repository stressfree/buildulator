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

import flexjson.JSON;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The Class Person.
 */
@RooJavaBean
@RooJpaActiveRecord(
        identifierColumn = "id",
        table = "person",
        finders = { "findPeopleByOpenIdIdentifier" })
@RooJson
public class Person implements UserDetails {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 13213413L;

    /** The open id identifier. */
    @NotNull
    @Column(name = "openid_identifier")
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


    /**
     * Returns the username used to authenticate the user. Cannot return null.
     *
     * @return the username (never null)
     */
    @Override
    @JSON(include=false)
    public final String getUsername() {
        return this.openIdIdentifier;
    }

    /**
     * Returns the password used to authenticate the user. Cannot return null.
     *
     * @return the password (never null)
     */
    @Override
    @JSON(include=false)
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
    @JSON(include=false)
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
    @JSON(include=false)
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
    @JSON(include=false)
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
    @JSON(include=false)
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
     * Find an ordered list of people.
     *
     * @return an ordered list of people
     */
    public static List<Person> findAllPeople() {
        return entityManager().createQuery(
                "SELECT o FROM Person o ORDER BY lastName, firstName",
                Person.class).getResultList();
    }
}
