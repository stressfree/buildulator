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
package net.triptech.buildulator.service;

import java.util.List;

import net.triptech.buildulator.model.Person;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * The Class OpenIdUserDetailsService.
 */
public class OpenIdUserDetailsService implements UserDetailsService {

    /**
     * Implementation of {@code UserDetailsService}. We only need this to
     * satisfy the {@code RememberMeServices} requirements.
     */
    public UserDetails loadUserByUsername(String id)
            throws UsernameNotFoundException {

        List<Person> people = Person.findPeopleByOpenIdIdentifier(id).getResultList();

        Person person = people.size() == 0 ? null : people.get(0);

        if (person == null) {
            throw new UsernameNotFoundException(id);
        }
        if (!person.isEnabled()) {
            throw new DisabledException("This user is disabled");
        }

        return person;
    }
}
