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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.triptech.buildulator.DataParser;
import net.triptech.buildulator.FlashScope;
import net.triptech.buildulator.model.MaterialDetail;
import net.triptech.buildulator.model.DataGrid;
import net.triptech.buildulator.model.MaterialType;

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
            @RequestParam(value = "materialType", required = true) final String typeKey,
            @RequestParam(value = "unitOfMeasure", required = true)
                    final String unitOfMeasure,
            @RequestParam(value = "lifeYears") final Integer lifeYearsVal,
            @RequestParam(value = "carbonPerUnit") final Double carbonPerUnitVal,
            @RequestParam(value = "energyPerUnit") final Double energyPerUnitVal,
            @RequestParam(value = "wastagePercent") final Double wastagePercentVal,
            final HttpServletRequest request,
            final HttpServletResponse response) {

        String returnMessage = "";

        int lifeYears = 0;
        double carbonPerUnit = 0, energyPerUnit = 0, wastagePercent = 0;

        if (lifeYearsVal != null) {
            lifeYears = lifeYearsVal;
        }
        if (carbonPerUnitVal != null) {
            carbonPerUnit = carbonPerUnitVal;
        }
        if (energyPerUnitVal != null) {
            energyPerUnit = energyPerUnitVal;
        }
        if (wastagePercentVal != null) {
            wastagePercent = wastagePercentVal;
        }
        MaterialType materialType = MaterialDetail.getMaterialType(typeKey, getContext());

        MaterialDetail material = new MaterialDetail();
        material.setName(name);
        material.setMaterialType(materialType);
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

        MaterialDetail material = MaterialDetail.findByName(id);

        if (material != null) {
            try {
                returnMessage = material.set(colId, value, this.getContext());
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

        MaterialDetail material = MaterialDetail.findByName(id);

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

            int materialCount = 0, nameId = 0, materialTypeId = 0, unitOfMeasureId = 0,
                    lifeYearsId = 0, carbonPerUnitId = 0, energyPerUnitId = 0,
                    wastagePercentId = 0, headerCount = 0;

            String prefix = "label_net_triptech_buildulator_model_materialdetail";

            for (String header : parsedData.getHeaderFields()) {
                if (StringUtils.equalsIgnoreCase(header, this.getMessage(
                        prefix + "_name"))) {
                    nameId = headerCount;
                }
                if (StringUtils.equalsIgnoreCase(header, this.getMessage(
                        prefix + "_materialtype"))) {
                    materialTypeId = headerCount;
                }
                if (StringUtils.equalsIgnoreCase(header, this.getMessage(
                        prefix + "_unitofmeasure"))) {
                    unitOfMeasureId = headerCount;
                }
                if (StringUtils.equalsIgnoreCase(header, this.getMessage(
                        prefix + "_lifeyears"))) {
                    lifeYearsId = headerCount;
                }
                if (StringUtils.equalsIgnoreCase(header, this.getMessage(
                        prefix + "_carbonperunit"))) {
                    carbonPerUnitId = headerCount;
                }
                if (StringUtils.equalsIgnoreCase(header, this.getMessage(
                        prefix + "_energyperunit"))) {
                    energyPerUnitId = headerCount;
                }
                if (StringUtils.equalsIgnoreCase(header, this.getMessage(
                        prefix + "_wastagepercent"))) {
                    wastagePercentId = headerCount;
                }
                headerCount++;
            }

            for (List<String> row : parsedData.getRows()) {

                MaterialDetail material = new MaterialDetail();

                material.setName(DataParser.stripHtml(row.get(nameId)));
                material.setMaterialType(getMaterialType(row.get(materialTypeId)));
                material.setUnitOfMeasure(DataParser.stripHtml(row.get(unitOfMeasureId)));
                try {
                    material.setLifeYears(Integer.parseInt(row.get(lifeYearsId)));
                } catch (Exception e) {
                    logger.info("Error parsing lifeYears value: " + e.getMessage());
                }
                try {
                    material.setCarbonPerUnit(Double.parseDouble(
                            row.get(carbonPerUnitId)));
                } catch (Exception e) {
                    logger.info("Error parsing carbonPerUnit value: " + e.getMessage());
                }
                try {
                    material.setEnergyPerUnit(Double.parseDouble(
                            row.get(energyPerUnitId)));
                } catch (Exception e) {
                    logger.info("Error parsing energyPerUnit value: " + e.getMessage());
                }
                try {
                    material.setWastagePercent(Double.parseDouble(
                            row.get(wastagePercentId)));
                } catch (Exception e) {
                    logger.info("Error parsing wastagePercent value: " + e.getMessage());
                }

                try {
                    material.persist();
                    material.flush();
                    materialCount++;
                } catch (Exception e) {
                    logger.info("Error persisting material: " + e.getMessage());
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

        String prefix = "label_net_triptech_buildulator_model_materialdetail";

        dataGrid.addHeaderField(this.getMessage(prefix + "_name"));
        dataGrid.addHeaderField(this.getMessage(prefix + "_materialtype"));
        dataGrid.addHeaderField(this.getMessage(prefix + "_unitofmeasure"));
        dataGrid.addHeaderField(this.getMessage(prefix + "_lifeyears"));
        dataGrid.addHeaderField(this.getMessage(prefix + "_carbonperunit"));
        dataGrid.addHeaderField(this.getMessage(prefix + "_energyperunit"));
        dataGrid.addHeaderField(this.getMessage(prefix + "_wastagepercent"));

        return new ModelAndView("ExcelTemplateView", "dataGrid", dataGrid);
    }

    /**
     * List the materials.
     *
     * @return the string
     */
    @RequestMapping(value = "/materials/list.json", method = RequestMethod.GET)
    public @ResponseBody String listMaterials() {
        return MaterialDetail.toJson(MaterialDetail.findAllMaterialDetails(),
                this.getContext());
    }

    @RequestMapping(value = "/materials/types.json", method = RequestMethod.GET)
    public @ResponseBody String listMaterialTypes(
            final HttpServletRequest request,
            final HttpServletResponse response) {

        Map<String, String> jsonMap = new LinkedHashMap<String, String>();

        for (MaterialType materialType : MaterialType.values()) {
            String name = getMessage(materialType.getMessageKey());
            jsonMap.put(name, name);
        }
        JSONObject jsonObject = JSONObject.fromObject(jsonMap);

        return jsonObject.toString();
    }

    @ModelAttribute("materialTypes")
    public final List<MaterialType> getMaterialTypes() {
        List<MaterialType> materialTypes = new ArrayList<MaterialType>();

        for (MaterialType mt : MaterialType.values()) {
            materialTypes.add(mt);
        }
        return materialTypes;
    }

    /**
     * Gets the material type.
     *
     * @param materialName the material name
     * @return the material type
     */
    private MaterialType getMaterialType(final String materialName) {
        MaterialType materialType = MaterialType.CONSTRUCTION;

        for (MaterialType mt : MaterialType.values()) {
            if (StringUtils.equalsIgnoreCase(materialName,
                    getMessage(mt.getMessageKey()))) {
                materialType = mt;
            }
        }
        return materialType;
    }

}
