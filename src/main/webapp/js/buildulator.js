var bomData;

$(document).ready(function() {
    if ($('#flashMessage p.flashMessageContent').html() != null) {
        $.gritter.add({
            title: $('#flashMessage p.flashMessageTitle').html(),
            text: $('#flashMessage p.flashMessageContent').html()
        });
    }
});

$(document).ready(function() {

    $('#btnEditProject').click(function() {
        document.location.href = editProjectUrl;
    });

    $('#formCloneProject').dialog({
        autoOpen : false,
        modal : true,
        resizable: false
    });

    $('#btnCloneProject').click(function() {
        $('#formCloneProject').dialog('open');
    });

    $('#formCloneProject .cancelAction a').click(function() {
        $('#formCloneProject').dialog('close');
    });

    $('#formDeleteProject').dialog({
        autoOpen : false,
        modal : true,
        resizable: false
    });

    $('#btnDeleteProject').click(function() {
        $('#formDeleteProject').dialog('open');
    });

    $('#formDeleteProject .cancelAction a').click(function() {
        $('#formDeleteProject').dialog('close');
    });
});

$(document).ready(function() {
    $('#materialsList').dataTable({
        'bProcessing' : true,
        'bJQueryUI' : true,
        'sPaginationType' : 'full_numbers',
        'bLengthChange' : false,
        'iDisplayLength' : 50,
        'sAjaxSource' : materialLibraryUrl + '/list.json',
        'aoColumnDefs' : [ {
            'sClass' : 'column-1',
            'aTargets' : [ 0 ]
        }, {
            'sClass' : 'column-2',
            'aTargets' : [ 1 ]
        }, {
            'sClass' : 'column-3',
            'aTargets' : [ 2 ]
        }, {
            'sClass' : 'column-4',
            'aTargets' : [ 3 ]
        }, {
            'sClass' : 'column-5',
            'aTargets' : [ 4 ]
        }, {
            'sClass' : 'column-6',
            'aTargets' : [ 5 ]
        }, {
            'sClass' : 'column-7',
            'aTargets' : [ 6 ]
        } ]
    }).makeEditable({
        sAddURL : materialLibraryUrl,
        sDeleteURL : materialLibraryUrl + '/delete',
        sUpdateURL : materialLibraryUrl + '/update',
        sAddNewRowButtonId: 'btnAddMaterial',
        sAddNewRowFormId: 'formAddMaterial',
        sDeleteRowButtonId: 'btnDeleteMaterial',
        oDeleteRowButtonOptions : {},
        aoColumns : [ {
            cssclass : 'required'
        }, {
            cssclass : 'required',
            type: 'select',
            onblur: 'submit',
            loadurl: materialLibraryUrl + '/types.json',
            loadtype: 'GET',
            event: 'click'
        }, {
            cssclass : 'required'
        }, {
            cssclass : 'number'
        }, {
            cssclass : 'required number'
        }, {
            cssclass : 'required number'
        }, {
            cssclass : 'number'
        } ]
    });

    $('#formBulkAddMaterials').dialog({
        autoOpen : false,
        modal : true,
        resizable: false
    });
    $('#btnBulkAddMaterials').removeAttr("disabled").click(function() {
        $('#formBulkAddMaterials').dialog('open')
    });
});

