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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.triptech.buildulator.DataParser;
import net.triptech.buildulator.FlashScope;
import net.triptech.buildulator.model.EnergySource;
import net.triptech.buildulator.model.Material;
import net.triptech.buildulator.model.DataGrid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@RequestMapping("/library")
@Controller
public class LibraryController extends BaseController {

    /** The logger. */
    private static Logger logger = Logger.getLogger(LibraryController.class);

    /**
     * Show the index page.
     *
     * @return the string
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('ROLE_EDITOR','ROLE_ADMIN')")
    public String index() {
        return "library/list";
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
    @PreAuthorize("hasAnyRole('ROLE_EDITOR','ROLE_ADMIN')")
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
    @PreAuthorize("hasAnyRole('ROLE_EDITOR','ROLE_ADMIN')")
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
    @PreAuthorize("hasAnyRole('ROLE_EDITOR','ROLE_ADMIN')")
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

    @PreAuthorize("hasAnyRole('ROLE_EDITOR','ROLE_ADMIN')")
    @RequestMapping(value = "/materials/bulk", method = RequestMethod.POST)
    public String importMaterialList(
            @RequestParam(value = "materialsData", required = true) String data,
            Model uiModel, HttpServletRequest request) throws Exception {

        StringBuilder message = new StringBuilder();

        if (StringUtils.isNotBlank(data)) {
            DataGrid parsedData = new DataGrid(data);

            int materialCount = 0, nameId = 0, unitOfMeasureId = 0, lifeYearsId = 0,
                carbonPerUnitId = 0, energyPerUnitId = 0, wastagePercentId = 0,
                headerCount = 0;

            for (String header : parsedData.getHeaderFields()) {
                if (StringUtils.equalsIgnoreCase(header, this.getMessage(
                        "label_net_triptech_buildulator_model_material_name"))) {
                    nameId = headerCount;
                }
                if (StringUtils.equalsIgnoreCase(header, this.getMessage(
                        "label_net_triptech_buildulator_model_material_unitofmeasure"))) {
                    unitOfMeasureId = headerCount;
                }
                if (StringUtils.equalsIgnoreCase(header, this.getMessage(
                        "label_net_triptech_buildulator_model_material_lifeyears"))) {
                    lifeYearsId = headerCount;
                }
                if (StringUtils.equalsIgnoreCase(header, this.getMessage(
                        "label_net_triptech_buildulator_model_material_carbonperunit"))) {
                    carbonPerUnitId = headerCount;
                }
                if (StringUtils.equalsIgnoreCase(header, this.getMessage(
                        "label_net_triptech_buildulator_model_material_energyperunit"))) {
                    energyPerUnitId = headerCount;
                }
                if (StringUtils.equalsIgnoreCase(header, this.getMessage(
                        "label_net_triptech_buildulator_model_material_wastagepercent")))
                {
                    wastagePercentId = headerCount;
                }
                headerCount++;
            }

            for (List<String> row : parsedData.getRows()) {

                Material material = new Material();

                material.setName(DataParser.stripHtml(row.get(nameId)));
                material.setUnitOfMeasure(DataParser.stripHtml(row.get(unitOfMeasureId)));
                try {
                    material.setLifeYears(Integer.parseInt(row.get(lifeYearsId)));
                } catch (Exception e) {
                    logger.error("Error parsing lifeYears value: " + e.getMessage());
                }
                try {
                    material.setCarbonPerUnit(Double.parseDouble(
                            row.get(carbonPerUnitId)));
                } catch (Exception e) {
                    logger.error("Error parsing carbonPerUnit value: " + e.getMessage());
                }
                try {
                    material.setEnergyPerUnit(Double.parseDouble(
                            row.get(energyPerUnitId)));
                } catch (Exception e) {
                    logger.error("Error parsing energyPerUnit value: " + e.getMessage());
                }
                try {
                    material.setWastagePercent(Double.parseDouble(
                            row.get(wastagePercentId)));
                } catch (Exception e) {
                    logger.error("Error parsing wastagePercent value: " + e.getMessage());
                }

                try {
                    material.persist();
                    material.flush();
                    materialCount++;
                } catch (Exception e) {
                    logger.error("Error persisting material: " + e.getMessage());
                }
            }

            message.append(materialCount);
            message.append(" ");
            message.append(getMessage("materials_library_bulkadd_complete"));

        } else {
            message.append(getMessage("materials_library_bulkadd_nodata"));
        }

        FlashScope.appendMessage(message.toString(), request);

        return "redirect:/library";
    }

    @RequestMapping(value = "/material-template.xls", method = RequestMethod.GET)
    public ModelAndView buildTemplate(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DataGrid dataGrid = new DataGrid();
        dataGrid.setTitle(this.getMessage("materials_library_bulkadd_template_title"));

        dataGrid.addHeaderField(this.getMessage(
                "label_net_triptech_buildulator_model_material_name"));
        dataGrid.addHeaderField(this.getMessage(
                "label_net_triptech_buildulator_model_material_unitofmeasure"));
        dataGrid.addHeaderField(this.getMessage(
                "label_net_triptech_buildulator_model_material_lifeyears"));
        dataGrid.addHeaderField(this.getMessage(
                "label_net_triptech_buildulator_model_material_carbonperunit"));
        dataGrid.addHeaderField(this.getMessage(
                "label_net_triptech_buildulator_model_material_energyperunit"));
        dataGrid.addHeaderField(this.getMessage(
                "label_net_triptech_buildulator_model_material_wastagepercent"));

        return new ModelAndView("ExcelTemplateView", "dataGrid", dataGrid);
    }


    /**
     * Create a new energy source.
     *
     * @param name the name
     * @param carbonPerUnit the carbon per unit
     * @param energyPerUnit the energy per unit
     * @param request the request
     * @param response the response
     * @return the string
     */
    @RequestMapping(value = "/energysources", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('ROLE_EDITOR','ROLE_ADMIN')")
    public @ResponseBody String newEnergySource(
            @RequestParam(value = "name", required = true) final String name,
            @RequestParam(value = "carbonPerUnit") final Double carbonPerUnit,
            @RequestParam(value = "energyPerUnit") final Double energyPerUnit,
            final HttpServletRequest request,
            final HttpServletResponse response) {

        String returnMessage = "";

        EnergySource energySource = new EnergySource();
        energySource.setName(name);
        energySource.setCarbonPerUnit(carbonPerUnit);
        energySource.setEnergyPerUnit(energyPerUnit);

        try {
            energySource.persist();
            returnMessage = String.valueOf(energySource.getId());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            returnMessage = this.getMessage("energysources_library_add_error");
        }
        return returnMessage;
    }

    /**
     * Update the energy source.
     *
     * @param id the id
     * @param colId the col id
     * @param value the value
     * @param request the request
     * @param response the response
     * @return the string
     */
    @RequestMapping(value = "/energysources/update", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('ROLE_EDITOR','ROLE_ADMIN')")
    public @ResponseBody String updateEnergySource(
            @RequestParam(value = "id", required = true) final String id,
            @RequestParam(value = "columnPosition", required = true) final Integer colId,
            @RequestParam(value = "value", required = true) final String value,
            final HttpServletRequest request,
            final HttpServletResponse response) {

        String returnMessage = "";

        EnergySource energySource = EnergySource.findByName(id);

        if (energySource != null) {
            try {
                returnMessage = energySource.set(colId, value);
                energySource.merge();
                energySource.flush();
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                returnMessage = this.getMessage("energysources_library_update_error");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            returnMessage = this.getMessage("energysources_library_update_notfounderror");
        }
        return returnMessage;
    }

    /**
     * Delete the energy source.
     *
     * @param id the id
     * @param request the request
     * @param response the response
     * @return the string
     */
    @RequestMapping(value = "/energysources/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('ROLE_EDITOR','ROLE_ADMIN')")
    public @ResponseBody String deleteEnergySource(
            @RequestParam(value = "id", required = true) final String id,
            final HttpServletRequest request,
            final HttpServletResponse response) {

        String returnMessage = "";

        EnergySource energySource = EnergySource.findByName(id);

        if (energySource != null) {
            try {
                energySource.remove();
                returnMessage = "ok";
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                returnMessage = this.getMessage("energysources_library_delete_error");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            returnMessage = this.getMessage("energysources_library_delete_notfounderror");
        }
        return returnMessage;
    }


    /**
     * List the materials.
     *
     * @return the string
     */
    @RequestMapping(value = "/materials/list.json", method = RequestMethod.GET)
    public @ResponseBody String listMaterials() {
        return Material.toJson(Material.findAllMaterials());
    }

    /**
     * List the energy sources.
     *
     * @return the string
     */
    @RequestMapping(value = "/energysources/list.json", method = RequestMethod.GET)
    public @ResponseBody String listEnergySources() {
        return EnergySource.toJson(EnergySource.findAllEnergySources());
    }

    @ModelAttribute("controllerUrl")
    public final String getControllerUrl() {
        return "/library";
    }

    @ModelAttribute("controllerName")
    public final String getControllerName() {
        return getMessage("controller_library");
    }

}
