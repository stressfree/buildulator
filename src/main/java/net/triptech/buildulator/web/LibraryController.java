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
package net.triptech.buildulator.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.triptech.buildulator.model.Material;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@RequestMapping("/library")
@Controller
public class LibraryController extends BaseController {

    /**
     * Show the index page.
     *
     * @return the string
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "library/materials";
    }

    /**
     * Create a new material.
     *
     * @param name the name
     * @param unitOfMeasure the unit of measure
     * @param lifeYears the life years
     * @param carbonPerUnit the carbon per unit
     * @param energyPerUnit the energy per unit
     * @param wastagePercent the wastage percent
     * @param request the request
     * @param response the response
     * @return the string
     */
    @RequestMapping(value = "/materials", method = RequestMethod.POST)
    public @ResponseBody String newMaterial(
    		@RequestParam(value = "name", required = true) final String name,
    		@RequestParam(value = "unitOfMeasure") final String unitOfMeasure,
    		@RequestParam(value = "lifeYears") final Integer lifeYears,
    		@RequestParam(value = "carbonPerUnit") final Double carbonPerUnit,
    		@RequestParam(value = "energyPerUnit") final Double energyPerUnit,
    		@RequestParam(value = "wastagePercent") final Double wastagePercent,
    		final HttpServletRequest request,
    		final HttpServletResponse response) {

    	String returnMessage = "";

    	Material material = new Material();
    	material.setName(name);
    	material.setUnitOfMeasure(unitOfMeasure);
    	material.setLifeYears(lifeYears);
    	material.setCarbonPerUnit(carbonPerUnit);
    	material.setEnergyPerUnit(energyPerUnit);
    	material.setWastagePercent(wastagePercent);

    	try {
    		material.persist();
    		returnMessage = String.valueOf(material.getId());
    	} catch (Exception e) {
    		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    		returnMessage = this.getMessage("materials_library_add_error");
    	}
        return returnMessage;
    }

    /**
     * Update the material.
     *
     * @param id the id
     * @param colId the col id
     * @param value the value
     * @param request the request
     * @param response the response
     * @return the string
     */
    @RequestMapping(value = "/materials/update", method = RequestMethod.POST)
    public @ResponseBody String updateMaterial(
    		@RequestParam(value = "id", required = true) final String id,
    		@RequestParam(value = "columnPosition", required = true) final Integer colId,
    		@RequestParam(value = "value", required = true) final String value,
    		final HttpServletRequest request,
    		final HttpServletResponse response) {

    	String returnMessage = "";

    	Material material = Material.findByName(id);

    	if (material != null) {
    		try {
    			returnMessage = material.set(colId, value);
    			material.merge();
    			material.flush();
    		} catch (Exception e) {
        		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        		returnMessage = this.getMessage("materials_library_update_error");
    		}
    	} else {
    		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    		returnMessage = this.getMessage("materials_library_update_notfounderror");
    	}
        return returnMessage;
    }

    /**
     * Delete the material.
     *
     * @param id the id
     * @param request the request
     * @param response the response
     * @return the string
     */
    @RequestMapping(value = "/materials/delete", method = RequestMethod.POST)
    public @ResponseBody String deleteMaterial(
    		@RequestParam(value = "id", required = true) final String id,
    		final HttpServletRequest request,
    		final HttpServletResponse response) {

    	String returnMessage = "";

    	Material material = Material.findByName(id);

    	if (material != null) {
    		try {
    			material.remove();
    			returnMessage = "ok";
    		} catch (Exception e) {
        		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        		returnMessage = this.getMessage("materials_library_delete_error");
    		}
    	} else {
    		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    		returnMessage = this.getMessage("materials_library_delete_notfounderror");
    	}
        return returnMessage;
    }

    /**
     * List the materials.
     *
     * @return the string
     */
    @RequestMapping(value = "/materials/list.json", method = RequestMethod.GET)
    public @ResponseBody String list() {
        return Material.toJson(Material.findAllMaterials());
    }

}