$(document).ready(function() {
    $('#projectsList').dataTable({
        "bProcessing" : true,
        "bJQueryUI" : true,
        "sPaginationType" : "full_numbers",
        "bLengthChange" : false,
        "iDisplayLength" : 50,
        "sAjaxSource" : './projects/list.json',
        "fnDrawCallback": function(){
            $('.dataTable tbody tr').click(function() {
                var projectId = $(this).attr('id');
                document.location.href = showProjectUrl + projectId;
            });
            $('.dataTable tbody tr').hover(function() {
                $(this).addClass('row_selected');
            }, function() {
                $(this).removeClass('row_selected');
            });
        },
        "aoColumns": [ {
            "bSortable": true
        }, {
            "bVisible": false
        }, {
            "bSortable": true
        }, {
            "iDataSort": 4
        }, {
            "bVisible": false
        } ],
        "aoColumnDefs" : [ {
            "fnRender": function ( oObj, sVal ) {
                var name = '<strong>' + sVal + '</strong>';
                var address = oObj.aData[1];
                if (address != '') {
                    name += '<br/>' + address;
                }
                return name;
            },
            "sClass" : "column-1",
            "aTargets" : [ 0 ]
        }, {
            "sClass" : "column-2",
            "aTargets" : [ 2 ]
        }, {
            "sClass" : "column-3",
            "aTargets" : [ 3 ]
        } ]
    });

    $('#formAddMaterial select').selectmenu({width: '250px'});
});

$(document).ready(function() {
    $('#adminTabs').tabs();
    $('#adminAccordion').accordion({autoHeight: false});

    $('#usersList').dataTable({
        "bProcessing" : true,
        "bJQueryUI" : true,
        "sPaginationType" : "full_numbers",
        "bLengthChange" : false,
        "iDisplayLength" : 50,
        "sAjaxSource" : './admin/users/list.json',
        "aoColumnDefs" : [ {
            "sClass" : "column-1",
            "aTargets" : [ 0 ]
        }, {
            "sClass" : "column-2",
            "aTargets" : [ 1 ]
        }, {
            "sClass" : "column-3",
            "aTargets" : [ 2 ]
        }, {
            "sClass" : "column-4",
            "aTargets" : [ 3 ]
        }, {
            "sClass" : "column-5",
            "aTargets" : [ 4 ]
        }]
    }).makeEditable({
        sDeleteURL : "./admin/users/delete",
        sUpdateURL : "./admin/users/update",
        oDeleteRowButtonOptions : {},
        aoColumns : [ {
            cssclass : "required"
        }, {
            cssclass : "required"
        }, {
            cssclass : "required"
        }, {
            cssclass : "required",
            type: "select",
            onblur: "submit",
            loadurl: "./admin/users/roles.json",
            loadtype: "GET",
            event: "click"
        }, {
            cssclass : "required",
            type: "select",
            onblur: "submit",
            loadurl: "./admin/users/statuses.json",
            loadtype: "GET",
            event: "click"
        } ]
    });
});

$(document).ready(function() {
    $('button, input:submit').button();
});

$(document).ready(function() {
    $("form").validate();
});

$(document).ready(function() {
    $('textarea.rteditor').wysiwyg({
        css: wysiwygCss,
        iFrameClass : "wysiwyg-input",
        controls : {
            paragraph : {
                visible : true,
                groupIndex : 0
            },
            h1 : {
                visible : false
            },
            h2 : {
                visible : true,
                groupIndex : 0
            },
            h3 : {
                visible : true,
                groupIndex : 0
            },
            h4 : {
                visible : true,
                groupIndex : 0
            },
            bold : {
                visible : true,
                groupIndex : 1
            },
            italic : {
                visible : true,
                groupIndex : 1
            },
            underline : {
                visible : false
            },
            strikeThrough : {
                visible : false
            },
            justifyLeft : {
                visible : false
            },
            justifyRight : {
                visible : false
            },
            justifyCenter : {
                visible : false
            },
            justifyFull : {
                visible : false
            },
            insertImage : {
                visible : false
            },
            insertTable : {
                visible : false
            },
            code : {
                visible : false
            },
            html : {
                visible : true
            },
        }
    });
});

$(document).ready(function() {
    $('#project select').selectmenu({width: '320px'});
});

$(document).ready(function() {
    $('#errorAccordion').accordion({autoHeight: false});
});

