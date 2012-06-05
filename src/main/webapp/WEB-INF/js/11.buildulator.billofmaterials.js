function BillOfMaterials (config) {
    if (config === undefined) { config = []; }
    var _data = [];
    var _materials = [];
    var _div = (config.div !== undefined) ? config.div : 'div.bomTable';
    var _summary = (config.summary !== undefined) ? config.summary : null;
    var _editing = (config.editing !== undefined) ? config.editing : 'false';
    var _type = (config.type !== undefined) ? config.type : 'construction';
    var _projectId = (config.projectId !== undefined) ? config.projectId : 0;
    var _publicId = (config.publicId !== undefined) ? config.publicId : 0;
    var _projectUrl = (config.projectUrl !== undefined) ? config.projectUrl : './projects/';
    var _addText = (config.addText !== undefined) ? config.addText : 'Add';
    var _editText = (config.editText !== undefined) ? config.editText : 'OK';
    var _cancelText = (config.cancelText !== undefined) ? config.cancelText : 'Cancel';
    var _sectionText = (config.sectionText !== undefined) ? config.sectionText : 'section';
    var _elementText = (config.elementText !== undefined) ? config.elementText : 'element';
    var _materialText = (config.materialText !== undefined) ? config.materialText : 'material';
    var _deleteText = (config.deleteText !== undefined) ? config.deleteText : 'Delete';
    var _deleteConfirmText = (config.deleteConfirmText !== undefined) ? config.deleteConfirmText : 'Are you sure you want to delete this';
    var _headerNameText = (config.headerNameText !== undefined) ? config.headerNameText : '';
    var _headerQuantityText = (config.headerQuantityText !== undefined) ? config.headerQuantityText : 'Quantity';
    var _headerCarbonText = (config.headerCarbonText !== undefined) ? config.headerCarbonText : 'Carbon (kg CO<sub>2</sub>)';
    var _footerSummaryText = (config.footerSummaryText !== undefined) ? config.footerSummaryText : 'Total embodied carbon (kg)';
    var _editStartText = (config.editStartText !== undefined) ? config.editStartText : 'Edit table';
    var _editFinishText = (config.editFinishText !== undefined) ? config.editFinishText : 'Finish editing';
    var _quantityText = (config.quantityText !== undefined) ? config.quantityText : 'Quantity';
    var _unitsText = (config.unitsText !== undefined) ? config.unitsText : 'Units';
    

    function _addDefaultHandlers() {
        $(_div + ' div.bomHEdit button').button().click(function () {
            var currentlyEditing = true;
            var sections = $(_div + ' ul.bomSections');
            if ($(sections).hasClass('bomNotEditing')) {
                currentlyEditing = false;
            }

            if (currentlyEditing) {
                // Stop editing
                $(_div + ' .bomCancel').trigger('click');

                $(this).find('span').html(_editStartText);
                $(sections).addClass('bomNotEditing');
                _removeDefaultEditHandlers();
            } else {
                // Begin editing
                $(this).find('span').html(_editFinishText);
                $(sections).removeClass('bomNotEditing');
                _addDefaultEditHandlers();
            }
        });

        if (_editing !== 'false') {
            _addDefaultEditHandlers();
        }
    }

    function _addDefaultEditHandlers() {
        $(_div + ' div.deleteFromBOM a').bind('click.bomEdit', function() {
            _confirmDelete(this);
        });
        $(_div + ' li.addToBOM a.addToBOM').bind('click.bomEdit', function() {
            _displayAddForm(this);
        });
        $(_div + ' div.bomEditable').bind('dblclick.bomEdit', function () {
            _displayEditForm(this);
        });
    }

    function _removeDefaultEditHandlers() {
        $(_div + ' div.deleteFromBOM a').unbind('click.bomEdit');
        $(_div + ' li.addToBOM a.addToBOM').unbind('click.bomEdit');
        $(_div + ' div.bomEditable').unbind('dblclick.bomEdit');
    }

    function _addRowHandlers(node) {
        $(node).find('div.deleteFromBOM a').click(function () {
            _confirmDelete(this);
        });
        $(_div + ' li.addToBOM a.addToBOM').click(function () {
            _displayAddForm(this);
        });
        $(node).find('div.bomEditable').dblclick(function () {
            _displayEditForm(this);
        });
    }

    function _assignAddSubmitCancelOptionEventHandlers(addDiv) {
        var type = _getType(addDiv);

        $(addDiv).find('input.bomHelpText').bind('focus.bomHelpText', function(e) {
            $(this).removeClass('bomHelpText').val('').unbind('focus.bomHelpText');
        });

        $(addDiv).find('input.bomSubmit').button().click(function(e) {
            e.preventDefault();
            $(this).closest('form').submit();
        });

        $(addDiv).find('input.bomCancel').button().click(function(e) {
            e.preventDefault();
            addDiv.html(_renderAddControlText(type));
            $(addDiv).find('a').click(function() {
                _displayAddForm(this);
            });
        });

        var form = $(addDiv).find('form');
        form.validate();
        form.submit(function (e) {
            e.preventDefault();

            var valid = true;

            $(this).find('input.required, input.number').each(function() {
                if (!$(form).validate().element(this)) {
                    valid = false;
                }
            });
            if (!valid) {
                return false;
            }

            var name = '', quantity = '', units = '', sid = '', eid = '';

            switch (type) {
                case 'section':
                    name = $(addDiv).find('div.bomSectionNameInput input').val();
                    break;
                case 'element':
                    name = $(addDiv).find('div.bomElementNameInput input').val();
                    quantity = $(addDiv).find('div.bomElementQuantityInput input').val();
                    units = $(addDiv).find('div.bomElementUnitsInput input').val();
                    sid = $(addDiv).closest('li.bomSection').index() + 1;
                    break;
                case 'material':
                    name = $(addDiv).find('div.bomMaterialNameInput input').val();
                    quantity = $(addDiv).find('div.bomMaterialQuantityInput input').val();
                    sid = $(addDiv).closest('li.bomSection').index() + 1;
                    eid = $(addDiv).closest('li.bomElement').index() + 1;
                    break;
            }

            var params = 'type=' + _type + '&name=' + name + '&quantity=' + quantity +
                    '&units=' + units + '&sid=' + sid + '&eid=' + eid;
            var addUrl = _projectUrl + _projectId + '/newitem';
            $.ajax({
                type: 'POST',
                url: addUrl,
                async: false,
                cache: false,
                data: params,
                success: function(data){
                    _data = $.parseJSON(data);
                    var lastId = '';
                    switch (type) {
                        case 'section':
                            lastId = _data.sections.length - 1;
                            $(addDiv).before(_renderSection(_data.sections[lastId]));
                            break;
                        case 'element':
                            var section = _data.sections[sid - 1];
                            lastId = section.elements.length - 1;
                            $(addDiv).before(_renderElement(section.elements[lastId]));
                            break;
                        case 'material':
                            var element = _data.sections[sid - 1].elements[eid - 1];
                            lastId = element.materials.length - 1;
                            $(addDiv).before(_renderMaterial(element.materials[lastId]));
                            break;
                    }
                    _updateFooterTotals();
                    _addRowHandlers($(addDiv).prev());
                    if (_summary !== null) {
                        _summary.update();
                    }

                    // Reset the inputs
                    $(addDiv).find('input.bomText').val('').first().focus();
                },
                error: function(e){
                    alert('Error adding ' + type + ': ' + e);
                }
            });
        });
    }

    function _assignEditSubmitCancelOptionEventHandlers(editDiv) {
        var type = _getType(editDiv);

        $(editDiv).find('input.bomHelpText').bind('focus.bomHelpText', function(e) {
            $(this).removeClass('bomHelpText').val('').unbind('focus.bomHelpText');
        });

        $(editDiv).find('input.bomSubmit').button().click(function(e) {
            e.preventDefault();
            $(this).closest('form').submit();
        });

        $(editDiv).find('input.bomCancel').button().click(function(e) {
            e.preventDefault();
            $(editDiv).children('form').remove();
            $(editDiv).children('div').removeClass('bomHidden');

            $(editDiv).dblclick(function () {
                _displayEditForm(this);
            });
        });

        var form = $(editDiv).find('form');
        form.validate();
        form.submit(function (e) {
            e.preventDefault();

            var valid = true;

            $(this).find('input.required, input.number').each(function() {
                if (!$(form).validate().element(this)) {
                    valid = false;
                }
            });
            if (!valid) {
                return false;
            }

            var name = '';
            var quantity = '';
            var units = '';
            var sid = $(editDiv).closest('li.bomSection').index() + 1;
            var eid = '';
            var mid = '';

            switch (type) {
                case 'section':
                    name = $(editDiv).find('div.bomSectionNameInput input').val();
                    break;
                case 'element':
                    name = $(editDiv).find('div.bomElementNameInput input').val();
                    quantity = $(editDiv).find('div.bomElementQuantityInput input').val();
                    units = $(editDiv).find('div.bomElementUnitsInput input').val();
                    eid = $(editDiv).closest('li.bomElement').index() + 1;
                    break;
                case 'material':
                    name = $(editDiv).find('div.bomMaterialNameInput input').val();
                    quantity = $(editDiv).find('div.bomMaterialQuantityInput input').val();
                    eid = $(editDiv).closest('li.bomElement').index() + 1;
                    mid = $(editDiv).closest('li.bomMaterial').index() + 1;
                    break;
            }

            var params = 'type=' + _type + '&name=' + name + '&quantity=' + quantity +
                    '&units=' + units + '&sid=' + sid + '&eid=' + eid + '&mid=' + mid;
            var editUrl = _projectUrl + _projectId + '/edititem';
            $.ajax({
                type: 'POST',
                url: editUrl,
                async: false,
                cache: false,
                data: params,
                success: function(data){
                    _data = $.parseJSON(data);
                    var parentLi = $(editDiv).closest('li');
                    switch (type) {
                        case 'section':
                            parentLi.before(_renderSection(_data.sections[sid - 1]));
                            break;
                        case 'element':
                            var section = _data.sections[sid - 1];
                            parentLi.before(_renderElement(section.elements[eid - 1]));
                            break;
                        case 'material':
                            var element = _data.sections[sid - 1].elements[eid - 1];
                            parentLi.before(_renderMaterial(element.materials[mid - 1]));
                            break;
                    }
                    _updateFooterTotals();
                    _addRowHandlers(parentLi.prev());
                    if (_summary !== null) {
                        _summary.update();
                    }
                    parentLi.remove();
                },
                error: function(e){
                    alert('Error editing ' + type + ': ' + e);
                }
            });
        });
    }

    function _displayAddForm(node) {
        var addDiv = $(node).closest('li.addToBOM');
        $(addDiv).html('<form>' + _renderFormInputs(node) + _renderSubmitCancelOptions(_addText, _cancelText) + '</form>');
        _populateMaterialSelect(addDiv);
        _assignAddSubmitCancelOptionEventHandlers(addDiv);

        $(addDiv).find('input:first').focus();
    }

    function _displayEditForm(node) {
        var sid = $(node).closest('li.bomSection').index();
        var field = [];
        var eid;
        var mid;
        
        switch(_getType(node)) {
            case 'material':
                eid = $(node).closest('li.bomElement').index();
                mid = $(node).closest('li.bomMaterial').index();
                field = _data.sections[sid].elements[eid].materials[mid];
                break;
            case 'element':
                eid = $(node).closest('li.bomElement').index();
                field = _data.sections[sid].elements[eid];
                break;
            default:
                field = _data.sections[sid];
        }
        $(node).children('div').addClass('bomHidden');
        $(node).append('<form>' + _renderFormInputs(node, field) + _renderSubmitCancelOptions(_editText, _cancelText) + '</form>');
        _populateMaterialSelect(node);
        _assignEditSubmitCancelOptionEventHandlers(node);

        $(node).off('dblclick');

        $(node).find('input:first').focus();
    }

    function _populateMaterialSelect(node) {
        if (_getType(node) === 'material') {
            var selectedValue = $(node).find('input.bomSelectValue').val();
            var selectedUnits = '';

            $(node).find('select.bomSelect').each(function() {
                var materialOptions = [];
                $(_materials).each(function(index, material){
                    var selected = '';

                    if (selectedValue === '') {
                        selectedValue = material.name;
                    }
                    if (selectedValue === material.name) {
                        selected = 'selected';
                        selectedUnits = material.units;
                    }
                    materialOptions[index] = '<option value="' + material.name + '" ' + selected + '>' + material.name + '</option>';
                });
                $(this).html(materialOptions.join(''));
                $(this).selectmenu({width: '351px'}).change(function() {
                    var selected = $(this).val();
                    $(_materials).each(function(index, material) {
                        if (material.name === selected) {
                            $(node).find('input.bomSelectValue').val(material.name);
                            $(node).find('div.bomMUnits').html(material.units);
                        }
                    });
                });
            });
            $(node).find('input.bomSelectValue').val(selectedValue);
            $(node).find('div.bomMUnits').html(selectedUnits);
        }
    }

    function _renderSubmitCancelOptions(submitText, cancelText) {
        var html = '<div class="bomAddSubmitCancelOptions"><input type="submit" class="bomSubmit" value="' + submitText + '" />';
        return html + '<input type="button" class="bomCancel" value="' + cancelText + '" /></div>';
    }

    function _renderAddControl(type) {
        return '<li class="addToBOM">' + _renderAddControlText(type) + '</li>';
    }

    function _renderFormInputs(node, field) {
        var helperCss = '';
        if (field === undefined) {
            field = [];
            if (_quantityText !== '') {
                field.quantity = _quantityText;
                helperCss = 'bomHelpText';
            }
            if (_unitsText !== '') {
                field.units = _unitsText;
                helperCss = 'bomHelpText';
            }
        }

        var name = (field.name !== undefined) ? field.name : '';
        var units = (field.units !== undefined) ? field.units : '';
        var quantity = (field.quantity !== undefined) ? field.quantity : '';
        
        if (field.mname !== undefined) {
            name = field.mname;
        }
        
        var html = '';
        var sid;
        var eid;

        switch(_getType(node)) {
            case "element":
                sid = $(node).closest('li.bomSection').index();
                html += '<div class="bomElementNameInput"><input type="text" class="bomText required" id="bomS' +
                        sid + 'ElementName" value="' + name + '" /></div>';
                html += '<div class="bomElementQuantityInput"><input type="text" class="bomText required number ' +
                        helperCss + '" id="bomS' + sid + 'ElementQuantity" value="' + quantity + '" /></div>';
                html += '<div class="bomElementUnitsInput"><input type="text" class="bomText ' + helperCss +
                        '" id="bomS' + sid + 'ElementUnits" value="' + units + '" /></div>';
                break;
            case "material":
                sid = $(node).closest('li.bomSection').index();
                eid = $(node).closest('li.bomElement').index();
                html = '<div class="bomMaterialNameInput"><input type="text" class="bomSelectValue" id="bomS' +
                        sid + 'E' + eid + 'MaterialName" value="' + name + '" />';
                html += '<select type="text" class="bomSelect" id="bomS' + sid + 'E' + eid +
                        'MaterialSelect" value="' + name + '" /></div>';
                html += '<div class="bomMaterialQuantityInput"><input type="text" class="bomText required number ' +
                        helperCss + '" id="bomS' + sid + 'E' + eid + 'MaterialQuantity" value="' + quantity + '" />';
                html += '</div><div class="bomMUnits"></div>';
                break;
            default:
                html += '<div class="bomSectionNameInput"><input type="text" class="bomText required" id="bomSectionName" value="' +
                        name + '" /></div>';
        }
        return html;
    }

    function _renderAddControlText(type) {
        return '<a class="addToBOM">' + _addText + ' ' + _getTypeText(type) + '</a>';
    }

    function _confirmDelete(node) {
        var type = _getType(node);
        if (confirm(_deleteConfirmText + ' ' + _getTypeText(type) + '?')) {
            var sid = $(node).closest('li.bomSection').index() + 1;
            var eid = '';
            var mid = '';

            switch(type) {
                case 'material':
                    eid = $(node).closest('li.bomElement').index() + 1;
                    mid = $(node).closest('li.bomMaterial').index() + 1;
                    break;
                case 'element':
                    eid = $(node).closest('li.bomElement').index() + 1;
                    break;
            }
            var params = 'type=' + _type + '&sid=' + sid + '&eid=' + eid + '&mid=' + mid;
            var deleteUrl = _projectUrl + _projectId + '/deleteitem';

            $.ajax({
                type: 'POST',
                url: deleteUrl,
                async: false,
                cache: false,
                data: params,
                success: function(data) {
                    _data = $.parseJSON(data);
                    $(node).closest('li').remove();
                    _updateFooterTotals();
                    if (_summary !== null) {
                        _summary.update();
                    }
                },
                error: function(e){
                    alert('Error deleting ' + type + ': ' + e);
                }
            });
        }
    }

    function _renderSection(section) {
        var html = '<li class="bomSection"><div class="bomEditable"><div class="deleteFromBOM"><a>';
        html += _deleteText + '</a></div><div class="bomSName">';
        html += _readValue(section.name);
        html += '</div><div class="bomSCarbon">';
        html += '</div></div><ul class="bomElements">';

        var elements = [];
        $(section.elements).each(function(index, element){
            elements[index] = _renderElement(element);
        });
        html += elements.join('');

        html += _renderAddControl('element') + '</ul><div class="bomSectionDivider"></div></li>';

        return html;
    }

    function _renderElement(element) {
        var html = '<li class="bomElement"><div class="bomEditable"><div class="deleteFromBOM"><a>';
        html += _deleteText + '</a></div><div class="bomEName">';
        html += _readValue(element.name);
        html += '</div><div class="bomEQuantity">';
        html += _readValue(element.quantity);
        html += '</div><div class="bomEUnits">';
        html += _readValue(element.units);
        html += '</div><div class="bomECarbon">';
        html += '</div></div><ul class="bomMaterials">';

        var materials = [];
        $(element.materials).each(function(index, material){
            materials[index] = _renderMaterial(material);
        });
        html += materials.join('');

        html += _renderAddControl('material') + '</ul></li>';

        return html;
    }

    function _renderMaterial(material) {
        var html = '<li class="bomMaterial"><div class="bomEditable"><div class="deleteFromBOM"><a>';
        html += _deleteText + '</a></div><div class="bomMName">';
        html += _readValue(material.mname);
        html += '</div><div class="bomMQuantity">';
        html += _readValue(material.quantity);
        html += '</div><div class="bomMUnits">';
        html += _readValue(material.units);
        html += '</div><div class="bomMCarbon">';
        html += _readValue(material.totalCarbon);
        html += '</div></div></li>';

        return html;
    }

    function _renderHeader() {
        var html = '<div class="bomHEdit">';
        
        if (_projectId > 0) {
            html += '<button>';
            if (_editing === 'false') {
                html += _editStartText;
            } else {
                html += _editFinishText;
            }
            html += '</button>';
        }
        html += '</div><div class="bomHName">';
        html += _headerNameText;
        html += '</div><div class="bomHQuantity">';
        html += _headerQuantityText;
        html += '</div><div class="bomHCarbon">';
        html += _headerCarbonText;
        html += '</div><div class="bomHClear"></div>';

        return html;
    }

    function _renderFooter() {
        var html = '<div class="bomFSummary">';
        html += _footerSummaryText;
        html += '</div><div class="bomFCarbon">';
        html += _data.totalCarbon;
        html += '</div><div class="bomFClear"></div>';

        return html;
    }

    function _updateFooterTotals() {
        $(_div + ' div.bomFCarbon').html(_data.totalCarbon);
    }

    function _getType(node) {
        var level = $(node).closest('ul').attr('class');
        var type = 'section';
        switch(level) {
            case 'bomElements':
                type = 'element';
                break;
            case 'bomMaterials':
                type = 'material';
                break;
        }
        return type;
    }

    function _getTypeText(type) {
        var typeText = _materialText;

        switch (type) {
            case 'section':
                typeText = _sectionText;
                break;
            case 'element':
                typeText = _elementText;
                break;
        }
        return typeText;
    }

    function _readValue(value) {
        var returnVal = '';
        if (value !== undefined && value !== '') {
            returnVal = value;
        }
        return returnVal;
    }
    
    
    var bomUrl = _projectUrl + 'share/' + _publicId + '/bom.json?type=' + _type.toLowerCase();
    var materialsUrl = _projectUrl + '/materials.json?type=' + _type.toLowerCase();

    $.getJSON(materialsUrl, function(data){
        _materials = data;
    });

    $.getJSON(bomUrl, function(data){
        _data = data;

        var sections = [];
        $(_data.sections).each(function(index, section){
            sections[index] = _renderSection(section);
        });

        $('<div/>', {
            'class': 'bomHeader', html: _renderHeader()
        }).appendTo(_div);

        var sectionsCss = 'bomSections';
        if (_editing === 'false') {
            sectionsCss += ' bomNotEditing';
        }

        $('<ul/>', {
            'class': sectionsCss, html: sections.join('') +
                _renderAddControl('section')
        }).appendTo(_div);

        $('<div/>', {
            'class': 'bomFooter', html: _renderFooter()
        }).appendTo(_div);

        _addDefaultHandlers();
    });
}