$(document).ready(function() {

    $('#formImportBOM').dialog({
        autoOpen : false,
        modal : true,
        resizable: false
    });

    $('#bomImportButton').click(function() {
        $('#formImportBOM').dialog('open');
    });

    $('#bomSkipImportButton').click(function() {
        $('.bomEmpty').removeClass('bomEmpty');
        $('div.bomImport').remove();
    });

    $('div.billOfMaterials').each(function () {
        var pid = this.id.replace('billOfMaterials','');
        var bom = new BillOfMaterials({projectId: pid, projectUrl: showProjectUrl});
        bom.render();
     });
 });


function BillOfMaterials (config) {
    if (config == undefined) { config = new Array(); }
    var _data;
    var _materials;
    var _div = (config.div != undefined) ? config.div : 'div.billOfMaterials div.content';
    var _projectId = (config.projectId != undefined) ? config.projectId : 1;
    var _projectUrl = (config.projectUrl != undefined) ? config.projectUrl : './projects/';
    var _addText = (config.addText != undefined) ? config.addText : 'Add';
    var _editText = (config.editText != undefined) ? config.editText : 'Update';
    var _cancelText = (config.cancelText != undefined) ? config.cancelText : 'Cancel';
    var _addSectionText = (config.sectionText != undefined) ? config.sectionText : 'Add section';
    var _addElementText = (config.elementText != undefined) ? config.materialText : 'Add element';
    var _addMaterialText = (config.materialText != undefined) ? config.materialText : 'Add material';
    var _deleteText = (config.deleteText != undefined) ? config.deleteText : 'Delete';
    var _deleteConfirmText = (config.deleteConfirmText != undefined) ? config.deleteConfirmText : 'Are you sure you want to delete this';
    var _headerNameText = (config.headerNameText != undefined) ? config.headerNameText : '';
    var _headerQuantityText = (config.headerQuantityText != undefined) ? config.headerQuantityText : 'Quantity';
    var _headerEnergyText = (config.headerEnergyText != undefined) ? config.headerEnergyText : 'Energy';
    var _headerCarbonText = (config.headerCarbonText != undefined) ? config.headerCarbonText : 'Carbon';
    var _footerSummaryText = (config.footerSummaryText != undefined) ? config.footerSummaryText : 'Total material energy/carbon footprint';

    this._render = function() {
        var bomUrl = _projectUrl + _projectId + '/bom.json';
        var materialsUrl = _projectUrl + '/materials.json';

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

            $('<ul/>', {
                'class': 'bomSections', html: sections.join('')
                + _renderAddControl('section')
            }).appendTo(_div);

            $('<div/>', {
                'class': 'bomFooter', html: _renderFooter()
            }).appendTo(_div);

            _addDefaultHandlers();
        });
    };

    function _addDefaultHandlers() {
        $(_div + ' div.deleteFromBOM a').click(function () {
            _confirmDelete(this);
        });
        $(_div + ' li.addToBOM a.addToBOM').click(function () {
            _displayAddForm(this);
        });
        $(_div + ' div.bomEditable').dblclick(function () {
            _displayEditForm(this);
        });
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

            var params = 'name=' + name + '&quantity=' + quantity
                    + '&units=' + units + '&sid=' + sid + '&eid=' + eid;
            var addUrl = _projectUrl + _projectId + '/newitem';
            $.ajax({
                type: 'POST',
                url: addUrl,
                data: params,
                success: function(data){
                    _data = $.parseJSON(data);
                    var html = '';
                    switch (type) {
                        case 'section':
                            var lastId = _data.sections.length - 1;
                            var html = _renderSection(_data.sections[lastId]);
                            break;
                        case 'element':
                            var section = _data.sections[sid - 1];
                            var lastId = section.elements.length - 1;
                            var html = _renderElement(section.elements[lastId]);
                            break;
                        case 'material':
                            var element = _data.sections[sid - 1].elements[eid - 1];
                            var lastId = element.materials.length - 1;
                            var html = _renderMaterial(element.materials[lastId]);
                            break;
                    }
                    $(addDiv).before(html);
                    _addRowHandlers($(addDiv).prev());

                    // Reset the inputs
                    $(addDiv).find('input.bomText').val('').first().focus();
                },
                error: function(e){
                    alert('Error adding ' + type + ': ' + e);
                },
            });
        });
    }

    function _assignEditSubmitCancelOptionEventHandlers(editDiv) {
        var type = _getType(editDiv);

        $(editDiv).find('input.bomSubmit').button().click(function(e) {
            e.preventDefault();
            $(this).closest('form').submit();
        });

        $(editDiv).find('input.bomCancel').button().click(function(e) {
            e.preventDefault();
            $(editDiv).children('form').remove();
            $(editDiv).children('div').show();

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

            var params = 'name=' + name + '&quantity=' + quantity + '&units=' + units + '&sid=' + sid + '&eid=' + eid + '&mid=' + mid;
            var editUrl = _projectUrl + _projectId + '/edititem';
            $.ajax({
                type: 'POST',
                url: editUrl,
                data: params,
                success: function(data){
                    _data = $.parseJSON(data);
                    var html = '';
                    switch (type) {
                        case 'section':
                            var html = _renderSection(_data.sections[sid - 1]);
                            break;
                        case 'element':
                            var section = _data.sections[sid - 1];
                            var html = _renderElement(section.elements[eid - 1]);
                            break;
                        case 'material':
                            var element = _data.sections[sid - 1].elements[eid - 1];
                            var html = _renderMaterial(element.materials[mid - 1]);
                            break;
                    }
                    var parentLi = $(editDiv).closest('li');
                    parentLi.before(html);
                    _addRowHandlers(parentLi.prev());

                    parentLi.remove();
                },
                error: function(e){
                    alert('Error editing ' + type + ': ' + e);
                },
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
        var field = new Array();
        switch(_getType(node)) {
            case 'material':
                var eid = $(node).closest('li.bomElement').index();
                var mid = $(node).closest('li.bomMaterial').index();
                field = _data.sections[sid].elements[eid].materials[mid];
                break;
            case 'element':
                var eid = $(node).closest('li.bomElement').index();
                field = _data.sections[sid].elements[eid];
                break;
            default:
                field = _data.sections[sid];
        }
        $(node).children('div').hide();
        $(node).append('<form>' + _renderFormInputs(node, field) + _renderSubmitCancelOptions(_editText, _cancelText) + '</form>');
        _populateMaterialSelect(node);
        _assignEditSubmitCancelOptionEventHandlers(node);

        $(node).off('dblclick');

        $(node).find('input:first').focus();
    }

    function _populateMaterialSelect(node) {
        if (_getType(node) == 'material') {
            var selectedValue = $(node).find('input.bomSelectValue').val();
            var selectedUnits = '';

            $(node).find('select.bomSelect').each(function() {
                var materialOptions = [];
                $(_materials).each(function(index, material){
                    var selected = '';

                    if (selectedValue == '') {
                        selectedValue = material.name;
                    }
                    if (selectedValue == material.name) {
                        selected = 'selected';
                        selectedUnits = material.units;
                    }
                    materialOptions[index] = '<option value="' + material.name + '" ' + selected + '>' + material.name + '</option>';
                });
                $(this).html(materialOptions.join(''));
                $(this).selectmenu({width: '295px'}).change(function() {
                    var selected = $(this).val();
                    $(_materials).each(function(index, material) {
                        if (material.name == selected) {
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
        if (field == undefined) { field = new Array(); }

        var name = (field.name != undefined) ? field.name : '';
        var units = (field.units != undefined) ? field.units : '';
        var quantity = (field.quantity != undefined) ? field.quantity : '';
        html = '';

        switch(_getType(node)) {
            case "element":
                var sid = $(node).closest('li.bomSection').index();
                html += '<div class="bomElementNameInput"><input type="text" class="bomText required" id="bomS' + sid + 'ElementName" value="' + name + '" /></div>';
                html += '<div class="bomElementQuantityInput"><input type="text" class="bomText required number" id="bomS' + sid + 'ElementQuantity" value="' + quantity + '" /></div>'
                html += '<div class="bomElementUnitsInput"><input type="text" class="bomText" id="bomS' + sid + 'ElementUnits" value="' + units + '" /></div>'
                break;
            case "material":
                var sid = $(node).closest('li.bomSection').index();
                var eid = $(node).closest('li.bomElement').index();
                html = '<div class="bomMaterialNameInput"><input type="text" class="bomSelectValue" id="bomS' + sid + 'E' + eid + 'MaterialName" value="' + name + '" />';
                html += '<select type="text" class="bomSelect" id="bomS' + sid + 'E' + eid + 'MaterialSelect" value="' + name + '" /></div>';
                html += '<div class="bomMaterialQuantityInput"><input type="text" class="bomText required number" id="bomS' + sid + 'E' + eid + 'MaterialQuantity" value="' + quantity + '" />';
                html += '</div><div class="bomMUnits"></div>'
                break;
            default:
                html += '<div class="bomSectionNameInput"><input type="text" class="bomText required" id="bomSectionName" value="' + name + '" /></div>';
        }
        return html;
    }

    function _renderAddControlText(type) {
        var returnText = _addMaterialText;
        switch(type) {
            case "section":
                returnText = _addSectionText;
                break;
            case "element":
                returnText = _addElementText;
                break;
        }
        return '<a class="addToBOM">' + returnText + '</a>';
    }

    function _confirmDelete(node) {
        var type = _getType(node);
        if (confirm(_deleteConfirmText + ' ' + type + '?')) {
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
            var params = 'sid=' + sid + '&eid=' + eid + '&mid=' + mid;
            var deleteUrl = _projectUrl + _projectId + '/deleteitem';

            $.ajax({
                type: 'POST',
                url: deleteUrl,
                data: params,
                success: function(data) {
                    // Remove the parent li
                    $(node).closest('li').remove();
                    // Re-render the sustainability totals
                },
                error: function(e){
                    alert('Error deleting ' + type + ': ' + data);
                },
            });
        }
    }

    function _renderSection(section) {
        var html = '<li class="bomSection"><div class="bomEditable"><div class="deleteFromBOM"><a>';
        html += _deleteText + '</a></div><div class="bomSName">';
        html += _readValue(section.name);
        html += '</div><div class="bomSEnergy">';
        html += '</div><div class="bomSCarbon">';
        html += '</div></div><ul class="bomElements">';

        var elements = [];
        $(section.elements).each(function(index, element){
            elements[index] = _renderElement(element);
        });
        html += elements.join('');

        html += _renderAddControl('element') + '</ul></li>';

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
        html += '</div><div class="bomEEnergy">';
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
        html += _readValue(material.name);
        html += '</div><div class="bomMQuantity">';
        html += _readValue(material.quantity);
        html += '</div><div class="bomMUnits">';
        html += _readValue(material.units);
        html += '</div><div class="bomMEnergy">';
        html += _readValue(material.totalEnergy);
        html += '</div><div class="bomMCarbon">';
        html += _readValue(material.totalCarbon);
        html += '</div></div></li>';

        return html;
    }

    function _renderHeader() {
        html = '<div class="bomHName">';
        html += _headerNameText;
        html += '</div><div class="bomHQuantity">';
        html += _headerQuantityText;
        html += '</div><div class="bomHEnergy">';
        html += _headerEnergyText;
        html += '</div><div class="bomHCarbon">';
        html += _headerCarbonText;
        html += '</div><div class="bomHClear"></div>';

        return html;
    }

    function _renderFooter() {
        html = '<div class="bomFSummary">';
        html += _footerSummaryText;
        html += '</div><div class="bomFEnergy">';
        html += '</div><div class="bomFCarbon">';
        html += '</div><div class="bomFClear"></div>';

        return html;
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

    function _readValue(value) {
        var returnVal = '';
        if (value != undefined && value != '') {
            returnVal = value;
        }
        return returnVal
    }

}

BillOfMaterials.prototype = {
    render : function() { this._render(); }
}